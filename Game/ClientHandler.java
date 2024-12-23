package Game;

import java.io.*;
import java.net.*;
import java.util.*;

class ClientHandler implements Runnable {
    private Socket socket;
    private HangmanServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket, HangmanServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("Enter your username:");
            username = in.readLine();
            if (username == null || username.isEmpty()) {
                username = "Player" + socket.getPort();
            }

            out.println("Welcome to Hangman, " + username + "!");
            server.broadcast(username + " has joined the game.", "SERVER");

            while (!server.getGameLogic().isGameOver()) {
                sendGameState();
                String input = in.readLine();
                String prefix = "/";

                if (input != null && input.length() == 2 && input.startsWith(prefix)) {
                    char guess = input.charAt(1);
                    boolean correct = server.getGameLogic().makeGuess(guess);

                    if (correct) {
                        server.broadcast(username + " guessed right --> " + Character.toUpperCase(guess) + "!", "SERVER");
                    } else {
                        server.broadcast(username + " guessed wrong --> " + Character.toUpperCase(guess) + "!", "SERVER");
                    }
                } else {
                    if (input != null) server.broadcast(input, username);
                }
                broadcastGameState();
            }

            sendGameState();
            if (server.getGameLogic().isWin()) {
                server.broadcast("Game over! The word was: " + server.getGameLogic().getSecretWord() + ". Players win!", "SERVER");
            } else {
                server.broadcast("Game over! The word was: " + server.getGameLogic().getSecretWord() + ". You lost.", "SERVER");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void sendGameState() {
        GameLogic gameLogic = server.getGameLogic();
        sendMessage("Current word: " + gameLogic.getDisplayWord());
        sendMessage("Remaining attempts: " + gameLogic.getRemainingAttempts());
    }

    private void broadcastGameState() {
        GameLogic gameLogic = server.getGameLogic();
        server.broadcastSystem("Current word: " + gameLogic.getDisplayWord());
        server.broadcastSystem("Remaining attempts: " + gameLogic.getRemainingAttempts());
    }
}
