
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conectado ao banco!");

            Users user1 = new Users(1, "John Doe", "john.doe@example.com", "123-456-7890", 5, 15);
            Clothing shirt = new Clothing(1, "T-Shirt", 19.99, 50, "M", "Red");
            SQLController.insertUser(conn, user1);
            SQLController.insertClothing(conn, shirt);

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
