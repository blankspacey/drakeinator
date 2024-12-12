package Game;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HangmanClient {
    private String host;
    private int port;
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JLabel wordLabel;
    private JLabel attemptsLabel;
    private PrintWriter out;
    private GameLogic gameLogic;
    private Socket socket;

    public HangmanClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.gameLogic = new GameLogic(host, port);

        setupGUI();
    }

    private void startClient() {
        BufferedReader in = null;
        PrintWriter outWriter = null;

        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outWriter = new PrintWriter(socket.getOutputStream(), true);
            this.out = outWriter;
    
            String response;
            while ((response = in.readLine()) != null) {
                if (response.startsWith("Current word:")) {
                    wordLabel.setText(response);
                } else if (response.startsWith("Remaining attempts:")) {
                    attemptsLabel.setText(response);
                } else {
                    chatArea.append(response + "\n");
                }
            }
        } catch (IOException e) {
            chatArea.append("Error: " + e.getMessage() + "\n");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (outWriter != null) {
                    outWriter.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                chatArea.append("Error closing resources: " + ex.getMessage() + "\n");
            }
        }
    }
    
    private void setupGUI() {
        String attemptsRemaining = String.valueOf(gameLogic.getRemainingAttempts());

        frame = new JFrame("Hangman Client");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel gamePanel = new JPanel(new GridLayout(2, 1));
        wordLabel = new JLabel("Current Word: _ _ _ _ _", SwingConstants.CENTER);
        attemptsLabel = new JLabel("Remaining Attempts: " + attemptsRemaining, SwingConstants.CENTER);
        gamePanel.add(wordLabel);
        gamePanel.add(attemptsLabel);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        inputField.addActionListener(_ -> sendInput());

        JButton closeButton = new JButton("Close Game");
        closeButton.addActionListener(_ -> {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            frame.dispose();
        });
    
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(closeButton, BorderLayout.EAST);
    
        frame.add(gamePanel, BorderLayout.NORTH);
        frame.add(chatScrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void sendInput() {
        String input = inputField.getText().trim();
        inputField.setText("");
        if (out != null) {
            out.println(input);
        }
    }

    public static void main(String[] args) {
        HangmanClient client = new HangmanClient("localhost", 12345);
        client.startClient();
    }
}