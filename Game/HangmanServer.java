package Game;

import java.io.*;
import java.net.*;
import java.util.*;

class HangmanServer {
    private int port;
    private GameLogic gameLogic;
    private List<ClientHandler> clients;

    public HangmanServer(int port, String secretWord, int maxAttempts) {
        this.port = port;
        this.gameLogic = new GameLogic(secretWord, maxAttempts);
        this.clients = new ArrayList<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcast(String message, String sender) {
        for (ClientHandler client : clients) {
            client.sendMessage(sender + ": " + message);
        }
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }
}