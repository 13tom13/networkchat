import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.*;

public class ClientWindow extends JFrame implements ActionListener, ConnectionListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final JTextArea log = new JTextArea();
    private final JTextArea nickname = new JTextArea();
    private final JTextField input = new JTextField();
    private final Logger logger;

    private Connection connection;

    protected ClientWindow(String ip, int port, String name, Logger logger) {
        this.logger = logger;
        nickname.append(name);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        log.setEditable(false);
        log.setLineWrap(true);
        log.setBackground(Color.LIGHT_GRAY);
        nickname.setEditable(false);
        nickname.setBackground(Color.GRAY);
        input.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(input, BorderLayout.SOUTH);
        add(nickname, BorderLayout.NORTH);
        setVisible(true);
        try {
            connection = new Connection(this, ip, port);
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = input.getText();
        if (msg.equals("")) return;
        input.setText(null);
        if (msg.equals("exit")) {
            disconnect(connection);
        }
        connection.sendMessage(nickname.getText() + ": " + msg);
    }

    @Override
    public void connectionReady(Connection connection) {
        printMessage("Connection ready");
        logger.log(Level.INFO, "Connection ready");
    }

    @Override
    public void disconnect(Connection connection) {
        printMessage("Connection close");
        connection.getThread().interrupt();
        System.exit(0);
        logger.log(Level.INFO, "Connection close");
    }

    @Override
    public void receiveString(Connection connection, String value) {
        printMessage(value);
        logger.log(Level.INFO, "Message: (" + value + ")");
    }

    @Override
    public void exception(Connection connection, Exception e) {
        printMessage("Connection exception client: " + e);
        logger.log(Level.FINE, "Connection exception: " + e);
    }

    private synchronized void printMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }
}
