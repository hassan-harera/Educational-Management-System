package Main;

import Persons.Doctor;
import Persons.Student;
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
        System.out.print("----------------1○ Sign up----------------\n"
                + "----------------2○ Sign in----------------\n"
                + "----------------3○ Exit----------------\n");
        System.out.println("----------------Please enter a choice---------------");

        try {
            int choice = in.nextInt();
            if (choice == 1) {
                signUp();
            } else if (choice == 2) {
                signIn();
            } else if (choice == 3) {

            } else {
                System.out.println("----------------Please enter a correct choice---------------");
            }
        } catch (InputMismatchException e) {
            System.out.println("----------------Please enter a correct choice---------------");
            main(args);
        }
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
            System.out.println("----------------Please enter a correct choice---------------");
        }
    }

    private static void signIn() {
        System.out.println("---------------- Please enter the username ---------------");
        String username = in.nextLine();
        System.out.println("---------------- Please enter the password ---------------");
        String password = in.nextLine();

        if (!checkUsername(username)) {

        } else if (!checkPassword(username,password)) {
        
        } else {
            
        }
    }

}
