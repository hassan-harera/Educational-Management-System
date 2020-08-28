package Persons;

import static DataBase.MyConnection.con;
import static Encryption.MyEncryption.encryptPassword;
import Items.Course;
import static Persons.User.checkUsername;
import static Persons.User.insertTeacher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;
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
        con = con();
        setId();
    }

    public void showMainMenu() throws IOException, IOException {

        while (true) {
            out.println("------------------------------------------------------------------------------------------------------------------------------");
            out.println("-------------------------------------------------------------------TEACHER MENU ---------------------------------------------");
            out.println("------------------------------------------------------------------------------------------------------------------------------");

            out.println("1○ List all courses\n"
                    + "2○ List my courses\n"
                    + "3○ View a Course\n"
                    + "4○ Log out");

            out.println("-------------------------------------------------------------------Please enter a choice ---------------------------------");
            var choice = in.readLine();
            int ch;

            if (choice.matches("^\\d+$")) {
                ch = parseInt(choice);
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
                    out.println("-------------------------------------------------------------------INVALID CHOICE-------------------------------------------------------------------");
                }
            } else {
                out.println("-------------------------------------------------------------------INVALID CHOICE-------------------------------------------------------------------");
            }
        }
    }

    private void listAllCourses() {
        var query = "select  C.name, C.code, D.name from course C JOIN doctor D ON C.did = D.id;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            var rs = ps.executeQuery();
            if (rs.next()) {
                out.println("-------------------------------------------------------------------COURSES LIST-------------------------------------------------------------------");
                out.println("-------------------------------------------------------------------course code: " + rs.getInt("code")
                        + " , Course name: " + rs.getString("name")
                        + " , Course doctor: " + rs.getString("D.name") + " ---------------------------------");
                while (rs.next()) {
                    out.println("-------------------------------------------------------------------course code: " + rs.getInt("code")
                            + " , Course name: " + rs.getString("name")
                            + " , Course doctor: " + rs.getString("D.name") + " ---------------------------------");

                }
            } else {
                out.println("-------------------------------------------------------------------There is no courses was created in the site ---------------");
            }

        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    private void viewCourse() throws IOException {
        var css = listMyCourses();
        if (!css.isEmpty()) {
            while (true) {
                out.println("-------------------------------------------------------------------Enter the course code to view or 0 to cancel -------------------------------------------------------------------");;
                try {
                    var ccode = in.readLine();
                    if (ccode.equals("0")) {
                        return;
                    } else if (css.contains(ccode)) {
                        new Course(parseInt(ccode)).viewCourse();
                        break;
                    } else {
                        out.println("-------------------------------------------------------------------This course code is not found try another or eneter 0 to cancel -------------------------------------------------------------------");;
                    }
                } catch (InputMismatchException e) {
                    out.println("-------------------------------------------------------------------Please enter a correct input-------------------------------------------------------------------");;
                }
            }
        }
    }

    private void viewCourse(int ccode) {
        List<String> courseStudents = new ArrayList<>();
        List<String> courseTeachers = new ArrayList<>();
        String dname = null, cname = null;

        var query = "select  S.sname from student_course C JOIN student S ON S.sid = C.sid where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setString(1, ccode + "");
            var rs = ps.executeQuery();
            while (rs.next()) {
                courseStudents.add("sname");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        query = "select  D.dname from course C JOIN doctor D ON C.did = D.did where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setString(1, ccode + "");
            var rs = ps.executeQuery();
            while (rs.next()) {
                dname = rs.getString("dname");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        query = "select  T.tname from teacher T JOIN course_teacher C ON C.tid = T.tid where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setString(1, ccode + "");
            var rs = ps.executeQuery();
            while (rs.next()) {
                courseTeachers.add("tname");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        query = "select  cname from course where ccode = ?;";
        try {
            query = "select  cname from course where ccode = ?;";

            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setString(1, ccode + "");
            var rs = ps.executeQuery();
            while (rs.next()) {
                courseTeachers.add("tname");
            }

            out.println("-------------------------------------------------------------------Course name : " + cname + " -------------------------------------------------------------------");;
            out.println("-------------------------------------------------------------------Course ccode : " + ccode + " -------------------------------------------------------------------");;
            out.println("-------------------------------------------------------------------Course doctor : " + dname + " -------------------------------------------------------------------");;
            out.println("-------------------------------------------------------------------Course teachers : ");
            for (var ct : courseTeachers) {
                out.print(ct + "--");
            }
            out.println("");
            out.println("-------------------------------------------------------------------Course students : ");
            for (var cs : courseStudents) {
                out.print(cs + "--");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    public static void signUp() throws IOException {

        var in = new BufferedReader(new InputStreamReader(System.in));

        out.println("-------------------------------------------------------------------Please enter the username -------------------------------------------------------------------");;
        String username, password;
        while (true) {
            username = in.readLine();
            if (username.equals("0")) {
                return;
            } else if (checkUsername(username)) {
                out.println("-------------------------------------------------------------------This username is already found enter another or enter 0 to cancel-------------------------------------------------------------------");;
            } else {
                break;
            }
        }

        out.println("-------------------------------------------------------------------Please enter the password -------------------------------------------------------------------");;
        password = in.readLine();

        out.println("-------------------------------------------------------------------Please enter your name-------------------------------------------------------------------");;
        var name = in.readLine();
        var encrPassword = encryptPassword(password);
        insertTeacher(username, encrPassword, name);

        out.println("-------------------------------------------------------------------SUCCESSFULLY SIGNED UP-------------------------------------------------------------------");;

    }

    private void setId() {
        var query = "select id from TA where username = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            var rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    private List<Integer> listMyCourses() {
        List<Integer> courses = new ArrayList();
        var query = "select * from student_course where sid = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            var rs = ps.executeQuery();

            while (rs.next()) {
                var c = rs.getInt("code");
                out.println("-------------------------------------------------------------------course code: " + c + " , "
                        + "  Course name: " + rs.getString("name") + " ---------------------------------");
                courses.add(c);
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        if (courses.isEmpty()) {
            out.println("-------------------------------------------------------------------You are not registered in any course-------------------------------------------------------------------");;
        }
        return courses;
    }
}
