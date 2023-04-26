import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.*;

public class ClientWindow extends JFrame implements ActionListener, ConnectionListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final JTextArea log = new JTextArea();
    private final JTextArea nickname = new JTextArea();
    private final JTextField input = new JTextField();

    private static StartWindow startWindow;

    private final Logger LOGGER;

    private Connection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> startWindow = new StartWindow());
    }

    protected ClientWindow() {
        Setting setting = Setting.getInstance();
        LOGGER = Logger.getLogger(ClientWindow.class.getName());
        nickname.append(startWindow.name);
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
        try (FileInputStream inLog = new FileInputStream("client/src/main/resources/log.config")) {
            LogManager.getLogManager().readConfiguration(inLog);
            connection = new Connection(this, setting.getIp(), setting.getPort());
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = input.getText();
        if (msg.equals("")) return;
        input.setText(null);
        connection.sendMessage(nickname.getText() + ": " + msg);
    }

    @Override
    public void connectionReady(Connection connection) {
        printMessage("Connection ready");
        LOGGER.log(Level.INFO, "Connection ready");
    }

    @Override
    public void disconnect(Connection connection) {
        printMessage("Connection close");
        LOGGER.log(Level.INFO, "Connection close");
    }

    @Override
    public void receiveString(Connection connection, String value) {
        printMessage(value);
        LOGGER.log(Level.INFO, "Message: (" + value + ")");
    }

    @Override
    public void exception(Connection connection, Exception e) {
        printMessage("Connection exception: " + e);
        LOGGER.log(Level.FINE, "Connection exception: " + e);
    }

    private synchronized void printMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }
}
