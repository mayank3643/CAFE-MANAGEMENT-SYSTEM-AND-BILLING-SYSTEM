
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;

public class BillingManagement extends JFrame {

    private final Color BG = new Color(18, 20, 25);
    private final Color PANEL = new Color(29, 32, 39);
    private final Color GOLD = new Color(212, 175, 100);
    private final Color WHITE = new Color(245, 245, 245);
    private final Color MUTED = new Color(165, 169, 178);

    private JComboBox<OrderItem> cmbOrder;
    private JComboBox<String> cmbMethod;
    private JComboBox<String> cmbStatus;

    private JTextField txtAmount;
    private JTextField txtSearch;

    private JTable table;
    private DefaultTableModel model;

    private JLabel lblBills;
    private JLabel lblRevenue;
    private JLabel lblSelected;

    private int selectedBillId = -1;

    public BillingManagement() {

        setTitle("CAFE NOIR | Billing Management");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BG);

        createHeader();
        createMainPanel();
        createFooter();

        loadOrders();
        loadBills();
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
                "PREMIUM BILLING MANAGEMENT  |  06 / 07"
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

        JPanel stats = new JPanel(new GridLayout(1, 3, 12, 0));
        stats.setBackground(BG);

        lblBills = new JLabel("0");
        lblRevenue = new JLabel("₹0.00");
        lblSelected = new JLabel("NO BILL SELECTED");

