package nz.ac.auckland.se206.controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class IntroController {
  @FXML private Rectangle level;
  @FXML private Rectangle time;
  @FXML private Rectangle Start;
  @FXML private Label title;
  private boolean levelIsPicked;
  private boolean timeIsPicked;

  public void initialize() {
    // create a translate transition for the label
    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), title);

    // set the Y-axis translation value
    translateTransition.setByY(-10);

    // set the number of cycles for the animation
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);

    // Set auto-reverse to true to make the label return to its original position
    translateTransition.setAutoReverse(true);

    // Start the animation
    translateTransition.play();
  }

  @FXML
  public void pickLevel(MouseEvent event) {
    // switch to the new scene for the player to pick a level
  }

  @FXML
  public void pickTime(MouseEvent event) {
    // switch to a new scene for the player to pick a time duration
  }

  @FXML
  public void startGame(MouseEvent event) {
    if (levelIsPicked && timeIsPicked) {
      App.setUi("room");
    } else {
      showDialog("Info", "select", "You need to select a time and level to start the game");
    }
  }

  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
