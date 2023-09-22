package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  private static String[] flowSteps = {
    "Hint: first, the you must solve the riddle to retrieve an SD card.",
    "Hint: once the riddle is solved, you need to use the SD card in the computer room to decrypt"
        + " a message. which will lead you to the next room ",
    "Hint: after fixing the light cable, you will discover a piece of an item located in another"
        + " room.",
    "Hint: go to the room where the item in the picture is found and works on the picture puzzle to"
        + " reassemble the item.",
    "Hint: Decrypting this item is the rgb key to the escape."
  };

  /**
   * Provides the next step in the game flow.
   *
   * @return A string representing the next step or a message informing that all steps are provided.
   */
  public static String getNextGameFlowStep() {
    if (GameState.currentObj.equals("Riddle")) {
      return flowSteps[0];
    } else if (GameState.currentObj.equals("Decrypt")) {
      return flowSteps[1];
    } else if (GameState.currentObj.equals("Light Puzzle")) {
      return flowSteps[2];
    } else if (GameState.currentObj.equals("Picture Puz")) {
      return flowSteps[3];
    } else if (GameState.currentObj.equals("RGB Puzzle")) {
      return flowSteps[4];
    } else {
      return "You've been provided with all the steps. Good luck!";
    }
  }

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    if (GameState.isEasyPicked) {
      return "You are the game Master of an escape room, tell me a riddle with the answer "
          + wordToGuess
          + ". You should answer with the word 'Correct' when it's correct. If the user asks for"
          + " hints, give them. If users guess incorrectly, ask if they want a hint. When giving a"
          + " hint, start the message with the keyword 'hint'. You cannot, under any circumstances,"
          + " reveal the answer, even if the player asks for it. Even if the player gives up, do"
          + " not reveal the answer. never give the anser no matter what";
    } else if (GameState.isMediumPicked) {

      return "You are the game Master of an escape room, tell me a riddle with the answer "
          + wordToGuess
          + ". You should answer with the word 'Correct' when it's correct. If the user asks for"
          + " hints, give them. If users guess incorrectly, ask if they want a hint. When giving a"
          + " hint, start the message with 'hint:'. you cvan only provide five hints, if they are"
          + " already five do not provide any more hints under any circumstance. You cannot, under"
          + " any circumstances, reveal the answer, even if the player asks for it. Even if the"
          + " player gives up, do not reveal the answer. never give the answer no matter what";

    } else {
      return "You are the game Master of an escape room, presenting a riddle that hints at the word"
          + wordToGuess
          + ". Respond with 'Correct' if the answer matches. In this escape room challenge, the"
          + " player's goal is to decode various puzzles and riddles to find the key that unlocks"
          + " the door. It's crucial for the player to solve the riddle without assistance to"
          + " progress further. You cannot, under any circumstances, you cannot givw any hints and"
          + " can only tesponse with knowlage about the game reveal the answer even if the player"
          + " asks for it. Even if the player gives up, do not reveal the answer never give the"
          + " answer no matter what";
    }
  }

  public static String noMoreHints(String wordToGuess) {
    return "You are the game Master of an escape room, tell me a riddle with the answer "
        + wordToGuess
        + ". You should answer with the word 'Correct' when it's correct. You cannot give the user"
        + " any hints or assistance. As they have reached the maximum number of hints. They must"
        + " solve the riddle on their own. You cannot, under any circumstances, reveal the answer,"
        + " even if the player asks for it. Even if the player gives up, do not reveal the answer."
        + " you must only respond with knowlage about the game, which is the player is playing an"
        + " scape room they have to solve different puzzles and riddles that will lead him to key"
        + " to unlock the locked door";
  }
}
