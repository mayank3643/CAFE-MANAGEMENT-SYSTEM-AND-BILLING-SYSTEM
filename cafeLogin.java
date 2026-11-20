
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CafeLogin extends JFrame {

    Color bg = new Color(20, 17, 16);
    Color card = new Color(39, 32, 28);
    Color gold = new Color(218, 165, 90);
    Color cream = new Color(245, 235, 220);

    JTextField username;
    JPasswordField password;
    JCheckBox showPassword;

    public CafeLogin() {

        setTitle("CAFE NOIR | Premium Login");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel(new GridLayout(1, 2));
        main.setBackground(bg);

        // LEFT BRAND PANEL
        JPanel left = new JPanel(null);
        left.setBackground(new Color(30, 24, 21));

        JLabel number = new JLabel("01 / 07");
        number.setBounds(35, 25, 150, 30);
        number.setFont(new Font("Arial", Font.BOLD, 14));
        number.setForeground(gold);
        left.add(number);

        JLabel logo = new JLabel("CN");
        logo.setBounds(100, 130, 250, 100);
        logo.setFont(new Font("Serif", Font.BOLD, 90));
        logo.setForeground(gold);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(logo);

        JLabel brand = new JLabel("CAFE NOIR");
        brand.setBounds(50, 250, 350, 55);
        brand.setFont(new Font("Serif", Font.BOLD, 38));
        brand.setForeground(cream);
        brand.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(brand);

        JLabel line = new JLabel(
            "━━━━━━━━━━━━━━━━━━━━━━━━"
        );
        line.setBounds(95, 305, 280, 25);
        line.setForeground(gold);
        left.add(line);

        JLabel tagline = new JLabel(
            "THE ART OF PERFECT COFFEE"
        );
        tagline.setBounds(50, 345, 350, 30);
        tagline.setForeground(gold);
        tagline.setFont(new Font("Arial", Font.PLAIN, 13));
        tagline.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(tagline);

        JLabel footer = new JLabel(
            "EST. 2026  |  PREMIUM EXPERIENCE"
        );
        footer.setBounds(50, 520, 350, 25);
        footer.setForeground(Color.GRAY);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(footer);

        // RIGHT LOGIN PANEL
        JPanel right = new JPanel(null);
        right.setBackground(bg);

        JLabel heading = new JLabel("WELCOME BACK");
        heading.setBounds(55, 90, 350, 45);
        heading.setFont(new Font("Serif", Font.BOLD, 30));
        heading.setForeground(gold);
        right.add(heading);

        JLabel sub = new JLabel(
            "Sign in to manage your cafe"
        );
        sub.setBounds(55, 135, 350, 25);
        sub.setForeground(Color.LIGHT_GRAY);
        right.add(sub);

        JLabel userLabel = new JLabel("USERNAME");
        userLabel.setBounds(55, 195, 300, 25);
        userLabel.setForeground(cream);
        right.add(userLabel);

        username = new JTextField();
        username.setBounds(55, 225, 350, 43);
        username.setBackground(card);
        username.setForeground(Color.WHITE);
        username.setCaretColor(gold);
        username.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(gold),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
            )
        );
        username.setFont(new Font("Arial", Font.PLAIN, 15));
        right.add(username);

        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setBounds(55, 290, 300, 25);
        passLabel.setForeground(cream);
        right.add(passLabel);

        password = new JPasswordField();
        password.setBounds(55, 320, 350, 43);
        password.setBackground(card);
        password.setForeground(Color.WHITE);
        password.setCaretColor(gold);
        password.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(gold),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
            )
        );
        password.setFont(new Font("Arial", Font.PLAIN, 15));
        right.add(password);

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(55, 370, 180, 25);
        showPassword.setBackground(bg);
        showPassword.setForeground(Color.LIGHT_GRAY);
        right.add(showPassword);

        showPassword.addActionListener(e -> {
            password.setEchoChar(
                showPassword.isSelected() ? (char) 0 : '•'
            );
        });

        JButton login = new JButton("SIGN IN  →");
        login.setBounds(55, 420, 350, 48);
        login.setBackground(gold);
        login.setForeground(bg);
        login.setFont(new Font("Arial", Font.BOLD, 16));
        login.setFocusPainted(false);
        login.setBorderPainted(false);
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));
        right.add(login);

        login.addActionListener(e -> loginUser());

        password.addActionListener(e -> loginUser());

        JLabel bottom = new JLabel(
            "CAFE NOIR  •  MANAGEMENT SYSTEM"
        );
        bottom.setBounds(55, 500, 350, 25);
        bottom.setForeground(Color.GRAY);
        bottom.setHorizontalAlignment(SwingConstants.CENTER);
        right.add(bottom);

        main.add(left);
        main.add(right);

        add(main);
        setVisible(true);
    }

    private void loginUser() {

        String user = username.getText().trim();
        String pass = new String(password.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter username and password!",
                "Missing Details",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (user.equals("admin") && pass.equals("1234")) {

            JOptionPane.showMessageDialog(
                this,
                "Welcome to CAFE NOIR!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            new MainDashboard();
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Invalid Login Credentials!",
                "Access Denied",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new CafeLogin();
        });
    }
}
