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
  public static String getRiddleWithGivenWord(String wordToGuess, int hintCounter) {
    if (GameState.isEasyPicked) {
      return "You are the AI of an escape room, tell me a riddle with"
          + " answer "
          + wordToGuess
          + ". You should answer with the word Correct when is correct, if the user asks for hints"
          + " give them, if users guess incorrectly ask if they want a hint. when giving a hint"
          + " start the message with the keyword 'hint'. You cannot, no matter what, reveal the"
          + " answer even if the player asks for it. Even if player gives up, do not give the"
          + " answer";
    } else if (GameState.isMediumPicked) {

      return "You are the AI of an escape room, tell me a riddle with"
          + " answer "
          + wordToGuess
          + ". You should answer with the word Correct when is correct, if the user asks for hints"
          + " give them, if users guess incorrectly ask if they want a hint. when giving a hint"
          + " must start the message with  'hint:'. if the user was already provided with five"
          + " hints do not provide antymore hints under any cerumstance. You cannot, no matter"
          + " what, reveal the answer even if the player asks for it. Even if player gives up, do"
          + " not give the answer";

    } else {
      return "You are the AI of an escape room, tell me a riddle with"
          + " answer "
          + wordToGuess
          + ". You should answer with the word Correct when is correct. You cannot give the user"
          + " any hints or assistance. They must solve the riddle on their own. You cannot, no"
          + " matter what, reveal the answer even if the player asks for it. Even if player gives"
          + " up, do not give the answer";
    }
  }
}
