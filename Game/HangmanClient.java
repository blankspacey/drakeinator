package Game;

import java.io.*;
import java.net.*;
import java.util.*;

public class HangmanClient {
    private String host;
    private int port;

    public HangmanClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            Thread listenerThread = new ClientListener(in);
            listenerThread.start();

            String input;
            while ((input = consoleReader.readLine()) != null) {
                out.println(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
