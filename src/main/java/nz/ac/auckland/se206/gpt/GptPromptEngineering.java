package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {


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
}
