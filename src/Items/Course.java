package Items;

import DataBase.MyConnection;
import Persons.Student;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author harera
 */
public class Course {

    String cname;
    int ccode;
    Scanner in;

    public Course(int ccode) {
        this.ccode = ccode;
        in = new Scanner(System.in);
    }

    public void viewCourse() {
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

        System.out.println("---------------- Course name : " + cname + " ---------------");
        System.out.println("---------------- Course ccode : " + ccode + " ---------------");
        System.out.println("---------------- Course doctor : " + dname + " ---------------");
        System.out.println("---------------- Course teachers : ");
        for (String ct : courseTeachers) {
            System.out.print(ct + "--");
        }
        System.out.println("");
        System.out.println("---------------- Course students : ");
        for (String cs : courseStudents) {
            System.out.print(cs + "--");
        }
        System.out.println("");
    }

//    private void makeChoice() {
//        System.out.println("----------------Please enter a input---------------");
//        try {
//            int choice = in.nextInt();
//            if (choice == 1) {
//                doctorGradeReport();
//            } else if (choice == 2) {
//                doctorAssignments();
//            } else if (choice == 3) {
//                CreateAssignment();
//            } else if (choice == 4) {
//
//            } else if (choice == 5) {
//
//            } else {
//                System.out.println("----------------Please enter a correct choice---------------");
//            }
//        } catch (InputMismatchException e) {
//            System.out.println("----------------Please enter a correct input---------------");
//        }
//    }
    public void gradeReport() {
        List<Student> students = new ArrayList<>();

        String query = "select  S.name, S.id, C.mid_grade, C.final_grade, C.total_grade , C.bonus_grade, C.year_grade form Student S join course_student C on S.id = C.id where ccode = ?;";
        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setInt(1, ccode);
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
        }
    }

    private void doctorAssignments() {
        List<String> assignmentName = new ArrayList<>();
        List<String> assignmentCode = new ArrayList<>();

        String query = "select  A.acode, C.acode from assignments A JOIN course_assignment C ON A.acode = C.acode where ccode = ?;";

        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
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

    public void createAssignment() {
        System.out.println("----------------Please enter assignment code---------------");
        try {
            int code = in.nextInt();
            if (!checkAssignmentCode(code)) {
                System.out.println("----------------This code was already found try another or enter 0 to go back---------------");
                int choice = in.nextInt();
                if (choice != 0) {
                    createAssignment();
                }
            } else {
                System.out.println("----------------Please enter assignment name---------------");
                String name = in.nextLine();

                System.out.println("----------------Please enter the assignment questions manually---------------");
                String questions = in.nextLine();

                System.out.println("----------------Please enter assignment grade---------------");
                int grade = in.nextInt();

                String query = "insert (ccode,code,grade,name,question) into assignment values (?,?,?,?,?);";
                try {
                    PreparedStatement ps;
                    ps = MyConnection.con().prepareStatement(query);
                    ps.setInt(1, ccode);
                    ps.setInt(2, code);
                    ps.setInt(3, grade);
                    ps.setString(4, name);
                    ps.setString(5, questions);
                    ps.execute();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter a correct input---------------");
            createAssignment();
        }
    }

    private void gradeActions() {
        System.out.println("----------------To put bonus for all students enter 1---------------");
        System.out.println("----------------To put bonus for some student enter 2---------------");
        System.out.println("----------------To go back enter 0---------------");

        try {
            int choice = in.nextInt();
            if (choice != 0) {
                if (choice == 1) {
                    putBonusForAll();
                } else if (choice == 2) {
                    putBonusForStudent();
                } else {
                    System.out.println("----------------Please enter a correct input---------------");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter correct input---------------");
        }
    }

    private void putBonusForAll() {
        System.out.println("---------------- Please put bonus value ---------------");
        try {
            int bonus = in.nextInt();
            String query = "update student_course set bonus (bonus+?) where ccod = ?;";
            try {
                PreparedStatement ps;
                ps = MyConnection.con().prepareStatement(query);
                ps.setInt(1, bonus);
                ps.setInt(2, ccode);
                ps.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter a correct input---------------");
        }
    }

    private void putBonusForStudent() {

        try {
            System.out.println("---------------- Please enter student id ---------------");
            int sid = in.nextInt();
            System.out.println("---------------- Please put bonus value ---------------");
            int bonus = in.nextInt();
            String query = "update student_course set bonus (bonus+?) where ccod = ? and sid = ?;";
            try {
                PreparedStatement ps;
                ps = MyConnection.con().prepareStatement(query);
                ps.setInt(1, bonus);
                ps.setInt(2, ccode);
                ps.setInt(3, sid);
                ps.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter a correct input---------------");
        }
    }

    public List<Assignment> listAssignments() {
        List<Assignment> assis = new ArrayList<>();

        String query = "select  A.name, A.grade, A.code , S.COUNT(sid) from assignment A JOIN assignment_student S ON A.code = S.acode where ccode = ?;";

        try {
            PreparedStatement ps;
            ps = MyConnection.con().prepareStatement(query);
            ps.setString(1, ccode + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                assis.add(new Assignment(rs.getInt("code"), rs.getInt("grade"), rs.getInt("COUNT(sid)"), rs.getString("name")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        if (!assis.isEmpty()) {
            for (int i = 0; !assis.isEmpty(); i++) {
                System.out.println("---------------- Assignment name : " + assis.get(i).name + " , "
                        + "Assignment code : " + assis.get(i).code + " , "
                        + "Assignment grade : " + assis.get(i).grade + " , "
                        + "number of submission : " + assis.get(i).solved + "---------------- ");
            }
        } else {
            System.out.println("---------------- There is no assignments to view ---------------");
        }
        return assis;
    }

    public void viewAssignment() {
        List<Assignment> assis = listAssignments();
        if (assis.isEmpty()) {
            System.out.println("---------------- There is no assignments was created to view ---------------");
        } else {
            System.out.println("---------------- Enter the assignment code to view or 0 to cancel ---------------");
            try {
                int code = in.nextInt();
                if (code != 0) {
                    if (assis.contains(ccode + "")) {
                        new Course(ccode).viewCourse();
                        courseMenue(ccode);
                    } else {
                        System.out.println("---------------- This course code is not found try again ---------------");
                        viewCourse();
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("----------------Please enter a correct input---------------");
                viewCourse();
            }
        }
    }

    public void addStudent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeStduent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addTeacher() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeTeacher() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean checkAssignmentCode(int acode) {
        PreparedStatement ps = null;
        String query = "select acode from course_assignment where acode = ?;";
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
}
