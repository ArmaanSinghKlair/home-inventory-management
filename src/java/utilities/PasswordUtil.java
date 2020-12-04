/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
public class PasswordUtil {

/* This code uses SHA-256. If this algorithm isn't available to you, you can try a weaker level of encryption such as SHA-128.
*/ 
    public static String hashPassword(String password)throws NoSuchAlgorithmException { 
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes()); 
        byte[] mdArray = md.digest(); 
        StringBuilder sb = new StringBuilder(mdArray.length * 2); 
        for (byte b : mdArray) {
            int v = b & 0xff; 
            if (v < 16) {
                sb.append('0'); 
            } 
            sb.append(Integer.toHexString(v));
        }
            return sb.toString();
    }
}

        

