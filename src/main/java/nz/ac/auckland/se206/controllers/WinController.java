package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

public class WinController {

  @FXML private ImageView alein;
  @FXML private Button buttonReplay;
  @FXML private Label timeLeft;

  private Image[] runningImages = new Image[4];
  private int currentImageIndex = 0;

  @FXML
  public void initialize() {
    // Load images RUN1 to RUN4
    for (int i = 0; i < 4; i++) {
      runningImages[i] = new Image(("images/RUN" + (i + 1) + ".png"));
    }

    timeLeft.setText("YOU ESCAPED WITH " + GameState.getTimeLeft() + " LEFT!");

    // Set initial image to Alein
    alein.setImage(runningImages[currentImageIndex]);

    // Timeline to switch images
    double switchSpeed = 0.2; // adjust for faster or slower switching
    Timeline switchImageTimeline =
        new Timeline(new KeyFrame(Duration.seconds(switchSpeed), evt -> switchImage()));
    switchImageTimeline.setCycleCount(Timeline.INDEFINITE);
    switchImageTimeline.play();

    // Timeline to move the ImageView
    double movementDuration =
        10.0; // Adjust this for the entire movement duration across the screen
    Timeline moveTimeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(movementDuration),
                new KeyValue(alein.layoutXProperty(), 1100 - alein.getFitWidth())));
    moveTimeline.setCycleCount(Timeline.INDEFINITE);
    moveTimeline.setOnFinished(evt -> alein.setLayoutX(0)); // Reset position to leftmost
    moveTimeline.play();
  }

  private void switchImage() {
    currentImageIndex = (currentImageIndex + 1) % 4;
    alein.setImage(runningImages[currentImageIndex]);
  }

  @FXML
  private void reset() {
    GameState.currentRoom = "lockedroom";
    GameState.chatContents = "";
    GameState.chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    GameState.isKeyFound = false;
    GameState.isElectricalTapeFound = false;
    GameState.isEasyPicked = false;
    GameState.isMediumPicked = false;
    GameState.isDifficultPicked = false;
    GameState.isDecryptCompleted = false;
    GameState.isLightPuzzleStarted = false;
    GameState.isLightPuzzleSolved = false;
    GameState.isPuzzleSolved = false;
    GameState.isWireOneFixed = false;
    GameState.isWireTwoFixed = false;
    GameState.isWireThreeFixed = false;
    GameState.isWireFourFixed = false;
    GameState.isRgbSolved = false;
    GameState.isSdCardFound = false;
    GameState.isGlobeFound = false;
    GameState.isGameMasterLoaded = false;
    GameState.isRgbClueFound = false;
    GameState.minutes = 0;
    GameState.seconds = 0;
    GameState.wireFixes = 0;
    GameState.timeline = null;
    GameState.randomNum = (int) (Math.random() * 3);
    GameState.randomLight = GameState.randomLights[GameState.randomNum];
    GameState.currentObj = GameState.objectives[0];
    GameState.objMessage = "";
    GameState.isRiddleResolved = false;
    GameState.hintMessage = "";
    GameState.hintCounter = 0;
    GameState.password =
        GameState.randomLetters[(int) (Math.random() * 4)]
            + GameState.randomLetters[(int) (Math.random() * 4)]
            + GameState.randomLetters[(int) (Math.random() * 4)]
            + GameState.randomLetters[(int) (Math.random() * 4)];
    App.setUi("intro");
  }
}
