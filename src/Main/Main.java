package Main;

import Encryption.MyEncryption;
import Persons.Doctor;
import Persons.Student;
import Persons.Teacher;
import Persons.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author harera
 */
public class Main {

    static BufferedReader in;

    public static void main(String[] args) throws IOException {
        in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------- MAIN MENUE -------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------1 Sign up---------------------------------------------------------------\n"
                + "--------------------------------------------------------------------2 Sign in---------------------------------------------------------------\n"
                + "--------------------------------------------------------------------3 Exit------------------------------------------------------------------");
        System.out.println("----------------------------------------------------------------Please enter a input--------------------------------------------------------");
        try {
            String choice = in.readLine();
            if (choice.equals("1")) {
                signUp();
            } else if (choice.equals("2")) {
                signIn();
            } else if (choice.equals("3")) {
                return;
            } else {
                System.out.println("----------------------------------------------------------------Please enter a correct input--------------------------------------------------------");
            }
        } catch (InputMismatchException e) {
            System.out.println("---------------------------------Please enter a correct input---------------");
        }
        main(args);
    }

    private static void signUp() throws IOException {
        System.out.println("---------------------------------To signup for doctor enter 1---------------");
        System.out.println("---------------------------------To signup for teacher enter 2---------------");
        System.out.println("---------------------------------To signup for student enter 3---------------");
        System.out.println("---------------------------------To return enter 3---------------");

        String choice = in.readLine();
        if (choice.equals("1")) {
            Doctor.signUp();
        } else if (choice.equals("2")) {
//                Teacher.signUp();
        } else if (choice.equals("3")) {
//                Student.signUp();
        } else {
            System.out.println("---------------------------------Invalid Choice---------------");
            signUp();
        }
    }

    private static void signIn() throws IOException {

        System.out.println("--------------------------------- Please enter the username ---------------");
        String username, password;
        while (true) {
            username = in.readLine();
            if (username.equals("0")) {
                return;
            } else if (!User.checkUsername(username)) {
                System.out.println("--------------------------------- Username is not correct try another or enter 0 to cancel---------------");
            } else {
                break;
            }
        }

        System.out.println("--------------------------------- Please enter the password ---------------");

        while (true) {
            password = in.readLine();
            String encrPassword = MyEncryption.encryptPassword(password);
            if (password.equals("0")) {
                return;
            } else if (!User.checkPassword(username, encrPassword)) {
                System.out.println("--------------------------------- The password is not correct try another or enter 0 to cancel---------------");
            } else {
                break;
            }
        }
        Boolean isdoctor = User.isDoctor(username);
        Boolean isteacher = User.isTeacher(username);
        if (isdoctor) {
            new Doctor(username).showMainMenue();
        } else if (isteacher) {
            new Teacher().showMainMenue();
        } else {
            new Student().showMainMenue();
        }
    }
}
