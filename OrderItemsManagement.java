
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;

public class OrderItemsManagement extends JFrame {

    private JComboBox<String> orderBox, menuBox;
    private JSpinner quantitySpinner;
    private JTextField priceField, subtotalField, searchField;

    private JTable table;
    private DefaultTableModel model;

    private int selectedItemId = -1;

    private final Color BG = new Color(18, 24, 35);
    private final Color PANEL = new Color(29, 38, 52);
    private final Color GOLD = new Color(218, 174, 92);
    private final Color WHITE = new Color(240, 240, 240);

    public OrderItemsManagement() {
        createTable();
        createUI();
        loadOrders();
        loadMenuItems();
        loadOrderItems();

        setTitle("CAFE NOIR | Order Items Management");
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS order_items ("
                + "order_item_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "order_id INT NOT NULL, "
                + "menu_id INT NOT NULL, "
                + "quantity INT NOT NULL, "
                + "unit_price DECIMAL(10,2) NOT NULL, "
                + "subtotal DECIMAL(10,2) NOT NULL, "
                + "FOREIGN KEY (order_id) REFERENCES orders(order_id), "
                + "FOREIGN KEY (menu_id) REFERENCES menu(menu_id)"
                + ")";

        try (Connection con = cafemanagmentsystem.getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate(sql);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Order Items Table Error: " + e.getMessage());
        }
    }

    private void createUI() {

        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

        JLabel title = new JLabel("ORDER ITEMS MANAGEMENT");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(GOLD);

        JLabel number = new JLabel("07 / 07");
        number.setFont(new Font("SansSerif", Font.BOLD, 14));
        number.setForeground(WHITE);

        header.add(title, BorderLayout.WEST);
        header.add(number, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(15, 15));
        center.setBackground(BG);
        center.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));

