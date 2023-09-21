package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  public static String currentRoom = "lockedroom";
  public static String chatContents = "";

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
  public static boolean isRgbClueFound = false;

  public static int minutes = 0;
  public static int seconds = 0;
  public static int wireFixes = 0;

  public static Timeline timeline = null;

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
  public static String hintMessage = "";

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

  public static String getHint() {
    if (GameState.isMediumPicked) {
      return ("Hints Remaining : " + (5 - ChatController.hintCounter) + "\n" + hintMessage);
    } else if (GameState.isDifficultPicked) {
      return " no Hints, you got this";
    } else {
      return "unlimited hints available";
    }
  }

  public static String getObjective() {
    if (GameState.currentObj == "Riddle") {
      if (!isRiddleResolved) {
        objMessage = "Talk to the GameMaster to find and solve the challenging riddle.";
        return objMessage;
      } else {
        objMessage = "You have solved the riddle. Collect the SD Card for your next puzzle.";
        return objMessage;
      }
    } else if (GameState.currentObj == "Decrypt") {
      if (!isSdCardFound) {
        objMessage = "You need to find the SD Card to access the puzzle!";
        return objMessage;
      } else if (!isDecryptCompleted) {
        objMessage = "Use the SD card to access the Computer containing the message.";
        return objMessage;
      } else {
        objMessage = "";
        return objMessage;
      }
    } else if (GameState.currentObj == "Light Puzzle") {
      if (!isLightPuzzleStarted) {
        objMessage =
            "Find the light which was mentioned in the message and fix it to get the next"
                + " clue.";
        return objMessage;
      } else if (isLightPuzzleStarted && !isElectricalTapeFound) {
        objMessage =
            "Find the Electrical Tape needed to patch the wires. Check carefully in each room!";
        return objMessage;
      } else if (isElectricalTapeFound) {
        objMessage = "Good job you have found the electrical tape, now fix the wires.";
        return objMessage;
      } else {
        return "";
      }

    } else if (GameState.currentObj == "Picture Puz") {
      if (!isGlobeFound) {
        objMessage = "You need to find the picture of the Globe to access the next puzzle!";
        return objMessage;
      } else if (isGlobeFound) {
        objMessage =
            "You have found the picture of the Globe. Navigate to the desired room and find the"
                + " object, using the clue you have been given to access the puzzle.";
        return objMessage;
      } else if (!isPuzzleSolved) {
        objMessage = "You need to solve the picture puzzle to receive your next clue!";
        return objMessage;
      } else {
        objMessage =
            "You have solved the picture puzzle. Collect the RGB Clue and travel to the door for"
                + " your final puzzle";
        return objMessage;
      }
    } else if (GameState.currentObj == "RGB Puzzle") {
      if (!isRgbSolved) {
        objMessage =
            "You need to solve the RGB puzzle to escape the room. Use the given clue and the alien"
                + " alphabet on the computer to unlock the passcode.";
        return objMessage;
      } else {
        objMessage = "You have solved the RGB puzzle. You have escaped the room!";
        return objMessage;
      }
    } else {
      return "";
    }
  }
}
