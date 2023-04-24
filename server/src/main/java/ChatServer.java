import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements ConnectionListener {


    public static void main(String[] args) {
        new ChatServer();
    }

    private final ArrayList<Connection> connections = new ArrayList<>();

    private ChatServer(){
        System.out.println("Server start...");
        try (ServerSocket serverSocket = new ServerSocket(getPort())){
            while (true){
                 try {
                     new Connection(this, serverSocket.accept());
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
    }

    @Override
    public synchronized void disconnect(Connection connection) {
        connections.remove(connection);
        sendToAll("Client disconnected: " + connection);
    }

    @Override
    public synchronized void receiveString(Connection connection, String value) {
        sendToAll(value);
    }

    @Override
    public synchronized void exception(Connection connection, Exception e) {
        System.out.println("Connection exception: " + e);
    }

    private void sendToAll (String msg){
        System.out.println(msg);
        final int size = connections.size();
        for (int i = 0; i < size; i++) {
            connections.get(i).sendMesage(msg);
        }
    }

    private int getPort () {
        try (BufferedReader fileReader = new BufferedReader(new FileReader("Setting.txt"))) {
            String line;
            String setting = "port";
            while ((line = fileReader.readLine()) != null) {
                if (line.startsWith(setting)){
                    return Integer.parseInt(line.substring(setting.length()).trim());
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        return 0;
    }
}
