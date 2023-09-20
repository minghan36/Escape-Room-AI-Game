package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  public static String currentRoom = "lockedroom";

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

  public static int minutes = 0;
  public static int seconds = 0;
  public static int wireFixes = 0;

  public static Timeline timeline;

  // choose a random number between zero and two
  public static int randomNum = (int) (Math.random() * 3);
  // create string array having first second third
  public static String[] randomLights = {"first", "second", "third"};
  public static String randomLight = randomLights[randomNum];

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

  public static String[] randomLetters = {"R", "G", "B", "Y"};
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
}
