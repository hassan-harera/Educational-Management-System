package Items;

import static DataBase.MyConnection.con;
import Persons.Student;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Course {

    private String name, doctorName;
    private int code, doctorId, studentId, TAid;
    private Connection con;
    private BufferedReader in;

    public Course(int code) {
        in = new BufferedReader(new InputStreamReader(System.in));
        con = con();
        this.code = code;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setTAid(int TAid) {
        this.TAid = TAid;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void viewCourse() {
        List<String> courseStudents = new ArrayList<>();
        List<String> courseTAs = new ArrayList<>();

        var query = "select S.name from student_course C JOIN student S ON S.id = C.sid where C.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            while (rs.next()) {
                courseStudents.add(rs.getString("S.name"));
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        query = "select  D.name from course C JOIN doctor D ON C.did = D.id where code = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            while (rs.next()) {
                doctorName = rs.getString("name");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        query = "select T.name from TA T JOIN TA_course C ON C.tid = T.id where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            while (rs.next()) {
                courseTAs.add(rs.getString("T.name"));
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        query = "select name from course where code = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        out.println("-------------------------------------------------------------------Course name : " + name + " ---------------");
        out.println("-------------------------------------------------------------------Course code : " + code + " ---------------");
        out.println("-------------------------------------------------------------------Course doctor : " + doctorName + " ---------------");
        if (!courseTAs.isEmpty()) {
            out.print("-------------------------------------------------------------------Course TAs : ");
            for (var ct : courseTAs) {
                out.print(ct + "-");
            }
            out.println("");
        }

        if (!courseStudents.isEmpty()) {
            out.print("-------------------------------------------------------------------Course students : ");
            for (var cs : courseStudents) {
                out.print(cs + ", ");
            }
            out.println("");
        }
    }

    public void markReport() throws IOException {
        List<Student> students = new ArrayList<>();

        var query = "SELECT  S.name, S.id, C.midmark, C.finalmark, C.totalmark , C.bonus, C.yearmark FROM student_course C JOIN student S ON S.id = C.sid where C.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            while (rs.next()) {
                var s = new Student(rs.getInt("id"));
                var midMark = rs.getInt("midmark");
                var finalMark = rs.getInt("finalmark");
                var yearMark = rs.getInt("yearmark");
                var bonus = rs.getInt("bonus");
                var totalMark = (midMark == -1 ? 0 : midMark) + (finalMark == -1 ? 0 : finalMark) + (yearMark == -1 ? 0 : yearMark) + bonus;

                s.setName(rs.getString("name"));
                s.setMidGrade(midMark);
                s.setFinalGrade(finalMark);
                s.setYearDoingGrade(yearMark);
                s.setBonusGrade(bonus);
                s.setTotalGrade(totalMark);

                students.add(s);
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        if (!students.isEmpty()) {
            for (var i = 0; i < students.size(); i++) {
                out.println("Student name: " + students.get(i).getName() + " ------ "
                        + "Student id: " + students.get(i).getId() + " ------ "
                        + "Mid exame mark: " + (students.get(i).getMidGrade() == -1 ? "unknown" : students.get(i).getMidGrade()) + " ------ "
                        + "coursework mark: " + (students.get(i).getYearDoingGrade() == -1 ? "unknown" : students.get(i).getYearDoingGrade()) + " ------ "
                        + "bonus marks: " + (students.get(i).getBonusGrade() == -1 ? "unknown" : students.get(i).getBonusGrade()) + " ------ "
                        + "Final exam mark: " + (students.get(i).getFinalGrade() == -1 ? "unknown" : students.get(i).getFinalGrade()) + " ------ "
                        + "Total mark: " + (students.get(i).getTotalGrade() == -1 ? "unknown" : students.get(i).getTotalGrade()));
            }
            markActions();
        } else {
            out.println("-------------------------------------------------------------------NO students was registerd in this course------------------------------");
        }

    }

    private void doctorAssignments() {
        List<String> assignmentName = new ArrayList<>();
        List<String> assignmentCode = new ArrayList<>();

        var query = "select  A.acode, C.acode from assignments A JOIN course_assignment C ON A.acode = C.acode where ccode = ?;";

        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setString(1, code + "");
            var rs = ps.executeQuery();
            while (rs.next()) {
                assignmentCode.add("acode");
                assignmentName.add("aname");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        for (var i = 0; !assignmentName.isEmpty() && !assignmentCode.isEmpty(); i++) {
            out.println("-------------------------------------------------------------------Assignment name : " + assignmentName.get(i) + " , " + "Assignment code : " + assignmentCode.get(i) + " ---------------");
        }
    }

    public void createAssignment() throws IOException {
        String code, name, mark, questions;
        int Code, Mark;

        while (true) {
            out.println("-------------------------------------------------------------------Please enter assignment code---------------");
            code = in.readLine();
            if (code.matches("^\\d+$")) {
                Code = parseInt(code);
                if (Code == 0) {
                    return;
                } else if (!checkAssignmentCode(Code)) {
                    out.println("-------------------------------------------------------------------This Assignment code is already exists---------------");
                } else {
                    break;
                }
            } else {
                out.println("-------------------------------------------------------------------INVALID CODE-------------------------------------------------------------------");
            }
        }

        out.println(
                "----------------Please enter assignment name---------------");
        name = in.readLine();

        out.println(
                "----------------Please enter the assignment questions manually---------------");
        questions = in.readLine();

        while (true) {
            out.println("-------------------------------------------------------------------Please enter the assignment mark---------------");
            mark = in.readLine();
            Mark = parseInt(mark);
            if (code.matches("^\\d+$")) {
                Code = parseInt(code);
                if (Code == 0) {
                    return;
                } else if (!checkAssignmentCode(Code)) {
                    out.println("----------------This Assignment code is already exists enter another code or 0 to go back---------------");
                } else {
                    break;
                }
            } else {
                out.println("-------------------------------------------------------------------INVALID CODE-------------------------------------------------------------------");
            }
        }

        var query = "insert  into assignment (ccode,code,grade,name,question) values (?,?,?,?,?);";

        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, this.code);
            ps.setInt(2, Code);
            ps.setInt(3, Mark);
            ps.setString(4, name);
            ps.setString(5, questions);
            ps.execute();
            out.println("-------------------------------------------------------------------SUCCESSFULLY CREATED---------------");
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    private void markActions() throws IOException {
        out.println("\n-------------------------------------------------------------------To put bonus for all students enter 1---------------");
        out.println("-------------------------------------------------------------------To put bonus for some student enter 2---------------");
        out.println("-------------------------------------------------------------------To go back enter 0---------------");

        OUTER:
        while (true) {
            var choice = in.readLine();
            switch (choice) {
                case "0":
                    return;
                case "1":
                    putBonusForAll();
                    break OUTER;
                case "2":
                    putBonusForStudent();
                    break OUTER;
                default:
                    out.println("-------------------------------------------------------------------Please enter correct input---------------");
                    break;
            }
        }
    }

    private void putBonusForAll() throws IOException {
        out.println("-------------------------------------------------------------------Please enter the bouns value---------------");
        String value;
        while (true) {
            value = in.readLine();
            if (value.matches("^\\d+$")) {
                var Value = parseInt(value);
                if (Value == 0) {
                    return;
                } else {
                    break;
                }
            } else {
                out.println("-------------------------------------------------------------------INVALID VALUE---------------");
            }
        }

        var query = "update student_course set bonus = (bonus+?) where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, parseInt(value));
            ps.setInt(2, code);
            ps.executeUpdate();
        } catch (SQLException ex) {
            out.println(ex);
        }
    }

    private void putBonusForStudent() throws IOException {
        out.println("-------------------------------------------------------------------Please enter the student id---------------");
        String id;
        while (true) {
            id = in.readLine();
            if (id.matches("^\\d+$")) {
                var ID = parseInt(id);
                if (ID == 0) {
                    return;
                } else {
                    break;
                }
            } else {
                out.println("-------------------------------------------------------------------INVALID ID---------------");
            }
        }

        out.println("-------------------------------------------------------------------Please enter the bouns value---------------");
        String value;
        while (true) {
            value = in.readLine();
            if (value.matches("^\\d+$")) {
                break;
            } else {
                out.println("-------------------------------------------------------------------INVALID VALUE---------------");
            }
        }

        var query = "update student_course set bonus = (bonus+?) where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, parseInt(value));
            ps.setInt(2, code);
            ps.executeUpdate();
        } catch (SQLException ex) {
            out.println(ex);
        }

    }

    public List<Assignment> listAssignments() {
        List<Assignment> assignments = new ArrayList();

        var query = "select * from assignment where ccode = ?;";

        try {
            PreparedStatement ps;
            ps = con().prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            while (rs.next()) {
                var assignment = new Assignment(rs.getInt("code"));
                assignment.setName(rs.getString("name"));
                assignment.setGrade(rs.getInt("grade"));
                assignments.add(assignment);
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        if (!assignments.isEmpty()) {
            for (var a : assignments) {
                out.println("-------------------------------------------------------------------Assignment name : " + a.getName() + " , "
                        + "Assignment code : " + a.getCode() + " , "
                        + "Assignment mark : " + a.getGrade());
            }
        } else {
            out.println("-------------------------------------------------------------------There is no assignments to view ---------------");
        }
        return assignments;
    }

    public Assignment viewAssignment() throws IOException {
        var assignments = listAssignments();
        if (assignments.isEmpty()) {
            out.println("-------------------------------------------------------------------There is no assignments was created to view ---------------");
        } else {
            while (true) {
                out.println("-------------------------------------------------------------------Enter the assignment code---------------");
                var code = in.readLine();
                if (code.matches("^\\d+$")) {
                    var Code = parseInt(code);
                    if (Code == 0) {
                        return null;
                    } else if (!checkAssignmentCode(Code)) {
                        var a = new Assignment(parseInt(code));
                        a.viewAssignment();
                        return a;
                    } else {
                        out.println("-------------------------------------------------------------------This course code is not corrected---------------");
                    }
                } else {
                    out.println("-------------------------------------------------------------------INVALID code-------------------------------------------------------------------");
                }
            }
        }
        return null;
    }

    public void addStudent() throws IOException {
        var students = listAllStudents();
        if (!students.isEmpty()) {
            while (true) {
                out.println("-------------------------------------------------------------------Enter the student id-------------------------------------------------------------------");
                var id = in.readLine();
                if (id.matches("^\\d+$")) {
                    var Id = parseInt(id);
                    Id = parseInt(id);
                    if (Id == 0) {
                        return;
                    } else if (!students.contains(Id)) {
                        out.println("-------------------------------------------------------------------this id is incorrect-------------------------------------------------------------------");
                    } else {
                        insertStudent(Id);
                        break;
                    }
                } else {
                    out.println("-------------------------------------------------------------------INVALID ID-------------------------------------------------------------------");
                }
            }
        }
    }

    public void removeStduent() throws IOException {
        var students = listStudents();

        if (students.isEmpty()) {
            out.println("-------------------------------------------------------------------There is no students registered in this course ---------------");
        } else {
            while (true) {
                out.println("-------------------------------------------------------------------Enter the student id-------------------------------------------------------------------");
                var id = in.readLine();
                if (id.matches("^\\d+$")) {
                    var Id = parseInt(id);
                    Id = parseInt(id);
                    if (Id == 0) {
                        return;
                    } else if (!students.contains(Id)) {
                        out.println("-------------------------------------------------------------------this id is incorrect-------------------------------------------------------------------");
                    } else {
                        removeStudent(Id);
                        break;
                    }
                } else {
                    out.println("-------------------------------------------------------------------INVALID ID-------------------------------------------------------------------");
                }
            }
        }
    }

    public void addTA() throws IOException {
        var TAs = listAllTAs();

        if (TAs.isEmpty()) {
            out.println("-------------------------------------------------------------------There is no TAs in the site to add ---------------");
        } else {
            while (true) {
                out.println("-------------------------------------------------------------------Enter the TA id-------------------------------------------------------------------");
                var id = in.readLine();
                if (id.matches("^\\d+$")) {
                    var Id = parseInt(id);
                    Id = parseInt(id);
                    if (Id == 0) {
                        return;
                    } else {
                        addTA(Id);
                        return;
                    }
                } else {
                    out.println("-------------------------------------------------------------------INVALID ID-------------------------------------------------------------------");
                }
            }
        }
    }

    public void removeTA() throws IOException {
        var TAs = listTAs();

        if (TAs.isEmpty()) {
            out.println("-------------------------------------------------------------------There is no TAs are teaching in the course ---------------");
        } else {
            while (true) {
                out.println("-------------------------------------------------------------------Enter the TA id-------------------------------------------------------------------");
                var id = in.readLine();
                if (id.matches("^\\d+$")) {
                    var Id = parseInt(id);
                    Id = parseInt(id);
                    if (Id == 0) {
                        return;
                    } else {
                        removeTA(Id);
                        return;
                    }
                } else {
                    out.println("-------------------------------------------------------------------INVALID ID-------------------------------------------------------------------");
                }
            }
        }
    }

    private boolean checkAssignmentCode(int acode) {
        PreparedStatement ps = null;
        var query = "select code from assignment where code = ?;";
        try {
            ps = con().prepareStatement(query);
            ps.setInt(1, acode);
            if (ps.executeQuery().next()) {
                return false;
            }
        } catch (SQLException e) {
            out.println(e);
        }
        return true;
    }

    private List<Integer> listAllStudents() {
        List<Integer> students = new ArrayList();

        var query = "select name,id from student;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            var rs = ps.executeQuery();
            while (rs.next()) {
                var s = rs.getInt("id");
                out.println("-------------------------------------------------------------------Student id: " + s
                        + " , Student name: " + rs.getString("name") + " ---------------");
                students.add(s);
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }

        return students;
    }

    public void insertStudent(int id) {
        Boolean isInserted = false;
        var query = "select sid from student_course where sid = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            isInserted = ps.executeQuery().next();
        } catch (SQLException ex) {
            out.println(ex.getMessage());

        }
        if (!isInserted) {
            query = "insert into student_course (sid,ccode) values (?,?);";
            try {
                PreparedStatement ps;
                ps = con.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, code);
                ps.execute();
            } catch (SQLException ex) {
                out.println(ex.getMessage());
            }
        }
    }

    private List<Integer> listStudents() {
        List<Integer> students = new ArrayList();

        var query = "select S.id, S.name from student_course C join student S on C.sid = S.id where C.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            while (rs.next()) {
                var s = rs.getInt("id");
                out.println("-------------------------------------------------------------------Student id: " + s
                        + " , Student name: " + rs.getString("name") + " ---------------");
                students.add(s);
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        return students;
    }

    private void removeStudent(int id) {
        var query = "delete from student_course where ccode = ? and sid = ?";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, id);
            ps.execute();
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    private List<Integer> listAllTAs() {
        List<Integer> TAs = new ArrayList();
        var query = "select name,id from TA;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            var rs = ps.executeQuery();
            while (rs.next()) {
                var t = rs.getInt("id");
                out.println("-------------------------------------------------------------------TA id: " + t + " , "
                        + "TA name: " + rs.getString("name") + " ---------------");
                TAs.add(t);
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        return TAs;
    }

    private void insertTA(int id) {
        var query = "insert into TA_course (tid,ccode) values(?,?);";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2, code);
            ps.execute();
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    private List<Integer> listTAs() {
        List<Integer> TAs = new ArrayList();

        var query = "select T.id, T.name from TA_course C join TA T on C.tid = T.id where C.ccode = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            var rs = ps.executeQuery();
            while (rs.next()) {
                var s = rs.getInt("id");
                out.println("-------------------------------------------------------------------TA id: " + s
                        + " , TA name: " + rs.getString("name") + " ---------------");
                TAs.add(s);
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
        return TAs;
    }

    private void addTA(int id) {
        if (checkTAId(id)) {
            var query = "insert into TA_course Values (?,?);";
            try {
                PreparedStatement ps;
                ps = con.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, code);
                ps.execute();
            } catch (SQLException ex) {
                out.println(ex.getMessage());
            }
        } else {
            out.println("-------------------------------------------------------------------INCORRECT ID-------------------------------------------------------------------");

        }
    }

    private void removeTA(int id) {
        var query = "delete from TA_course where ccode = ? and tid = ?";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, id);
            ps.execute();
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    public void studentGradeReport(int id) {
        var query = "select S.id, S.name, C.code,C.name, A.yearmark,A.midmark,A.finalmark,A.bonus,totalmark from Student S join student_course A on S.id = A.sid join course C on C.code = A.ccode where C.code = ? and S.id = ?;";
        try {
            PreparedStatement ps;
            ps = con.prepareStatement(query);
            ps.setInt(1, code);
            ps.setInt(2, id);
            var rs = ps.executeQuery();
            while (rs.next()) {
                out.println("-------------------------------------------------------------------Coures name : " + rs.getString("C.name") + " , Course code : " + rs.getString("C.code") + "-------------------------------------------------------------------");
                out.println("-------------------------------------------------------------------Student name : " + rs.getString("S.name") + " , Student id : " + rs.getString("S.id") + "-------------------------------------------------------------------");
                var yearMark = rs.getInt("A.yearmark");
                var midMark = rs.getInt("A.midmark");
                var finalrMark = rs.getInt("A.finalmark");
                var bonusMark = rs.getInt("A.bonus");
                var totalMark = rs.getInt("A.totalmark");
                out.println("-------------------------------------------------------------------Workcourse Mark : " + (yearMark == -1 ? "unknown" : yearMark) + "-------------------------------------------------------------------");
                out.println("-------------------------------------------------------------------MidTerm Exam Mark : " + (midMark == -1 ? "unknown" : midMark) + "-------------------------------------------------------------------");
                out.println("-------------------------------------------------------------------Final Exam Mark : " + (finalrMark == -1 ? "unknown" : finalrMark) + "-------------------------------------------------------------------");
                out.println("-------------------------------------------------------------------Bonus Marks : " + (bonusMark == -1 ? "unknown" : bonusMark) + "-------------------------------------------------------------------");
                out.println("-------------------------------------------------------------------Total Mark : " + (totalMark == -1 ? "unknown" : totalMark) + "-------------------------------------------------------------------");
            }
        } catch (SQLException ex) {
            out.println(ex.getMessage());
        }
    }

    private boolean checkTAId(int id) {
        Boolean result = false;
        var query = "select id from TA where id = ?";
        try {
            var ps = con.prepareStatement(query);
            ps.setInt(1, id);
            result = ps.executeQuery().next();
        } catch (SQLException e) {
            out.println(e);
        }

        query = "select tid from TA_course where tid = ?";
        try {
            var ps = con.prepareStatement(query);
            ps.setInt(1, id);
            return result && !ps.executeQuery().next();
        } catch (SQLException e) {
            out.println(e);
        }
        return false;
    }
}
