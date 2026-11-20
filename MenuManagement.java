
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class MenuManagement extends JFrame {

    Color bg = new Color(22, 19, 17);
    Color panel = new Color(42, 34, 29);
    Color gold = new Color(218, 165, 90);
    Color cream = new Color(245, 235, 220);

    JTextField itemName, price, search;
    JComboBox<String> category, availability;

    JTable table;
    DefaultTableModel model;

    JLabel totalItems;

    public MenuManagement() {

        setTitle("CAFE NOIR | Menu Management");
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(20, 22, 20, 22));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bg);

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setBackground(bg);

        JLabel number = new JLabel("03 / 07     CAFE NOIR");
        number.setForeground(gold);
        number.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel title = new JLabel("Menu Management");
        title.setForeground(cream);
        title.setFont(new Font("Serif", Font.BOLD, 34));

        JLabel subtitle = new JLabel(
            "Manage your cafe's food and beverage collection."
        );
        subtitle.setForeground(Color.LIGHT_GRAY);

        heading.add(number);
        heading.add(Box.createVerticalStrut(8));
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        header.add(heading, BorderLayout.WEST);

        totalItems = new JLabel("TOTAL ITEMS: 0");
        totalItems.setForeground(gold);
        totalItems.setFont(new Font("Arial", Font.BOLD, 14));
        totalItems.setBorder(
            new CompoundBorder(
                new LineBorder(gold),
                new EmptyBorder(12, 15, 12, 15)
            )
        );

        header.add(totalItems, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(bg);

        // LEFT FORM
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(panel);
        form.setPreferredSize(new Dimension(315, 500));
        form.setBorder(new EmptyBorder(20, 18, 20, 18));

        JLabel formTitle = new JLabel("ADD / EDIT ITEM");
        formTitle.setForeground(gold);
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));

        form.add(formTitle);
        form.add(Box.createVerticalStrut(22));

        addLabel(form, "ITEM NAME");

        itemName = new JTextField();
        styleField(itemName);
        form.add(itemName);

        form.add(Box.createVerticalStrut(15));

        addLabel(form, "CATEGORY");

        category = new JComboBox<>(new String[] {
            "Coffee",
            "Tea",
            "Pizza",
            "Burger",
            "Sandwich",
            "Snacks",
            "Dessert",
            "Cold Drinks",
            "Other"
        });

        styleCombo(category);
        form.add(category);

        form.add(Box.createVerticalStrut(15));

        addLabel(form, "PRICE (IN RUPEES)");

        price = new JTextField();
        styleField(price);
        form.add(price);

        form.add(Box.createVerticalStrut(15));

        addLabel(form, "AVAILABILITY");

        availability = new JComboBox<>(new String[] {
            "Available",
            "Not Available"
        });

        styleCombo(availability);
        form.add(availability);

        form.add(Box.createVerticalStrut(25));

        JButton add = createButton("ADD MENU ITEM", gold);
        JButton update = createButton(
            "UPDATE ITEM", new Color(90, 125, 155)
        );
        JButton delete = createButton(
            "DELETE ITEM", new Color(160, 65, 60)
        );
        JButton clear = createButton(
            "CLEAR FORM", new Color(90, 85, 80)
        );

        form.add(add);
        form.add(Box.createVerticalStrut(10));
        form.add(update);
        form.add(Box.createVerticalStrut(10));
        form.add(delete);
        form.add(Box.createVerticalStrut(10));
        form.add(clear);

        // RIGHT TABLE SECTION
        JPanel right = new JPanel(new BorderLayout(10, 12));
        right.setBackground(bg);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(bg);

        search = new JTextField();
        search.setFont(new Font("Arial", Font.PLAIN, 14));
        search.setBackground(panel);
        search.setForeground(cream);
        search.setCaretColor(gold);
        search.setBorder(
            new CompoundBorder(
                new LineBorder(gold),
                new EmptyBorder(10, 12, 10, 12)
            )
        );

        JButton searchBtn = createButton("SEARCH", gold);
        JButton refresh = createButton(
            "REFRESH", new Color(90, 125, 100)
        );

        JPanel searchButtons = new JPanel(
            new GridLayout(1, 2, 8, 0)
        );
        searchButtons.setBackground(bg);
        searchButtons.add(searchBtn);
        searchButtons.add(refresh);

        searchPanel.add(search, BorderLayout.CENTER);
        searchPanel.add(searchButtons, BorderLayout.EAST);

        right.add(searchPanel, BorderLayout.NORTH);

        // DATABASE TABLE
        model = new DefaultTableModel(
            new String[] {
                "ID",
                "ITEM NAME",
                "CATEGORY",
                "PRICE",
                "AVAILABILITY"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setBackground(panel);
        table.setForeground(cream);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setSelectionBackground(
            new Color(110, 80, 50)
        );
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(80, 65, 55));
        table.setShowGrid(true);

        table.getTableHeader().setBackground(
            new Color(65, 49, 37)
        );
        table.getTableHeader().setForeground(gold);
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(panel);
        scroll.setBorder(new LineBorder(gold));

        right.add(scroll, BorderLayout.CENTER);

        JLabel footer = new JLabel(
            "CN MENU SYSTEM     |     PREMIUM COLLECTION"
        );
        footer.setForeground(gold);
        footer.setFont(new Font("Arial", Font.BOLD, 11));

        right.add(footer, BorderLayout.SOUTH);

        main.add(form, BorderLayout.WEST);
        main.add(right, BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);

        add(root);

        // BUTTON ACTIONS

        add.addActionListener(e -> addMenu());

        update.addActionListener(e -> updateMenu());

        delete.addActionListener(e -> deleteMenu());

        clear.addActionListener(e -> clearForm());

        refresh.addActionListener(e -> loadMenu());

        searchBtn.addActionListener(e -> searchMenu());

        search.addActionListener(e -> searchMenu());

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                selectMenu();
            }

        });

        // CREATE DATABASE TABLE AND LOAD DATA
        createTable();
        loadMenu();

        setVisible(true);
    }

    // CREATE TABLE AUTOMATICALLY
    private void createTable() {

        String sql =
            "CREATE TABLE IF NOT EXISTS menu (" +
            "menu_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "item_name VARCHAR(100) NOT NULL, " +
            "category VARCHAR(50) NOT NULL, " +
            "price DECIMAL(10,2) NOT NULL, " +
            "availability VARCHAR(30) NOT NULL" +
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

    // ADD MENU ITEM
    private void addMenu() {

        if (!validateForm()) return;

        String sql =
            "INSERT INTO menu " +
            "(item_name, category, price, availability) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection con =
                 cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

                ps.setString(1, itemName.getText().trim());
                ps.setString(2,
                    (String) category.getSelectedItem());
                ps.setDouble(3,
                    Double.parseDouble(price.getText().trim()));
                ps.setString(4,
                    (String) availability.getSelectedItem());

                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                this,
                "Menu Item Added Successfully!",
                "CAFE NOIR",
                JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();
            loadMenu();

        } catch (SQLException e) {

            showError("Add Failed", e);
        }
    }

    // UPDATE MENU ITEM
    private void updateMenu() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select an item to update!"
            );
            return;
        }

        if (!validateForm()) return;

        int id = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        String sql =
            "UPDATE menu SET item_name=?, category=?, " +
            "price=?, availability=? WHERE menu_id=?";

        try (Connection con =
                 cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

                ps.setString(1, itemName.getText().trim());
                ps.setString(2,
                    (String) category.getSelectedItem());
                ps.setDouble(3,
                    Double.parseDouble(price.getText().trim()));
                ps.setString(4,
                    (String) availability.getSelectedItem());
                ps.setInt(5, id);

                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                this, "Menu Item Updated Successfully!"
            );

            clearForm();
            loadMenu();

        } catch (SQLException e) {

            showError("Update Failed", e);
        }
    }

    // DELETE MENU ITEM
    private void deleteMenu() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select an item to delete!"
            );
            return;
        }

        int id = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete selected menu item?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM menu WHERE menu_id=?";

        try (Connection con =
                 cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                this, "Menu Item Deleted Successfully!"
            );

            clearForm();
            loadMenu();

        } catch (SQLException e) {

            showError("Delete Failed", e);
        }
    }

    // VIEW ALL MENU ITEMS
    private void loadMenu() {

        model.setRowCount(0);

        String sql = "SELECT * FROM menu ORDER BY menu_id DESC";

        try (Connection con =
                 cafemanagmentsystem.getConnection()) {

            if (con == null) return;

            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {

                    model.addRow(new Object[] {
                        rs.getInt("menu_id"),
                        rs.getString("item_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("availability")
                    });
                }
            }

            totalItems.setText(
                "TOTAL ITEMS: " + model.getRowCount()
            );

        } catch (SQLException e) {

            showError("Load Failed", e);
        }
    }

    // SEARCH MENU
    private void searchMenu() {

        String keyword = search.getText().trim();

        model.setRowCount(0);

        String sql =
            "SELECT * FROM menu WHERE " +
            "CAST(menu_id AS CHAR) LIKE ? " +
            "OR item_name LIKE ? " +
            "OR category LIKE ? " +
            "ORDER BY menu_id DESC";

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
                            rs.getInt("menu_id"),
                            rs.getString("item_name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getString("availability")
                        });
                    }
                }
            }

            totalItems.setText(
                "SEARCH RESULTS: " + model.getRowCount()
            );

        } catch (SQLException e) {

            showError("Search Failed", e);
        }
    }

    // SELECT ROW
    private void selectMenu() {

        int row = table.getSelectedRow();

        if (row >= 0) {

            itemName.setText(
                model.getValueAt(row, 1).toString()
            );

            category.setSelectedItem(
                model.getValueAt(row, 2).toString()
            );

            price.setText(
                model.getValueAt(row, 3).toString()
            );

            availability.setSelectedItem(
                model.getValueAt(row, 4).toString()
            );
        }
    }

    // FORM VALIDATION
    private boolean validateForm() {

        if (itemName.getText().trim().isEmpty()
                || price.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this, "Please fill all required fields!"
            );

            return false;
        }

        try {

            double p = Double.parseDouble(
                price.getText().trim()
            );

            if (!Double.isFinite(p) || p <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this, "Enter a valid price greater than zero!"
            );

            return false;
        }

        return true;
    }

    // CLEAR FORM
    private void clearForm() {

        itemName.setText("");
        price.setText("");

        category.setSelectedIndex(0);
        availability.setSelectedIndex(0);

        table.clearSelection();
        itemName.requestFocus();
    }

    // LABEL DESIGN
    private void addLabel(JPanel p, String text) {

        JLabel label = new JLabel(text);

        label.setForeground(gold);
        label.setFont(
            new Font("Arial", Font.BOLD, 11)
        );

        p.add(label);
        p.add(Box.createVerticalStrut(7));
    }

    // FIELD DESIGN
    private void styleField(JTextField field) {

        field.setMaximumSize(new Dimension(280, 38));
        field.setPreferredSize(new Dimension(280, 38));
        field.setBackground(bg);
        field.setForeground(cream);
        field.setCaretColor(gold);
        field.setFont(new Font("Arial", Font.PLAIN, 14));

        field.setBorder(
            new CompoundBorder(
                new LineBorder(new Color(110, 85, 60)),
                new EmptyBorder(7, 10, 7, 10)
            )
        );
    }

    // COMBO DESIGN
    private void styleCombo(JComboBox<String> combo) {

        combo.setMaximumSize(new Dimension(280, 38));
        combo.setPreferredSize(new Dimension(280, 38));
        combo.setBackground(bg);
        combo.setForeground(cream);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    // BUTTON DESIGN
    private JButton createButton(String text, Color color) {

        JButton b = new JButton(text);

        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 11));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(12, 10, 12, 10));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return b;
    }

    // ERROR MESSAGE
    private void showError(String title, SQLException e) {

        JOptionPane.showMessageDialog(
            this,
            title + "\n" + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MenuManagement();
        });
    }
}
