package Persons;

import DataBase.MyConnection;
import Encryption.MyEncryption;
import Items.Course;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Teacher {

    private String id, username, password, name, email;
    private int did;
    BufferedReader in;

    public Teacher() {
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    public Teacher(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void showMainMenu() throws IOException {
        System.out.println("1○ List My courses\n"
                + "2○ Create a course\n"
                + "3○ View a Course\n"
                + "4○ Log out\n");

        System.out.println("-------------------------------------------------------------------Please enter a input-------------------------------------------------------------------");;

        String choice = in.readLine();
        if (choice.equals("1")) {
            listCourses();
        } else if (choice.equals("2")) {
            createCourse();
        } else if (choice.equals("3")) {
            viewCourse();
        } else if (choice.equals("4")) {
            return;
        } else {
            System.out.println("-------------------------------------------------------------------Please enter a correct input-------------------------------------------------------------------");;
        }

        showMainMenu();
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
//                System.out.println("-------------------------------------------------------------------course Id: " + rs.getNString("cid") + ")Course name: " + rs.getString("cname") + " -------------------------------------------------------------------");;
//            }
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        if (courses.isEmpty()) {
//            System.out.println("-------------------------------------------------------------------There is no courses to register -------------------------------------------------------------------");;
//        } else {
//            while (true) {
//                System.out.println("-------------------------------------------------------------------Enter the course Id to register or zero to cancel -------------------------------------------------------------------");;
//                try {
//                    int choice = in.nextInt();
//                    if (choice != 0) {
//                        registerInCourse(choice);
//                    }
//                    break;
//                } catch (InputMismatchException e) {
//                    System.out.println("-------------------------------------------------------------------Please enter a correct choice-------------------------------------------------------------------");;
//                    registerInCourse();
//                }
//            }
//        }
//    }
    private void listAllCourses() {
        List<String> courses = new ArrayList();
        String query = "select  C.cname, C.ccode, D.dname from course C JOIN doctor D ON C.did = D.did;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, did);
            ResultSet rs = ps.executeQuery();
            for (int i = 1; rs.next(); i++) {
                courses.add(rs.getString(i));
                System.out.println("-------------------------------------------------------------------course code: " + rs.getNString("ccode")
                        + " , Course name: " + rs.getString("cname")
                        + " , Course doctor: " + rs.getString("dname") + " -------------------------------------------------------------------");;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

//        if (courses.isEmpty()) {
//            System.out.println("-------------------------------------------------------------------There is no courses was created to view -------------------------------------------------------------------");;
//        } else {
//            while (true) {
//                System.out.println("-------------------------------------------------------------------Enter the course Id to view or zero to go back -------------------------------------------------------------------");;
//                try {
//                    int choice = in.nextInt();
//                    if (choice != 0) {
//                        viewCourse(choice);
//                    }
//                    break;
//                } catch (InputMismatchException e) {
//                    System.out.println("-------------------------------------------------------------------Please enter a correct choice-------------------------------------------------------------------");;
//                    registerInCourse();
//                }
//            }
//        }
    }

    private void viewCourse() throws IOException {
        List<String> css = listCourses();
        if (css.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no courses was created to view -------------------------------------------------------------------");;
        } else {
            while (true) {
                System.out.println("-------------------------------------------------------------------Enter the course code to view or 0 to cancel -------------------------------------------------------------------");;
                try {
                    String ccode = in.readLine();
                    if (ccode.equals("0")) {
                        return;
                    } else if (css.contains(ccode)) {
                        new Course(Integer.parseInt(ccode)).viewCourse();
//                        courseMenu(Integer.parseInt(ccode));
                        break;
                    } else {
                        System.out.println("-------------------------------------------------------------------This course code is not found try another or eneter 0 to cancel -------------------------------------------------------------------");;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("-------------------------------------------------------------------Please enter a correct input-------------------------------------------------------------------");;
                }
            }
        }
    }

    private List<String> listCourses() {
        List<String> coursesCode = new ArrayList();
        String query = "select * from course where did = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            for (int i = 1; rs.next(); i++) {
                coursesCode.add(rs.getString("ccode"));
                System.out.println("-------------------------------------------------------------------course code: " + rs.getNString("ccode") + "  Course name: " + rs.getString("cname") + " -------------------------------------------------------------------");;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return coursesCode;
    }

    private void createCourse() throws IOException {
        String cname, ccode;
        System.out.println("-------------------------------------------------------------------Please enter the course name-------------------------------------------------------------------");;
        while (true) {
            cname = in.readLine();
            if (cname.equals("0")) {
                return;
            } else if (!checkCourseName(cname)) {
                System.out.println("-------------------------------------------------------------------This course name is already found enter another or 0 to cancel-------------------------------------------------------------------");;
            } else {
                break;
            }
        }

        System.out.println("-------------------------------------------------------------------Please enter the course code-------------------------------------------------------------------");
        while (true) {
            try {
                ccode = in.readLine();
                if (ccode.equals("0")) {
                    return;
                } else if (!checkCourseCode(Integer.parseInt(ccode))) {
                    System.out.println("-------------------------------------------------------------------This course code is already found enter another or 0 to cancel-------------------------------------------------------------------");;
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("-------------------------------------------------------------------Please enter the course code-------------------------------------------------------------------");
            }
        }

        if (insertCourse(cname, ccode)) {
            System.out.println("-------------------------------------------------------------------Successfully created-------------------------------------------------------------------");;
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

    private boolean checkCourseCode(int ccode) {
        String query = "select ccode from courses where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, ccode);
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
        String query = "insert (cname,ccode,did) into courses values (?,?,?);";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, cname);
            ps.setString(2, ccode);
            ps.setInt(3, did);
            return ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private void viewCourse(int ccode) {
        List<String> courseStudents = new ArrayList<>();
        List<String> courseTeachers = new ArrayList<>();
        String dname = null, cname = null;

        String query = "select  S.sname from student_course C JOIN student S ON S.sid = C.sid where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseStudents.add("sname");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select  D.dname from course C JOIN doctor D ON C.did = D.did where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dname = rs.getString("dname");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select  T.tname from teacher T JOIN course_teacher C ON C.tid = T.tid where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseTeachers.add("tname");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select  cname from course where ccode = ?;";
        try {
            query = "select  cname from course where ccode = ?;";

            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseTeachers.add("tname");
            }

            System.out.println("-------------------------------------------------------------------Course name : " + cname + " -------------------------------------------------------------------");;
            System.out.println("-------------------------------------------------------------------Course ccode : " + ccode + " -------------------------------------------------------------------");;
            System.out.println("-------------------------------------------------------------------Course doctor : " + dname + " -------------------------------------------------------------------");;
            System.out.println("-------------------------------------------------------------------Course teachers : ");
            for (String ct : courseTeachers) {
                System.out.print(ct + "--");
            }
            System.out.println("");
            System.out.println("-------------------------------------------------------------------Course students : ");
            for (String cs : courseStudents) {
                System.out.print(cs + "--");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

//    private void courseMenu(int ccode) {
////○ "List Assignments", "Create Assignment", "View Assignment", "Back"
//
//        System.out.println("1○ View mark report\n"
//                + "2○ List assignments\n"
//                + "3○ Create assignment\n"
//                + "4○ View assignment\n"
//                + "5○ Add stduent\n"
//                + "6○ Remove stduent\n"
//                + "7○ Add teacher\n"
//                + "8○ Remove teacher\n"
//                + "9○ Back\n");
//
//        System.out.println("-------------------------------------------------------------------Please enter a input-------------------------------------------------------------------");;
//        Course c = new Course(ccode);
//        try {
//            int choice = in.readLine();
//            if (choice == 1) {
//                c.markReport();
//            } else if (choice == 2) {
//                c.listAssignments();
//            } else if (choice == 3) {
//                c.createAssignment();
//            } else if (choice == 4) {
//                c.viewAssignment();
//            } else if (choice == 5) {
//                c.addStudent();
//            } else if (choice == 6) {
//                c.removeStduent();
//            } else if (choice == 7) {
//                c.addTeacher();
//            } else if (choice == 8) {
//                c.removeTeacher();
//            } else if (choice == 9) {
//
//            } else {
//                System.out.println("-------------------------------------------------------------------Please enter a correct choice-------------------------------------------------------------------");;
//            }
//        } catch (InputMismatchException e) {
//            System.out.println("-------------------------------------------------------------------Please enter a correct input-------------------------------------------------------------------");;
//        }
//
//    }

    public static void signUp() throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("-------------------------------------------------------------------Please enter the username -------------------------------------------------------------------");;
        String username, password;
        while (true) {
            username = in.readLine();
            if (username.equals("0")) {
                return;
            } else if (User.checkUsername(username)) {
                System.out.println("-------------------------------------------------------------------This username is already found enter another or enter 0 to cancel-------------------------------------------------------------------");;
            } else {
                break;
            }
        }

        System.out.println("-------------------------------------------------------------------Please enter the password -------------------------------------------------------------------");;
        password = in.readLine();

        System.out.println("-------------------------------------------------------------------Please enter your name-------------------------------------------------------------------");;
        String name = in.readLine();

        String encrPassword = MyEncryption.encryptPassword(password);
        User.insertTeacher(username, encrPassword, name);

        System.out.println("-------------------------------------------------------------------SUCCESSFULLY SIGNED UP-------------------------------------------------------------------");;

    }

}
