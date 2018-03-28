package pw.artwhite.server;

/**
 * Created by artwhite on 10/03/2018.
 */

import pw.artwhite.net.TCPConnect;
import pw.artwhite.net.TCPListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPListener {

    public static void main(String[] args) {
        new ChatServer();
    }

    private final ArrayList<TCPConnect> connections = new ArrayList<>();

    private ChatServer() {
        System.out.println("Server running");

        try (ServerSocket serverSocket = new ServerSocket(8289)) {
            while (true) {

                new TCPConnect(this, serverSocket.accept());

            }
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnect tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        for (TCPConnect connection: connections) {
            connection.sendString(value);

        }
    }

    @Override
    public synchronized void onReceiveString(TCPConnect tcpConnection, String str) {
        sendToAllConnections(str);
    }

    @Override
    public synchronized void onDisconnect(TCPConnect tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnect tcpConnection, IOException e) {
        System.out.println("TCPConnection exception: " + e);
    }
}