package com.josepdevs.Domain.dto.valueobjects;

import java.security.SecureRandom;
import java.util.Random;

import com.josepdevs.Domain.Exceptions.PasswordNotValidException;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Psswrd {
		
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
	
    public static byte[] getNextSalt() {
        byte[] saltArray = new byte[16];
        RANDOM.nextBytes(saltArray);
        return saltArray;
    }
    
	private String psswrd;
	
	//in the constructor we check all we want and throw personalized exceptions as required
    public Psswrd(String psswrd) {
        if (psswrd.length() <= 5) {
            throw new PasswordNotValidException("Password should have more than 5 characters", psswrd);
        }
        if (!psswrd.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,}$")) {
            throw new PasswordNotValidException("Password should have at least one number, one lowercase letter, one uppercase letter, one special character and no spaces", psswrd);
        }
        this.psswrd = psswrd;
    }
   

}
