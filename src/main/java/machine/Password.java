package machine;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class Password {


    protected static boolean hasSpecialChar(String password){
        int special = 0;
        for(int i = 0; i < password.length();i++){
            if(!Character.isDigit(password.charAt(i))  && !Character.isLetter(password.charAt(i)))special++;
        }
        return special > 1;
    }

    protected static boolean hasDigitsInSeq(String password){
        int consq = 0;
        for(int i = 0; i < password.length(); i++){
            if(Character.isDigit(password.charAt(i))){
                if(password.charAt(i) -  password.charAt(i-1) == 1)consq++;
            }
        }
        return consq > 2;
    }

    protected static boolean hasDigits(String password){
        int digit = 0;
        for(int i = 0; i < password.length() ;i++){
            if(Character.isDigit(password.charAt(i)))digit++;
        }
        return digit >= 2;
    }

    protected static boolean hasUpper(String password){
        int upper = 0;
        for(int i = 0; i < password.length();i++){
            if(Character.isUpperCase(password.charAt(i)))upper++;
        }
        return upper >= 2;
    }

    protected static boolean match(String password,String confirmPassword){
        return password.matches(confirmPassword);
    }

    protected static String genPassword(){
        StringBuilder builder = new StringBuilder();
        int i = 16;
        Random random = new Random();
        while(builder.length() < i){
            builder.append((char)(random.nextInt(125)+40));
        }
        return builder.toString();
    }

    protected static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA1024" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            return key.getEncoded();

        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }


    protected static String saltCreate(){
        StringBuilder stringBuilder = new StringBuilder();
        int j = 100;
        Random random = new Random();
        while (stringBuilder.length() < j){
            stringBuilder.append((char)(random.nextInt(90)+48));
        }
        return stringBuilder.toString();
    }

/*
    public static String readPassword (String prompt) {
            PasswordCover passwordCover = new PasswordCover(prompt);
            Thread mask = new Thread(passwordCover);
            mask.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String password = "";
            try {
                password = in.readLine();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            passwordCover.stopMasking();
            return password;
    }
*/
}
