package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

/** Controller class for the room view. */
public class LockedRoomController {

  @FXML private Rectangle toBathroom;
  @FXML private Rectangle toComputerRoom;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
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
   *
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }
*/
  @FXML
  public void enterBathroom(){
    App.setUi("bathroom");
  }

  @FXML
  public void enterComputerRoom(){
    App.setUi("computerroom");
  }

  @FXML
  public void highlightBathroom(){
    toBathroom.setOpacity(0.7);
  }

  @FXML 
  public void removeHighlightBathroom(){
    toBathroom.setOpacity(0.4);
  }

  @FXML
  public void highlightComputerRoom(){
    toComputerRoom.setOpacity(0.7);
  }

  @FXML 
  public void removeHighlightComputerRoom(){
    toComputerRoom.setOpacity(0.4);
  }
}
