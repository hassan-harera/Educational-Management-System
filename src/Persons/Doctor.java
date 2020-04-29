package Persons;

import DataBase.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
DOCTORS
● Each course is created by a doctor. He adds some TAs for the course.
● Course has name, code, and registered students
● Doctors can create assignments, view them, set grades and get statistics
● A Doctor main menu should be
○ "List Courses", "Create course", "View Course", "Log out"
■ Log out option back us to the main menu (sign in / sign up menu)
● If doctor selects View Course,his menu may be like
○ "List Assignments", "Create Assignment", "View Assignment", "Back"
○ Back opton bring him back to the main menu
● An Assignment View menu like
○ "Show Info", "Show Grades Report", "List Solutions", "View Solution", "Back"
● An Assignment Solution menu like
○ "Show Info", "Set Grade", "Set a Comment", "Back"
● Please, think what may be else missing….and try to imagine a good requreiments for
each of the above menu items
 */
public class Doctor {

    private String id, username, password, name, email;
    Scanner in;

    public Doctor() {
        in = new Scanner(System.in);
    }

    public void showMainMenue() {
        System.out.println("1○ Register in course\n"
                + "2○ List My Courses\n"
                + "3○ View a course\n"
                + "4○ Log out\n");
//                + "4○ Grades Report\n"

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
        } catch (NumberFormatException e) {
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
                } catch (NumberFormatException e) {
                    System.out.println("----------------Please enter a correct choice---------------");
                    registerInCourse();
                }
            }
        }
    }

    private void listMyCourses() {
        List<String> courses = new ArrayList();
        String query = "select * from course_student;";
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
                } catch (NumberFormatException e) {
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
        String uname, password;
        System.out.println("----------------Please enter a correct choice---------------");
    }

}