        JPanel form = new JPanel(new GridLayout(3, 3, 12, 12));
        form.setBackground(PANEL);

        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GOLD),
                "Order Item Details",
                0, 0,
                new Font("SansSerif", Font.BOLD, 15),
                GOLD));

        orderBox = new JComboBox<>();
        menuBox = new JComboBox<>();

        quantitySpinner = new JSpinner(
                new SpinnerNumberModel(1, 1, 1000, 1));

        priceField = new JTextField();
        subtotalField = new JTextField();
        searchField = new JTextField();

        priceField.setEditable(false);
        subtotalField.setEditable(false);

        styleInput(orderBox);
        styleInput(menuBox);
        styleInput(quantitySpinner);
        styleInput(priceField);
        styleInput(subtotalField);
        styleInput(searchField);

        form.add(createField("Select Order", orderBox));
        form.add(createField("Select Menu Item", menuBox));
        form.add(createField("Quantity", quantitySpinner));

        form.add(createField("Unit Price (Rs.)", priceField));
        form.add(createField("Subtotal (Rs.)", subtotalField));
        form.add(createField("Search Item ID", searchField));

        JButton addBtn = createButton("ADD ITEM");
        JButton updateBtn = createButton("UPDATE ITEM");
        JButton deleteBtn = createButton("DELETE ITEM");
        JButton clearBtn = createButton("CLEAR");
        JButton searchBtn = createButton("SEARCH");
        JButton viewBtn = createButton("VIEW ALL");

        form.add(addBtn);
        form.add(updateBtn);
        form.add(deleteBtn);
        form.add(clearBtn);
        form.add(searchBtn);
        form.add(viewBtn);

        addBtn.addActionListener(e -> addItem());
        updateBtn.addActionListener(e -> updateItem());
        deleteBtn.addActionListener(e -> deleteItem());
        clearBtn.addActionListener(e -> clearFields());
        searchBtn.addActionListener(e -> searchItem());
        viewBtn.addActionListener(e -> loadOrderItems());

        menuBox.addActionListener(e -> calculateSubtotal());

        quantitySpinner.addChangeListener(e -> calculateSubtotal());

        orderBox.addActionListener(e -> loadOrderItems());

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(PANEL);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        model = new DefaultTableModel(
                new String[]{
                    "Item ID",
                    "Order ID",
                    "Menu ID",
                    "Item Name",
                    "Quantity",
                    "Unit Price",
                    "Subtotal"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setBackground(PANEL);
        table.setForeground(WHITE);
        table.setSelectionBackground(new Color(83, 72, 49));
        table.setGridColor(new Color(65, 75, 90));

        table.getTableHeader().setBackground(GOLD);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13));

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && table.getSelectedRow() != -1) {

                int row = table.getSelectedRow();

                selectedItemId = Integer.parseInt(
                        model.getValueAt(row, 0).toString());

                String orderId =
                        model.getValueAt(row, 1).toString();

                String menuId =
                        model.getValueAt(row, 2).toString();

                selectCombo(orderBox, orderId);
                selectCombo(menuBox, menuId);

                quantitySpinner.setValue(
                        Integer.parseInt(
                                model.getValueAt(row, 4).toString()));

                priceField.setText(
                        model.getValueAt(row, 5).toString());

                subtotalField.setText(
                        model.getValueAt(row, 6).toString());
            }
        });

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        center.add(form, BorderLayout.NORTH);
        center.add(tablePanel, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    private JPanel createField(String label, JComponent input) {

        JPanel panel = new JPanel(new BorderLayout(4, 5));
        panel.setBackground(PANEL);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(input, BorderLayout.CENTER);

        return panel;
    }

    private void styleInput(JComponent input) {

        input.setFont(new Font("SansSerif", Font.PLAIN, 14));
        input.setBackground(new Color(40, 50, 65));
        input.setForeground(WHITE);
    }

    private JButton createButton(String text) {

        JButton button = new JButton(text);
        button.setBackground(GOLD);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setFocusPainted(false);

        return button;
    }

    private void loadOrders() {

        orderBox.removeAllItems();

        String sql = "SELECT order_id FROM orders "
                + "ORDER BY order_id DESC";

        try (Connection con = cafemanagmentsystem.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orderBox.addItem(
                        rs.getInt("order_id") + " - Order");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Order loading error: " + e.getMessage());
        }
    }

    private void loadMenuItems() {

        menuBox.removeAllItems();

        String sql = "SELECT menu_id, item_name, price "
                + "FROM menu WHERE availability='Available' "
                + "ORDER BY item_name";

        try (Connection con = cafemanagmentsystem.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                menuBox.addItem(
                        rs.getInt("menu_id") + " - "
                        + rs.getString("item_name") + " - Rs."
                        + rs.getBigDecimal("price").toPlainString());
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Menu loading error: " + e.getMessage());
        }
    }

    private int getOrderId() {

        if (orderBox.getSelectedItem() == null) {
            return -1;
        }

        return Integer.parseInt(
                orderBox.getSelectedItem().toString().split(" - ")[0]);
    }

    private int getMenuId() {

        if (menuBox.getSelectedItem() == null) {
            return -1;
        }

        return Integer.parseInt(
                menuBox.getSelectedItem().toString().split(" - ")[0]);
    }

    private void selectCombo(JComboBox<String> box, String id) {

        for (int i = 0; i < box.getItemCount(); i++) {

            if (box.getItemAt(i).startsWith(id + " - ")) {
                box.setSelectedIndex(i);
                return;
            }
        }
    }

    private BigDecimal getMenuPrice() {

        int menuId = getMenuId();

        if (menuId == -1) {
            return BigDecimal.ZERO;
        }

        String sql = "SELECT price FROM menu WHERE menu_id=?";

        try (Connection con = cafemanagmentsystem.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, menuId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getBigDecimal("price");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Price loading error: " + e.getMessage());
        }

        return BigDecimal.ZERO;
    }

    private void calculateSubtotal() {

        BigDecimal price = getMenuPrice();

        int quantity = (Integer) quantitySpinner.getValue();

        BigDecimal subtotal = price.multiply(
                BigDecimal.valueOf(quantity));

        priceField.setText(price.toPlainString());
        subtotalField.setText(subtotal.toPlainString());
    }

    private boolean validateForm() {

        if (getOrderId() == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please create an order first.");
            return false;
        }

        if (getMenuId() == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please add an available menu item first.");
            return false;
        }

        if (getMenuPrice().compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Menu price must be greater than zero.");
            return false;
        }

        return true;
    }

    private void addItem() {

        if (!validateForm()) return;

        calculateSubtotal();

        String sql = "INSERT INTO order_items "
                + "(order_id, menu_id, quantity, unit_price, subtotal) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = cafemanagmentsystem.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            BigDecimal price = getMenuPrice();

            int quantity = (Integer) quantitySpinner.getValue();

            BigDecimal subtotal = price.multiply(
                    BigDecimal.valueOf(quantity));

            ps.setInt(1, getOrderId());
            ps.setInt(2, getMenuId());
            ps.setInt(3, quantity);
            ps.setBigDecimal(4, price);
            ps.setBigDecimal(5, subtotal);

            ps.executeUpdate();

            updateOrderTotal(getOrderId());

            JOptionPane.showMessageDialog(this,
                    "Order item added successfully!");

            loadOrderItems();
            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Add item error: " + e.getMessage());
        }
    }

    private void updateItem() {

        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select an item from the table first.");
            return;
        }

        if (!validateForm()) return;

        calculateSubtotal();

        String sql = "UPDATE order_items SET order_id=?, menu_id=?, "
                + "quantity=?, unit_price=?, subtotal=? "
                + "WHERE order_item_id=?";

        try (Connection con = cafemanagmentsystem.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int oldOrderId = getOrderIdFromItem(selectedItemId);

            BigDecimal price = getMenuPrice();

            int quantity = (Integer) quantitySpinner.getValue();

            BigDecimal subtotal = price.multiply(
                    BigDecimal.valueOf(quantity));

            ps.setInt(1, getOrderId());
            ps.setInt(2, getMenuId());
            ps.setInt(3, quantity);
            ps.setBigDecimal(4, price);
            ps.setBigDecimal(5, subtotal);
            ps.setInt(6, selectedItemId);

            ps.executeUpdate();

            updateOrderTotal(oldOrderId);

            if (oldOrderId != getOrderId()) {
                updateOrderTotal(getOrderId());
            }

            JOptionPane.showMessageDialog(this,
                    "Order item updated successfully!");

            loadOrderItems();
            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Update error: " + e.getMessage());
        }
    }

    private int getOrderIdFromItem(int itemId) throws SQLException {

        String sql = "SELECT order_id FROM order_items "
                + "WHERE order_item_id=?";

        try (Connection con = cafemanagmentsystem.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("order_id");
                }
            }
        }

        return -1;
    }

    private void deleteItem() {

        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select an item first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this item?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {

            int orderId = getOrderIdFromItem(selectedItemId);

            String sql = "DELETE FROM order_items "
                    + "WHERE order_item_id=?";

            try (Connection con = cafemanagmentsystem.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, selectedItemId);
                ps.executeUpdate();
            }

            updateOrderTotal(orderId);

            JOptionPane.showMessageDialog(this,
                    "Order item deleted successfully!");

            loadOrderItems();
            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Delete error: " + e.getMessage());
        }
    }

    private void updateOrderTotal(int orderId) throws SQLException {

        String sql = "UPDATE orders SET total_amount = "
                + "(SELECT COALESCE(SUM(subtotal), 0) "
                + "FROM order_items WHERE order_id=?) "
                + "WHERE order_id=?";

        try (Connection con = cafemanagmentsystem.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }

        // Keep existing bills synchronized with the order total.
        String billSql = "UPDATE billing SET amount = "
                + "(SELECT total_amount FROM orders WHERE order_id=?) "
                + "WHERE order_id=?";

        try (Connection con = cafemanagmentsystem.getConnection();
             PreparedStatement ps = con.prepareStatement(billSql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    private void searchItem() {

        try {

            int id = Integer.parseInt(searchField.getText().trim());

            String sql = "SELECT oi.order_item_id, oi.order_id, "
                    + "oi.menu_id, m.item_name, oi.quantity, "
                    + "oi.unit_price, oi.subtotal "
                    + "FROM order_items oi "
                    + "JOIN menu m ON oi.menu_id=m.menu_id "
                    + "WHERE oi.order_item_id=?";

            try (Connection con = cafemanagmentsystem.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    model.setRowCount(0);

                    if (rs.next()) {
                        addRow(rs);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Order item not found.");
                    }
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Enter a valid Item ID.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Search error: " + e.getMessage());
        }
    }

    private void loadOrderItems() {

        if (model == null) return;

        model.setRowCount(0);

        int orderId = getOrderId();

        String sql = "SELECT oi.order_item_id, oi.order_id, "
                + "oi.menu_id, m.item_name, oi.quantity, "
                + "oi.unit_price, oi.subtotal "
                + "FROM order_items oi "
                + "JOIN menu m ON oi.menu_id=m.menu_id "
                + "WHERE oi.order_id=? "
                + "ORDER BY oi.order_item_id DESC";

        if (orderId == -1) return;

        try (Connection con = cafemanagmentsystem.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    addRow(rs);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Loading error: " + e.getMessage());
        }
    }

    private void addRow(ResultSet rs) throws SQLException {

        model.addRow(new Object[]{
            rs.getInt("order_item_id"),
            rs.getInt("order_id"),
            rs.getInt("menu_id"),
            rs.getString("item_name"),
            rs.getInt("quantity"),
            rs.getBigDecimal("unit_price"),
            rs.getBigDecimal("subtotal")
        });
    }

    private void clearFields() {

        selectedItemId = -1;
        table.clearSelection();

        quantitySpinner.setValue(1);
        searchField.setText("");

        if (menuBox.getItemCount() > 0) {
            menuBox.setSelectedIndex(0);
        }

        calculateSubtotal();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(OrderItemsManagement::new);
    }
}
