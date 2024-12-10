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