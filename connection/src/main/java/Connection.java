import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection {

    private final Socket socket;

    protected Thread getThread() {
        return thread;
    }

    private final Thread thread;
    private final ConnectionListener listener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public Connection (ConnectionListener listener, String ip, int port) throws IOException{
        this(listener, new Socket(ip, port));
    }

    public Connection(ConnectionListener listener, Socket socket) throws IOException {
        this.socket = socket;
        this.listener = listener;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.connectionReady(Connection.this);
                    while (!thread.isInterrupted()) {
                        listener.receiveString(Connection.this, in.readLine());
                    }
                } catch (IOException e) {
                    listener.disconnect(Connection.this);
                }
            }
        });
        thread.start();
    }

    public synchronized void sendMessage(String msg) {
        try {
            out.write(msg + "\r\n");
            out.flush();
        } catch (IOException e) {
            disconnect();
        }
    }

    public void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("я тут");
            listener.exception(Connection.this, e);
        }
    }

    @Override
    public String toString() {
        return "Connection (" + socket.getLocalSocketAddress() + ")";
    }


}
