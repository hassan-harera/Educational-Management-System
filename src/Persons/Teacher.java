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
            String code;
            int Code;
            while (true) {
                out.println("------------------------------------------------------------------Enter the cousre code------------------------------------------------------------------");
                code = in.readLine();
                if (code.matches("^\\d+$")) {
                    Code = parseInt(code);
                    if (Code == 0) {
                        return;
                    } else if (css.contains(Code)) {
                        viewCourse(Code);
                        break;
                    } else {
                        out.println("------------------------------------------------------------------INVALID CODE------------------------------------------------------------------");
                    }
                } else {
                    out.println("------------------------------------------------------------------INVALID CODE------------------------------------------------------------------");
                }
            }
        }
    }

    private void viewCourse(int code) {
        Course course = new Course(code);
        course.viewCourse();

    }

    public static void signUp() throws IOException {

        var in = new BufferedReader(new InputStreamReader(System.in));
        String name, username, password;

        while (true) {
            out.println("-------------------------------------------------------------------Please enter the username-------------------------------------------------------------------");
            username = in.readLine();
            if (username.equals("0")) {
                return;
            } else if (username.matches("^([a-zA-Z])+([\\w@]{2,})+$")) {
                if (checkUsername(username)) {
                    out.println("-------------------------------------------------------------------This username is already found-------------------------------------------------------------------");;
                } else {
                    break;
                }
            } else {
                out.println("Username is invalid, username terms: ");
                out.println("Must start with the alphabet\n"
                        + "Only allow underscore\n"
                        + "Minimum 3 chars");
            }
        }

        while (true) {
            out.println("-------------------------------------------------------------------Please enter the password-------------------------------------------------------------------");
            password = in.readLine();
            if (password.equals("0")) {
                return;
            } else if (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
                break;
            } else {
                out.println("Username is invalid, username terms: ");
                out.println("at least eight\n"
                        + "at least one digit\n"
                        + "at least one lower case letter\n"
                        + "at least one upper case\n"
                        + "at least one special character\n"
                        + "whitespace is not allowed");
            }
        }

        while (true) {
            out.println("-------------------------------------------------------------------Please enter your name-------------------------------------------------------------------");
            name = in.readLine();
            if (name.equals("0")) {
                return;
            } else if (name.matches("^[a-zA-Z\\s]+")) {
                break;
            } else {
                out.println("-------------------------------------------------------------------INVALID NAME-------------------------------------------------------------------");
            }
        }

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
        var query = "select C.name, C.code from TA_course T join course C on C.code = T.ccode where T.tid = ?;";
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

    private void courseMenu(int code) throws IOException {
        var c = new Course(code);

        while (true) {
            out.println("------------------------------------------------------------------------------------------------------------------------------");
            out.println("-------------------------------------------------------------------COURSE MENU ---------------------------------------------");
            out.println("------------------------------------------------------------------------------------------------------------------------------");
            out.println("1○ View mark report\n"
                    + "2○ List assignments\n"
                    + "3○ View assignment\n"
                    + "4○ Add stduent\n"
                    + "5○ Back");

            out.println("-------------------------------------------------------------------Please enter a choice------------------------------");

            var choice = in.readLine();
            int Cohice;
            if (choice.matches("^\\d+$")) {
                Cohice = parseInt(choice);
                if (Cohice == 1) {
                    c.markReport();
                } else if (Cohice == 2) {
                    c.listAssignments();
                } else if (Cohice == 3) {
                    c.viewAssignment();
                } else if (Cohice == 4) {
                    c.addStudent();
                } else if (Cohice == 5) {
                    return;
                } else {
                    out.println("-------------------------------------------------------------------INVALID CHOICE-------------------------------------------------------------------");
                }
            } else {
                out.println("-------------------------------------------------------------------INVALID CHOICE-------------------------------------------------------------------");
            }
        }

    }

}
