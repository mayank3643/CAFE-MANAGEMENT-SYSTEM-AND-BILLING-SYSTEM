
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class CustomerManagement extends JFrame {

    Color bg = new Color(19, 22, 27);
    Color panel = new Color(34, 40, 48);
    Color gold = new Color(218, 165, 90);
    Color white = new Color(240, 240, 240);
    Color muted = new Color(165, 175, 185);

    JTextField nameField, mobileField, emailField;
    JTextField cityField, addressField, searchField;

    JTable table;
    DefaultTableModel model;

    JLabel totalCustomers;

    public CustomerManagement() {

        setTitle("CAFE NOIR | Customer Registry");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(22, 25, 22, 25));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bg);

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setBackground(bg);

        JLabel number = new JLabel("04 / 07  |  CAFE NOIR");
        number.setForeground(gold);
        number.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel title = new JLabel("Customer Registry");
        title.setForeground(white);
        title.setFont(new Font("Serif", Font.BOLD, 36));

        JLabel subtitle = new JLabel(
            "Every guest matters. Manage your customer relationships."
        );
        subtitle.setForeground(muted);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));

        heading.add(number);
        heading.add(Box.createVerticalStrut(7));
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        header.add(heading, BorderLayout.WEST);

        totalCustomers = new JLabel("TOTAL CUSTOMERS : 0");
        totalCustomers.setForeground(gold);
        totalCustomers.setFont(new Font("Arial", Font.BOLD, 13));
        totalCustomers.setBorder(
            new CompoundBorder(
                new LineBorder(gold),
                new EmptyBorder(13, 16, 13, 16)
            )
        );

        header.add(totalCustomers, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // MAIN PANEL
        JPanel main = new JPanel(new BorderLayout(18, 18));
        main.setBackground(bg);

        // LEFT CUSTOMER FORM
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(panel);
        form.setBorder(new EmptyBorder(20, 18, 20, 18));
        form.setPreferredSize(new Dimension(320, 550));

        JLabel formTitle = new JLabel("CUSTOMER DETAILS");
        formTitle.setForeground(gold);
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));

        form.add(formTitle);
        form.add(Box.createVerticalStrut(22));

        nameField = addField(form, "FULL NAME");

        mobileField = addField(form, "MOBILE NUMBER");

        emailField = addField(form, "EMAIL ADDRESS");

        cityField = addField(form, "CITY");

        addressField = addField(form, "FULL ADDRESS");

        form.add(Box.createVerticalStrut(20));

        JButton add = createButton(
            "＋  REGISTER CUSTOMER", gold
        );

        JButton update = createButton(
            "UPDATE CUSTOMER", new Color(65, 115, 160)
        );

        JButton delete = createButton(
            "DELETE CUSTOMER", new Color(165, 65, 65)
        );

        JButton clear = createButton(
            "CLEAR DETAILS", new Color(95, 100, 110)
        );

        form.add(add);
        form.add(Box.createVerticalStrut(10));

        form.add(update);
        form.add(Box.createVerticalStrut(10));

        form.add(delete);
        form.add(Box.createVerticalStrut(10));

        form.add(clear);

        // RIGHT PANEL
        JPanel right = new JPanel(new BorderLayout(12, 15));
        right.setBackground(bg);

        // SEARCH BAR
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(bg);

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBackground(panel);
        searchField.setForeground(white);
        searchField.setCaretColor(gold);

        searchField.setBorder(
            new CompoundBorder(
                new LineBorder(gold),
                new EmptyBorder(11, 12, 11, 12)
            )
        );

        JButton search = createButton(
            "SEARCH", gold
        );

        JButton refresh = createButton(
            "REFRESH", new Color(65, 125, 100)
        );

        JPanel searchButtons = new JPanel(
            new GridLayout(1, 2, 8, 0)
        );

        searchButtons.setBackground(bg);
        searchButtons.add(search);
        searchButtons.add(refresh);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButtons, BorderLayout.EAST);

        right.add(searchPanel, BorderLayout.NORTH);

        // CUSTOMER TABLE
        model = new DefaultTableModel(
            new String[] {
                "CUSTOMER ID",
                "FULL NAME",
                "MOBILE",
                "EMAIL",
                "CITY",
                "ADDRESS"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(38);
        table.setBackground(panel);
        table.setForeground(white);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setSelectionBackground(
            new Color(105, 82, 57)
        );
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(65, 75, 85));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(170);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(180);

        table.getTableHeader().setBackground(
            new Color(55, 65, 75)
        );

        table.getTableHeader().setForeground(gold);

        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(gold));
        scroll.getViewport().setBackground(panel);

        right.add(scroll, BorderLayout.CENTER);

        // FOOTER
        JLabel footer = new JLabel(
            "CN  |  CUSTOMER EXPERIENCE MANAGEMENT  |  2026"
        );

        footer.setForeground(gold);
        footer.setFont(new Font("Arial", Font.BOLD, 11));

        right.add(footer, BorderLayout.SOUTH);

        main.add(form, BorderLayout.WEST);
        main.add(right, BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);

        add(root);

        // BUTTON ACTIONS

        add.addActionListener(e -> addCustomer());

        update.addActionListener(e -> updateCustomer());

        delete.addActionListener(e -> deleteCustomer());

        clear.addActionListener(e -> clearForm());

        search.addActionListener(e -> searchCustomer());

        refresh.addActionListener(e -> loadCustomers());

        searchField.addActionListener(e -> searchCustomer());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectCustomer();
            }
        });

        createTable();
        loadCustomers();

        setVisible(true);
    }

    // CREATE DATABASE TABLE

    private void createTable() {

        String sql =
            "CREATE TABLE IF NOT EXISTS customers (" +
            "customer_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "customer_name VARCHAR(100) NOT NULL, " +
            "mobile VARCHAR(15) NOT NULL, " +
            "email VARCHAR(100), " +
            "city VARCHAR(60), " +
            "address VARCHAR(255)" +
            ")";

        try (Connection con =
                 cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (Statement st = con.createStatement()) {
                st.executeUpdate(sql);
            }

        } catch (SQLException e) {
            showError("Table Creation Failed", e);
        }
    }

    // ADD CUSTOMER

    private void addCustomer() {

        if (!validateForm()) return;

        String sql =
            "INSERT INTO customers " +
            "(customer_name, mobile, email, city, address) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con =
                 cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

                setParameters(ps, false);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                this,
                "Customer Registered Successfully!",
                "CAFE NOIR",
                JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();
            loadCustomers();

        } catch (SQLException e) {
            showError("Registration Failed", e);
        }
    }

    // UPDATE CUSTOMER

    private void updateCustomer() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a customer!"
            );
            return;
        }

        if (!validateForm()) return;

        int id = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        String sql =
            "UPDATE customers SET customer_name=?, " +
            "mobile=?, email=?, city=?, address=? " +
            "WHERE customer_id=?";

        try (Connection con =
                 cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

                setParameters(ps, true);
                ps.setInt(6, id);

                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                this, "Customer Updated Successfully!"
            );

            clearForm();
            loadCustomers();

        } catch (SQLException e) {
            showError("Update Failed", e);
        }
    }

    // DELETE CUSTOMER

    private void deleteCustomer() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a customer!"
            );
            return;
        }

        int id = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this customer?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql =
            "DELETE FROM customers WHERE customer_id=?";

        try (Connection con =
                 cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                this, "Customer Deleted Successfully!"
            );

            clearForm();
            loadCustomers();

        } catch (SQLException e) {
            showError("Delete Failed", e);
        }
    }

    // VIEW ALL CUSTOMERS

    private void loadCustomers() {

        model.setRowCount(0);

        String sql =
            "SELECT * FROM customers ORDER BY customer_id DESC";

        try (Connection con =
                 cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {

                    model.addRow(new Object[] {
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("mobile"),
                        rs.getString("email"),
                        rs.getString("city"),
                        rs.getString("address")
                    });
                }
            }

            updateCount();

        } catch (SQLException e) {
            showError("Loading Failed", e);
        }
    }

    // SEARCH CUSTOMER

    private void searchCustomer() {

        String keyword = searchField.getText().trim();

        model.setRowCount(0);

        String sql =
            "SELECT * FROM customers WHERE " +
            "CAST(customer_id AS CHAR) LIKE ? " +
            "OR customer_name LIKE ? " +
            "OR mobile LIKE ? " +
            "ORDER BY customer_id DESC";

        try (Connection con =
                 cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

                String value = "%" + keyword + "%";

                ps.setString(1, value);
                ps.setString(2, value);
                ps.setString(3, value);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        model.addRow(new Object[] {
                            rs.getInt("customer_id"),
                            rs.getString("customer_name"),
                            rs.getString("mobile"),
                            rs.getString("email"),
                            rs.getString("city"),
                            rs.getString("address")
                        });
                    }
                }
            }

            totalCustomers.setText(
                "SEARCH RESULTS : " + model.getRowCount()
            );

        } catch (SQLException e) {
            showError("Search Failed", e);
        }
    }

    // SELECT CUSTOMER

    private void selectCustomer() {

        int row = table.getSelectedRow();

        if (row >= 0) {

            nameField.setText(
                model.getValueAt(row, 1).toString()
            );

            mobileField.setText(
                model.getValueAt(row, 2).toString()
            );

            emailField.setText(
                model.getValueAt(row, 3) == null
                    ? "" : model.getValueAt(row, 3).toString()
            );

            cityField.setText(
                model.getValueAt(row, 4) == null
                    ? "" : model.getValueAt(row, 4).toString()
            );

            addressField.setText(
                model.getValueAt(row, 5) == null
                    ? "" : model.getValueAt(row, 5).toString()
            );
        }
    }

    // VALIDATION

    private boolean validateForm() {

        String name = nameField.getText().trim();
        String mobile = mobileField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || mobile.isEmpty()) {

            JOptionPane.showMessageDialog(
                this, "Name and Mobile are required!"
            );

            return false;
        }

        if (!name.matches("[\\p{L} .'-]{2,100}")) {

            JOptionPane.showMessageDialog(
                this, "Enter a valid customer name!"
            );

            return false;
        }

        if (!mobile.matches("[0-9]{10}")) {

            JOptionPane.showMessageDialog(
                this, "Mobile number must contain 10 digits!"
            );

            return false;
        }

        if (!email.isEmpty() &&
            !email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            )) {

            JOptionPane.showMessageDialog(
                this, "Enter a valid email address!"
            );

            return false;
        }

        return true;
    }

    // SET SQL PARAMETERS

    private void setParameters(
        PreparedStatement ps, boolean update
    ) throws SQLException {

        ps.setString(1, nameField.getText().trim());
        ps.setString(2, mobileField.getText().trim());
        ps.setString(3, emailField.getText().trim());
        ps.setString(4, cityField.getText().trim());
        ps.setString(5, addressField.getText().trim());
    }

    // CLEAR FORM

    private void clearForm() {

        nameField.setText("");
        mobileField.setText("");
        emailField.setText("");
        cityField.setText("");
        addressField.setText("");

        table.clearSelection();

        nameField.requestFocus();
    }

    // UPDATE CUSTOMER COUNT

    private void updateCount() {

        totalCustomers.setText(
            "TOTAL CUSTOMERS : " + model.getRowCount()
        );
    }

    // INPUT FIELD DESIGN

    private JTextField addField(
        JPanel p, String labelText
    ) {

        JLabel label = new JLabel(labelText);

        label.setForeground(gold);
        label.setFont(
            new Font("Arial", Font.BOLD, 11)
        );

        p.add(label);
        p.add(Box.createVerticalStrut(7));

        JTextField field = new JTextField();

        field.setMaximumSize(new Dimension(280, 37));
        field.setPreferredSize(new Dimension(280, 37));

        field.setBackground(bg);
        field.setForeground(white);
        field.setCaretColor(gold);

        field.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        field.setBorder(
            new CompoundBorder(
                new LineBorder(new Color(90, 105, 120)),
                new EmptyBorder(7, 10, 7, 10)
            )
        );

        p.add(field);
        p.add(Box.createVerticalStrut(13));

        return field;
    }

    // BUTTON DESIGN

    private JButton createButton(
        String text, Color color
    ) {

        JButton button = new JButton(text);

        button.setBackground(color);
        button.setForeground(Color.WHITE);

        button.setFont(
            new Font("Arial", Font.BOLD, 11)
        );

        button.setFocusPainted(false);
        button.setBorder(
            new EmptyBorder(12, 10, 12, 10)
        );

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    // ERROR MESSAGE

    private void showError(
        String title, SQLException e
    ) {

        JOptionPane.showMessageDialog(
            this,
            title + "\n" + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new CustomerManagement();
        });
    }
}
