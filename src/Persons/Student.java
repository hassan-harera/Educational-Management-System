package Persons;

import DataBase.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
● Have details such as ID, username, password, full name, email
○ email must be validated against wrong formats
● Student main menu should allow him to:
○ Register in course
○ List My Courses
○ View a course
○ Grades Report
○ Log out
● When student choose Register in a course, he got list of courses where he is not
registered
○ Then, user make a choice to be registerd in the course.
● When student choose View a course
○ He got a course summary for him
■ Course name, code
■ List Assignments report (see which submitted, which not, any grading)
○ A menu allows him to
 */
public class Student {

    private String username, password;
    public String name;
    public int  sid, midGrade, finalGrade, yearDoingGrade ,bonusGrade, totalGrade;
    Scanner in;

    public Student(String name, int sid, int midGrade, int finalGrade, int yearDoingGrade, int bonusGrade, int totalGrade) {
        this.name = name;
        this.sid = sid;
        this.midGrade = midGrade;
        this.finalGrade = finalGrade;
        this.yearDoingGrade = yearDoingGrade;
        this.bonusGrade = bonusGrade;
        this.totalGrade = totalGrade;
    }

    
    
    public Student() {
        in = new Scanner(System.in);
    }

    public void showMainMenue() {
        System.out.println("1○ Register in course\n"
                + "2○ List My Courses\n"
                + "3○ View a course\n"
                + "4○ Log out\n");

        makeChoice();
    }

    private void makeChoice() {
        try {
            int choice = in.nextInt();
            if (choice == 1) {
                registerInCourse();
            } else if (choice == 2) {
                listMyCourses();
            } else if (choice == 3) {
                viewACourse();
            } else if (choice == 4) {
                logout();
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter a correct choice---------------");
        }
        makeChoice();
    }

    private void registerInCourse() {
        List<String> courses = new ArrayList();
        String query = "select * from courses;";
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
        String query = "select * from course_student where sid = ?;";
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

    private void logout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
