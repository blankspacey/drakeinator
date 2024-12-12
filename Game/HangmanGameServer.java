package Game;

public class HangmanGameServer {
    public static void main(String[] args) {
        String randomWord = getRandomWord();
        HangmanServer server = new HangmanServer(12345, randomWord, 10);
        server.start();
    }

    private static String getRandomWord() {
        String[] words = {"AWESOME", "JAVA", "PROGRAMMING", "HANGMAN", "COMPUTER", "SOFTWARE", "DEVELOPER", "CODE"};
        int randomIndex = (int) (Math.random() * words.length);
        return words[randomIndex];
    }


}