package ai_app;
import java.sql.*;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/AI_SQL_Assistant",
                "root",
                "root"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

