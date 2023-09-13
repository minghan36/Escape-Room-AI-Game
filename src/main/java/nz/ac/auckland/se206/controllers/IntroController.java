package nz.ac.auckland.se206.controllers;


import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;

public class IntroController {

  @FXML private Rectangle time;
  @FXML private Rectangle Start;
  @FXML private Label title;
  @FXML private Button easy;
  @FXML private Button medium;
  @FXML private Button difficult;
  private boolean levelIsPicked;
  private boolean timeIsPicked;
  @FXML private Label chooseEasy;
  @FXML private Label chooseMedium;
  @FXML private Label chooseDifficult;
  @FXML private Label minTwo;
  @FXML private Label minFour;
  @FXML private Label minSix;
  @FXML private Button twoMin;
  @FXML private Button fourMin;
  @FXML private Button sixMin;
  @FXML private Label startStatus;
  @FXML private Label levelDetail;

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
      App.setUi("lockedroom");
    } else {
      startStatus.setText("Please pick a level and a time duration");
    }
  }

  // private void showDialog(String title, String headerText, String message) {
  //   Alert alert = new Alert(Alert.AlertType.INFORMATION);
  //   alert.setTitle(title);
  //   alert.setHeaderText(headerText);
  //   alert.setContentText(message);
  //   alert.showAndWait();
  // }

  @FXML
  private void easyPicked(MouseEvent event) {
    levelIsPicked = true;
    chooseEasy.setText("CHOSEN");
    chooseDifficult.setText("");
    chooseMedium.setText("");
    GameState.isEasyPicked = true;
    GameState.isMediumPicked = false;
    GameState.isDifficultPicked = false;
    if (timeIsPicked) {
      startStatus.setText("");
    }
    levelDetail.setText(
        "EASY: You can ask as many questions as you like and get unlimited hints from the"
            + " Gamemaster");
  }

  @FXML
  private void mediumPicked(MouseEvent event) {
    levelIsPicked = true;
    GameState.isMediumPicked = true;
    GameState.isEasyPicked = false;
    GameState.isDifficultPicked = false;
    chooseMedium.setText("CHOSEN");
    chooseDifficult.setText("");
    chooseEasy.setText("");
    if (timeIsPicked) {
      startStatus.setText("");
    }
    levelDetail.setText(
        "MEDIUM: You have a maximum of five hints from the"
            + " Gamemaster, but feel free to talk to him");
  }

  @FXML
  private void difficultPicked(MouseEvent event) {
    levelIsPicked = true;
    GameState.isDifficultPicked = true;
    GameState.isEasyPicked = false;
    GameState.isMediumPicked = false;
    chooseDifficult.setText("CHOSEN");
    chooseEasy.setText("");
    chooseMedium.setText("");
    if (timeIsPicked) {
      startStatus.setText("");
    }
    levelDetail.setText(
        "DIFFICULT: You are not able to get hints from the"
            + " Gamemaster, but feel free to talk to him");
  }

  @FXML
  private void twoPicked(MouseEvent event) {
    timeIsPicked = true;
    GameState.minutes = 2;
    minTwo.setText("CHOSEN");
    minFour.setText("");
    minSix.setText("");

    if (levelIsPicked) {
      startStatus.setText("");
    }
  }

  @FXML
  private void fourPicked(MouseEvent event) {
    timeIsPicked = true;
    GameState.minutes = 4;
    minFour.setText("CHOSEN");
    minTwo.setText("");
    minSix.setText("");
    if (levelIsPicked) {
      startStatus.setText("");
    }
  }

  @FXML
  private void sixPicked(MouseEvent event) {
    timeIsPicked = true;
    GameState.minutes = 6;
    minSix.setText("CHOSEN");
    minTwo.setText("");
    minFour.setText("");
    if (levelIsPicked) {
      startStatus.setText("");
    }
  }

}
