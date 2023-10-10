package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

/** Controller class for the loss screen view. */
public class EndController {
  // Intialisng all the variables for the scene
  @FXML private ImageView alien;
  @FXML private Button buttonReplay;

  private Image[] runningImages = new Image[4];
  private int currentImageIndex = 0;

  /** Initializes the room view, it is called when the room loads. */
  @FXML
  public void initialize() {
    // Load images RUN1 to RUN4
    for (int i = 0; i < 4; i++) {
      runningImages[i] = new Image(("images/RUN" + (i + 1) + ".png"));
    }

    // Set initial image to Alien
    alien.setImage(runningImages[currentImageIndex]);

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
                new KeyValue(alien.layoutXProperty(), 1100 - alien.getFitWidth())));
    moveTimeline.setCycleCount(Timeline.INDEFINITE);
    moveTimeline.setOnFinished(evt -> alien.setLayoutX(0)); // Reset position to leftmost
    moveTimeline.play();
  }

  // Switching the image
  private void switchImage() {
    currentImageIndex = (currentImageIndex + 1) % 4;
    alien.setImage(runningImages[currentImageIndex]);
  }

  /** Resets the game for the user to play again. */
  @FXML
  private void reset() {
    // Resetting all the variables
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
    // Setting all the item and puzzle variables to false, so that the user has to
    // complete the
    // puzzles again
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
    // Resetting the game attributes such as the timer and the objectives
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
    GameState.isComputerAccessed = false;
    GameState.isGlobeAccessed = false;
    // Resetting the game attributes such as the puzzles and puzzle answers
    GameState.riddleAnswer = GameState.riddleAnswers[(int) (Math.random() * 3)];
    GameState.password =
        GameState.randomLetters[(int) (Math.random() * 4)]
            + GameState.randomLetters[(int) (Math.random() * 4)]
            + GameState.randomLetters[(int) (Math.random() * 4)]
            + GameState.randomLetters[(int) (Math.random() * 4)];
    // Setting the rooom to the intro room so the user is able to restart the game
    // and select their
    // options
    App.setUi("intro");
  }
}
