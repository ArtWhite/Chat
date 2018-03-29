package pw.artwhite.client;

import pw.artwhite.net.TCPConnect;
import pw.artwhite.net.TCPListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by artwhite on 28/03/2018.
 */

public class Client extends JFrame implements ActionListener, TCPListener {

    private static final String IP_ADDRESS ="127.0.0.1";
    private static final int PORT = 6663;

    @Override
    public void onConnectionReady(TCPConnect tcpConnection) {
        printMessage("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnect tcpConnection, String str) {
        printMessage(str);
    }

    @Override
    public void onDisconnect(TCPConnect tcpConnection) {
        printMessage("Connection close");
    }

    @Override
    public void onException(TCPConnect tcpConnection, IOException e) {
        printMessage("Connection exception " + e);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });
    }
    private final  JTextField textField = new JTextField("Гость");
    private final JTextArea log = new JTextArea();
    private final JTextField filedInput = new JTextField();

    private TCPConnect connection;

    public Client() throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
//        setAlwaysOnTop(true);

        add(textField,BorderLayout.NORTH);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        filedInput.addActionListener(this);
        add(filedInput, BorderLayout.SOUTH);

        setVisible(true);

        try{
            connection = new TCPConnect(this , IP_ADDRESS,PORT);
        } catch (IOException e){
            printMessage("Connection: " + e);
        }
    }

    private synchronized void printMessage(String s) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(s + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = filedInput.getText();
        if (msg.equals("")) return;
        filedInput.setText(null);
        connection.sendString(textField.getText() + ": " + msg);
    }
}