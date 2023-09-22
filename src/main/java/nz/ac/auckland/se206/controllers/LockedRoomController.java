package nz.ac.auckland.se206.controllers;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
// import javafx.scene.control.Alert;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;

/** Controller class for the room view. */
public class LockedRoomController {

  @FXML private ImageView toBathroom;
  @FXML private ImageView toComputerRoom;
  @FXML private Rectangle quizMaster;
  @FXML private Rectangle buttonBlue;
  @FXML private Rectangle buttonRed;
  @FXML private Rectangle buttonGreen;
  @FXML private Rectangle buttonYellow;
  @FXML private Rectangle rectangleDoorOne;
  @FXML private Rectangle rectangleDoorTwo;
  @FXML private Rectangle rectangleDoorThree;
  @FXML private Canvas gameMaster;
  @FXML private Label timer;
  @FXML private Label labelPasscode;
  @FXML private Label labelObjective;
  @FXML private ImageView globe;
  private Image[] alienImages;
  private int currentImageIndex = 0;
  @FXML private ImageView tape;
  @FXML private ImageView sdCard;
  @FXML private TextArea objText;
  @FXML private TextArea hintsText;
  @FXML private ImageView globe1;
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
    hintsText.setText(GameState.getHint());
    sdCard.setVisible(GameState.isSdCardFound);
    tape.setVisible(GameState.isElectricalTapeFound);
    globe1.setVisible(GameState.isGlobeFound);
    // labelObjective.setText(GameState.password);
    timer.setText(GameState.getTimeLeft());
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

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  private void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  private void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   *     <p>private void showDialog(String title, String headerText, String message) { Alert alert =
   *     new Alert(Alert.AlertType.INFORMATION); alert.setTitle(title);
   *     alert.setHeaderText(headerText); alert.setContentText(message); alert.showAndWait(); }
   */
  @FXML
  private void enterBathroom() {
    GameState.currentRoom = "bathroom";
    App.setUi("bathroom");
  }

  @FXML
  private void enterComputerRoom() {
    GameState.currentRoom = "computerroom";
    App.setUi("computerroom");
  }

  @FXML
  private void highlightBathroom() {
    toBathroom.setOpacity(1);
    toBathroom.setScaleX(1.2);
    toBathroom.setScaleY(1.2);
  }

  @FXML
  private void removeHighlightBathroom() {
    toBathroom.setOpacity(0.3);
    toBathroom.setScaleX(1);
    toBathroom.setScaleY(1);
  }

  @FXML
  private void highlightComputerRoom() {
    toComputerRoom.setOpacity(1);
    toComputerRoom.setScaleX(1.2);
    toComputerRoom.setScaleY(1.2);
  }

  @FXML
  private void removeHighlightComputerRoom() {
    toComputerRoom.setOpacity(0.3);
    toComputerRoom.setScaleX(1);
    toComputerRoom.setScaleY(1);
  }

  // pressing on the quiz master to open the chat box
  @FXML
  private void clickQuizMaster(MouseEvent event) {

    App.setUi("chat");
  }

  private void startTimer() {
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
  }

  @FXML
  private void increaseGlobeSize(MouseEvent event) {
    if (GameState.isLightPuzzleSolved) {
      globe.setScaleX(1.05);
      globe.setScaleY(1.05);
    } else {
      return;
    }
  }

  @FXML
  private void decreaseGlobeSize(MouseEvent event) {
    globe.setScaleX(1);
    globe.setScaleY(1);
  }

  @FXML
  private void enterBlue() {
    if (GameState.isPuzzleSolved) {
      buttonBlue.setScaleX(1.2);
      buttonBlue.setScaleY(1.2);
    }
  }

  @FXML
  private void exitBlue() {
    if (GameState.isPuzzleSolved) {
      buttonBlue.setScaleX(1);
      buttonBlue.setScaleY(1);
    }
  }

  @FXML
  private void enterRed() {
    if (GameState.isPuzzleSolved) {
      buttonRed.setScaleX(1.2);
      buttonRed.setScaleY(1.2);
    }
  }

  @FXML
  private void exitRed() {
    if (GameState.isPuzzleSolved) {
      buttonRed.setScaleX(1);
      buttonRed.setScaleY(1);
    }
  }

