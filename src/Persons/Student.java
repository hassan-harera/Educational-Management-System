package Persons;

import static DataBase.MyConnection.con;
import Items.Course;
import static Persons.User.checkUsername;
import static Persons.User.insertStudent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;

public class Student {

    private String assignmentAnswer;
    private String username, password;
    private String name;
    private int id, midGrade, finalGrade, yearDoingGrade, bonusGrade, totalGrade, assignmentGrade;
    private BufferedReader in;
    private Connection con;

    public Student(int id) {
        this.id = id;
        con = con();
        getIdByUsername();
    }

    public Student(String username) {
        this.username = username;
        con = con();
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

        while (true) {
            out.println("------------------------------------------------------------------------------------------------------------------------------");
            out.println("-------------------------------------------------------------------STUDENT MENU ---------------------------------------------");
            out.println("------------------------------------------------------------------------------------------------------------------------------");

            out.println("1○ Register in course\n"
                    + "2○ View My Courses\n"
                    + "3○ View a course\n"
                    + "4○ Log out");

            out.println("-------------------------------------------------------------------Please enter a choice ---------------------------------");
            var choice = in.readLine();
            int ch;

            if (choice.matches("^\\d+$")) {
                ch = parseInt(choice);
                if (ch >= 1 && ch <= 4) {
                    if (ch == 1) {
                        registerInCourse();
                    } else if (ch == 2) {
                        listMyCourses();
                    } else if (ch == 3) {
                        viewCourse();
                    } else if (ch == 4) {
                        return;
                    }
                } else {
                    out.println("-------------------------------------------------------------------INVALID CHOICE---------------");
                }
            } else {
                out.println("-------------------------------------------------------------------INVALID CHOICE---------------");
            }
        }
    }

    private void registerInCourse() throws IOException {
        var courses = listAllCourses();

        if (courses.isEmpty()) {
            out.println("-------------------------------------------------------------------There is no courses---------------");
        } else {
            out.println("-------------------------------------------------------------------Enter the course code that you want to register in---------------");
            while (true) {
                try {
                    var code = in.readLine();
                    if (code.equals("0")) {
                        return;
                    } else if (!courses.containsKey(parseInt(code))) {
                        out.println("-------------------------------------------------------------------This code is not correct enter another or 0 to cancel---------------");
                    } else {
                        new Course(parseInt(code)).insertStudent(id);
                        out.println("-------------------------------------------------------------------SUCCESFULLY REGISTERED---------------");
                        break;
                    }
                } catch (NumberFormatException e) {
                    out.println("-------------------------------------------------------------------The cousre code must be number as shown in the course list---------------");
                }
            }
        }
    }

    private Map<Integer, Boolean> listMyCourses() {
        Map<Integer, Boolean> courses = new HashMap<>();
        var query = "select C.name,S.ccode from student_course S join course C on C.code = S.ccode where S.sid = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, id);
            var rs = ps.executeQuery();
            for (var i = 1; rs.next(); i++) {
                var code = rs.getInt("S.ccode");
                courses.put(code, true);
                out.println("-------------------------------------------------------------------course code: " + code + " Course name: " + rs.getString("C.name") + " ---------------");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        return courses;
    }

    private void viewCourse() throws IOException {
        var courses = listMyCourses();
        String code;

        if (courses.isEmpty()) {
            out.println("-------------------------------------------------------------------You are not registered in any course---------------");
        } else {
            out.println("-------------------------------------------------------------------Please enter the course code---------------");
            while (true) {
                code = in.readLine();
                var intCode = parseInt(code);
                if (code.matches("^\\d+$")) {
                    if (intCode == 0) {
                        return;
                    } else if (courses.containsKey(intCode)) {
                        new Course(intCode).viewCourse();
                        courseMenu(intCode);
                        break;
                    } else {
                        out.println("-------------------------------------------------------------------Course code is not correct---------------");
                    }
                } else {
                    out.println("-------------------------------------------------------------------INVALID VALUE---------------");
                }
            }
        }
    }

    private Map<Integer, Boolean> listAllCourses() {
        Map<Integer, Boolean> courseList = new HashMap();
        var query = "select  C.name, C.code, D.name from course C JOIN doctor D ON C.did = D.id;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            var rs = ps.executeQuery();
            Boolean flag = false;
            while (rs.next()) {
                var code = rs.getInt("code");
                courseList.put(code, true);
                out.println("-------------------------------------------------------------------course code: " + code
                        + " , Course name: " + rs.getString("name")
                        + " , Course doctor: " + rs.getString("D.name") + " ---------------------------------");
                flag = true;
            }
            if (!flag) {
                out.println("-------------------------------------------------------------------There is no courses was created in the site ---------------");
            }

        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        return courseList;
    }

    private void registerInCourse(int choice) {

    }

    private void viewCourse(int choice) {

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

        insertStudent(username, password, name);

        out.println("-------------------------------------------------------------------SUCCESSFULLY SIGNED UP-------------------------------------------------------------------");;

    }

    private void getIdByUsername() {
        var query = "select id from student where username = ?;";
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

    private void courseMenu(int code) throws IOException {
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("-------------------------------------------------------------------COURSE MENU ---------------------------------------------");
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("1○ View my mark report\n"
                + "2○ List assignments\n"
                + "3○ View assignment\n"
                + "4○ Back");

        out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        var c = new Course(code);
        var choice = in.readLine();
        switch (choice) {
            case "1":
                c.studentGradeReport(id);
                break;
            case "2":
                c.listAssignments();
                break;
            case "3":
                var a = c.viewAssignment();
                if (a != null) {
                    a.setCourseCode(code);
                    a.setStudentId(id);
                    a.studentAssignmentMenu(code);
                }   break;
            case "4":
                return;
            default:
                out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
                break;
        }
        courseMenu(code);
    }

}