        stats.add(createStatCard("TOTAL BILLS", lblBills));
        stats.add(createStatCard("PAID REVENUE", lblRevenue));
        stats.add(createStatCard("SELECTED BILL", lblSelected));

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
        form.setPreferredSize(new Dimension(290, 0));

        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 61, 49)),
                BorderFactory.createEmptyBorder(20, 18, 20, 18)
        ));

        JLabel heading = new JLabel("BILL DETAILS");
        heading.setForeground(GOLD);
        heading.setFont(new Font("SansSerif", Font.BOLD, 17));

        cmbOrder = new JComboBox<>();
        cmbMethod = new JComboBox<>(new String[]{
            "Cash", "UPI", "Card"
        });

        cmbStatus = new JComboBox<>(new String[]{
            "Pending", "Paid", "Failed"
        });

        txtAmount = createField();
        txtAmount.setEditable(false);

        cmbOrder.addActionListener(e -> loadOrderAmount());

        form.add(heading);
        form.add(Box.createVerticalStrut(22));

        addFormField(form, "SELECT ORDER", cmbOrder);
        addFormField(form, "BILL AMOUNT (₹)", txtAmount);
        addFormField(form, "PAYMENT METHOD", cmbMethod);
        addFormField(form, "PAYMENT STATUS", cmbStatus);

        form.add(Box.createVerticalStrut(15));

        JButton add = createButton("GENERATE BILL", GOLD);
        JButton update = createButton(
                "UPDATE BILL", new Color(65, 105, 140)
        );
        JButton delete = createButton(
                "DELETE BILL", new Color(155, 65, 65)
        );
        JButton clear = createButton(
                "CLEAR FORM", new Color(75, 78, 86)
        );

        add.addActionListener(e -> addBill());
        update.addActionListener(e -> updateBill());
        delete.addActionListener(e -> deleteBill());
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
        txtSearch.setToolTipText("Search bill ID, order ID or customer");

        JButton search = createButton("SEARCH", GOLD);
        search.addActionListener(e -> loadBills());

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(BG);
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(search, BorderLayout.EAST);

        model = new DefaultTableModel(
                new String[]{
                    "BILL ID", "ORDER ID", "CUSTOMER",
                    "DATE", "AMOUNT (₹)", "METHOD", "STATUS"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
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
                new Font("SansSerif", Font.BOLD, 11)
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
                "CAFE NOIR  |  FINANCE CONTROL CENTER  |  LIVE DATABASE"
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
        button.setPreferredSize(new Dimension(125, 38));

        return button;
    }

    private void loadOrders() {

        cmbOrder.removeAllItems();

        String sql = "SELECT o.order_id, c.customer_name, " +
                "o.total_amount FROM orders o " +
                "JOIN customers c ON o.customer_id=c.customer_id " +
                "ORDER BY o.order_id DESC";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) {
                showError("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    cmbOrder.addItem(new OrderItem(
                            rs.getInt("order_id"),
                            rs.getString("customer_name"),
                            rs.getBigDecimal("total_amount")
                    ));
                }
            }

        } catch (SQLException e) {
            showError("Order loading failed: " + e.getMessage());
        }
    }

    private void loadOrderAmount() {

        if (cmbOrder == null || txtAmount == null) return;

        OrderItem order =
                (OrderItem) cmbOrder.getSelectedItem();

        if (order != null) {
            txtAmount.setText(order.amount.toPlainString());
        } else {
            txtAmount.setText("");
        }
    }

    private void loadBills() {

        if (model == null) return;

        model.setRowCount(0);

        String search = txtSearch == null
                ? "" : txtSearch.getText().trim();

        String sql = "SELECT b.bill_id, b.order_id, " +
                "c.customer_name, b.bill_date, b.amount, " +
                "b.payment_method, b.payment_status " +
                "FROM billing b " +
                "JOIN orders o ON b.order_id=o.order_id " +
                "JOIN customers c ON o.customer_id=c.customer_id " +
                "WHERE CAST(b.bill_id AS CHAR) LIKE ? " +
                "OR CAST(b.order_id AS CHAR) LIKE ? " +
                "OR c.customer_name LIKE ? " +
                "ORDER BY b.bill_id DESC";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) {
                showError("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, "%" + search + "%");
                ps.setString(2, "%" + search + "%");
                ps.setString(3, "%" + search + "%");

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        model.addRow(new Object[]{
                            rs.getInt("bill_id"),
                            rs.getInt("order_id"),
                            rs.getString("customer_name"),
                            rs.getTimestamp("bill_date"),
                            rs.getBigDecimal("amount"),
                            rs.getString("payment_method"),
                            rs.getString("payment_status")
                        });
                    }
                }
            }

            updateStatistics();

        } catch (SQLException e) {
            showError("Billing loading failed: " + e.getMessage());
        }
    }

    private void addBill() {

        OrderItem order =
                (OrderItem) cmbOrder.getSelectedItem();

        if (order == null) {
            showError("Please create an order first.");
            return;
        }

        if (order.amount.compareTo(BigDecimal.ZERO) <= 0) {
            showError("Order total must be greater than zero.");
            return;
        }

        String sql = "INSERT INTO billing " +
                "(order_id, amount, payment_method, payment_status) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) {
                showError("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, order.id);
                ps.setBigDecimal(2, order.amount);
                ps.setString(3, cmbMethod.getSelectedItem().toString());
                ps.setString(4, cmbStatus.getSelectedItem().toString());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Bill generated successfully!");

                refreshAll();
                clearForm();
            }

        } catch (SQLException e) {
            showError("Bill creation failed: " + e.getMessage());
        }
    }

    private void updateBill() {

        if (selectedBillId == -1) {
            showError("Please select a bill first.");
            return;
        }

        OrderItem order =
                (OrderItem) cmbOrder.getSelectedItem();

        if (order == null) {
            showError("Please select an order.");
            return;
        }

        if (order.amount.compareTo(BigDecimal.ZERO) <= 0) {
            showError("Order total must be greater than zero.");
            return;
        }

        String sql = "UPDATE billing SET order_id=?, amount=?, " +
                "payment_method=?, payment_status=? WHERE bill_id=?";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) {
                showError("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, order.id);
                ps.setBigDecimal(2, order.amount);
                ps.setString(3, cmbMethod.getSelectedItem().toString());
                ps.setString(4, cmbStatus.getSelectedItem().toString());
                ps.setInt(5, selectedBillId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Bill updated successfully!");

                refreshAll();
                clearForm();
            }

        } catch (SQLException e) {
            showError("Bill update failed: " + e.getMessage());
        }
    }

    private void deleteBill() {

        if (selectedBillId == -1) {
            showError("Please select a bill first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete Bill #" + selectedBillId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM billing WHERE bill_id=?";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) {
                showError("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, selectedBillId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Bill deleted successfully!");

                refreshAll();
                clearForm();
            }

        } catch (SQLException e) {
            showError("Bill deletion failed: " + e.getMessage());
        }
    }

    private void fillForm() {

        int row = table.getSelectedRow();

        if (row < 0) return;

        selectedBillId = Integer.parseInt(
                model.getValueAt(row, 0).toString()
        );

        int orderId = Integer.parseInt(
                model.getValueAt(row, 1).toString()
        );

        for (int i = 0; i < cmbOrder.getItemCount(); i++) {

            OrderItem item = cmbOrder.getItemAt(i);

            if (item.id == orderId) {
                cmbOrder.setSelectedIndex(i);
                break;
            }
        }

        txtAmount.setText(
                model.getValueAt(row, 4).toString()
        );

        cmbMethod.setSelectedItem(
                model.getValueAt(row, 5).toString()
        );

        cmbStatus.setSelectedItem(
                model.getValueAt(row, 6).toString()
        );

        lblSelected.setText("#" + selectedBillId);
    }

    private void clearForm() {

        selectedBillId = -1;

        if (cmbOrder.getItemCount() > 0) {
            cmbOrder.setSelectedIndex(0);
        } else {
            txtAmount.setText("");
        }

        cmbMethod.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);

        table.clearSelection();
        lblSelected.setText("NO BILL SELECTED");

        loadOrderAmount();
    }

    private void updateStatistics() {

        if (lblBills == null) return;

        String sql = "SELECT COUNT(*) AS total, " +
                "COALESCE(SUM(CASE WHEN payment_status='Paid' " +
                "THEN amount ELSE 0 END),0) AS revenue " +
                "FROM billing";

        try (Connection con = cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    lblBills.setText(rs.getString("total"));

                    lblRevenue.setText(
                            "₹" + rs.getBigDecimal("revenue")
                    );
                }
            }

        } catch (SQLException e) {
            showError("Statistics failed: " + e.getMessage());
        }
    }

    private void refreshAll() {
        loadOrders();
        loadBills();
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

    private static class OrderItem {

        int id;
        String customer;
        BigDecimal amount;

        OrderItem(int id, String customer, BigDecimal amount) {
            this.id = id;
            this.customer = customer;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Order #" + id + " - " + customer;
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(BillingManagement::new);
    }
}
