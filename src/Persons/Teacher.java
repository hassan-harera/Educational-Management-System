


package Persons;

import DataBase.MyConnection;
import Encryption.MyEncryption;
import Items.Course;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Teacher {

    private String username, password, name, email;
    private int id;
    private BufferedReader in;
    private Connection con;

    public Teacher() {
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    public Teacher(String username) {
        in = new BufferedReader(new InputStreamReader(System.in));
        this.username = username;
        con = MyConnection.con();
        setId();
    }

    public void showMainMenu() throws IOException, IOException {

        while (true) {
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------TEACHER MENU ---------------------------------------------");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");

            System.out.println("1○ List all courses\n"
                    + "2○ List my courses\n"
                    + "3○ View a Course\n"
                    + "4○ Log out");

            System.out.println("-------------------------------------------------------------------Please enter a choice ---------------------------------");
            String choice = in.readLine();
            int ch;

            if (choice.matches("^\\d+$")) {
                ch = Integer.parseInt(choice);
                if (ch >= 1 && ch <= 4) {
                    if (ch == 1) {
                        listAllCourses();
                    } else if (ch == 2) {
                        listMyCourses();
                    } else if (ch == 3) {
                        viewCourse();
                    } else if (ch == 4) {
                        break;
                    }
                } else {
                    System.out.println("-------------------------------------------------------------------INVALID CHOICE-------------------------------------------------------------------");
                }
            } else {
                System.out.println("-------------------------------------------------------------------INVALID CHOICE-------------------------------------------------------------------");
            }
        }
    }

    private void listAllCourses() {
        String query = "select  C.name, C.code, D.name from course C JOIN doctor D ON C.did = D.id;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("-------------------------------------------------------------------COURSES LIST-------------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------------course code: " + rs.getInt("code")
                        + " , Course name: " + rs.getString("name")
                        + " , Course doctor: " + rs.getString("D.name") + " ---------------------------------");
                while (rs.next()) {
                    System.out.println("-------------------------------------------------------------------course code: " + rs.getInt("code")
                            + " , Course name: " + rs.getString("name")
                            + " , Course doctor: " + rs.getString("D.name") + " ---------------------------------");

                }
            } else {
                System.out.println("-------------------------------------------------------------------There is no courses was created in the site ---------------");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewCourse() throws IOException {
        List<Integer> css = listMyCourses();
        if (!css.isEmpty()) {
            while (true) {
                System.out.println("-------------------------------------------------------------------Enter the course code to view or 0 to cancel -------------------------------------------------------------------");;
                try {
                    String ccode = in.readLine();
                    if (ccode.equals("0")) {
                        return;
                    } else if (css.contains(ccode)) {
                        new Course(Integer.parseInt(ccode)).viewCourse();
                        break;
                    } else {
                        System.out.println("-------------------------------------------------------------------This course code is not found try another or eneter 0 to cancel -------------------------------------------------------------------");;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("-------------------------------------------------------------------Please enter a correct input-------------------------------------------------------------------");;
                }
            }
        }
    }

    private void viewCourse(int ccode) {
        List<String> courseStudents = new ArrayList<>();
        List<String> courseTeachers = new ArrayList<>();
        String dname = null, cname = null;

        String query = "select  S.sname from student_course C JOIN student S ON S.sid = C.sid where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseStudents.add("sname");
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
            query = "select  cname from course where ccode = ?;";

            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseTeachers.add("tname");
            }

            System.out.println("-------------------------------------------------------------------Course name : " + cname + " -------------------------------------------------------------------");;
            System.out.println("-------------------------------------------------------------------Course ccode : " + ccode + " -------------------------------------------------------------------");;
            System.out.println("-------------------------------------------------------------------Course doctor : " + dname + " -------------------------------------------------------------------");;
            System.out.println("-------------------------------------------------------------------Course teachers : ");
            for (String ct : courseTeachers) {
                System.out.print(ct + "--");
            }
            System.out.println("");
            System.out.println("-------------------------------------------------------------------Course students : ");
            for (String cs : courseStudents) {
                System.out.print(cs + "--");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void signUp() throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("-------------------------------------------------------------------Please enter the username -------------------------------------------------------------------");;
        String username, password;
        while (true) {
            username = in.readLine();
            if (username.equals("0")) {
                return;
            } else if (User.checkUsername(username)) {
                System.out.println("-------------------------------------------------------------------This username is already found enter another or enter 0 to cancel-------------------------------------------------------------------");;
            } else {
                break;
            }
        }

        System.out.println("-------------------------------------------------------------------Please enter the password -------------------------------------------------------------------");;
        password = in.readLine();

        System.out.println("-------------------------------------------------------------------Please enter your name-------------------------------------------------------------------");;
        String name = in.readLine();

        String encrPassword = MyEncryption.encryptPassword(password);
        User.insertTeacher(username, encrPassword, name);

        System.out.println("-------------------------------------------------------------------SUCCESSFULLY SIGNED UP-------------------------------------------------------------------");;

    }

    private void setId() {
        String query = "select id from TA where username = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<Integer> listMyCourses() {
        List<Integer> courses = new ArrayList();
        String query = "select * from student_course where sid = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int c = rs.getInt("code");
                System.out.println("-------------------------------------------------------------------course code: " + c + " , "
                        + "  Course name: " + rs.getString("name") + " ---------------------------------");
                courses.add(c);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        if (courses.isEmpty()) {
            System.out.println("-------------------------------------------------------------------You are not registered in any course-------------------------------------------------------------------");;
        }
        return courses;
    }
}
