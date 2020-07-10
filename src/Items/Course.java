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
import java.util.List;

public class Course {

    private String name, dname;
    private int code, dId, sId, taId;
    private Connection con;
    private BufferedReader in;

    public Course(int code) {
        in = new BufferedReader(new InputStreamReader(System.in));
        con = MyConnection.con();
        this.code = code;
    }

    public void setTaId(int taId) {
        this.taId = taId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }
    
    
    
    public void viewCourse() {
        List<String> courseStudents = new ArrayList<>();
        List<String> courseTAs = new ArrayList<>();

        String query = "select S.name from student_course C JOIN student S ON S.id = C.sid where C.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseStudents.add(rs.getString("S.name"));
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

        query = "select T.name from TA T JOIN TA_course C ON C.tid = T.id where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseTAs.add("tname");
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

        System.out.println("-------------------------------------------------------------------Course name : " + name + " ---------------");
        System.out.println("-------------------------------------------------------------------Course code : " + code + " ---------------");
        System.out.println("-------------------------------------------------------------------Course doctor : " + dname + " ---------------");
        if (!courseTAs.isEmpty()) {
            System.out.println("---------------------------------------------------------------Course TAs : ");
            for (String ct : courseTAs) {
                System.out.print(ct + "-");
            }
            System.out.println("");
        }

        if (!courseStudents.isEmpty()) {
            System.out.print("-------------------------------------------------------------------Course students : ");
            for (String cs : courseStudents) {
                System.out.print(cs + ", ");
            }
            System.out.println("");
        }
    }

    public void gradeReport() throws IOException {
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
                System.out.println("Student name: " + students.get(i).name + " , "
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
            System.out.println("----------------Assignment name : " + assignmentName.get(i) + " , " + "Assignment code : " + assignmentCode.get(i) + " ---------------");
        }
    }

