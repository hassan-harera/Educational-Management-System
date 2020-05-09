package Items;

import DataBase.MyConnection;
import Persons.Student;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author harera
 */
public class Course {

    private String name, dname;
    private int code;
    private Connection con;
    private BufferedReader in;

    public Course(int code) {
        in = new BufferedReader(new InputStreamReader(System.in));
        con = MyConnection.con();
        this.code = code;
    }

    public void viewCourse() {
        List<String> courseStudents = new ArrayList<>();
        List<String> courseTeachers = new ArrayList<>();

        String query = "select  S.name from student_course C JOIN student S ON S.id = C.sid where C.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseStudents.add("name");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select  D.name from course C JOIN doctor D ON C.did = D.id where code = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dname = rs.getString("name");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select T.name from teacher T JOIN teacher_course C ON C.tid = T.id where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseTeachers.add("tname");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select name from course where code = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("---------------- Course name : " + name + " ---------------");
        System.out.println("---------------- Course code : " + code + " ---------------");
        System.out.println("---------------- Course doctor : " + dname + " ---------------");
        if (!courseTeachers.isEmpty()) {
            System.out.println("---------------- Course teachers : ");
            for (String ct : courseTeachers) {
                System.out.print(ct + "--");
            }
        }
        if (!courseStudents.isEmpty()) {
            System.out.println("");
            System.out.println("---------------- Course students : ");
            for (String cs : courseStudents) {
                System.out.print(cs + "--");
            }
        }
        System.out.println("");
    }

