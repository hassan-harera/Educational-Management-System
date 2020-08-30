package Items;

import static DataBase.MyConnection.con;
import Persons.Student;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import java.sql.*;
import java.util.Map;
import java.util.HashMap;

public class Assignment {

    private int code, mark, solutions, totalStudents, courseCode, studentId, doctorId, TAid;
    private String name, questions, doctorName, courseName;
    private BufferedReader in;
    private Connection con;

    public Assignment(int code) {
        this.code = code;
        in = new BufferedReader(new InputStreamReader(System.in));
        con = con();
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

        var query = "select * from assignment where code = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            if (rs.next()) {
                courseCode = rs.getInt("ccode");
                mark = rs.getInt("grade");
                name = rs.getString("name");
                questions = rs.getString("question");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        query = "select  D.name,C.name,C.code from course C JOIN doctor D ON C.did = D.id JOIN assignment A ON A.ccode = C.code where A.code = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            if (rs.next()) {
                doctorName = rs.getString("D.name");
                courseName = rs.getString("C.name");
                courseCode = rs.getInt("C.code");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        query = "select COUNT(sid) from assignment_student where acode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            if (rs.next()) {
                this.solutions = rs.getInt("COUNT(sid)");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        query = "select COUNT(sid) from student_course where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, courseCode);
            var rs = ps.executeQuery();
            if (rs.next()) {
                this.totalStudents = rs.getInt("COUNT(sid)");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        out.println("-------------------------------------------------------------------Assignment name : " + name + " ---------------");
        out.println("-------------------------------------------------------------------Assignment code : " + code + " ---------------");
        out.println("-------------------------------------------------------------------Assignment mark : " + mark + " ---------------");
        out.println("-------------------------------------------------------------------Course : " + courseName + " ---------------");
        out.println("-------------------------------------------------------------------Doctor : " + doctorName + " ---------------");
        out.println("-------------------------------------------------------------------Assignment questions: " + questions + " ---------------");
        out.println("-------------------------------------------------------------------Number of submissions : " + solutions + " ---------------");
        out.println("-------------------------------------------------------------------Number of scheduled students : " + totalStudents + " ---------------");
    }

    public void doctorReport() {
        out.println("-------------------------------------------------------------------ASSIGNMENT INFO-------------------------------------------------------------------");
        out.println("-------------------------------------------------------------------Number of students that solved the assignment : "
                + this.solutions + " ---------------");
        out.println("-------------------------------------------------------------------Number of students that didn't solve the assignment :"
                + (totalStudents - this.solutions) + " ---------------");

        Map<Student, Boolean> studentsSolved = new HashMap();
        var query = "select S.name,A.sid,A.grade from student S JOIN assignment_student A ON A.sid = S.id where A.acode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            while (rs.next()) {
                var studentName = rs.getString("S.name");
                var studentGrade = rs.getInt("A.grade");
                var sid = rs.getInt("A.sid");
                var s = new Student(sid);
                s.setName(name);
                s.setAssignmentGrade(studentGrade);
                studentsSolved.put(s, true);
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        Map<Student, Boolean> studentsNotSolved = new HashMap();
        query = "select S.name, A.sid from student S JOIN student_course A ON A.sid = S.id JOIN assignment_student T On T.sid != A.sid where A.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, courseCode);
            var rs = ps.executeQuery();
            while (rs.next()) {
                var studentName = rs.getString("S.name");
                var sid = rs.getInt("A.sid");
                var s = new Student(sid);
                s.setName(name);
                studentsNotSolved.put(s, true);
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        out.println("-------------------------------------------------------------------------------------STUDENT LIST-------------------------------------------------------------------");

        studentsSolved.keySet().forEach((student) -> {
            out.println("-------------------------------------------------------------------student id : " + student.getId() + " , "
                    + " student name : " + student.getName() + " , "
                    + " student status : solve , " + " student mark : "
                    + (student.getAssignmentGrade() == -1 ? "unknown" : student.getAssignmentGrade()) + " ---------------");
        });

        studentsNotSolved.keySet().forEach((student) -> {
            out.println("-------------------------------------------------------------------student id : " + student.getId() + " , "
                    + " student name : " + student.getName() + " , " + " student status : not solve ---------------");
        });

    }

    public void studentAssignmentMenu(int code) throws IOException {
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("-------------------------------------------------------------------Assignment MENU ---------------------------------------------");
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("1○ View the assignment questions\n"
                + "2○ submit my answer\n"
                + "3○ View my answer\n"
                + "4○ View my Assignment's mark\n"
                + "5○ Back");

        out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        var c = new Course(code);
        var choice = in.readLine();
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
                out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
                break;
        }
        studentAssignmentMenu(code);
    }

    public void viewSubmissions() throws IOException {
        Map<Student, Boolean> studentsSolution = new HashMap();
        var query = "select S.name, A.sid, A.answer from assignment_student A JOIN student S ON A.sid = S.id where A.acode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            while (rs.next()) {
                var studentName = rs.getString("S.name");
                var answer = rs.getString("A.answer");
                var sid = rs.getInt("A.sid");
                var s = new Student(sid);
                s.setName(studentName);
                s.setAssignmentAnswer(answer);
                studentsSolution.put(s, true);
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        if (studentsSolution.isEmpty()) {
            out.println("-------------------------------------------------------------------There is no submissions to view-------------------------------------------------------------");
        } else {
            for (var student : studentsSolution.keySet()) {
                out.println("-------------------------------------------------------------------Student name : " + student.getName()
                        + " , " + "Student id : " + student.getId() + "-------------------------------------------------------------");
                out.println("-------------------------------------------------------------------STUDENT SOLUTION-------------------------------------------------------------------");
                out.println("------------------------------------------------------------------" + student.getAssignmentAnswer());
            }
            submissionActions();
        }

    }

    public void editQuestions() throws IOException {
        var oldQuestions = "";
        var query = "select question from assignment where code = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            if (rs.next()) {
                oldQuestions = rs.getString("question");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        out.println("-------------------------------------------------------------------Old questions-------------------------------------------------------------");
        out.println(oldQuestions);

        enterNewQuestion();
    }

    private void viewQuestions() {
        if (questions != null) {
            out.println(questions);
        } else {
            var query = "select question from assignment where code = ?;";
            try {
                PreparedStatement ps;
                ps = con().prepareStatement(query);
                ps.setInt(1, code);
                var rs = ps.executeQuery();
                if (rs.next()) {
                    questions = rs.getString("question");
                    out.println(questions);
                }
            } catch (SQLException ex) {
                out.println(ex.getMessage());
            }
        }
    }

    private void submitAnswer() throws IOException {
        Boolean isSubmited = false;
        var query = "select answer from assignment_student where acode = ? and sid = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, studentId);
            var rs = ps.executeQuery();
            if (rs.next()) {
                isSubmited = true;
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        if (isSubmited) {
            out.println("-------------------------------------------------------------------You already have submitted your answer-------------------------------------------------------------");
        } else {
            out.println("-------------------------------------------------------------------Enter your answer-------------------------------------------------------------");
            var answer = in.readLine();

            query = "insert into assignment_student values (?,?,?,?,?);";
            try {
                PreparedStatement ps;
                ps = con().prepareStatement(query);
                ps.setInt(1, studentId);
                ps.setInt(2, courseCode);
                ps.setString(3, answer);
                ps.setInt(4, code);
                ps.setInt(5, -1);
                if (ps.execute()) {
                    out.println("-------------------------------------------------------------------SUCCESSFULLY SUBMITTED-------------------------------------------------------------");
                }
            } catch (SQLException ex) {
                out.println(ex.getMessage());
            }
        }
    }

    private void viewAnswer() {
        var query = "select answer from assignment_student where acode = ? and sid = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, studentId);
            var rs = ps.executeQuery();
            if (rs.next()) {
                var answer = rs.getString("answer");
                out.println("-------------------------------------------------------------------Your Answer-------------------------------------------------------------");
                out.println(answer);
            } else {
                out.println("-------------------------------------------------------------------Your hvae not submitted any answer yet-------------------------------------------------------------");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    private void viewGrade() {
        var query = "select grade from assignment_student where acode = ? and sid = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, studentId);
            var rs = ps.executeQuery();
            if (rs.next()) {
                var mark = rs.getInt("grade");
                out.println("-------------------------------------------------------------------" + "Your mark is " + (mark == -1 ? "unknown" : mark) + " -------------------------------------------------------------");
            } else {
                out.println("-------------------------------------------------------------------Your hvae not submitted any answer yet-------------------------------------------------------------");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    private void submissionActions() throws IOException {
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("-------------------------------------------------------------------SUBMISSION ACTIONS---------------------------------------------");
        out.println("------------------------------------------------------------------------------------------------------------------------------");
        out.println("1○ Put grade for solutions\n"
                + "2○ View the submissions again\n"
                + "3○ Back");

        out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        var choice = in.readLine();
        switch (choice) {
            case "1":
                enterStudentId();
                break;
            case "2":
                viewSubmissions();
                break;
            case "3":
                return;
            default:
                out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
                break;
        }
        submissionActions();
    }

    private void enterStudentId() throws IOException {
        out.println("Enter the student id from id list");
        String sid;
        while (true) {
            sid = in.readLine();
            if (sid.matches("^\\d+$")) {
                break;
            } else {
                out.println("------------------------------------------------------------------INVALID ID------------------------------------------------------------------");
            }
        }
        if (checkStudentId(parseInt(sid))) {
            enterGrade(parseInt(sid));
        } else {
            out.println("------------------------------------------------------------------INVALID ID------------------------------------------------------------------");
        }
    }

    private boolean checkStudentId(int id) {
        var query = "select id from student where id = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        return false;
    }

    private void enterGrade(int sid) throws IOException {
        out.println("Enter the grade value");
        String grade;
        while (true) {
            grade = in.readLine();
            if (grade.matches("^\\d+$")) {
                break;
            } else {
                out.println("-------------------------------------------------------------------INVALID VALUE---------------");
            }
        }

        var query = "update assignment_student set grade = ? where sid = ? and acode = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, parseInt(grade));
            ps.setInt(2, sid);
            ps.setInt(3, code);
            ps.execute();
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    private void enterNewQuestion() throws IOException {
        out.println("Enter the new question");
        var newQuestions = in.readLine();
        var query = "update assignment set question = ? where code = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setString(1, newQuestions);
            ps.setInt(2, code);
            ps.execute();
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        out.println("-------------------------------------------------------------------New questions-------------------------------------------------------------");
        out.println(newQuestions);
    }
}
