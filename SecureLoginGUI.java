import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SecureLoginGUI {
    private static final HashMap<String, String> users = new HashMap<>();
    private static final HashMap<String, Integer> failedAttempts = new HashMap<>();
    private static final HashMap<String, Long> lockTime = new HashMap<>();
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_DURATION = 60000;

    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public SecureLoginGUI() {
        users.put("admin", "secure123");

        frame = new JFrame("Secure Login System");
        frame.setSize(350, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 1));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        frame.add(panel);
        frame.add(messageLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        frame.setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (lockTime.containsKey(username) && System.currentTimeMillis() - lockTime.get(username) < LOCK_DURATION) {
            messageLabel.setText("Account locked! Try later.");
            return;
        } else {
            lockTime.remove(username);
        }

        if (users.containsKey(username) && users.get(username).equals(password)) {
            messageLabel.setText("Login Successful!");
            failedAttempts.put(username, 0);
        } else {
            failedAttempts.put(username, failedAttempts.getOrDefault(username, 0) + 1);
            messageLabel.setText("Incorrect credentials. Attempts left: " + (MAX_ATTEMPTS - failedAttempts.get(username)));

            if (failedAttempts.get(username) >= MAX_ATTEMPTS) {
                lockTime.put(username, System.currentTimeMillis());
                messageLabel.setText("Account locked for 1 min.");
            }
        }
    }

    public static void main(String[] args) {
        new SecureLoginGUI();
    }
}
