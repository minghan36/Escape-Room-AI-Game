package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  public static String currentRoom = "lockedroom";

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  public static boolean isEasyPicked = false;
  public static boolean isMediumPicked = false;
  public static boolean isDifficultPicked = false;
  public static boolean isDecryptCompleted = false;

  public static int minutes = 0;
  public static int seconds = 0;

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
}
