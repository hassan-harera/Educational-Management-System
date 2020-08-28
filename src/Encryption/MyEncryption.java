package Encryption;

public class MyEncryption {

    public static String encryptPassword(String str) {

        var len = str.length();
        var encrpted = len + "~";
        encrpted += len;

        for (var i = 0; i < str.length(); i++) {
            if (i != str.length() - 1) {
                encrpted += (int) str.charAt(i) - 'A' + "~";
            } else {
                encrpted += (int) str.charAt(i) + len;
            }
        }

        return encrpted;
    }
    
}