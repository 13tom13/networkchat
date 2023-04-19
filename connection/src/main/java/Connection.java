import java.io.*;
import java.net.Socket;

public class Connection {

    private final Socket socket;
    private final Thread thread;
    private final ConnectionListener listener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public Connection(Socket socket, ConnectionListener listener) throws IOException {
        this.socket = socket;
        this.listener = listener;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.connectionReady(Connection.this);
                    while (!thread.isInterrupted()) {
                        listener.receiveString(Connection.this, in.readLine());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    listener.disconnect(Connection.this);
                }
            }
        });
        thread.start();
    }

    public synchronized void sendMesage(String msg) {
        try {
            out.write(msg + "\r\n");
            out.flush();
        } catch (IOException e) {
            listener.exception(Connection.this, e);
            disconnect();
        }
    }

    public void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            listener.exception(Connection.this, e);
        }
    }


}
