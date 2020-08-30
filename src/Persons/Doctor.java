package Persons;

import static DataBase.MyConnection.con;
import Items.Assignment;
import Items.Course;
import static Persons.User.checkUsername;
import static Persons.User.insertDoctor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Doctor {

    private String username, password, name;
    private int id;
    private Connection con;
    private BufferedReader in;

    public Doctor(int id) {
        in = new BufferedReader(new InputStreamReader(System.in));
        this.id = id;
        con = con();
    }

    public Doctor(String username) {
        in = new BufferedReader(new InputStreamReader(System.in));
        this.username = username;
        con = con();
        setId();
    }

    public void showMainMenu() throws IOException {
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("-------------------------------------------------------------------DOCTOR MENU ---------------------------------------------");
        out.println("------------------------------------------------------------------------------------------------------------------------------");

        out.println("1○ List all courses\n"
                + "2○ List my courses\n"
                + "3○ Create a course\n"
                + "4○ View a Course\n"
                + "5○ Log out");

        out.println("-------------------------------------------------------------------Please enter a choice ---------------------------------");
        try {
            var choice = in.readLine();
            switch (choice) {
                case "1":
                    listAllCourses();
                    break;
                case "2":
                    listMyCourses();
                    break;
                case "3":
                    createCourse();
                    break;
                case "4":
                    viewCourse();
                    break;
                case "5":
                    return;
                default:
                    break;
            }
        } catch (InputMismatchException e) {
            out.println("-------------------------------------------------------------------Please enter a correct input---------------");
        }
        showMainMenu();
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
        var courses = listMyCourses();

        if (courses.isEmpty()) {
            out.println("-------------------------------------------------------------------There is no courses was created to view ---------------------------------");
        } else {
            out.println("-------------------------------------------------------------------Enter the course code to view or 0 to cancel ---------------------------------");
            try {
                var code = parseInt(in.readLine());
                if (code != 0) {
                    if (courses.contains(code)) {
                        new Course(code).viewCourse();
                        courseMenu(code);
                    } else {
                        out.println("-------------------------------------------------------------------This course code is not found try again ---------------------------------");
                        viewCourse();
                    }
                }
            } catch (NumberFormatException e) {
                out.println("-------------------------------------------------------------------Please enter a correct input---------------");
                viewCourse();
            }
        }
    }

    private List<Integer> listMyCourses() {
        List<Integer> courses = new ArrayList();
        var query = "select * from course where did = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            var rs = ps.executeQuery();
            if (rs.next()) {
                var c = rs.getInt("code");
                out.println("-------------------------------------------------------------------course code: " + c + " , "
                        + "  Course name: " + rs.getString("name") + " ---------------------------------");
                courses.add(c);
                while (rs.next()) {
                    c = rs.getInt("code");
                    out.println("-------------------------------------------------------------------course code: " + c + " , "
                            + "  Course name: " + rs.getString("name") + " ---------------------------------");
                    courses.add(c);
                }
            } else {
                out.println("-------------------------------------------------------------------There is no courses was created in the site ---------------");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        return courses;
    }

    private void createCourse() throws IOException {
        out.println("-------------------------------------------------------------------Please enter the course name or enter 0 to cancel---------------");
        String cname;

        while (true) {
            cname = in.readLine();
            if (cname.equals("0")) {
                return;
            } else if (!checkCourseName(cname)) {
                out.println("-------------------------------------------------------------------This course name is already found enter another or enter 0 to cancel---------------");
            } else {
                insertCourse(cname);
                break;
            }
        }
        out.println("-------------------------------------------------------------------Successfully created---------------");
    }

    private boolean checkCourseName(String cname) {
        var query = "select name from course where name = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setString(1, cname);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        return true;
    }

    private boolean checkCourseCode(int code) {
        var query = "select code from course where code = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        return true;
    }

    private Boolean insertCourse(String cname) {
        var query = "insert into course (name,did) values (?,?);";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setString(1, cname);
            ps.setInt(2, id);
            return ps.execute();
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        return false;
    }

    private void courseMenu(int code) throws IOException {
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("-------------------------------------------------------------------COURSE MENU ---------------------------------------------");
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("1○ View mark report\n"
                + "2○ List assignments\n"
                + "3○ Create assignment\n"
                + "4○ View assignment\n"
                + "5○ Add stduent\n"
                + "6○ Remove stduent\n"
                + "7○ Add TA\n"
                + "8○ Remove TA\n"
                + "9○ Back");

        out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        var c = new Course(code);
        var choice = in.readLine();
        switch (choice) {
            case "1":
                c.markReport();
                break;
            case "2":
                c.listAssignments();
                break;
            case "3":
                c.createAssignment();
                break;
            case "4":
                var a = c.viewAssignment();
                if (a != null) {
                    assignmentMenu(a);
                }
                break;
            case "5":
                c.addStudent();
                break;
            case "6":
                c.removeStduent();
                break;
            case "7":
                c.addTA();
                break;
            case "8":
                c.removeTA();
                break;
            case "9":
                return;
            default:
                out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
                break;
        }
        courseMenu(code);
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

        insertDoctor(username, password, name);

        out.println("-------------------------------------------------------------------SUCCESSFULLY SIGNED UP-------------------------------------------------------------------");;

    }

    private void setId() {
        var query = "select id from doctor where username = ?;";
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

    public void assignmentMenu(Assignment a) throws IOException {
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("-------------------------------------------------------------------ASSIGNMENT MENU ---------------------------------------------");
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("1○ View the assignment report\n"
                + "2○ View the submissions\n"
                + "3○ Edit the questions\n"
                + "4○ View the assignment info\n"
                + "5○ Back");

        out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        var choice = in.readLine();
        switch (choice) {
            case "1":
                a.doctorReport();
                break;
            case "2":
                a.viewSubmissions();
                break;
            case "3":
                a.editQuestions();
                break;
            case "4":
                a.viewAssignment();
                break;
            case "5":
                return;
            default:
                out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
                break;
        }
        assignmentMenu(a);
    }

}
