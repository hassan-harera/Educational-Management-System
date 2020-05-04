package Items;

import DataBase.MyConnection;
import Persons.Student;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Assignment {

    public int code, grade, solved, ccode;
    public String name,question;

    public Assignment(int code, int grade, int solved, String name) {
        this.code = code;
        this.grade = grade;
        this.solved = solved;
        this.name = name;
    }

    public void viewAssignment(int code) {

        String query = "select * from assignment where code = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ccode = rs.getInt("ccode");
                grade = rs.getInt("grade");
                name = rs.getString("name");
                question= rs.getString("question");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select  D.dname from course C JOIN doctor D ON C.did = D.did where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dname = rs.getString("dname");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select  T.tname from teacher T JOIN course_teacher C ON C.tid = T.tid where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseTeachers.add("tname");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select  cname from course where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseTeachers.add("tname");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("---------------- Course name : " + cname + " ---------------");
        System.out.println("---------------- Course ccode : " + ccode + " ---------------");
        System.out.println("---------------- Course doctor : " + dname + " ---------------");
        System.out.println("---------------- Course teachers : ");
        for (String ct : courseTeachers) {
            System.out.print(ct + "--");
        }
        System.out.println("");
        System.out.println("---------------- Course students : ");
        for (String cs : courseStudents) {
            System.out.print(cs + "--");
        }
        System.out.println("");

    }

}
