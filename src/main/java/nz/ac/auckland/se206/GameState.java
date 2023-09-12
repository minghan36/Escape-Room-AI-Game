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
  public static boolean isTwoMinutesPicked = false;
  public static boolean isFourMinutesPicked = false;
  public static boolean isSixMinutesPicked = false;
}
