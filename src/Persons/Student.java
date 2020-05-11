package Persons;

import DataBase.MyConnection;
import Encryption.MyEncryption;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Student implements Comparable<Student> {

    public String assignmentAnswer;
    private String username, password;
    public String name;
    public int id, midGrade, finalGrade, yearDoingGrade, bonusGrade, totalGrade, assignmentGrade;
    Scanner in;

    public Student(String name, int id, int midGrade, int finalGrade, int yearDoingGrade, int bonusGrade, int totalGrade) {
        this.name = name;
        this.id = id;
        this.midGrade = midGrade;
        this.finalGrade = finalGrade;
        this.yearDoingGrade = yearDoingGrade;
        this.bonusGrade = bonusGrade;
        this.totalGrade = totalGrade;
    }

    public Student(String name, int id, String assignmentAnswer) {
        this.name = name;
        this.id = id;
        this.assignmentAnswer = assignmentAnswer;
    }

    public Student(String name, int id, int assignmentGrade) {
        this.name = name;
        this.id = id;
        this.assignmentGrade = assignmentGrade;
    }

    public Student() {
        in = new Scanner(System.in);
    }

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void showMainMenue() {
        System.out.println("1○ Register in course\n"
                + "2○ List My Courses\n"
                + "3○ View a course\n"
                + "4○ Log out\n");

        try {
            int choice = in.nextInt();
            if (choice == 1) {
                registerInCourse();
            } else if (choice == 2) {
                listMyCourses();
            } else if (choice == 3) {
                viewACourse();
            } else if (choice == 4) {
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
        }

        showMainMenue();

    }

    private void registerInCourse() {
        List<String> courses = new ArrayList();
        String query = "select * from course;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            for (int i = 1; rs.next(); i++) {
                courses.add(rs.getString(i));
                System.out.println("-------------------------------------------------------------------course Id: " + rs.getNString("cid") + ")Course name: " + rs.getString("cname") + " ---------------");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        if (courses.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no courses to register ---------------");
        } else {
            while (true) {
                System.out.println("-------------------------------------------------------------------Enter the course Id to register or zero to cancel ---------------");
                try {
                    int choice = in.nextInt();
                    if (choice != 0) {
                        registerInCourse(choice);
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
                    registerInCourse();
                }
            }
        }
    }

    private void listMyCourses() {
        List<String> courses = new ArrayList();
        String query = "select * from student_course where sid = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            for (int i = 1; rs.next(); i++) {
                courses.add(rs.getString(i));
                System.out.println("-------------------------------------------------------------------course Id: " + rs.getNString("cid") + ")Course name: " + rs.getString("cname") + " ---------------");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        if (courses.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no courses was registered in to view ---------------");
        } else {
            while (true) {
                System.out.println("-------------------------------------------------------------------Enter the course Id to view or zero to cancel ---------------");
                try {
                    int choice = in.nextInt();
                    if (choice != 0) {
                        viewCourse(choice);
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
                    registerInCourse();
                }
            }
        }
    }

    private void viewACourse() {

    }

    private void chooseCourse() {

    }

    private void listAllCourses() {

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

}
