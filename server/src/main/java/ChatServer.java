import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.*;

public class ChatServer implements ConnectionListener {


    public static void main(String[] args) {
        new ChatServer();
    }

    private final ArrayList<Connection> connections = new ArrayList<>();

    private final Logger LOGGER;
    private ChatServer(){
        System.out.println("Server start...");
        LOGGER = Logger.getLogger(ChatServer.class.getName());
        try (ServerSocket serverSocket = new ServerSocket(8993);
             FileInputStream inLog = new FileInputStream("server/src/main/resources/log.config")){
            LogManager.getLogManager().readConfiguration(inLog);
            while (true){
                 try {
                     new Connection(this, serverSocket.accept());
                     LOGGER.log(Level.INFO,"соединение создано");
                 } catch (IOException e){
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
        LOGGER.log(Level.INFO,"Client connected: " + connection);
    }

    @Override
    public synchronized void disconnect(Connection connection) {
        connections.remove(connection);
        sendToAll("Client disconnected: " + connection);
        LOGGER.log(Level.INFO,"Client disconnected: " + connection);
    }

    @Override
    public synchronized void receiveString(Connection connection, String value) {
        sendToAll(value);
        LOGGER.log(Level.INFO,"Message: ("+ value + ") from: " + connection);
    }

    @Override
    public synchronized void exception(Connection connection, Exception e) {
        System.out.println("Connection exception: " + e);
        LOGGER.log(Level.FINE,"Client disconnected: " + connection);
    }

    private void sendToAll (String msg){
        System.out.println(msg);
        final int size = connections.size();
        for (int i = 0; i < size; i++) {
            connections.get(i).sendMesage(msg);
        }
    }

}
