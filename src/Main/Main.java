/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Persons.Doctor;
import Persons.Student;
import java.util.Scanner;

/**
 *
 * @author harera
 */
public class Main {

    static Scanner in;

    public static void main(String[] args) {

    }

    public static void showMainMenue() {
        in = new Scanner(System.in);
        System.out.println("1○ Sign up\n"
                + "2○ Sign in\n"
                + "2○ Exit\n");
        makeChoice();
    }

    private static void makeChoice() {
        try {
            int choice = in.nextInt();
            if (choice == 1) {
                signUp();
            } else if (choice == 2) {
                signIn();
            } else if (choice == 3) {
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("----------------Please enter a correct choice---------------");
        }
        makeChoice();

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
        } catch (NumberFormatException e) {
            System.out.println("----------------Please enter a correct choice---------------");
        }
    }

    private static void signIn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
