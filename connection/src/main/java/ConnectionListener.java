public interface ConnectionListener {

    void connectionReady (Connection connection);
    void disconnect (Connection connection);
    void receiveString (Connection connection, String value);
    void exception (Connection connection, Exception e);
}
