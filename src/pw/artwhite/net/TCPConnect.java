package pw.artwhite.net;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by artwhite on 10/03/2018.
 */
public class TCPConnect {
    private final Socket socket;
    private final Thread thread;
    private final TCPListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    private TCPConnect(Socket socket, TCPListener eventListener) throws IOException {
        this.socket = socket;

        this.eventListener = eventListener;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Charset.forName("UTF-8")));
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnect.this);
                    while (!thread.isInterrupted()){
                        eventListener.onReceiveString(TCPConnect.this,in.readLine());
                    }

                } catch (IOException e) {
                    eventListener.onException(TCPConnect.this,e);
                } finally {
                    eventListener.onDisconnect(TCPConnect.this);
                }
            }
        });

        thread.start();


    }

    public TCPConnect(TCPListener eventListener, Socket socket) throws IOException {
        this(socket,eventListener);
    }

    public TCPConnect(TCPListener eventListener, String ipAddres, int port) throws IOException {
        this(eventListener,new Socket(ipAddres,port));
    }


    public synchronized void sendString(String value){
        try{
            out.write(value + "\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(this,e);
            disconnect();
        }
    }

    private synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(this,e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
