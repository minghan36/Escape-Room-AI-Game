package nz.ac.auckland.se206.controllers;

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
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;

public class ComputerRoomController {

  @FXML private ImageView tape;
  @FXML private ImageView toLockedRoom;
  @FXML private Rectangle quizMaster;
  @FXML private Canvas gameMaster;
  @FXML private Label Timer;
  @FXML private Rectangle decrypt;
  private Image[] alienImages;
  private int currentImageIndex = 0;
  @FXML private ImageView hoverImage;
  @FXML private ImageView tape1;
  @FXML private ImageView sdCard;
  @FXML private TextArea objText;
  @FXML private TextArea hintsText;
  @FXML private ImageView globe;
  @FXML private Button rgbClue1;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    if (GameState.isRgbClueFound) {
      rgbClue1.setVisible(true);
      rgbClue1.setText(GameState.password);
    } else {
      rgbClue1.setVisible(false);
    }
    objText.setText(GameState.getObjective());
    sdCard.setVisible(GameState.isSdCardFound);
    tape1.setVisible(GameState.isElectricalTapeFound);
    globe.setVisible(GameState.isGlobeFound);
    if (!GameState.isLightPuzzleStarted) {
      tape.setOnMouseClicked(null);
      tape.setOnMouseEntered(null);
      tape.setOpacity(0);
    }
    if (GameState.isElectricalTapeFound) {
      tape.setOpacity(0);
      tape.setOnMouseClicked(null);
    }
    Timer.setText(GameState.getTimeLeft());
    Thread timeThread =
        new Thread(
            () -> {
              startTimer();
            });
    timeThread.start();
    // game master animation
    // Initialize alienImages with your image paths
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

  @FXML
  public void enterLockedRoom(MouseEvent event) {
    GameState.currentRoom = "lockedroom";
    App.setUi("lockedroom");
  }

  @FXML
  public void highlight() {
    toLockedRoom.setOpacity(1);
    toLockedRoom.setX(1.2);
    toLockedRoom.setY(1.2);

    toLockedRoom.setScaleX(1.2);
    toLockedRoom.setScaleY(1.2);

  }

  @FXML
  public void removeHighlight() {
    toLockedRoom.setOpacity(0.3);

    toLockedRoom.setX(1);
    toLockedRoom.setY(1);
    toLockedRoom.setScaleX(1);
    toLockedRoom.setScaleY(1);
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
                            Timer.setText(GameState.getTimeLeft());
                          }
                        });
                  }
                }));

    timeline.setCycleCount((GameState.minutes * 60) + GameState.seconds - 1);
    timeline.play();
  }

  @FXML
  public void enterDecrypt(MouseEvent event) {
    if (!GameState.isRiddleResolved) {
      objText.setText("You need the SD card to access the computer!");
    } else {
      GameState.currentRoom = "decrypt";
      App.setUi("decrypt");
    }
  }

  @FXML
  public void increaseSize(MouseEvent event) {
    if (!GameState.isRiddleResolved) {
      return;
    } else {
      hoverImage.setScaleX(1.05); // Increase the size by a factor of 1.2 horizontally
      hoverImage.setScaleY(1.05);
    }
  }

  @FXML
  public void decreaseSize(MouseEvent event) {
    // decrease the size of the image
    hoverImage.setScaleX(1);
    hoverImage.setScaleY(1);
  }

  @FXML
  public void clickTape() {
    tape.setOpacity(0);
    tape.setOnMouseClicked(null);
    GameState.isElectricalTapeFound = true;
    objText.setText("Good job you have found the electrical tape, now fix the wires.");
    tape1.setVisible(true);
  }

  @FXML
  public void increaseTapeSize(MouseEvent event) {
    tape.setScaleX(1.2);
    tape.setScaleY(1.2);
  }

  @FXML
  public void decreaseTapeSize(MouseEvent event) {
    tape.setScaleX(1);
    tape.setScaleY(1);
  }
}
