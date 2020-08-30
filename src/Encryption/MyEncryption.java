package Encryption;

public class MyEncryption {

    public static String encryptPassword(String str) {

        String encrpted = "";

        for (var i = 0; i < str.length(); i++) {
            encrpted += (int) str.charAt(i) % 26;
        }
        
        return encrpted;
    }

}