    public void gradeReport() {
        List<Student> students = new ArrayList<>();

        String query = "SELECT  S.name, S.id, C.midmark, C.finalmark, C.totalmark , C.bonus, C.yearmark FROM student_course C JOIN student S ON S.id = C.sid where C.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                students.add(new Student(rs.getString("name"), rs.getInt("id"), rs.getInt("mid_grade"), rs.getInt("final_grade"), rs.getInt("year_grade"), rs.getInt("bonus_grade"), rs.getInt("total_grade")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        if (!students.isEmpty()) {
            for (int i = 0; !students.isEmpty(); i++) {
                System.out.println("---------------- Student name: " + students.get(i).name + " , "
                        + "Student id: " + students.get(i).id + " , "
                        + "Mid exame grade: " + students.get(i).midGrade + " , "
                        + "Year doing grade: " + students.get(i).yearDoingGrade + " , "
                        + "bonus: " + students.get(i).bonusGrade + " , "
                        + "Final exam grid: " + students.get(i).finalGrade + " , "
                        + "Total grid: " + students.get(i).totalGrade);
            }
            gradeActions();
        } else {
            System.out.println("---------------------------------NO students was registerd in this course------------------------------");
        }

    }

    private void doctorAssignments() {
        List<String> assignmentName = new ArrayList<>();
        List<String> assignmentCode = new ArrayList<>();

        String query = "select  A.acode, C.acode from assignments A JOIN course_assignment C ON A.acode = C.acode where ccode = ?;";

        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, code + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                assignmentCode.add("acode");
                assignmentName.add("aname");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        for (int i = 0; !assignmentName.isEmpty() && !assignmentCode.isEmpty(); i++) {
            System.out.println("---------------- Assignment name : " + assignmentName.get(i) + " , " + "Assignment code : " + assignmentCode.get(i) + " ---------------");
        }
    }

    public void createAssignment() throws IOException {
        System.out.println("----------------Please enter assignment code---------------");
        String code, name, grade, questions;
        int cod, grad;

        while (true) {
            try {
                code = in.readLine();
                cod = Integer.parseInt(code);
                if (code.equals("0")) {
                    return;
                } else if (!checkAssignmentCode(cod)) {
                    System.out.println("----------------This Assignment code is already exists enter another code or 0 to go back---------------");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("----------------Please enter The assignment code in number---------------");
            }
        }

        System.out.println("----------------Please enter assignment name---------------");
        name = in.readLine();

        System.out.println("----------------Please enter the assignment questions manually---------------");
        questions = in.readLine();

        System.out.println("----------------Please enter assignment grade---------------");

        while (true) {
            try {
                grade = in.readLine();
                grad = Integer.parseInt(grade);
                break;
            } catch (NumberFormatException e) {
                System.out.println("----------------Please enter The assignment grade in number---------------");
            }
        }

        String query = "insert  into assignment (ccode,code,grade,name,question) values (?,?,?,?,?);";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, this.code);
            ps.setInt(2, cod);
            ps.setInt(3, grad);
            ps.setString(4, name);
            ps.setString(5, questions);
            ps.execute();
            System.out.println("---------------------------------SUCCESSFULLY CREATED---------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void gradeActions() {
        System.out.println("----------------To put bonus for all students enter 1---------------");
        System.out.println("----------------To put bonus for some student enter 2---------------");
        System.out.println("----------------To go back enter 0---------------");

        try {
            int choice = in.readLine();
            if (choice != 0) {
                if (choice == 1) {
                    putBonusForAll();
                } else if (choice == 2) {
                    putBonusForStudent();
                } else {
                    System.out.println("----------------Please enter correct input---------------");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter correct input---------------");
        }
    }

    private void putBonusForAll() {
        System.out.println("---------------- Please put bonus value ---------------");
        try {
            int bonus = in.readLine();
            String query = "update student_course set bonus (bonus+?) where ccod = ?;";
            try {
                PreparedStatement ps;
                ps = MyConnection.con().prepareStatement(query);
                ps.setInt(1, bonus);
                ps.setInt(2, code);
                ps.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter correct input---------------");
        }
    }

    private void putBonusForStudent() {

        try {
            System.out.println("---------------- Please enter student id ---------------");
            int sid = in.readLine();
            System.out.println("---------------- Please put bonus value ---------------");
            int bonus = in.readLine();
            String query = "update student_course set bonus (bonus+?) where ccod = ? and sid = ?;";
            try {
                PreparedStatement ps;
                ps = MyConnection.con().prepareStatement(query);
                ps.setInt(1, bonus);
                ps.setInt(2, code);
                ps.setInt(3, sid);
                ps.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter correct input---------------");
        }
    }

    public List<Assignment> listAssignments() {
        List<Assignment> assignments = new ArrayList();

        String query = "select * from assignment where ccode = ?;";

        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                assignments.add(new Assignment(rs.getInt("code"), rs.getInt("grade"), rs.getString("name")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        if (!assignments.isEmpty()) {
            for (Assignment a : assignments) {
                System.out.println("---------------- Assignment name : " + a.name + " , "
                        + "Assignment code : " + a.code + " , "
                        + "Assignment grade : " + a.grade);
            }
        } else {
            System.out.println("---------------- There is no assignments to view ---------------");
        }
        return assignments;
    }

    public void viewAssignment() throws IOException {
        List<Assignment> assignments = listAssignments();
        if (assignments.isEmpty()) {
            System.out.println("---------------- There is no assignments was created to view ---------------");
        } else {
            System.out.println("---------------- Enter the assignment code to view or 0 to cancel ---------------");
            while (true) {
                try {
                    String code = in.readLine();
                    int cod = Integer.parseInt(code);
                    if (code.equals("0")) {
                        return;
                    } else if (!checkAssignmentCode(cod)) {
                        new Assignment(cod).viewAssignment();
                        break;
                    } else {
                        System.out.println("---------------- This course code is not existed enter another or 0 to cancel ---------------");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("----------------Please enter correct input---------------");
                }
            }
        }
    }

    public void addStudent() {
        List<Integer> students = listAllStudents();

        if (students.isEmpty()) {
            System.out.println("---------------- There is no students in the site to add ---------------");
        } else {
            while (true) {
                System.out.println("---------------- Enter the student id to add or 0 to cancel ---------------");
                try {
                    int id = in.readLine();
                    if (id != 0) {
                        if (students.contains(id)) {
                            insertStudent(id);
                        } else {
                            System.out.println("---------------- This id is incorrect ---------------");
                            addStudent();
                        }
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("----------------Please enter correct input---------------");
                    viewCourse();
                }
            }
        }

    }

    public void removeStduent() {
        List<Integer> students = listStudents();

        if (students.isEmpty()) {
            System.out.println("---------------- There is no students registerd in the course ---------------");
        } else {
            while (true) {
                System.out.println("---------------- Enter the student id to remove or 0 to cancel ---------------");
                try {
                    int id = in.readLine();
                    if (id != 0) {
                        if (students.contains(id)) {
                            removeStudent(id);
                        } else {
                            System.out.println("---------------- This id is incorrect ---------------");
                            addStudent();
                        }
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("----------------Please enter correct input---------------");
                    viewCourse();
                }
            }
        }
    }

    public void addTeacher() {
        List<Integer> teacher = listAllTeachers();

        if (teacher.isEmpty()) {
            System.out.println("---------------- There is no teachers in the site to add ---------------");
        } else {
            while (true) {
                System.out.println("---------------- Enter the techer id to add or 0 to cancel ---------------");
                try {
                    int id = in.readLine();
                    if (id != 0) {
                        if (teacher.contains(id)) {
                            insertTeacher(id);
                        } else {
                            System.out.println("---------------- This id is incorrect ---------------");
                            addTeacher();
                        }
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("----------------Please enter correct input---------------");
                }
            }
        }
    }

    public void removeTeacher() {
        List<Integer> teachers = listTeachers();

        if (teachers.isEmpty()) {
            System.out.println("---------------- There is no teachers are teaching in the course ---------------");
        } else {
            System.out.println("---------------- Enter the teacher id to remove or 0 to cancel ---------------");
            try {
                int id = in.readLine();
                if (id != 0) {
                    if (teachers.contains(id)) {
                        removeTeacher(id);
                    } else {
                        System.out.println("---------------- This id is incorrect ---------------");
                        removeTeacher();
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("----------------Please enter correct input---------------");
                removeTeacher();
            }
        }
    }

    private boolean checkAssignmentCode(int acode) {
        PreparedStatement ps = null;
        String query = "select code from assignment where code = ?;";
        try {
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, acode);
            if (ps.executeQuery().next()) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return true;
    }

    private List<Integer> listAllStudents() {
        List<Integer> students = new ArrayList();

        String query = "select (name,id) from student;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int s = rs.getInt("id");
                System.out.println("---------------- Student id: " + s + "id"
                        + " , Student name: " + rs.getString("name") + " ---------------");
                students.add(s);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return students;
    }

    private void insertStudent(int id) {
        String query = "insert into student_course (sid,ccode) values(?,?);";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2, code);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<Integer> listStudents() {
        List<Integer> students = new ArrayList();

        String query = "select S.id, S.name from student_course C join student S on C.sid = S.id where C.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ps.setInt(1, code);
            while (rs.next()) {
                int s = rs.getInt("id");
                System.out.println("---------------- Student id: " + s + "id"
                        + " , Student name: " + rs.getString("name") + " ---------------");
                students.add(s);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return students;
    }

    private void removeStudent(int id) {
        String query = "delete from student_course where ccode = ? and sid = ?";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, id);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<Integer> listAllTeachers() {
        List<Integer> teachers = new ArrayList();

        String query = "select (name,id) from teacher;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int t = rs.getInt("id");
                System.out.println("---------------- teacher id: " + t + " , "
                        + "teacher name: " + rs.getString("name") + " ---------------");
                teachers.add(t);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return teachers;
    }

    private void insertTeacher(int id) {
        String query = "insert into teacher_course (tid,ccode) values(?,?);";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2, code);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<Integer> listTeachers() {
        List<Integer> teachers = new ArrayList();

        String query = "select T.id, T.name from teacher_course C join teacher T on C.tid = T.id where C.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ps.setInt(1, code);
            while (rs.next()) {
                int s = rs.getInt("id");
                System.out.println("---------------- Teacher id: " + s + "id"
                        + " , Teacher name: " + rs.getString("name") + " ---------------");
                teachers.add(s);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return teachers;
    }

    private void removeTeacher(int id) {
        String query = "delete from teacher_course where ccode = ? and tid = ?";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, id);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
