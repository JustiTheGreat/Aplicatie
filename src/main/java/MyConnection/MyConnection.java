package MyConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7372667", "sql7372667", "n16nEQUeNT");
        return connection;
    }
}
