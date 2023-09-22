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
  // Intialising variables required for the room

  @FXML private ImageView tape;
  @FXML private ImageView toLockedRoom;
  @FXML private Rectangle quizMaster;
  @FXML private Canvas gameMaster;
  @FXML private Label timer;
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
    // Displaying the items which are collected/not collected
    if (GameState.isRgbClueFound) {
      rgbClue1.setVisible(true);
      rgbClue1.setText(GameState.password);
    } else {
      rgbClue1.setVisible(false);
    }
    // Displaying the items which are collected/not collected
    objText.setText(GameState.getObjective());
    hintsText.setText(GameState.getHint());
    sdCard.setVisible(GameState.isSdCardFound);
    tape1.setVisible(GameState.isElectricalTapeFound);
    globe.setVisible(GameState.isGlobeFound);
    // Allowing the user to collect the tape only when the light puzzle is started
    if (!GameState.isLightPuzzleStarted) {
      tape.setOnMouseClicked(null);
      tape.setOnMouseEntered(null);
      tape.setOpacity(0);
    }
    // Tape disappeares once picked up
    if (GameState.isElectricalTapeFound) {
      tape.setOpacity(0);
      tape.setOnMouseClicked(null);
    }
    // Timer thread
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

  // pressing on the quiz master to open the chat box
  @FXML
  public void clickQuizMaster(MouseEvent event) {
    App.setUi("chat");
  }

  // Method to enter the locked room
  @FXML
  public void enterLockedRoom(MouseEvent event) {
    GameState.currentRoom = "lockedroom";
    App.setUi("lockedroom");
  }

  // Method for the highlighting of arrows when on them
  @FXML
  public void highlight() {
    toLockedRoom.setOpacity(1);
    toLockedRoom.setScaleX(1.2);
    toLockedRoom.setScaleY(1.2);
  }

  // Method for removing the highlight
  @FXML
  public void removeHighlight() {
    toLockedRoom.setOpacity(0.3);
    toLockedRoom.setScaleX(1);
    toLockedRoom.setScaleY(1);
  }

  // Method for starting the timer
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
  }

  // Method for enterign the decrypt puzzle
  @FXML
  public void enterDecrypt(MouseEvent event) {
    // Conditions letting the user to enter the puzzle
    if (!GameState.isRiddleResolved || !GameState.isSdCardFound) {
      objText.setText("You need the SD card to access the computer!");
    } else {
      GameState.currentRoom = "decrypt";
      if (!GameState.isComputerAccessed) {
        // Thread to send the prompt to the GPT to over look progress
        Thread thread =
            new Thread(
                () -> {
                  GameState.sendPrompt(
                      "The player has accessed the computer. The player must decipher an alien"
                          + " message using an onscreen alien alphabet.");
                });
        thread.start();
        GameState.isComputerAccessed = true;
      }
      App.setUi("decrypt");
    }
  }

  // Method for increasing the size of the image
  @FXML
  public void increaseSize(MouseEvent event) {
    if (!GameState.isRiddleResolved) {
      return;
    } else {
      hoverImage.setScaleX(1.05); // Increase the size by a factor of 1.2 horizontally
      hoverImage.setScaleY(1.05);
    }
  }

  // Method for decreasing the size of the image
  @FXML
  public void decreaseSize(MouseEvent event) {
    // decrease the size of the image
    hoverImage.setScaleX(1);
    hoverImage.setScaleY(1);
  }

  // Method for picking the tape up
  @FXML
  public void clickTape() {
    if (!GameState.isComputerAccessed) {
      // Thread to send the prompt to the GPT to over look progress
      Thread thread =
          new Thread(
              () -> {
                GameState.sendPrompt(
                    "The player has gotten the tape. The player must now go back to the broken"
                        + " light and fix the broken wires by simply clicking on areas that appear"
                        + " broken. The player does not need to consider the colours of the"
                        + " wires.");
              });
      thread.start();
      GameState.isComputerAccessed = true;
    }
    tape.setOpacity(0);
    tape.setOnMouseClicked(null);
    GameState.isElectricalTapeFound = true;
    objText.setText("Good job you have found the electrical tape, now fix the wires.");
    tape1.setVisible(true);
  }

  // Method for hovering over tape
  @FXML
  public void increaseTapeSize(MouseEvent event) {
    tape.setScaleX(1.2);
    tape.setScaleY(1.2);
  }

  // Method for decreasing the size of the tape
  @FXML
  public void decreaseTapeSize(MouseEvent event) {
    tape.setScaleX(1);
    tape.setScaleY(1);
  }
}
