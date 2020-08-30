package Main;

import static Encryption.MyEncryption.encryptPassword;
import Persons.Doctor;
import Persons.Student;
import Persons.Teacher;
import static Persons.User.checkPassword;
import static Persons.User.checkUsername;
import static Persons.User.isDoctor;
import static Persons.User.isTA;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.out;
import java.util.InputMismatchException;

/**
 *
 * @author harera
 */
public class Main {

    static BufferedReader in;

    public static void main(String[] args) throws IOException {
        in = new BufferedReader(new InputStreamReader(System.in));
        out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
        out.println("-------------------------------------------------------------------MAIN MENUE -------------------------------------------------------------");
        out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
        out.println("--------------------------------------------------------------------1 Sign up---------------------------------------------------------------\n"
                + "--------------------------------------------------------------------2 Sign in---------------------------------------------------------------\n"
                + "--------------------------------------------------------------------3 Exit------------------------------------------------------------------");
        out.println("--------------------------------------------------------------------Please enter a input--------------------------------------------------------");
        try {
            var choice = in.readLine();
            switch (choice) {
                case "1":
                    signUp();
                    break;
                case "2":
                    signIn();
                    break;
                case "3":
                    return;
                default:
                    out.println("----------------------------------------------------------------Please enter a correct input--------------------------------------------------------");
                    break;
            }
        } catch (InputMismatchException e) {
            out.println("-------------------------------------------------------------------Please enter a correct input---------------");
        }
        main(args);
    }

    private static void signUp() throws IOException {
        out.println("-------------------------------------------------------------------To signup for doctor enter 1---------------");
        out.println("-------------------------------------------------------------------To signup for TA enter 2---------------");
        out.println("-------------------------------------------------------------------To signup for student enter 3---------------");
        out.println("-------------------------------------------------------------------To return enter 0---------------");

        var choice = in.readLine();
        switch (choice) {
            case "1":
                Doctor.signUp();
                break;
            case "2":
                Teacher.signUp();
                break;
            case "3":
                Student.signUp();
                break;
            case "0":
                return;
            default:
                out.println("-------------------------------------------------------------------Invalid Choice---------------");
                signUp();
                break;
        }
    }

    private static void signIn() throws IOException {

        out.println("-------------------------------------------------------------------Please enter the username ---------------");
        String username, password;
        while (true) {
            username = in.readLine();
            if (username.equals("0")) {
                return;
            } else if (!checkUsername(username)) {
                out.println("-------------------------------------------------------------------Username is not correct try another or enter 0 to cancel---------------");
            } else {
                break;
            }
        }

        out.println("-------------------------------------------------------------------Please enter the password ---------------");

        while (true) {
            password = in.readLine();
            if (password.equals("0")) {
                return;
            } else if (!checkPassword(username, password)) {
                out.println("-------------------------------------------------------------------The password is not correct try another or enter 0 to cancel---------------");
            } else {
                break;
            }
        }
        var isdoctor = isDoctor(username);
        var isTA = isTA(username);
        if (isdoctor) {
            new Doctor(username).showMainMenu();
        } else if (isTA) {
            new Teacher(username).showMainMenu();
        } else {
            new Student(username).showMainMenu();
        }
    }
}
