package DataBase;

import static java.lang.System.out;
import java.sql.Connection;
import static java.sql.DriverManager.getConnection;
import java.sql.SQLException;

public class MyConnection {

    public static Connection con() {
        Connection con = null;
        try {
            con = getConnection("jdbc:mysql://localhost:3306/EducationalManagementSystem?autoReconnect=true&useSSL=false", "root", "0000");
        } catch (SQLException e) {
            out.println(e.getMessage());
        }
        return con;
    }

}
