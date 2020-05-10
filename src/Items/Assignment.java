package Items;

import DataBase.MyConnection;
import Persons.Student;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assignment {

    public int code, grade, solved, ccode, totalStudents;
    public String name, question, dname, cname;

    public Assignment(int code, int grade, String name) {
        this.code = code;
        this.grade = grade;
        this.name = name;
    }

    public Assignment(int code) {
        this.code = code;
    }

    public void viewAssignment() {

        String query = "select * from assignment where code = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ccode = rs.getInt("ccode");
                grade = rs.getInt("grade");
                name = rs.getString("name");
                question = rs.getString("question");
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
                dname = rs.getString("D.name");
                cname = rs.getString("C.name");
                ccode = rs.getInt("C.code");
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
                solved = rs.getInt("COUNT(sid)");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        query = "select COUNT(sid) from student_course where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, ccode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalStudents = rs.getInt("COUNT(sid)");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("---------------- Assignment name : " + name + " ---------------");
        System.out.println("---------------- Assignment code : " + code + " ---------------");
        System.out.println("---------------- Assignment grade : " + grade + " ---------------");
        System.out.println("---------------- Course : " + cname + " ---------------");
        System.out.println("---------------- Doctor : " + dname + " ---------------");
        System.out.println("---------------- Assignment content: " + question + " ---------------");
        System.out.println("---------------- Number of submissions : " + solved + " ---------------");
        System.out.println("---------------- Number of scheduled students : " + totalStudents + " ---------------");
    }

    public void report() {
        System.out.println("---------------- Number of students that solved the assignment : " + solved + " ---------------");
        System.out.println("---------------- Number of students that didn't solve the assignment : " + (totalStudents - solved) + " ---------------");

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
            ps.setInt(1, ccode);
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
            System.out.println("---------------- student id : " + student.id + " , " + " student name : " + student.name + " , " + " student status : solve , " + " student grade : " + (student.assignmentGrade == -1 ? "N/A" : student.assignmentGrade) + " ---------------");
        }
        for (Student student : studentsNotSolved) {
            System.out.println("---------------- student id : " + student.id + " , " + " student name : " + student.name + " , " + " student status : not solve ---------------");
        }

    }

    public void listSubmissions() {

    }

    public void editQuestions() {

    }
}
