package Persons;

/**
 *
 * @author harera
 */
import DataBase.MyConnection;
import java.sql.*;

public class User {

    public static Boolean checkUsername(String username) {
        String query = "select username from user wher username = ?";
        PreparedStatement ps;
        try {
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, username);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public static Boolean checkPassword(String username, String password) {
        String query = "select username from user wher username = ? and password = ?";
        PreparedStatement ps;
        try {
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public static Boolean insertDoctor(String username, String password, String name) {
        String query = "insert into user values (?,?,?,?)";
        PreparedStatement ps;
        try {
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setBoolean(3, true);
            ps.setBoolean(4, false);
            ps.executeQuery();
        } catch (SQLException e) {
            System.out.println(e);
        }

        query = "insert into  doctor values (?,?);";
        try {
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, name);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    public static Boolean insertStudent(String username, String password) {
        String query = "select username from user wher username = ? and password = ?";
        PreparedStatement ps;
        try {
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public static Boolean insertTeacher(String username, String password) {
        String query = "select username from user wher username = ? and password = ?";
        PreparedStatement ps;
        try {
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public static Boolean isTeacher(String username) {
        String query = "select isteacher from user wher username = ?";
        PreparedStatement ps;
        try {
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isteacher");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public static Boolean isDoctor(String username) {
        String query = "select isdoctor from user wher username = ?";
        PreparedStatement ps;
        try {
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isdoctor");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

}
