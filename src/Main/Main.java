package Main;

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
    static Scanner sn;

    public static void main(String[] args) throws IOException {
        in = new BufferedReader(new InputStreamReader(System.in));
        sn = new Scanner(System.in);
        System.out.print("----------------1 Sign up----------------\n"
                + "----------------2 Sign in----------------\n"
                + "----------------3 Exit----------------\n");
        System.out.println("----------------Please enter a input---------------");

        try {
            int choice = sn.nextInt();
            if (choice == 1) {
                signUp();
            } else if (choice == 2) {
                signIn();
            } else if (choice == 3) {
                return;
            } else {
                System.out.println("----------------Please enter a correct input---------------");
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter a correct input---------------");
        }
        main(args);
    }

    private static void signUp() throws IOException {
        System.out.println("----------------To signup for doctor enter 1---------------");
        System.out.println("----------------To signup for teacher enter 2---------------");
        System.out.println("----------------To signup for student enter 3---------------");

        try {
            int choice = sn.nextInt();
            if (choice == 1) {
                new Doctor().signUp();
            } else if (choice == 2) {
                new Teacher().signUp();
            } else if (choice == 3) {
                new Student().signUp();
            } else {
                System.out.println("----------------Invalid Choice---------------");
                signUp();
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter a correct input---------------");
            signUp();
        }
    }

    private static void signIn() throws IOException {
        try {
            System.out.println("---------------- Please enter the username ---------------");
            String username = in.readLine();
            if (!User.checkUsername(username)) {
                System.out.println("---------------- Username is not correct try another or enter 0 to cancel---------------");
                int choice = sn.nextInt();
                if (choice != 0) {
                    signIn();
                }
            } else {
                System.out.println("---------------- Please enter the password ---------------");
                String password = in.readLine();
                if (!User.checkPassword(username, password)) {
                    System.out.println("---------------- password is not correct try another or enter 0 to cancel---------------");
                    int choice = sn.nextInt();
                    if (choice != 0) {
                        signIn();
                    }
                } else {
                    Boolean isdoctor = User.isDoctor(username);
                    Boolean isteacher = User.isTeacher(username);
                    if (isdoctor) {
                        new Doctor().showMainMenue();
                    } else if (isteacher) {
                        new Teacher().showMainMenue();
                    } else {
                        new Student().showMainMenue();
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println(e);
            signIn();
        }
    }
}
