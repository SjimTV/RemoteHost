package com.sjimtv.server;

import com.sjimtv.App;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private final ServerCommunicator serverCommunicator;

    public static final int SERVER_PORT = 4444;
    private boolean isServerRunning;
    private PrintWriter bufferOut;
    private BufferedReader bufferIn;
    private ServerSocket serverSocket;
    private Socket clientSocket;



    public Server(ServerCommunicator serverCommunicator) {
       this.serverCommunicator = serverCommunicator;
    }

    @Override
    public void run() {
        super.run();
        try {
            System.out.println("Starting Server");
            runServer();
        } catch (IOException e) {
            System.out.println("Error with starting Server..");
            e.printStackTrace();
        }

    }

    private void runServer() throws IOException {
        isServerRunning = true;
        openSockets();
        initializeBufferCommunication();
        listenForMessages();
    }

    private void openSockets() throws IOException {
        System.out.println("Opening sockets...");
        serverSocket = new ServerSocket(SERVER_PORT);
        System.out.println("Waiting for connection...");
        clientSocket = serverSocket.accept();
        System.out.println("Connection accepted!");
    }

    private void initializeBufferCommunication() throws IOException {
        bufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
        bufferIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    private void listenForMessages() {
        while (isServerRunning) {
            String message = tryReadMessage();
            if (message != null) messageReceived(message);
        }
    }

    private String tryReadMessage() {
        try {
            String message = bufferIn.readLine();
            if (isClientDead(message)) close();
            return message;
        } catch (IOException e) {
            System.out.println("Error reading message: " + e.getMessage());
            return null;
        }
    }

    private boolean isClientDead(String message){

        return message == null;

    }

    private void messageReceived(String message) {
        serverCommunicator.getMessage(message);
    }

    public void sendMessage(String message) {
        if (bufferOut != null && !bufferOut.checkError()) {
            bufferOut.println(message);
            bufferOut.flush();
        }
    }

    public void close() {
        System.out.println("Disconnecting server");
        isServerRunning = false;
        cleanupBuffer();
        closeAndEraseSockets();

        run();

    }

    private void cleanupBuffer() {
        if (bufferOut != null) {
            bufferOut.flush();
            bufferOut.close();
            bufferOut = null;
        }
    }

    private void closeAndEraseSockets() {
        try {
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        clientSocket = null;
        serverSocket = null;
    }
}
