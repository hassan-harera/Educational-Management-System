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
        System.out.println("1○ List My courses\n"
                + "2○ Create a course\n"
                + "3○ View a Course\n"
                + "4○ Log out\n");

        makeChoice();
    }

    private void makeChoice() {
        try {
            int choice = in.nextInt();
            if (choice == 1) {
                listCourses();
            } else if (choice == 2) {
                createCourse();
            } else if (choice == 3) {
                viewCourse();
            } else if (choice == 4) {
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("----------------Please enter a correct choice---------------");
        }
        makeChoice();
    }

//    private void registerInCourse() {
//        List<String> courses = new ArrayList();
//        String query = "select * from courses;";
//        try {
//            PreparedStatement ps;
//            ps = MyConnection.con().prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//            for (int i = 1; rs.next(); i++) {
//                courses.add(rs.getString(i));
//                System.out.println("---------------- course Id: " + rs.getNString("cid") + ")Course name: " + rs.getString("cname") + " ---------------");
//            }
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        if (courses.isEmpty()) {
//            System.out.println("---------------- There is no courses to register ---------------");
//        } else {
//            while (true) {
//                System.out.println("---------------- Enter the course Id to register or zero to cancel ---------------");
//                try {
//                    int choice = in.nextInt();
//                    if (choice != 0) {
//                        registerInCourse(choice);
//                    }
//                    break;
//                } catch (NumberFormatException e) {
//                    System.out.println("----------------Please enter a correct choice---------------");
//                    registerInCourse();
//                }
//            }
//        }
//    }
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

    private List<String> listCourses() {
        List<String> courses = new ArrayList();
        String query = "select * from course_doctor where did = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            for (int i = 1; rs.next(); i++) {
                courses.add(rs.getString("ccode"));
                System.out.println("---------------- course code: " + rs.getNString("ccode") + "  Course name: " + rs.getString("cname") + " ---------------");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return courses;
    }

    private void viewCourse() {
        List<String> css = listCourses();
        if (css.isEmpty()) {
            System.out.println("---------------- There is no courses was created to view ---------------");
        } else {
            while (true) {
                System.out.println("---------------- Enter the course code to view or 0 to cancel ---------------");
                try {
                    int choice = in.nextInt();
                    if (choice != 0) {

                        if (checkCourseCode(choice + "")) {
                            viewCourse(choice);
                        } else {
                            System.out.println("---------------- This course code is not found  try again ---------------");
                            viewCourse();
                        }
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("----------------Please enter a correct choice---------------");
                }
            }
        }
    }

    public void signUp() {
        String uname, password;
        System.out.println("----------------Please enter a correct choice---------------");
    }

    private void createCourse() {
        System.out.println("----------------Please enter the course name---------------");
        String cname = in.nextLine();
        if (!checkCourseName(cname)) {
            System.out.println("----------------This course name is already found---------------");
            System.out.println("----------------To try another name eneter 1---------------");
            System.out.println("----------------To cancel eneter 0---------------");
            int choice = in.nextInt();
            if (choice != 0) {
                createCourse();
            }
        } else {
            System.out.println("----------------Please enter the course code---------------");
            String ccode = in.nextLine();
            if (!checkCourseCode(ccode)) {
                System.out.println("----------------This course code is already found---------------");
                System.out.println("----------------To try another code eneter 1---------------");
                System.out.println("----------------To cancel eneter 0---------------");
                int choice = in.nextInt();
                if (choice != 0) {
                    createCourse();
                }
            } else {
                if (insertCourse(cname, ccode)) {
                    System.out.println("----------------Successfully created---------------");
                }
            }
        }
    }

    private boolean checkCourseName(String cname) {
        String query = "select cname from courses where cname = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
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

    private boolean checkCourseCode(String ccode) {
        String query = "select ccode from courses where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    private Boolean insertCourse(String cname, String ccode) {
        String query = "insert (cname,ccode) into courses values (?,?);";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, cname);
            ps.setString(2, ccode);
            return ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private void viewCourse(int choice) {
        
        Sout

    }
}
