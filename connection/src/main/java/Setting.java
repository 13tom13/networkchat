import java.io.*;

public final class Setting {

    private String ip;
    private int port;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    private Setting() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader("Setting.txt"))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                if (line.startsWith("ip")) {
                    ip = line.substring(3).trim();
                } else if (line.startsWith("port")) {
                    port = Integer.parseInt(line.substring(4).trim());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class SingletonHolder {
        public static final Setting HOLDER_INSTANCE = new Setting();
    }

    public static Setting getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

}
