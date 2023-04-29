import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.*;

public class ChatServer implements ConnectionListener {

    private final ArrayList<Connection> connections = new ArrayList<>();

    private final Logger logger;

    protected ChatServer(int port, Logger log) {
        this.logger = log;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    new Connection(this, serverSocket.accept());
                    logger.log(Level.INFO, "соединение создано");
                } catch (IOException e) {
                    System.out.println("Connection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public synchronized void connectionReady(Connection connection) {
        connections.add(connection);
        sendToAll("Client connected: " + connection);
        logger.log(Level.INFO, "Client connected: " + connection);
    }

    @Override
    public synchronized void disconnect(Connection connection) {
        connections.remove(connection);
        sendToAll("Client disconnected: " + connection);
        logger.log(Level.INFO, "Client disconnected: " + connection);
    }

    @Override
    public synchronized void receiveString(Connection connection, String value) {
        sendToAll(value);
        logger.log(Level.INFO, "Message: (" + value + ") from: " + connection);
    }

    @Override
    public synchronized void exception(Connection connection, Exception e) {
        System.out.println("Connection exception server: " + e);
        logger.log(Level.FINE, "Client disconnected: " + connection);
    }

    private void sendToAll(String msg) {
        System.out.println(msg);
        for (Connection connection : connections) {
            connection.sendMessage(msg);
        }
    }

}
