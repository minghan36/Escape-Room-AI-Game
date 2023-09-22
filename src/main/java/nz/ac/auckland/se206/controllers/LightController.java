package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class LightController {

  @FXML private Circle behindLight;
  @FXML private Label timer;
  @FXML private Canvas gameMaster;
  @FXML private Rectangle quizMaster;
  @FXML private ImageView fixOne;
  @FXML private ImageView fixTwoOne;
  @FXML private ImageView fixTwoTwo;
  @FXML private Rectangle fixTwoThree;
  @FXML private ImageView fixThreeOne;
  @FXML private ImageView fixThreeTwo;
  @FXML private ImageView fixFour;
  @FXML private Label lightSuggest;
  private Image[] alienImages;
  private int currentImageIndex = 0;
  @FXML private TextArea objText;
  @FXML private TextArea hintsText;
  @FXML private ImageView tape;
  @FXML private ImageView sdCard;
  @FXML private ImageView globe;
  @FXML private ImageView globe1;
  @FXML private Button rgbClue1;

  public void initialize() {
    if (GameState.isRgbClueFound) {
      rgbClue1.setVisible(true);
      rgbClue1.setText(GameState.password);
    } else {
      rgbClue1.setVisible(false);
    }
    objText.setText(GameState.getObjective());
    hintsText.setText(GameState.getHint());
    tape.setVisible(GameState.isElectricalTapeFound);
    sdCard.setVisible(GameState.isSdCardFound);
    if (!GameState.isGlobeFound && GameState.isLightPuzzleSolved) {
      globe.setVisible(GameState.isRiddleResolved);
    } else {
      globe.setVisible(false);
    }
    globe1.setVisible(GameState.isGlobeFound);
    if (GameState.isLightPuzzleSolved) {
      objText.setText(GameState.getObjective());
    } else {
      objText.setText("Fix the wires to turn on the light.");
    }
    if (GameState.isWireOneFixed) {
      fixOne.setOpacity(1);
      fixOne.setOnMouseClicked(null);
    }
    if (GameState.isWireTwoFixed) {
      fixTwoOne.setOpacity(1);
      fixTwoTwo.setOpacity(1);
      fixTwoThree.setOpacity(1);
      fixTwoOne.setOnMouseClicked(null);
      fixTwoTwo.setOnMouseClicked(null);
      fixTwoThree.setOnMouseClicked(null);
    }
    if (GameState.isWireThreeFixed) {
      fixThreeOne.setOpacity(1);
      fixThreeTwo.setOpacity(1);
      fixThreeOne.setOnMouseClicked(null);
      fixThreeTwo.setOnMouseClicked(null);
    }
    if (GameState.isWireFourFixed) {
      fixFour.setOpacity(1);
      fixFour.setOnMouseClicked(null);
    }
    if (GameState.wireFixes == 4) {
      lightSuggest.setText("All the wires have been fixed.");
    }
    lightSuggest.setWrapText(true);
    timer.setText(GameState.getTimeLeft());
    Thread timeThread =
        new Thread(
            () -> {
              startTimer();
            });
    timeThread.start();
    alienImages =
        new Image[] {
          new Image("images/move1.png"),
          new Image("images/move2.png"),
          new Image("images/move3.png"),
          new Image("images/move4.png")
        };

    // Start the animation
    startAnimation();

    TranslateTransition translateTransition =
        new TranslateTransition(Duration.seconds(2), gameMaster);

    // set the Y-axis translation value
    translateTransition.setByY(-10);

    // set the number of cycles for the animation
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);

    // Set auto-reverse to true to make the label return to its original position
    translateTransition.setAutoReverse(true);

    // Start the animation
    translateTransition.play();
  }

  private void startAnimation() {
    GraphicsContext gc = gameMaster.getGraphicsContext2D();
    AnimationTimer timer =
        new AnimationTimer() {
          private long lastTime = 0;
          private final long frameDurationMillis = 100; // 1000 milliseconds = 1 second

          @Override
          public void handle(long currentTime) {
            if (currentTime - lastTime >= frameDurationMillis * 1_000_000) {
              if (currentImageIndex < alienImages.length) {
                gc.clearRect(0, 0, gameMaster.getWidth(), gameMaster.getHeight());
                gc.drawImage(alienImages[currentImageIndex], 0, 0);
                currentImageIndex++;
                // Check if we have displayed all images; if so, reset the index to 0
                if (currentImageIndex >= alienImages.length) {
                  currentImageIndex = 0;
                }
                lastTime = currentTime;
              }
            }
          }
        };
    timer.start();
  }

  // pressing on the quiz master to open the chat box
  @FXML
  public void clickQuizMaster(MouseEvent event) {
    App.setUi("chat");
  }

  public void startTimer() {
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    // Counts down the timer.
                    Platform.runLater(
                        new Runnable() {
                          @Override
                          public void run() {
                            timer.setText(GameState.getTimeLeft());
                          }
                        });
                  }
                }));

    timeline.setCycleCount((GameState.minutes * 60) + GameState.seconds - 1);
    timeline.play();
    // randomLight.setText(GameState.randomLight);

  }

  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    if (GameState.isLightPuzzleSolved) {
      GameState.currentObj = "Picture Puz";
    }
    GameState.isLightPuzzleStarted = true;
    GameState.currentRoom = "bathroom";
    App.setUi("bathroom");
  }

  @FXML
  private void clickBreakOne() {
    if (GameState.isElectricalTapeFound) {
      objText.setText("Patch the wires with the electrical tape.");
      fixOne.setOpacity(1);
      GameState.isWireOneFixed = true;
      fixOne.setOnMouseClicked(null);
      GameState.wireFixes++;
      if (GameState.wireFixes == 4) {
        lightSuggest.setText("Good Job! Collect your next clue.");
        objText.setText(
            "Good Job! You have solved the light puzzle. Collect the Picture of the Globe and"
                + " travel to your next puzzle.");
        GameState.isLightPuzzleSolved = true;
        globe.setVisible(true);
      }
    } else {
      objText.setText(
          "You need to find the Electrical Tape needed to patch the wires. Check every room"
              + " carefully!");
    }
  }

  @FXML
  private void clickBreakTwo() {
    if (GameState.isElectricalTapeFound) {
      objText.setText("Patch the wires with the electrical tape.");
      fixTwoOne.setOpacity(1);
      fixTwoTwo.setOpacity(1);
      fixTwoThree.setOpacity(1);
      GameState.isWireTwoFixed = true;
      fixTwoOne.setOnMouseClicked(null);
      fixTwoTwo.setOnMouseClicked(null);
      fixTwoThree.setOnMouseClicked(null);
      GameState.wireFixes++;
      if (GameState.wireFixes == 4) {
        lightSuggest.setText("Good Job! Collect your next clue.");
        objText.setText(
            "Good Job! You have solved the light puzzle. Collect the Picture of the Globe and"
                + " travel to your next puzzle.");
        GameState.isLightPuzzleSolved = true;
        globe.setVisible(true);
      }
    } else {
      objText.setText(
          "You need to find the Electrical Tape needed to patch the wires. Check every room"
              + " carefully!");
    }
  }

  @FXML
  private void clickBreakThree() {
    if (GameState.isElectricalTapeFound) {
      objText.setText("Patch the wires with the electrical tape.");
      fixThreeOne.setOpacity(1);
      fixThreeTwo.setOpacity(1);
      GameState.isWireThreeFixed = true;
      fixThreeOne.setOnMouseClicked(null);
      fixThreeTwo.setOnMouseClicked(null);
      GameState.wireFixes++;
      if (GameState.wireFixes == 4) {
        lightSuggest.setText("Good Job! Collect your next clue.");
        objText.setText(
            "Good Job! You have solved the light puzzle. Collect the Picture of the Globe and"
                + " travel to your next puzzle.");
        GameState.isLightPuzzleSolved = true;
        globe.setVisible(true);
      }
    } else {
      objText.setText(
          "You need to find the Electrical Tape needed to patch the wires. Check every room"
              + " carefully!");
    }
  }

  @FXML
  private void clickBreakFour() {
    if (GameState.isElectricalTapeFound) {
      objText.setText("Patch the wires with the electrical tape.");
      fixFour.setOpacity(1);
      GameState.isWireFourFixed = true;
      fixFour.setOnMouseClicked(null);
      GameState.wireFixes++;
      if (GameState.wireFixes == 4) {
        lightSuggest.setText("Good Job! Collect your next clue.");
        objText.setText(
            "Good Job! You have solved the light puzzle. Collect the Picture of the Globe and"
                + " travel to your next puzzle.");
        GameState.isLightPuzzleSolved = true;
        globe.setVisible(true);
      }
    } else {
      objText.setText(
          "You need to find the Electrical Tape needed to patch the wires. Check every room"
              + " carefully!");
    }
  }

  @FXML
  private void increaseGlobeSize() {
    globe.setScaleX(1.2);
    globe.setScaleY(1.2);
  }

  @FXML
  private void decreaseGlobeSize() {
    globe.setScaleX(1);
    globe.setScaleY(1);
  }

  @FXML
  private void clickGlobe() {
    GameState.isGlobeFound = true;
    GameState.currentObj = "Picture Puz";
    lightSuggest.setText("");
    globe.setVisible(false);
    globe1.setVisible(true);
  }
}
