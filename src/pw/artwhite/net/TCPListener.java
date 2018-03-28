package pw.artwhite.net;

import java.io.IOException;

/**
 * Created by artwhite on 10/03/2018.
 */

public interface TCPListener {

    void onConnectionReady(TCPConnect tcpConnection);

    void onReceiveString(TCPConnect tcpConnection, String str);

    void onDisconnect(TCPConnect tcpConnection);

    void onException(TCPConnect tcpConnection, IOException e);
}
