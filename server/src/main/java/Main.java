import java.io.FileInputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

    private static final int port = Setting.getInstance().getPort();

    public static void main(String[] args) {
        Logger logger =  Logger.getLogger(ChatServer.class.getName());
        try (FileInputStream inLog = new FileInputStream("server/src/main/resources/log.config")) {
            LogManager.getLogManager().readConfiguration(inLog);
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Server start...");
        new ChatServer(port, logger);
    }
}
