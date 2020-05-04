package Main;

import Persons.Doctor;
import Persons.Student;
import Persons.Teacher;
import Persons.User;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author harera
 */
public class Main {

    static Scanner in;

    public static void main(String[] args) {
        in = new Scanner(System.in);
        System.out.print("----------------1 Sign up----------------\n"
                + "----------------2 Sign in----------------\n"
                + "----------------3 Exit----------------\n");
        System.out.println("----------------Please enter a input---------------");

        try {
            int choice = in.nextInt();
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

    private static void signUp() {
        System.out.println("----------------To signup for doctor enter 1---------------");
        System.out.println("----------------To signup for student enter 2---------------");

        try {
            int choice = in.nextInt();
            if (choice == 1) {
                new Doctor().signUp();
            } else if (choice == 2) {
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

    private static void signIn() {
        System.out.println("---------------- Please enter the username ---------------");
        String username = in.nextLine();
        System.out.println("---------------- Please enter the password ---------------");
        String password = in.nextLine();

        try {

            if (!User.checkUsername(username)) {
                System.out.println("---------------- Username or password is not correct try another or enter 0 to cancel---------------");
                int choice = in.nextInt();
                if (choice != 0) {
                    signIn();
                }
            } else if (!User.checkPassword(username, password)) {
                System.out.println("---------------- Username or password is not correct try another or enter 0 to cancel---------------");
                int choice = in.nextInt();
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
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter a correct input---------------");
            signIn();
        }
    }
}