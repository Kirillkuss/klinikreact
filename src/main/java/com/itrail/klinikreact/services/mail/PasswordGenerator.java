package com.itrail.klinikreact.services.mail;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";
    private static final int PASSWORD_LENGTH = 12; 
    private static final SecureRandom random = new SecureRandom();

    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        password.append( LOWERCASE.charAt( random.nextInt( LOWERCASE.length() )));
        password.append( UPPERCASE.charAt( random.nextInt( UPPERCASE.length() )));
        password.append( DIGITS.charAt( random.nextInt( DIGITS.length() )));
        password.append( SPECIAL_CHARACTERS.charAt( random.nextInt( SPECIAL_CHARACTERS.length() )));
        String allCharacters = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;
        for ( int i = 4; i < PASSWORD_LENGTH; i++ ) {
            int randomIndex = random.nextInt( allCharacters.length() );
            password.append( allCharacters.charAt( randomIndex ));
        }
        return shuffleString(password.toString());
    }

    private String shuffleString(String input) {
        char[] array = input.toCharArray();
        for (int i = 0; i < array.length; i++) {
            int randomIndex = random.nextInt(array.length);
            char temp = array[i];
            array[i] = array[randomIndex];
            array[randomIndex] = temp;
        }
        return new String(array);
    }
    
}
