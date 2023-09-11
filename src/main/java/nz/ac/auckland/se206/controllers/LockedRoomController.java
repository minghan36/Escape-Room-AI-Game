package nz.ac.auckland.se206.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
// import javafx.scene.control.Alert;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

/** Controller class for the room view. */
public class LockedRoomController {

  @FXML private Rectangle toBathroom;
  @FXML private Rectangle toComputerRoom;
  @FXML private Rectangle quizMaster;
  @FXML private Canvas gameMaster;
  private Image[] alienImages;
  private int currentImageIndex = 0;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
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
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
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
  public void enterBathroom() {
    App.setUi("bathroom");
  }

  @FXML
  public void enterComputerRoom() {
    App.setUi("computerroom");
  }

  @FXML
  public void highlightBathroom() {
    toBathroom.setOpacity(0.7);
  }

  @FXML
  public void removeHighlightBathroom() {
    toBathroom.setOpacity(0.4);
  }

  @FXML
  public void highlightComputerRoom() {
    toComputerRoom.setOpacity(0.7);
  }

  @FXML
  public void removeHighlightComputerRoom() {
    toComputerRoom.setOpacity(0.4);
  }

  // pressing on the quiz master to open the chat box
  @FXML
  public void clickQuizMaster(MouseEvent event) {
    App.setUi("chat");
  }
}