    public void createAssignment() throws IOException {
        System.out.println("----------------Please enter assignment code or 0 to cancel ---------------");
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

    private void gradeActions() throws IOException {
        System.out.println("----------------To put bonus for all students enter 1---------------");
        System.out.println("----------------To put bonus for some student enter 2---------------");
        System.out.println("----------------To go back enter 0---------------");

        while (true) {
            String choice = in.readLine();
            if (choice.equals("0")) {
                return;
            } else if (choice.equals("1")) {
                putBonusForAll();
                break;
            } else if (choice.equals("2")) {
                putBonusForStudent();
                break;
            } else {
                System.out.println("----------------Please enter correct input---------------");
            }
        }
    }

    private void putBonusForAll() throws IOException {
        System.out.println("----------------Please put bonus value ---------------");
        String bonus = null;
        while (true) {
            try {
                bonus = in.readLine();
                if (bonus.equals("0")) {
                    return;
                } else if (Integer.parseInt(bonus) != 0) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("----------------bonus input must be number or enter 0 to cancel---------------");
            }
        }

        String query = "update student_course set bonus (bonus+?) where ccod = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, Integer.parseInt(bonus));
            ps.setInt(2, code);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void putBonusForStudent() throws IOException {

        try {
            System.out.println("----------------Please enter student id---------------");
            int sid = Integer.parseInt(in.readLine());

            System.out.println("----------------Please put bonus value---------------");
            int bonus = Integer.parseInt(in.readLine());

            String query = "update student_course set bonus (bonus+?) where ccod = ? and sid = ?;";
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, sid);
            ps.setInt(2, code);
            ps.setInt(3, bonus);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (NumberFormatException e) {
            System.out.println("----------------the student id and the bonus value must be number---------------");
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
                System.out.println("----------------Assignment name : " + a.name + " , "
                        + "Assignment code : " + a.code + " , "
                        + "Assignment grade : " + a.grade);
            }
        } else {
            System.out.println("-------------------------------------------------------------------There is no assignments to view ---------------");
        }
        return assignments;
    }

    public int viewAssignment() throws IOException {
        List<Assignment> assignments = listAssignments();
        if (assignments.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no assignments was created to view ---------------");
        } else {
            System.out.println("----------------Enter the assignment code to view or 0 to cancel ---------------");
            while (true) {
                try {
                    String code = in.readLine();
                    int cod = Integer.parseInt(code);
                    if (code.equals("0")) {
                        return -1;
                    } else if (!checkAssignmentCode(cod)) {
                        new Assignment(Integer.parseInt(code)).viewAssignment();
                        return cod;
                    } else {
                        System.out.println("----------------This course code is not existed enter another or 0 to cancel ---------------");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("----------------Please enter correct input---------------");
                }
            }
        }
        return -1;
    }

    public void addStudent() throws IOException {
        List<Integer> students = listAllStudents();

        if (students.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no students in the site to add ---------------");
        } else {
            System.out.println("----------------Enter the student id ---------------");
            while (true) {
                try {
                    String id = in.readLine();
                    int Id = Integer.parseInt(id);
                    if (id.equals("0")) {
                        return;
                    } else if (!students.contains(Id)) {
                        System.out.println("----------------This id is incorrect enter another id or 0 to cancel ---------------");
                    } else {
                        insertStudent(Id);
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("----------------The id must me number ---------------");
                }
            }
        }
    }

    public void removeStduent() throws IOException {
        List<Integer> students = listStudents();

        if (students.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no students registered in this course ---------------");
        } else {
            System.out.println("----------------Enter the student id that you want to remove ---------------");
            while (true) {
                try {
                    String id = in.readLine();
                    int Id = Integer.parseInt(id);
                    if (id.equals("0")) {
                        return;
                    } else if (!students.contains(Id)) {
                        System.out.println("----------------This id is incorrect enter another id or 0 to cancel ---------------");
                    } else {
                        removeStudent(Id);
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("----------------The id must me number ---------------");
                }
            }
        }
    }

    public void addTA() throws IOException {
        List<Integer> TAs = listAllTAs();

        if (TAs.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no TAs in the site to add ---------------");
        } else {
            while (true) {
                try {
                    String id = in.readLine();
                    int Id = Integer.parseInt(id);
                    if (id.equals("0")) {
                        return;
                    } else if (!TAs.contains(Id)) {
                        System.out.println("----------------This id is incorrect enter another id or 0 to cancel ---------------");
                    } else {
                        insertTA(Id);
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("----------------The id must me number ---------------");
                }
            }
        }
    }

    public void removeTA() throws IOException {
        List<Integer> TAs = listTAs();

        if (TAs.isEmpty()) {
            System.out.println("-------------------------------------------------------------------There is no TAs are teaching in the course ---------------");
        } else {
            System.out.println("----------------Enter the TA id to that you want to remove ---------------");
            while (true) {
                try {
                    String id = in.readLine();
                    int Id = Integer.parseInt(id);
                    if (id.equals("0")) {
                        return;
                    } else if (!TAs.contains(Id)) {
                        System.out.println("----------------This id is incorrect enter another id or 0 to cancel ---------------");
                    } else {
                        removeTA(Id);
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("----------------The id must me number ---------------");
                }
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

        String query = "select name,id from student;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int s = rs.getInt("id");
                System.out.println("----------------Student id: " + s + "id"
                        + " , Student name: " + rs.getString("name") + " ---------------");
                students.add(s);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return students;
    }

    public void insertStudent(int id) {
        String query = "INSERT INTO student_course (sid,ccode) SELECT * FROM (SELECT ?,?) AS tmp WHERE NOT EXISTS (SELECT sid FROM student_course WHERE sid = ?) LIMIT 1;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2, code);
            ps.setInt(3, id);
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
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int s = rs.getInt("id");
                System.out.println("-------------------------------------------------------------------Student id: " + s + "id"
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

    private List<Integer> listAllTAs() {
        List<Integer> TAs = new ArrayList();

        String query = "select name,id from TA;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int t = rs.getInt("id");
                System.out.println("-------------------------------------------------------------------TA id: " + t + " , "
                        + "TA name: " + rs.getString("name") + " ---------------");
                TAs.add(t);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return TAs;
    }

    private void insertTA(int id) {
        String query = "insert into TA_course (tid,ccode) values(?,?);";
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

    private List<Integer> listTAs() {
        List<Integer> TAs = new ArrayList();

        String query = "select T.id, T.name from TA_course C join TA T on C.tid = T.id where C.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int s = rs.getInt("id");
                System.out.println("-------------------------------------------------------------------TA id: " + s + "id"
                        + " , TA name: " + rs.getString("name") + " ---------------");
                TAs.add(s);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return TAs;
    }

    private void removeTA(int id) {
        String query = "delete from TA_course where ccode = ? and tid = ?";
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

    public void studentGradeReport(int id) {
        String query = "select S.id, S.name, C.code,C.name, A.yearmark,A.midmark,A.finalmark,A.bonus,totalmark from Student S join student_course A on S.id = A.sid join course C on C.code = A.ccode where C.code = ? and S.id = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("-------------------------------------------------------------------Coures name : " + rs.getString("C.name") + " , Course code : " + rs.getString("C.code") + "-------------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------------Student name : " + rs.getString("S.name") + " , Student id : " + rs.getString("S.id") + "-------------------------------------------------------------------");
                int yearMark = rs.getInt("A.yearmark");
                int midMark = rs.getInt("A.midmark");
                int finalrMark = rs.getInt("A.finalmark");
                int bonusMark = rs.getInt("A.bonus");
                int totalMark = rs.getInt("A.totalmark");
                System.out.println("-------------------------------------------------------------------Year Mark : " + (yearMark == -1 ? "N/A" : yearMark) + "-------------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------------MidTerm Exam Mark : " + (midMark == -1 ? "N/A" : midMark) + "-------------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------------Final Exam Mark : " + (finalrMark == -1 ? "N/A" : finalrMark) + "-------------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------------Bonus Mark : " + (bonusMark == -1 ? "N/A" : bonusMark) + "-------------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------------Total Mark : " + (totalMark == -1 ? "N/A" : totalMark) + "-------------------------------------------------------------------");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
