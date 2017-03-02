package school.journal.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Generator {
    public static String generate(String value) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(value.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException exc) {

        }
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);
        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }
        return md5Hex;
    }
}
