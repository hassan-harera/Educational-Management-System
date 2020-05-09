package Items;

import DataBase.MyConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Assignment {

    public int code, grade, solved,ccode,totalStudents;
    public String name, question,dname,cname;

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
            if(rs.next()) {
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
}
