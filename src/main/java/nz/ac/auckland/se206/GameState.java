package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  public static String currentRoom = "lockedroom";
  public static String chatContents;

  public static ChatCompletionRequest chatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  public static boolean isElectricalTapeFound = false;

  public static boolean isEasyPicked = false;
  public static boolean isMediumPicked = false;
  public static boolean isDifficultPicked = false;
  public static boolean isDecryptCompleted = false;
  public static boolean isLightPuzzleStarted = false;
  public static boolean isLightPuzzleSolved = false;
  public static boolean isPuzzleSolved = false;
  public static boolean isWireOneFixed = false;
  public static boolean isWireTwoFixed = false;
  public static boolean isWireThreeFixed = false;
  public static boolean isWireFourFixed = false;
  public static boolean isRgbSolved = false;
  public static boolean isSdCardFound = false;
  public static boolean isGlobeFound = false;
  public static boolean isGameMasterLoaded = false;

  public static int minutes = 0;
  public static int seconds = 0;
  public static int wireFixes = 0;

  public static Timeline timeline;

  // choose a random number between zero and two
  public static int randomNum = (int) (Math.random() * 3);
  // create string array having first second third
  public static String[] randomLights = {"first", "second", "third"};
  public static String randomLight = randomLights[randomNum];

  public static String[] objectives = {
    "Riddle", "Decrypt", "Light Puzzle", "Picture Puz", "RGB Puzzle"
  };

  public static String currentObj = objectives[0];
  public static String objMessage = "";

  /**
   * Returns time left in the round.
   *
   * @return String of the time left in minutes:seconds format.
   */
  public static String getTimeLeft() {
    if (seconds == 0) {
      return String.valueOf(minutes) + ":00";
    } else if (seconds < 10) {
      return String.valueOf(minutes) + ":0" + String.valueOf(seconds);
    } else {
      return String.valueOf(minutes) + ":" + String.valueOf(seconds);
    }
  }

  public static String[] randomLetters = {"⍀", "☌", "⏚", "⊬"};
  public static String password =
      randomLetters[(int) (Math.random() * 4)]
          + randomLetters[(int) (Math.random() * 4)]
          + randomLetters[(int) (Math.random() * 4)]
          + randomLetters[(int) (Math.random() * 4)];

  public static void startTimer() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    // Counts down the timer.
                    if (GameState.seconds == 0) {
                      GameState.minutes--;
                      GameState.seconds = 59;
                    } else if (GameState.seconds > 0) {
                      GameState.seconds--;
                    }
                  }
                }));

    timeline.setCycleCount((GameState.minutes * 60) + GameState.seconds - 1);
    timeline.play();
  }

  public String getObjective() {
    if (GameState.currentObj == "Riddle") {
      if (!isRiddleResolved) {
        objMessage = "Talk to the GameMaster to find and solve the challenging riddle.";
        return objMessage;
      } else {
        objMessage = "You have solved the riddle. Collect the SD Card for your next puzzle.";
        return objMessage;
      }
    } else if (GameState.currentObj == "Decrypt") {
      if (!isDecryptCompleted) {
        objMessage = "Decrypt the message to find the password.";
        return objMessage;
      } else {
        objMessage =
            "You have decrypted the message. Use the message you've deciphered to find the next"
                + " puzzle.";
        return objMessage;
      }
    } else if (GameState.currentObj == "Light Puzzle") {
      if (!isGlobeFound) {
        objMessage = "You need to find the picture of the Globe to access the puzzle!";
        return objMessage;
      } else if (!isLightPuzzleSolved) {
        objMessage = "You need to patch the wires to solve the light puzzle using the tape.";
        return objMessage;
      } else {
        objMessage =
            "You have solved the light puzzle. Collect the Picture of the Globe and travel to your"
                + " next puzzle.";
        return objMessage;
      }
    } else if (GameState.currentObj == "Picture Puz") {
      if (!isPuzzleSolved) {
        objMessage = "Solve the puzzle to find the RGB code.";
        return objMessage;
      } else {
        objMessage =
            "You have solved the puzzle. Collect the RGB code and travel to your next puzzle.";
        return objMessage;
      }
    } else if (GameState.currentObj == "RGB Puzzle") {
      if (!isRgbSolved) {
        objMessage = "Solve the RGB puzzle to find the SD Card.";
        return objMessage;
      } else {
        objMessage = "You have solved the RGB puzzle. Collect the SD Card and escape the room.";
        return objMessage;
      }
    } else {
      return "Error";
    }
  }
}
