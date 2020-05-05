package Encryption;

public class MyEncryption {

    public static String encryptPassword(String str) {

        int len = str.length();

        String encrpted = len + "~";
        encrpted += len;

        for (int i = 0; i < str.length(); i++) {
            if (i != str.length() - 1) {
                encrpted += (int) str.charAt(i) - 'A' + "~";
            } else {
                encrpted += (int) str.charAt(i) + len;
            }
        }

        return encrpted;
    }
    
}