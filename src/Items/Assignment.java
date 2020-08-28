package Items;

import DataBase.MyConnection;
import Persons.Student;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Assignment {

    private int code, mark, solutions, totalStudents, courseCode, studentId, doctorId, TAid;
    private String name, questions, doctorName, courseName;
    BufferedReader in;
    private Connection con;

    public Assignment(int code) {
        this.code = code;
        in = new BufferedReader(new InputStreamReader(System.in));
        con = MyConnection.con();
    }

    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setTAid(int TAid) {
        this.TAid = TAid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(int mark) {
        this.mark = mark;
    }

    public int getCode() {
        return code;
    }

    public int getGrade() {
        return mark;
    }

    public String getName() {
        return name;
    }

    public void viewAssignment() {

        String query = "select * from assignment where code = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                courseCode = rs.getInt("ccode");
                mark = rs.getInt("grade");
                name = rs.getString("name");
                questions = rs.getString("question");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select  D.name,C.name,C.code from course C JOIN doctor D ON C.did = D.id JOIN assignment A ON A.ccode = C.code where A.code = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                doctorName = rs.getString("D.name");
                courseName = rs.getString("C.name");
                courseCode = rs.getInt("C.code");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select COUNT(sid) from assignment_student where acode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.solutions = rs.getInt("COUNT(sid)");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select COUNT(sid) from student_course where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, courseCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.totalStudents = rs.getInt("COUNT(sid)");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("-------------------------------------------------------------------Assignment name : " + name + " ---------------");
        System.out.println("-------------------------------------------------------------------Assignment code : " + code + " ---------------");
        System.out.println("-------------------------------------------------------------------Assignment mark : " + mark + " ---------------");
        System.out.println("-------------------------------------------------------------------Course : " + courseName + " ---------------");
        System.out.println("-------------------------------------------------------------------Doctor : " + doctorName + " ---------------");
        System.out.println("-------------------------------------------------------------------Assignment questions: " + questions + " ---------------");
        System.out.println("-------------------------------------------------------------------Number of submissions : " + solutions + " ---------------");
        System.out.println("-------------------------------------------------------------------Number of scheduled students : " + totalStudents + " ---------------");
    }

    public void doctorReport() {
        System.out.println("-------------------------------------------------------------------ASSIGNMENT INFO-------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------Number of students that solved the assignment : "
                + this.solutions + " ---------------");
        System.out.println("-------------------------------------------------------------------Number of students that didn't solve the assignment :"
                + (totalStudents - this.solutions) + " ---------------");

        List<Student> studentsSolved = new ArrayList();
        String query = "select S.name,A.sid,A.grade from student S JOIN assignment_student A ON A.sid = S.id where A.acode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String studentName = rs.getString("S.name");
                int studentGrade = rs.getInt("A.grade");
                int sid = rs.getInt("A.sid");
                Student s = new Student(sid);
                s.setName(name);
                s.setAssignmentGrade(studentGrade);
                studentsSolved.add(s);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        List<Student> studentsNotSolved = new ArrayList();
        query = "select S.name, A.sid from student S JOIN student_course A ON A.sid = S.id JOIN assignment_student T On T.sid != A.sid where A.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, courseCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String studentName = rs.getString("S.name");
                int sid = rs.getInt("A.sid");
                Student s = new Student(sid);
                s.setName(name);
                studentsNotSolved.add(s);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("-------------------------------------------------------------------------------------STUDENT LIST-------------------------------------------------------------------");

        studentsSolved.forEach((student) -> {
            System.out.println("-------------------------------------------------------------------student id : " + student.getId() + " , "
                    + " student name : " + student.getName() + " , "
                    + " student status : solve , " + " student mark : "
                    + (student.getAssignmentGrade() == -1 ? "unknown" : student.getAssignmentGrade()) + " ---------------");
        });

        studentsNotSolved.forEach((student) -> {
            System.out.println("-------------------------------------------------------------------student id : " + student.getId() + " , "
                    + " student name : " + student.getName() + " , " + " student status : not solve ---------------");
        });

    }

    public void studentAssignmentMenu(int code) throws IOException {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------Assignment MENU ---------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("1○ View the assignment questions\n"
                + "2○ submit my answer\n"
                + "3○ View my answer\n"
                + "4○ View my Assignment's mark\n"
                + "5○ Back");

        System.out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        Course c = new Course(code);

        String choice = in.readLine();
        switch (choice) {
            case "1":
                viewQuestions();
                break;
            case "2":
                submitAnswer();
                break;
            case "3":
                viewAnswer();
                break;
            case "4":
                viewGrade();
                break;
            case "5":
                return;
            default:
                System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
                break;
        }
        studentAssignmentMenu(code);
    }

    public void viewSubmissions() throws IOException {
        List<Student> studentsSolution = new ArrayList();
        String query = "select S.name, A.sid, A.answer from assignment_student A JOIN student S ON A.sid = S.id where A.acode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String studentName = rs.getString("S.name");
                String answer = rs.getString("A.answer");
                int sid = rs.getInt("A.sid");
                Student s = new Student(sid);
                s.setName(studentName);
                s.setAssignmentAnswer(answer);
                studentsSolution.add(s);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        if (studentsSolution.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no submissions to view-------------------------------------------------------------");
        } else {
            for (Student student : studentsSolution) {
                System.out.println("-------------------------------------------------------------------Student name : " + student.getName()
                        + " , " + "Student id : " + student.getId() + "-------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------------STUDENT SOLUTION-------------------------------------------------------------------");
                System.out.println("------------------------------------------------------------------" + student.getAssignmentAnswer());
            }
            submissionActions();
        }

    }

    public void editQuestions() throws IOException {
        String oldQuestions = "";
        String query = "select question from assignment where code = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                oldQuestions = rs.getString("question");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("-------------------------------------------------------------------Old questions-------------------------------------------------------------");
        System.out.println(oldQuestions);

        enterNewQuestion();
    }

    private void viewQuestions() {
        if (questions != null) {
            System.out.println(questions);
        } else {
            String query = "select question from assignment where code = ?;";
            try {
                PreparedStatement ps;
                ps = MyConnection.con().prepareStatement(query);
                ps.setInt(1, code);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    questions = rs.getString("question");
                    System.out.println(questions);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void submitAnswer() throws IOException {
        Boolean isSubmited = false;
        String query = "select answer from assignment_student where acode = ? and sid = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isSubmited = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        if (isSubmited) {
            System.out.println("-------------------------------------------------------------------You already have submitted your answer-------------------------------------------------------------");
        } else {
            System.out.println("-------------------------------------------------------------------Enter your answer-------------------------------------------------------------");
            String answer = in.readLine();

            query = "insert into assignment_student values (?,?,?,?,?);";
            try {
                PreparedStatement ps;
                ps = MyConnection.con().prepareStatement(query);
                ps.setInt(1, studentId);
                ps.setInt(2, courseCode);
                ps.setString(3, answer);
                ps.setInt(4, code);
                ps.setInt(5, -1);
                if (ps.execute()) {
                    System.out.println("-------------------------------------------------------------------SUCCESSFULLY SUBMITTED-------------------------------------------------------------");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void viewAnswer() {
        String query = "select answer from assignment_student where acode = ? and sid = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String answer = rs.getString("answer");
                System.out.println("-------------------------------------------------------------------Your Answer-------------------------------------------------------------");
                System.out.println(answer);
            } else {
                System.out.println("-------------------------------------------------------------------Your hvae not submitted any answer yet-------------------------------------------------------------");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewGrade() {
        String query = "select grade from assignment_student where acode = ? and sid = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int mark = rs.getInt("grade");
                System.out.println("-------------------------------------------------------------------" + "Your mark is " + (mark == -1 ? "unknown" : mark) + " -------------------------------------------------------------");
            } else {
                System.out.println("-------------------------------------------------------------------Your hvae not submitted any answer yet-------------------------------------------------------------");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void submissionActions() throws IOException {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------SUBMISSION ACTIONS---------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("1○ Put grade for solutions\n"
                + "2○ View the submissions again\n"
                + "3○ Back");

        System.out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        String choice = in.readLine();

        if (choice.equals("1")) {
            enterStudentId();
        } else if (choice.equals("2")) {
            viewSubmissions();
        } else if (choice.equals("3")) {
            return;
        } else {
            System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
        }
        submissionActions();
    }

    private void enterStudentId() throws IOException {
        System.out.println("Enter the student id from id list");
        String sid;
        while (true) {
            sid = in.readLine();
            if (sid.matches("^\\d+$")) {
                break;
            } else {
                System.out.println("------------------------------------------------------------------INVALID ID------------------------------------------------------------------");
            }
        }
        if (checkStudentId(Integer.parseInt(sid))) {
            enterGrade(Integer.parseInt(sid));
        } else {
            System.out.println("------------------------------------------------------------------INVALID ID------------------------------------------------------------------");
        }
    }

    private boolean checkStudentId(int id) {
        String query = "select id from student where id = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private void enterGrade(int sid) throws IOException {
        System.out.println("Enter the grade value");
        String grade;
        while (true) {
            grade = in.readLine();
            if (grade.matches("^\\d+$")) {
                break;
            } else {
                System.out.println("-------------------------------------------------------------------INVALID VALUE---------------");
            }
        }

        String query = "update assignment_student set grade = ? where sid = ? and acode = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(grade));
            ps.setInt(2, sid);
            ps.setInt(3, code);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void enterNewQuestion() throws IOException {
        System.out.println("Enter the new question");
        String newQuestions = in.readLine();
        String query = "update assignment set question = ? where code = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, newQuestions);
            ps.setInt(2, code);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("-------------------------------------------------------------------New questions-------------------------------------------------------------");
        System.out.println(newQuestions);
    }
}
