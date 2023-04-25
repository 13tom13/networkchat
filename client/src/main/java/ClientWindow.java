import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, ConnectionListener {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 8993;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final JTextArea log = new JTextArea();
    private final JTextField nickname = new JTextField("user");
    private final JTextField input = new JTextField();

    private Connection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        log.setEditable(false);
        log.setLineWrap(true);
        input.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(input, BorderLayout.SOUTH);
        add(nickname, BorderLayout.NORTH);
        setVisible(true);
        try {
            connection = new Connection(this, IP, PORT);
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = input.getText();
        if (msg.equals("")) return;
        input.setText(null);
        connection.sendMesage(nickname.getText() + ": " + msg);
    }

    @Override
    public void connectionReady(Connection connection) {
        printMessage("Connection ready");
    }

    @Override
    public void disconnect(Connection connection) {
        printMessage("Connection close");
    }

    @Override
    public void receiveString(Connection connection, String value) {
        printMessage(value);
    }

    @Override
    public void exception(Connection connection, Exception e) {
        printMessage("Connection exception: " + e);
    }

    private synchronized void printMessage(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
