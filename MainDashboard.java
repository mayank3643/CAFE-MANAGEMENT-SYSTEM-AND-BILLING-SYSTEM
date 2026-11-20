
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainDashboard extends JFrame {

    Color bg = new Color(20, 18, 17);
    Color sidebar = new Color(30, 26, 23);
    Color panel = new Color(43, 36, 31);
    Color gold = new Color(218, 165, 90);
    Color cream = new Color(245, 235, 220);
    Color muted = new Color(170, 155, 140);

    JPanel content;
    JLabel revenueValue, ordersValue;
    JLabel customersValue, menuValue;

    public MainDashboard() {

        setTitle("CAFE NOIR | Management Dashboard");
        setSize(1250, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 650));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);

        // SIDEBAR
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(255, 780));
        side.setBackground(sidebar);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel number = new JLabel("02 / 07");
        number.setForeground(gold);
        number.setFont(new Font("Arial", Font.BOLD, 13));
        number.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(number);

        JLabel logo = new JLabel("CN");
        logo.setForeground(gold);
        logo.setFont(new Font("Serif", Font.BOLD, 55));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(Box.createVerticalStrut(10));
        side.add(logo);

        JLabel brand = new JLabel("CAFE NOIR");
        brand.setForeground(cream);
        brand.setFont(new Font("Serif", Font.BOLD, 23));
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(brand);

        JLabel tagline = new JLabel("PREMIUM CAFE MANAGEMENT");
        tagline.setForeground(muted);
        tagline.setFont(new Font("Arial", Font.PLAIN, 9));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(Box.createVerticalStrut(5));
        side.add(tagline);

        side.add(Box.createVerticalStrut(35));

        JLabel navigation = new JLabel("MAIN NAVIGATION");
        navigation.setForeground(gold);
        navigation.setFont(new Font("Arial", Font.BOLD, 11));
        navigation.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(navigation);

        side.add(Box.createVerticalStrut(15));

        addNav(side, "01   Dashboard", null);
        addNav(side, "02   Menu Management", "MenuManagement");
        addNav(side, "03   Customer Management", "CustomerManagement");
        addNav(side, "04   Order Management", "OrderManagement");
        addNav(side, "05   Billing Management", "BillingManagement");
        addNav(side, "06   Order Items Management",
               "OrderItemsManagement");

        side.add(Box.createVerticalGlue());

        JLabel status = new JLabel("● SYSTEM ONLINE");
        status.setForeground(new Color(100, 210, 130));
        status.setFont(new Font("Arial", Font.BOLD, 12));
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(status);

        side.add(Box.createVerticalStrut(15));

        JButton logout = createButton("LOGOUT  ↗", gold);
        side.add(logout);

        logout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "CAFE NOIR",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                new CafeLogin();
                dispose();
            }
        });

        // MAIN CONTENT
        content = new JPanel(new BorderLayout(15, 20));
        content.setBackground(bg);
        content.setBorder(new EmptyBorder(25, 25, 25, 25));

        // TOP HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bg);

        JPanel heading = new JPanel();
        heading.setBackground(bg);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("GOOD DAY, ADMIN");
        welcome.setForeground(gold);
        welcome.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel title = new JLabel("Dashboard Overview");
        title.setForeground(cream);
        title.setFont(new Font("Serif", Font.BOLD, 32));

        JLabel description = new JLabel(
            "Manage your cafe operations from one place."
        );
        description.setForeground(muted);
        description.setFont(new Font("Arial", Font.PLAIN, 13));

        heading.add(welcome);
        heading.add(Box.createVerticalStrut(5));
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(description);

        header.add(heading, BorderLayout.WEST);

        JPanel datePanel = new JPanel();
        datePanel.setBackground(panel);
        datePanel.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel date = new JLabel(
            new SimpleDateFormat("dd MMM yyyy").format(new Date())
        );
        date.setForeground(gold);
        date.setFont(new Font("Arial", Font.BOLD, 14));
        datePanel.add(date);

        header.add(datePanel, BorderLayout.EAST);

        content.add(header, BorderLayout.NORTH);

        // CENTER DASHBOARD
        JPanel center = new JPanel();
        center.setBackground(bg);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel statsTitle = new JLabel("BUSINESS STATISTICS");
        statsTitle.setForeground(gold);
        statsTitle.setFont(new Font("Arial", Font.BOLD, 14));
        statsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        center.add(statsTitle);
        center.add(Box.createVerticalStrut(15));

        // STATISTICS CARDS
        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 0));
        stats.setBackground(bg);
        stats.setMaximumSize(new Dimension(1200, 125));

        revenueValue = new JLabel("₹ 0");
        ordersValue = new JLabel("0");
        customersValue = new JLabel("0");
        menuValue = new JLabel("0");

        stats.add(createCard(
            "01", "TOTAL BILL RECORDS", revenueValue, "Billing entries"
        ));

        stats.add(createCard(
            "02", "TOTAL ORDERS", ordersValue, "Order records"
        ));

        stats.add(createCard(
            "03", "TOTAL CUSTOMERS", customersValue, "Registered customers"
        ));

        stats.add(createCard(
            "04", "MENU ITEMS", menuValue, "Available menu records"
        ));

        center.add(stats);
        center.add(Box.createVerticalStrut(25));

        // MIDDLE SECTION
        JPanel middle = new JPanel(new GridLayout(1, 2, 18, 0));
        middle.setBackground(bg);
        middle.setMaximumSize(new Dimension(1200, 225));

        // CAFE PROFILE
        JPanel profile = createSection("CAFE PROFILE");

        JLabel cafeName = new JLabel("CAFE NOIR");
        cafeName.setForeground(gold);
        cafeName.setFont(new Font("Serif", Font.BOLD, 30));

        JLabel profileText = new JLabel(
            "<html><div style='line-height:1.8'>" +
            "Premium Coffee & Dining Experience<br>" +
            "Management System: CN-2026<br>" +
            "Category: Cafe & Restaurant<br>" +
            "Account: Administrator<br>" +
            "Status: Active</div></html>"
        );

        profileText.setForeground(cream);
        profileText.setFont(new Font("Arial", Font.PLAIN, 13));

        profile.add(cafeName);
        profile.add(Box.createVerticalStrut(10));
        profile.add(profileText);

        // QUICK ACTIONS
        JPanel quick = createSection("QUICK ACTIONS");

        quick.add(createActionButton(
            "01   Manage Menu", "MenuManagement"
        ));

        quick.add(createActionButton(
            "02   Add Customer", "CustomerManagement"
        ));

        quick.add(createActionButton(
            "03   Create Order", "OrderManagement"
        ));

        quick.add(createActionButton(
            "04   Manage Billing", "BillingManagement"
        ));

        middle.add(profile);
        middle.add(quick);

        center.add(middle);
        center.add(Box.createVerticalStrut(20));

        // BOTTOM INFORMATION
        JPanel bottom = createSection("RECENT ACTIVITY");

        JLabel recent = new JLabel(
            "<html><div style='line-height:1.8'>" +
            "Welcome to CAFE NOIR Management System.<br>" +
            "Use the navigation panel to manage menu, customers, " +
            "orders, billing and order items.<br>" +
            "Your database records are displayed in the respective modules." +
            "</div></html>"
        );

        recent.setForeground(cream);
        recent.setFont(new Font("Arial", Font.PLAIN, 14));

        bottom.add(recent);

        center.add(bottom);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(bg);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        content.add(scroll, BorderLayout.CENTER);

        root.add(side, BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);

        add(root);

        loadStatistics();

        setVisible(true);
    }

    // NAVIGATION BUTTON
    private void addNav(
        JPanel side, String text, String className
    ) {

        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(230, 43));
        button.setPreferredSize(new Dimension(230, 43));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(sidebar);
        button.setForeground(cream);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBorder(new EmptyBorder(8, 12, 8, 5));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            if (className != null) {
                openModule(className);
            }
        });

        side.add(button);
        side.add(Box.createVerticalStrut(7));
    }

    // STATISTICS CARD
    private JPanel createCard(
        String no, String heading, JLabel value, String description
    ) {

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(
            cardPanel, BoxLayout.Y_AXIS
        ));

        cardPanel.setBackground(panel);
        cardPanel.setBorder(
            new CompoundBorder(
                new LineBorder(new Color(90, 70, 52)),
                new EmptyBorder(12, 12, 12, 12)
            )
        );

        JLabel number = new JLabel(no);
        number.setForeground(gold);
        number.setFont(new Font("Arial", Font.BOLD, 11));

        JLabel name = new JLabel(heading);
        name.setForeground(muted);
        name.setFont(new Font("Arial", Font.BOLD, 10));

        value.setForeground(cream);
        value.setFont(new Font("Serif", Font.BOLD, 25));

        JLabel info = new JLabel(description);
        info.setForeground(muted);
        info.setFont(new Font("Arial", Font.PLAIN, 10));

        cardPanel.add(number);
        cardPanel.add(Box.createVerticalStrut(7));
        cardPanel.add(name);
        cardPanel.add(Box.createVerticalStrut(7));
        cardPanel.add(value);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(info);

        return cardPanel;
    }

    // SECTION PANEL
    private JPanel createSection(String heading) {

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(panel);
        p.setBorder(
            new CompoundBorder(
                new LineBorder(new Color(90, 70, 52)),
                new EmptyBorder(15, 15, 15, 15)
            )
        );

        JLabel title = new JLabel(heading);
        title.setForeground(gold);
        title.setFont(new Font("Arial", Font.BOLD, 14));

        p.add(title);
        p.add(Box.createVerticalStrut(15));

        return p;
    }

    // QUICK ACTION BUTTON
    private JButton createActionButton(
        String text, String className
    ) {

        JButton b = new JButton(text);

        b.setMaximumSize(new Dimension(350, 35));
        b.setPreferredSize(new Dimension(350, 35));
        b.setBackground(new Color(55, 44, 36));
        b.setForeground(cream);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(5, 10, 5, 10));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addActionListener(e -> openModule(className));

        return b;
    }

    // COMMON BUTTON
    private JButton createButton(String text, Color color) {

        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(bg);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(12, 15, 12, 15));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return b;
    }

    // OPEN OTHER MODULES
    private void openModule(String className) {

        try {

            Class<?> cls = Class.forName(className);

            JFrame frame = (JFrame)
                cls.getDeclaredConstructor().newInstance();

            frame.setVisible(true);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Module " + className +
                " is not ready or could not be opened.\n" +
                e.getMessage(),
                "Module Information",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // DATABASE STATISTICS
    private void loadStatistics() {

        try (Connection con =
            cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            ordersValue.setText(
                getCount(con, "orders")
            );

            customersValue.setText(
                getCount(con, "customers")
            );

            menuValue.setText(
                getCount(con, "menu")
            );

            revenueValue.setText(
                getCount(con, "billing")
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Statistics could not be loaded:\n" +
                e.getMessage(),
                "Database Error",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private String getCount(Connection con, String table) {

        String sql = "SELECT COUNT(*) FROM " + table;

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }

        } catch (SQLException e) {
            return "N/A";
        }

        return "0";
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MainDashboard();
        });
    }
}
