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

public class Student {

    private String assignmentAnswer;
    private String username, password;
    private String name;
    private int id, midGrade, finalGrade, yearDoingGrade, bonusGrade, totalGrade, assignmentGrade;
    BufferedReader in;
    private Connection con;

    public Student(int id) {
        this.id = id;
        con = MyConnection.con();
        getIdByUsername();
    }

    public Student(String username) {
        this.username = username;
        con = MyConnection.con();
        getIdByUsername();
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssignmentGrade(int assignmentGrade) {
        this.assignmentGrade = assignmentGrade;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public int getAssignmentGrade() {
        return assignmentGrade;
    }

    public void setAssignmentAnswer(String assignmentAnswer) {
        this.assignmentAnswer = assignmentAnswer;
    }

    public String getAssignmentAnswer() {
        return assignmentAnswer;
    }

    public void setBonusGrade(int bonusGrade) {
        this.bonusGrade = bonusGrade;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public void setFinalGrade(int finalGrade) {
        this.finalGrade = finalGrade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMidGrade(int midGrade) {
        this.midGrade = midGrade;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTotalGrade(int totalGrade) {
        this.totalGrade = totalGrade;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setYearDoingGrade(int yearDoingGrade) {
        this.yearDoingGrade = yearDoingGrade;
    }

    public int getBonusGrade() {
        return bonusGrade;
    }

    public int getFinalGrade() {
        return finalGrade;
    }

    public int getMidGrade() {
        return midGrade;
    }

    public String getPassword() {
        return password;
    }

    public int getTotalGrade() {
        return totalGrade;
    }

    public String getUsername() {
        return username;
    }

    public int getYearDoingGrade() {
        return yearDoingGrade;
    }

    public void showMainMenu() throws IOException {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------STUDENT MENU ---------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");

        System.out.println("1○ Register in course\n"
                + "2○ View My Courses\n"
                + "3○ View a course\n"
                + "4○ Log out");

        try {
            String choice = in.readLine();
            if (choice.equals("1")) {
                registerInCourse();
            } else if (choice.equals("2")) {
                listMyCourses();
            } else if (choice.equals("3")) {
                viewCourse();
            } else if (choice.equals("4")) {
                return;
            } else {
                System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
            }
        } catch (InputMismatchException e) {
            System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
        }

        showMainMenu();
    }

    private void registerInCourse() throws IOException {
        List<Integer> courses = listAllCourses();

        if (courses.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no courses---------------");
        } else {
            System.out.println("-------------------------------------------------------------------Enter the course code that you want to register in---------------");
            while (true) {
                try {
                    String code = in.readLine();
                    if (code.equals("0")) {
                        return;
                    } else if (!courses.contains(Integer.parseInt(code))) {
                        System.out.println("-------------------------------------------------------------------This code is not correct enter another or 0 to cancel---------------");
                    } else {
                        new Course(Integer.parseInt(code)).insertStudent(id);
                        System.out.println("-------------------------------------------------------------------SUCCESFULLY REGISTERED---------------");
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("-------------------------------------------------------------------The cousre code must be number as shown in the course list---------------");
                }
            }
        }
    }

    private List<Integer> listMyCourses() {
        List<Integer> courses = new ArrayList();
        String query = "select C.name,S.ccode from student_course S join course C on C.code = S.ccode where S.sid = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            for (int i = 1; rs.next(); i++) {
                int code = rs.getInt("S.ccode");
                courses.add(code);
                System.out.println("-------------------------------------------------------------------course code: " + code + " Course name: " + rs.getString("C.name") + " ---------------");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return courses;
    }

    private void viewCourse() throws IOException {
        List<Integer> courses = listMyCourses();

        if (courses.isEmpty()) {
            System.out.println("-------------------------------------------------------------------You are not registered in any course---------------");
        } else {
            System.out.println("-------------------------------------------------------------------Enter the course code that you want to view---------------");
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

    private List<Integer> listAllCourses() {
        List<Integer> courseList = new ArrayList();
        String query = "select  C.name, C.code, D.name from course C JOIN doctor D ON C.did = D.id;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            Boolean flag = false;
            while (rs.next()) {
                int code = rs.getInt("code");
                courseList.add(code);
                System.out.println("-------------------------------------------------------------------course code: " + code
                        + " , Course name: " + rs.getString("name")
                        + " , Course doctor: " + rs.getString("D.name") + " ---------------------------------");
                flag = true;
            }
            if (!flag) {
                System.out.println("-------------------------------------------------------------------There is no courses was created in the site ---------------");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return courseList;
    }

    private void registerInCourse(int choice) {

    }

    private void viewCourse(int choice) {

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
        User.insertStudent(username, encrPassword, name);

        System.out.println("-------------------------------------------------------------------SUCCESSFULLY SIGNED UP---------------");

    }

    private void getIdByUsername() {
        String query = "select id from student where username = ?;";
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

    private void courseMenu(int code) throws IOException {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------COURSE MENU ---------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("1○ View my mark report\n"
                + "2○ List assignments\n"
                + "3○ View assignment\n"
                + "4○ Back");

        System.out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        Course c = new Course(code);
        String choice = in.readLine();

        if (choice.equals("1")) {
            c.studentGradeReport(id);
        } else if (choice.equals("2")) {
            c.listAssignments();
        } else if (choice.equals("3")) {
            Assignment a = c.viewAssignment();
            if (a != null) {
                a.setCourseCode(code);
                a.setStudentId(id);
                a.studentAssignmentMenu(code);
            }
        } else if (choice.equals("4")) {
            return;
        } else {
            System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
        }
        courseMenu(code);
    }
}
