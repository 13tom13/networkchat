import javax.swing.*;
import java.io.FileInputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

    private final static String ip = Setting.getInstance().getIp();

    private final static int port = Setting.getInstance().getPort();

    private final static Logger logger = Logger.getLogger(ClientWindow.class.getName());


    public static void main(String[] args) {
        try (FileInputStream inLog = new FileInputStream("client/src/main/resources/log.config")) {
            LogManager.getLogManager().readConfiguration(inLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ClientWindow(ip,port,JOptionPane.showInputDialog("enter your nickname:","user"),logger);
    }
}
