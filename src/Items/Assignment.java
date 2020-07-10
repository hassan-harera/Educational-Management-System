package Items;

import DataBase.MyConnection;
import Persons.Student;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assignment {

    public int code, grade, solutions, totalStudents, studentId, doctorId, courseId, TAid;
    public String name, questions, doctorName, courseName;
    BufferedReader in;

    public Assignment(int code) {
        this.code = code;
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

    public void viewAssignment() {

        String query = "select * from assignment where code = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                courseId = rs.getInt("ccode");
                grade = rs.getInt("grade");
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
                courseId = rs.getInt("C.code");
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
                solutions = rs.getInt("COUNT(sid)");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select COUNT(sid) from student_course where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalStudents = rs.getInt("COUNT(sid)");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("-------------------------------------------------------------------Assignment name : " + name + " ---------------");
        System.out.println("-------------------------------------------------------------------Assignment code : " + code + " ---------------");
        System.out.println("-------------------------------------------------------------------Assignment grade : " + grade + " ---------------");
        System.out.println("-------------------------------------------------------------------Course : " + courseName + " ---------------");
        System.out.println("-------------------------------------------------------------------Doctor : " + doctorName + " ---------------");
        System.out.println("-------------------------------------------------------------------Assignment questions: " + questions + " ---------------");
        System.out.println("-------------------------------------------------------------------Number of submissions : " + solutions + " ---------------");
        System.out.println("-------------------------------------------------------------------Number of scheduled students : " + totalStudents + " ---------------");
    }

    public void report() {
        System.out.println("-------------------------------------------------------------------Number of students that solved the assignment : " + solutions + " ---------------");
        System.out.println("-------------------------------------------------------------------Number of students that didn't solve the assignment : " + (totalStudents - solutions) + " ---------------");

        List<Student> studentsSolved = new ArrayList();
        Map<Integer, Integer> mapId = new HashMap();
        String query = "select S.name,A.sid,A.grade from student S JOIN assignment_student A ON A.sid = S.id where A.acode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("S.name");
                int grade = rs.getInt("A.grade");
                int sid = rs.getInt("A.sid");
                studentsSolved.add(new Student(name, sid, grade));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        List<Student> studentsNotSolved = new ArrayList();
        query = "select S.name, A.sid from student S JOIN student_course A ON A.sid = S.id where A.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("S.name");
                int sid = rs.getInt("A.sid");
                if (!mapId.containsKey(sid)) {
                    Student s = new Student(name, sid);
                    studentsNotSolved.add(s);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        for (Student student : studentsSolved) {
            System.out.println("-------------------------------------------------------------------student id : " + student.id + " , " + " student name : " + student.name + " , " + " student status : solve , " + " student grade : " + (student.assignmentGrade == -1 ? "N/A" : student.assignmentGrade) + " ---------------");
        }
        for (Student student : studentsNotSolved) {
            System.out.println("-------------------------------------------------------------------student id : " + student.id + " , " + " student name : " + student.name + " , " + " student status : not solve ---------------");
        }

        try {
            AssignmentMenue(code);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void AssignmentMenue(int code) throws IOException {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------Assignment MENUE ---------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("1○ View the assignment questions\n"
                + "2○ submit your answer\n"
                + "3○ View my answer\n"
                + "4○ View My Assignment's grade\n"
                + "5○ Back");

        System.out.println("-------------------------------------------------------------------Please enter a choice------------------------------");
        Course c = new Course(code);

        String choice = in.readLine();
        if (choice.equals("1")) {
            viewQuestions();
        } else if (choice.equals("2")) {
            submitAnswer();
        } else if (choice.equals("3")) {
            viewAnswer();
        } else if (choice.equals("4")) {
            viewGrade;
        } else if (choice.equals("5")) {
            return;
        } else {
            System.out.println("-------------------------------------------------------------------Please enter a correct choice---------------");
        }
        AssignmentMenue(code);
    }

    public void viewSubmissions() {
        List<Student> studentsSolution = new ArrayList();
        String query = "select S.name, A.sid, A.answer from assignment_student A JOIN student S ON A.sid = S.id where A.acode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("S.name");
                String answer = rs.getString("S.name");
                int sid = rs.getInt("A.sid");
                studentsSolution.add(new Student(name, sid, answer));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        for (Student student : studentsSolution) {
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------Student name : " + student.name + " , " + "Student id : " + student.id + "-------------------------------------------------------------");
            System.out.println(student.assignmentAnswer);
        }

        if (studentsSolution.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no submissions to view-------------------------------------------------------------");
        }

    }

    public void editQuestions() {
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

            query = "insert into assignment_student () values (?,?,?);";
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
        }
    }
}