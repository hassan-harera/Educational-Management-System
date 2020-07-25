package Persons;

import DataBase.MyConnection;
import Encryption.MyEncryption;
import Items.Assignment;
import Items.Course;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        con = MyConnection.con();
    }

    public Doctor(String username) {
        in = new BufferedReader(new InputStreamReader(System.in));
        this.username = username;
        con = MyConnection.con();
        setId();
    }

    public void showMainMenu() throws IOException {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------DOCTOR MENU ---------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");

        System.out.println("1○ List all courses\n"
                + "2○ List my courses\n"
                + "3○ Create a course\n"
                + "4○ View a Course\n"
                + "5○ Log out");

        System.out.println("-------------------------------------------------------------------Please enter a choice ---------------------------------");
        try {
            String choice = in.readLine();
            if (choice.equals("1")) {
                listAllCourses();
            } else if (choice.equals("2")) {
                listMyCourses();
            } else if (choice.equals("3")) {
                createCourse();
            } else if (choice.equals("4")) {
                viewCourse();
            } else if (choice.equals("5")) {
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("-------------------------------------------------------------------Please enter a correct input---------------");
        }
        showMainMenu();
    }

    private void listAllCourses() {
        String query = "select  C.name, C.code, D.name from course C JOIN doctor D ON C.did = D.id;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
        List<Integer> courses = listMyCourses();

        if (courses.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no courses was created to view ---------------------------------");
        } else {
            System.out.println("-------------------------------------------------------------------Enter the course code to view or 0 to cancel ---------------------------------");
            try {
                int code = Integer.parseInt(in.readLine());
                if (code != 0) {
                    if (courses.contains(code)) {
                        new Course(code).viewCourse();
                        courseMenu(code);
                    } else {
                        System.out.println("-------------------------------------------------------------------This course code is not found try again ---------------------------------");
                        viewCourse();
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("-------------------------------------------------------------------Please enter a correct input---------------");
                viewCourse();
            }
        }
    }

    private List<Integer> listMyCourses() {
        List<Integer> courses = new ArrayList();
        String query = "select * from course where did = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int c = rs.getInt("code");
                System.out.println("-------------------------------------------------------------------course code: " + c + " , "
                        + "  Course name: " + rs.getString("name") + " ---------------------------------");
                courses.add(c);
                while (rs.next()) {
                    c = rs.getInt("code");
                    System.out.println("-------------------------------------------------------------------course code: " + c + " , "
                            + "  Course name: " + rs.getString("name") + " ---------------------------------");
                    courses.add(c);
                }
            } else {
                System.out.println("-------------------------------------------------------------------There is no courses was created in the site ---------------");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return courses;
    }

    private void createCourse() throws IOException {
        System.out.println("-------------------------------------------------------------------Please enter the course name---------------");
        String cname;

        while (true) {
            cname = in.readLine();
            if (username.equals("0")) {
                return;
            } else if (!checkCourseName(cname)) {
                System.out.println("-------------------------------------------------------------------This course name is already found enter another or enter 0 to cancel---------------");
            } else {
                insertCourse(cname);
                break;
            }
        }
        System.out.println("-------------------------------------------------------------------Successfully created---------------");
    }

    private boolean checkCourseName(String cname) {
        String query = "select name from course where name = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setString(1, cname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    private boolean checkCourseCode(int code) {
        String query = "select code from course where code = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    private Boolean insertCourse(String cname) {
        String query = "insert into course (name,did) values (?,?);";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setString(1, cname);
            ps.setInt(2, id);
            return ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private void courseMenu(int code) throws IOException {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------COURSE MENU ---------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("1○ View mark report\n"
                + "2○ List assignments\n"
                + "3○ Create assignment\n"
                + "4○ View assignment\n"
                + "5○ Add stduent\n"
                + "6○ Remove stduent\n"
                + "7○ Add TA\n"
                + "8○ Remove TA\n"
                + "9○ Back");

        System.out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        Course c = new Course(code);
        String choice = in.readLine();

        if (choice.equals("1")) {
            c.markReport();
        } else if (choice.equals("2")) {
            c.listAssignments();
        } else if (choice.equals("3")) {
            c.createAssignment();
        } else if (choice.equals("4")) {
            Assignment a = c.viewAssignment();
            if (a != null) {
                assignmentMenu(a);
            }
        } else if (choice.equals("5")) {
            c.addStudent();
        } else if (choice.equals("6")) {
            c.removeStduent();
        } else if (choice.equals("7")) {
            c.addTA();
        } else if (choice.equals("8")) {
            c.removeTA();
        } else if (choice.equals("9")) {
            return;
        } else {
            System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
        }
        courseMenu(code);
    }

    public static void signUp() throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("-------------------------------------------------------------------Please enter the username ---------------");
        String username, password;
        while (true) {
            username = in.readLine();
            if (username.equals("0")) {
                return;
            } else if (User.checkUsername(username)) {
                System.out.println("-------------------------------------------------------------------This username is already found enter another or enter 0 to cancel---------------");
            } else {
                break;
            }
        }

        System.out.println("-------------------------------------------------------------------Please enter the password ---------------");
        password = in.readLine();

        System.out.println("-------------------------------------------------------------------Please enter your name---------------");
        String name = in.readLine();

        String encrPassword = MyEncryption.encryptPassword(password);
        User.insertDoctor(username, encrPassword, name);

        System.out.println("-------------------------------------------------------------------SUCCESSFULLY SIGNED UP---------------");

    }

    private void setId() {
        String query = "select id from doctor where username = ?;";
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

    public void assignmentMenu(Assignment a) throws IOException {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------ASSIGNMENT MENU ---------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("1○ View the assignment report\n"
                + "2○ View the submissions\n"
                + "3○ Edit the questions\n"
                + "4○ View the assignment info\n"
                + "5○ Back");

        System.out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        String choice = in.readLine();

        if (choice.equals("1")) {
            a.report();
        } else if (choice.equals("2")) {
            a.viewSubmissions();
        } else if (choice.equals("3")) {
            a.editQuestions();
        } else if (choice.equals("4")) {
            a.viewAssignment();
        } else if (choice.equals("5")) {
            return;
        } else {
            System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
        }
        assignmentMenu(a);
    }

}
