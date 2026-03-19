import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLController {
    public static void insertUser(Connection conn, Users user) throws SQLException {
        String sql = "INSERT INTO users (name, email, cellPhone, month, day) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getCellPhone());
        ps.setInt(4, user.getDateOfBirth()[0]);
        ps.setInt(5, user.getDateOfBirth()[1]);
        ps.executeUpdate();
    }

    public static void readUser(Connection conn, int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.executeQuery();
    }

    public static void updateUserEmail(Connection conn, int userId, String newEmail) throws SQLException {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newEmail);
        ps.setInt(2, userId);
        ps.executeUpdate();
    }

    public static void deleteUser(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.executeUpdate();
    }

    public static void listAllUsers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.executeQuery();
    }

    public static void insertClothing(Connection conn, Clothing clothing) throws SQLException {
        String sql = "INSERT INTO clothing (name, price, stock, size, color) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, clothing.getName());
        ps.setDouble(2, clothing.getPrice());
        ps.setInt(3, clothing.getStock());
        ps.setString(4, clothing.getSize());
        ps.setString(5, clothing.getColor());
        ps.executeUpdate();
    }

    public static void readClothing(Connection conn, int clothingId) throws SQLException {
        String sql = "SELECT * FROM clothing WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, clothingId);
        ps.executeQuery();
    }

    public static void updateClothingPrice(Connection conn, int clothingId, double newPrice) throws SQLException {
        String sql = "UPDATE clothing SET price = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, newPrice);
        ps.setInt(2, clothingId);
        ps.executeUpdate();
    }

    public static void deleteClothing(Connection conn, int clothingId) throws SQLException {
        String sql = "DELETE FROM clothing WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, clothingId);
        ps.executeUpdate();
    }

    public static void listAllClothing(Connection conn) throws SQLException {
        String sql = "SELECT * FROM clothing";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.executeQuery();
    }
}
