import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JFrame implements ActionListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 100;
    private final JTextField field = new JTextField();
    private final JLabel label = new JLabel("enter your nickname: ");
    private final JPanel panel = new JPanel();
    protected String name = null;

    protected StartWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        field.addActionListener(this);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(field);
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String value = field.getText();
        if (value.equals("")) {
            return;
        } else {
            name = value;
            new ClientWindow();
            setVisible(false);
        }
    }
}
