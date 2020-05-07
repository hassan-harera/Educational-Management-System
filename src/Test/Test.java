/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import DataBase.MyConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author harera
 */
public class Test {

    private static Boolean insertCourse(String cname, int id) {
        String query = "insert into course  (name,did) values (?,?);";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, cname);
            ps.setInt(2, id);
            return ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        insertCourse("prog1", 1);
    }
}
