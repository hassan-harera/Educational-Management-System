package Encryption;

public class MyEncryption {

    public static String encryptPassword(String str) {

        StringBuilder encrypted = new StringBuilder();

        for (var i = 0; i < str.length(); i++) {
            encrypted.append(((int) str.charAt(i)) % 26);
        }
        
        return encrypted.toString();
    }

}
