
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class cafemanagmentsystem {

    private static final String URL =
        "jdbc:mysql://localhost:3306/cafe_management";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Database Connection
    public static Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                URL, USER, PASSWORD
            );

            return con;

        } catch (ClassNotFoundException | SQLException e) {

            JOptionPane.showMessageDialog(
                null,
                "Database Connection Failed!\n" + e.getMessage(),
                "Connection Error",
                JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }

    // Test Database Connection
    public static void main(String[] args) {

        Connection con = getConnection();

        if (con != null) {

            JOptionPane.showMessageDialog(
                null,
                "Welcome to CAFE NOIR!\n" +
                "Database Connected Successfully.",
                "Connection Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
