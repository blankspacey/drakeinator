package Game;

import java.io.*;
import java.net.*;
import java.util.*;

class GameLogic {
    /*
     * SecretWord --> parola segreta da scoprire
     * displayWord --> parola vista dagli utenti che giocano (StringBuilder perchè piu prestante di String)
     * incorrectGuesses --> numero di tentativi (Set di chars)
     */
    private String secretWord;
    private StringBuilder displayWord;
    private Set<Character> incorrectGuesses;
    private int maxAttempts;

    public GameLogic(String secretWord, int maxAttempts) {
        this.secretWord = secretWord.toUpperCase();
        this.displayWord = new StringBuilder("_".repeat(secretWord.length()));
        this.incorrectGuesses = new HashSet<>();
        this.maxAttempts = maxAttempts;
    }

    public synchronized boolean makeGuess(char guess) {
        guess = Character.toUpperCase(guess);
        if (secretWord.indexOf(guess) != -1) {
            for (int i = 0; i < secretWord.length(); i++) {
                if (secretWord.charAt(i) == guess) {
                    displayWord.setCharAt(i, guess);
                }
            }
            return true;
        } else {
            incorrectGuesses.add(guess);
            return false;
        }
    }

    public String getDisplayWord() {
        return displayWord.toString();
    }

    public int getRemainingAttempts() {
        return maxAttempts - incorrectGuesses.size();
    }

    public boolean isGameOver() {
        return incorrectGuesses.size() >= maxAttempts || displayWord.toString().equals(secretWord);
    }

    public boolean isWin() {
        return displayWord.toString().equals(secretWord);
    }

    public String getSecretWord() {
        return secretWord;
    }
}