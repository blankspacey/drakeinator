package Game;

public class HangmanGameServer {
    public static void main(String[] args) {
        HangmanServer server = new HangmanServer(12345, "AWESOME", 6);
        server.start();
    }
}
