import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionTest {

    private Connection connection;

    private ServerSocket serverSocket;
    private final String testIp = "127.0.0.1";
    private final int testPort = 8899;

    @BeforeAll
    public static void init() {
        System.out.println("Connection tests started");
    }

    @AfterAll
    public static void end() {
        System.out.println("Connection finished");
    }

    @BeforeEach
    void beforeEach() {
        try {
            serverSocket = new ServerSocket(testPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void afterEach() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void connectionTest() {
        System.out.println("connection test");
        // given:
        ConnectionListener connectionListenerTest = Mockito.mock(ConnectionListener.class);
        // when:
        try {
            connection = new Connection(connectionListenerTest, testIp, testPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // then:
        Assertions.assertTrue(connection.getThread().isAlive());
        System.out.println("connection test - checked");
    }

    @Test
    void disconnectTest() {
        System.out.println("disconnect test");
        // given:
        ConnectionListener connectionListenerTest = Mockito.mock(ConnectionListener.class);
        // when:
        try {
            connection = new Connection(connectionListenerTest, testIp, testPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.disconnect();
        // then:
        Assertions.assertTrue(connection.getThread().isInterrupted());
        System.out.println("disconnect test - checked");
    }
}