  @FXML
  private void enterGreen() {
    if (GameState.isPuzzleSolved) {
      buttonGreen.setScaleX(1.2);
      buttonGreen.setScaleY(1.2);
    }
  }

  @FXML
  private void exitGreen() {
    if (GameState.isPuzzleSolved) {
      buttonGreen.setScaleX(1);
      buttonGreen.setScaleY(1);
    }
  }

  @FXML
  private void enterYellow() {
    if (GameState.isPuzzleSolved) {
      buttonYellow.setScaleX(1.2);
      buttonYellow.setScaleY(1.2);
    }
  }

  @FXML
  private void exitYellow() {
    if (GameState.isPuzzleSolved) {
      buttonYellow.setScaleX(1);
      buttonYellow.setScaleY(1);
    }
  }

  private void checkPasscode() {
    // If passcode is correct, pauses the timer.
    if (labelPasscode.getText().equals(GameState.password)) {
      GameState.timeline.pause();
      System.out.println("Success");
      buttonBlue.setOnMouseClicked(null);
      buttonBlue.setOnMouseEntered(null);
      buttonRed.setOnMouseClicked(null);
      buttonRed.setOnMouseEntered(null);
      buttonGreen.setOnMouseClicked(null);
      buttonGreen.setOnMouseEntered(null);
      buttonYellow.setOnMouseClicked(null);
      buttonYellow.setOnMouseEntered(null);
      rectangleDoorOne.setOpacity(0);
      rectangleDoorTwo.setOpacity(0);
      rectangleDoorThree.setOpacity(0);
      GameState.isRgbSolved = true;

    } else {
      labelPasscode.setText("");
    }
  }

  public void endGame() {
    // create a to mintues delay
    // then go to the win screen
    if (GameState.isRgbSolved) {
      Timeline timeline =
          new Timeline(
              new KeyFrame(
                  Duration.seconds(1.2),
                  new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                      GameState.currentRoom = "win";
                      App.setUi("win");
                    }
                  }));
      timeline.play();
    }
  }

  @FXML
  private void clickBlue(MouseEvent event) {
    // Adds the letter "B" to the passcode and checks if it is correct if the length of the passcode
    // is now equal to four.
    if (GameState.isPuzzleSolved) {
      labelPasscode.setText(labelPasscode.getText() + "⏚");
      try {
        Thread.sleep(250);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (labelPasscode.getText().length() == 4) {
        checkPasscode();
        endGame();
      }
    }
  }

  @FXML
  private void clickRed(MouseEvent event) {
    // Adds the letter "B" to the passcode and checks if it is correct if the length of the passcode
    // is now equal to four.
    if (GameState.isPuzzleSolved) {
      labelPasscode.setText(labelPasscode.getText() + "⍀");
      try {
        Thread.sleep(250);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (labelPasscode.getText().length() == 4) {
        checkPasscode();
        endGame();
      }
    }
  }

  @FXML
  private void clickGreen(MouseEvent event) {
    // Adds the letter "B" to the passcode and checks if it is correct if the length of the passcode
    // is now equal to four.
    if (GameState.isPuzzleSolved) {
      labelPasscode.setText(labelPasscode.getText() + "☌");
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (labelPasscode.getText().length() == 4) {
        checkPasscode();
        endGame();
      }
    }
  }

  @FXML
  private void clickYellow(MouseEvent event) {
    // Adds the letter "B" to the passcode and checks if it is correct if the length of the passcode
    // is now equal to four.
    if (GameState.isPuzzleSolved) {
      labelPasscode.setText(labelPasscode.getText() + "⊬");
      try {
        Thread.sleep(250);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (labelPasscode.getText().length() == 4) {
        checkPasscode();
        endGame();
      }
    }
  }

  @FXML
  private void clickGlobe(MouseEvent event) {
    if (!GameState.isGlobeFound) {
      objText.setText("You're missing the globe item required to access the puzzle!");
      return;
    } else {
      Thread thread =
                    new Thread(
                        () -> {
                          GameState.sendPrompt("The player has found the globe and started the jigsaw puzzle. The jigsaw puzzle represent an image of part of the locked room.");
                        });
                thread.start();
                GameState.isGlobeAccessed = true;
      App.setUi("puz");
    }
  }
}
