package Persons;

import DataBase.MyConnection;
import Encryption.MyEncryption;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Student implements Comparable<Student> {

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
            System.out.println("----------------Please enter a correct choice---------------");
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
                System.out.println("---------------- course Id: " + rs.getNString("cid") + ")Course name: " + rs.getString("cname") + " ---------------");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        if (courses.isEmpty()) {
            System.out.println("---------------- There is no courses to register ---------------");
        } else {
            while (true) {
                System.out.println("---------------- Enter the course Id to register or zero to cancel ---------------");
                try {
                    int choice = in.nextInt();
                    if (choice != 0) {
                        registerInCourse(choice);
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("----------------Please enter a correct choice---------------");
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
                System.out.println("---------------- course Id: " + rs.getNString("cid") + ")Course name: " + rs.getString("cname") + " ---------------");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        if (courses.isEmpty()) {
            System.out.println("---------------- There is no courses was registered in to view ---------------");
        } else {
            while (true) {
                System.out.println("---------------- Enter the course Id to view or zero to cancel ---------------");
                try {
                    int choice = in.nextInt();
                    if (choice != 0) {
                        viewCourse(choice);
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("----------------Please enter a correct choice---------------");
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

    public void signUp() {
        System.out.println("----------------Please enter username---------------");
        String username = in.nextLine();

        try {
            if (User.checkUsername(username)) {
                System.out.println("----------------This username is already found enter another or enter 0 to cancel---------------");
                int choice = in.nextInt();
                if (choice != 0) {
                    signUp();
                }
            } else {
                System.out.println("----------------Please enter password---------------");
                String password = in.nextLine();
                System.out.println("----------------Please enter your name---------------");
                String name = in.nextLine();
                String encrPassword = MyEncryption.encryptPassword(password);
                User.insertStudent(username, encrPassword, name);
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter a correct input---------------");
            signUp();
        }

    }

    @Override
    public int compareTo(Student o) {
        if (id == o.id) {
            return 0;
        }
        return 1;

    }

}
