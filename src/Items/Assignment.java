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

public class Assignment {
    
    public int code,grade,solved;
    public String name;

    public Assignment(int code, int grade, int solved, String name) {
        this.code = code;
        this.grade = grade;
        this.solved = solved;
        this.name = name;
    }
}