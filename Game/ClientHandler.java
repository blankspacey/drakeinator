package Game;

import java.io.*;
import java.net.*;
import java.util.*;

class ClientHandler implements Runnable {
    private Socket socket;
    private HangmanServer server;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, HangmanServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sendMessage("Welcome to Hangman!");
            server.broadcast("A new player has joined the game.");

            while (!server.getGameLogic().isGameOver()) {
                sendGameState();
                String input = in.readLine();

                if (input != null && input.length() == 1) {
                    char guess = input.charAt(0);
                    boolean correct = server.getGameLogic().makeGuess(guess);

                    if (correct) {
                        server.broadcast("Correct guess: " + guess);
                    } else {
                        server.broadcast("Incorrect guess: " + guess);
                    }
                }
            }

            sendGameState();
            if (server.getGameLogic().isWin()) {
                server.broadcast("Game over! The word was: " + server.getGameLogic().getSecretWord() + ". Players win!");
            } else {
                server.broadcast("Game over! The word was: " + server.getGameLogic().getSecretWord() + ". You lost.");
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
}