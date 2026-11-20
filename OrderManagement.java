
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class OrderManagement extends JFrame {

    private final Color BG = new Color(18, 20, 25);
    private final Color PANEL = new Color(29, 32, 39);
    private final Color GOLD = new Color(212, 175, 100);
    private final Color WHITE = new Color(245, 245, 245);
    private final Color MUTED = new Color(165, 169, 178);

    private JComboBox<CustomerItem> cmbCustomer;
    private JComboBox<String> cmbStatus;
    private JTextField txtDate, txtSearch;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal, lblSelected;

    private int selectedOrderId = -1;

    public OrderManagement() {

        setTitle("CAFE NOIR | Order Management");
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BG);

        createHeader();
        createMainPanel();
        createFooter();

        loadCustomers();
        loadOrders();
        updateStatistics();

        setVisible(true);
    }

    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setBackground(BG);

        JLabel title = new JLabel("CAFE NOIR");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(GOLD);

        JLabel subtitle = new JLabel(
                "ORDER MANAGEMENT  |  05 / 07"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(subtitle);

        JButton refresh = createButton("REFRESH", GOLD);
        refresh.addActionListener(e -> refreshAll());

        header.add(titlePanel, BorderLayout.WEST);
        header.add(refresh, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void createMainPanel() {

        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(0, 25, 10, 25));

        JPanel stats = new JPanel(new GridLayout(1, 2, 12, 0));
        stats.setBackground(BG);

        lblTotal = new JLabel("0");
        lblSelected = new JLabel("NO ORDER SELECTED");

        stats.add(createStatCard("TOTAL ORDERS", lblTotal));
        stats.add(createStatCard("SELECTED ORDER", lblSelected));

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setBackground(BG);

        center.add(createFormPanel(), BorderLayout.WEST);
        center.add(createTablePanel(), BorderLayout.CENTER);

        main.add(stats, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String heading, JLabel value) {

        JPanel card = new JPanel(new GridLayout(2, 1, 0, 8));
        card.setBackground(PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 61, 49)),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
        ));

        JLabel label = new JLabel(heading);
        label.setForeground(MUTED);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));

        value.setForeground(GOLD);
        value.setFont(new Font("SansSerif", Font.BOLD, 20));

        card.add(label);
        card.add(value);

        return card;
    }

    private JPanel createFormPanel() {

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(PANEL);
        form.setPreferredSize(new Dimension(285, 0));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 61, 49)),
                BorderFactory.createEmptyBorder(20, 18, 20, 18)
        ));

        JLabel heading = new JLabel("ORDER DETAILS");
        heading.setForeground(GOLD);
        heading.setFont(new Font("SansSerif", Font.BOLD, 17));

        cmbCustomer = new JComboBox<>();
        cmbStatus = new JComboBox<>(new String[]{
            "Pending", "Preparing", "Completed", "Cancelled"
        });

        txtDate = createField();
        txtDate.setText(LocalDate.now().toString());

        form.add(heading);
        form.add(Box.createVerticalStrut(22));

        addFormField(form, "SELECT CUSTOMER", cmbCustomer);
        addFormField(form, "ORDER DATE (YYYY-MM-DD)", txtDate);
        addFormField(form, "ORDER STATUS", cmbStatus);

        form.add(Box.createVerticalStrut(15));

        JButton add = createButton("CREATE ORDER", GOLD);
        JButton update = createButton("UPDATE ORDER",
                new Color(65, 105, 140));
        JButton delete = createButton("DELETE ORDER",
                new Color(155, 65, 65));
        JButton clear = createButton("CLEAR FORM",
                new Color(75, 78, 86));

        add.addActionListener(e -> addOrder());
        update.addActionListener(e -> updateOrder());
        delete.addActionListener(e -> deleteOrder());
        clear.addActionListener(e -> clearForm());

        form.add(add);
        form.add(Box.createVerticalStrut(10));
        form.add(update);
        form.add(Box.createVerticalStrut(10));
        form.add(delete);
        form.add(Box.createVerticalStrut(10));
        form.add(clear);

        return form;
    }

    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBackground(BG);

        txtSearch = createField();
        txtSearch.setToolTipText("Search by customer or order ID");

        JButton search = createButton("SEARCH", GOLD);
        search.addActionListener(e -> loadOrders());

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(BG);
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(search, BorderLayout.EAST);

        model = new DefaultTableModel(
                new String[]{
                    "ORDER ID", "CUSTOMER", "DATE",
                    "STATUS", "TOTAL (₹)"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(36);
        table.setBackground(PANEL);
        table.setForeground(WHITE);
        table.setSelectionBackground(new Color(95, 76, 43));
        table.setSelectionForeground(WHITE);
        table.setGridColor(new Color(55, 58, 65));
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setBackground(
                new Color(45, 43, 38)
        );
        table.getTableHeader().setForeground(GOLD);
        table.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        table.getTableHeader().setPreferredSize(
                new Dimension(0, 40)
        );

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillForm();
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(PANEL);
        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(65, 61, 49)
        ));

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void createFooter() {

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(BG);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));

        JLabel label = new JLabel(
                "CAFE NOIR  |  ORDER CONTROL CENTER  |  LIVE DATABASE"
        );

        label.setForeground(MUTED);
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));

        footer.add(label);
        add(footer, BorderLayout.SOUTH);
    }

    private JTextField createField() {

        JTextField field = new JTextField();
        field.setBackground(new Color(40, 43, 50));
        field.setForeground(WHITE);
        field.setCaretColor(GOLD);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 75, 75)),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)
        ));

        return field;
    }

    private void addFormField(
            JPanel panel, String name, JComponent input) {

        JLabel label = new JLabel(name);
        label.setForeground(MUTED);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));

        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        input.setPreferredSize(new Dimension(220, 38));

        input.setBackground(new Color(40, 43, 50));
        input.setForeground(WHITE);

        panel.add(label);
        panel.add(Box.createVerticalStrut(6));
        panel.add(input);
        panel.add(Box.createVerticalStrut(15));
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setPreferredSize(new Dimension(120, 38));

        return button;
    }

    private void loadCustomers() {

        cmbCustomer.removeAllItems();

        String sql = "SELECT customer_id, customer_name " +
                     "FROM customers ORDER BY customer_name";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) {
                showError("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    cmbCustomer.addItem(new CustomerItem(
                            rs.getInt("customer_id"),
                            rs.getString("customer_name")
                    ));
                }
            }

        } catch (SQLException e) {
            showError("Customer loading failed: " + e.getMessage());
        }
    }

    private void loadOrders() {

        if (model == null) return;

        model.setRowCount(0);

        String search = txtSearch == null
                ? "" : txtSearch.getText().trim();

        String sql = "SELECT o.order_id, c.customer_name, " +
                "o.order_date, o.status, o.total_amount " +
                "FROM orders o " +
                "JOIN customers c ON o.customer_id = c.customer_id " +
                "WHERE CAST(o.order_id AS CHAR) LIKE ? " +
                "OR c.customer_name LIKE ? " +
                "ORDER BY o.order_id DESC";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) {
                showError("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, "%" + search + "%");
                ps.setString(2, "%" + search + "%");

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getInt("order_id"),
                            rs.getString("customer_name"),
                            rs.getDate("order_date"),
                            rs.getString("status"),
                            rs.getBigDecimal("total_amount")
                        });
                    }
                }
            }

            updateStatistics();

        } catch (SQLException e) {
            showError("Order loading failed: " + e.getMessage());
        }
    }

    private void addOrder() {

        CustomerItem customer =
                (CustomerItem) cmbCustomer.getSelectedItem();

        if (customer == null) {
            showError("Please add a customer first.");
            return;
        }

        Date date = validateDate();
        if (date == null) return;

        String sql = "INSERT INTO orders " +
                "(customer_id, order_date, status, total_amount) " +
                "VALUES (?, ?, ?, 0)";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) {
                showError("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, customer.id);
                ps.setDate(2, date);
                ps.setString(3, cmbStatus.getSelectedItem().toString());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "New order created successfully!");

                refreshAll();
                clearForm();
            }

        } catch (SQLException e) {
            showError("Order creation failed: " + e.getMessage());
        }
    }

    private void updateOrder() {

        if (selectedOrderId == -1) {
            showError("Please select an order from the table.");
            return;
        }

        CustomerItem customer =
                (CustomerItem) cmbCustomer.getSelectedItem();

        if (customer == null) {
            showError("Please select a customer.");
            return;
        }

        Date date = validateDate();
        if (date == null) return;

        String sql = "UPDATE orders SET customer_id=?, " +
                "order_date=?, status=? WHERE order_id=?";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) {
                showError("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, customer.id);
                ps.setDate(2, date);
                ps.setString(3, cmbStatus.getSelectedItem().toString());
                ps.setInt(4, selectedOrderId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Order updated successfully!");

                refreshAll();
                clearForm();
            }

        } catch (SQLException e) {
            showError("Order update failed: " + e.getMessage());
        }
    }

    private void deleteOrder() {

        if (selectedOrderId == -1) {
            showError("Please select an order first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete Order #" + selectedOrderId +
                " and its related records?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        Connection con = null;

        try {

            con = cafemanagmentsystem.getConnection();

            if (con == null) {
                showError("Database connection failed.");
                return;
            }

            con.setAutoCommit(false);

            try (PreparedStatement p1 = con.prepareStatement(
                    "DELETE FROM billing WHERE order_id=?");
                 PreparedStatement p2 = con.prepareStatement(
                    "DELETE FROM order_items WHERE order_id=?");
                 PreparedStatement p3 = con.prepareStatement(
                    "DELETE FROM orders WHERE order_id=?")) {

                p1.setInt(1, selectedOrderId);
                p1.executeUpdate();

                p2.setInt(1, selectedOrderId);
                p2.executeUpdate();

                p3.setInt(1, selectedOrderId);
                p3.executeUpdate();

                con.commit();

                JOptionPane.showMessageDialog(this,
                        "Order and related records deleted!");

                refreshAll();
                clearForm();
            }

        } catch (SQLException e) {

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }
            }

            showError("Order deletion failed: " + e.getMessage());

        } finally {

            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Date validateDate() {

        try {
            return Date.valueOf(txtDate.getText().trim());
        } catch (IllegalArgumentException e) {
            showError("Enter date in YYYY-MM-DD format.");
            return null;
        }
    }

    private void fillForm() {

        int row = table.getSelectedRow();

        if (row < 0) return;

        selectedOrderId = Integer.parseInt(
                model.getValueAt(row, 0).toString()
        );

        String customerName = model.getValueAt(row, 1).toString();

        for (int i = 0; i < cmbCustomer.getItemCount(); i++) {

            CustomerItem item = cmbCustomer.getItemAt(i);

            if (item.name.equals(customerName)) {
                cmbCustomer.setSelectedIndex(i);
                break;
            }
        }

        txtDate.setText(model.getValueAt(row, 2).toString());

        cmbStatus.setSelectedItem(
                model.getValueAt(row, 3).toString()
        );

        lblSelected.setText("#" + selectedOrderId);
    }

    private void clearForm() {

        selectedOrderId = -1;

        if (cmbCustomer.getItemCount() > 0) {
            cmbCustomer.setSelectedIndex(0);
        }

        txtDate.setText(LocalDate.now().toString());
        cmbStatus.setSelectedIndex(0);

        table.clearSelection();
        lblSelected.setText("NO ORDER SELECTED");
    }

    private void updateStatistics() {

        if (lblTotal == null) return;

        String sql = "SELECT COUNT(*) FROM orders";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    lblTotal.setText(rs.getString(1));
                }
            }

        } catch (SQLException e) {
            showError("Statistics failed: " + e.getMessage());
        }
    }

    private void refreshAll() {
        loadCustomers();
        loadOrders();
        updateStatistics();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "CAFE NOIR",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private static class CustomerItem {

        int id;
        String name;

        CustomerItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return id + " - " + name;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OrderManagement::new);
    }
}
