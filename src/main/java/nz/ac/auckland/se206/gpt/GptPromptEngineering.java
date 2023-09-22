package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {


  /* 
  private static String[] flowSteps = {
    "Hint: first, the you must solve the riddle to retrieve an SD card.",
    "Hint: once the riddle is solved, you need to use the SD card in the computer room to decrypt"
        + " a message.",
    "Hint: the decrypted message will lead you to the next room where you repair light cables.",
    "Hint: after fixing the light cable, you will discover a piece of an item located in another"
        + " room.",
    "Hint: go to the room where the item in the picture is found and works on the picture puzzle to"
        + " reassemble the item.",
    "Hint: Solving this puzzle is the key to the escape."
  };

  private static int currentStep = 0;
*/
  /**
   * Provides the next step in the game flow.
   *
   * @return A string representing the next step or a message informing that all steps are provided.
   
  public static String getNextGameFlowStep() {
    if (currentStep < flowSteps.length) {
      return flowSteps[currentStep++];
    } else {
      return "You've been provided with all the steps. Good luck!";
    }
  }
  */


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
          + " not reveal the answer.";
    } else if (GameState.isMediumPicked) {

      return "You are the game Master of an escape room, tell me a riddle with the answer "
          + wordToGuess
          + ". You should answer with the word 'Correct' when it's correct. If the user asks for"
          + " hints, give them. If users guess incorrectly, ask if they want a hint. When giving"
          + " a hint, start the message with 'hint:'. The player has a limit of 5 hints and clue overall."
          + " Once reached, do not provide any more hints under any circumstance. You cannot, under any"
          + " circumstances, reveal the answer, even if the player asks for it. Even if the"
          + " player gives up, do not reveal the answer.";

    } else {
      return "You are the game Master of an escape room, tell me a riddle with the answer "
          + wordToGuess
          + ". Respond with 'Correct' if the answer matches. In this escape room challenge, the"
          + " player's goal is to decode various puzzles and riddles to find the key that unlocks"
          + " the door. It's crucial for the player to solve the riddle without any assistance in the form of help, hints, or clues to"
          + " progress further. You cannot, under any"
          + " circumstances, reveal the answer, even if the player asks for it. Even if the"
          + " player gives up, do not reveal the answer.";
    }
  }

  /*
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
  */
}
