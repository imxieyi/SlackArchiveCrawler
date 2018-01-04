package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {

    // utf8mb4: https://stackoverflow.com/questions/24389862/mysql-connectorj-character-set-results-does-not-support-utf8mb4

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/slackdata?serverTimezone=UTC&useSSL=true", "root", "123456");
    }

    public static void close(Connection conn, Statement stat) {
        try {
            if(stat != null && !stat.isClosed()) {
                stat.close();
            }
        } catch (SQLException ignored) {
        } finally {
            try {
                if(conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

}
