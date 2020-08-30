/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenerateUsers;

import Encryption.MyEncryption;
import Persons.User;

/**
 *
 * @author harera
 */
public class Main {

    public static void gernerateUsers() {

        int i = 0;
        for (; i < 6; i++) {
            String name = "", username = "", password = "" + (char) ('a' + i - 32);

            for (int j = 0; j < 3; j++) {
                name += (char) ('a' + i);
                username += (char) ('a' + i);
                password += (char) ('a' + i);
            }

            password += "+2020";

            User.insertDoctor(username, password, name);
            System.out.println(name + "  " + username + "  " + MyEncryption.encryptPassword(password));
        }
        
        for (; i < 12; i++) {
            String name = "", username = "", password = "" + (char) ('a' + i - 32);

            for (int j = 0; j < 3; j++) {
                name += (char) ('a' + i);
                username += (char) ('a' + i);
                password += (char) ('a' + i);
            }

            password += "+2020";

            User.insertTeacher(username, password, name);
            System.out.println(name + "  " + username + "  " + MyEncryption.encryptPassword(password));

        }
        
        for (; i < 26; i++) {
            String name = "", username = "", password = "" + (char) ('a' + i - 32);

            for (int j = 0; j < 3; j++) {
                name += (char) ('a' + i);
                username += (char) ('a' + i);
                password += (char) ('a' + i);
            }

            password += "+2020";

            User.insertStudent(username, password, name);
            System.out.println(name + "  " + username + "  " + MyEncryption.encryptPassword(password));

        }
    }

}
