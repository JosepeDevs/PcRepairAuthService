package com.josepedevs.domain.entity.valueobjects;

import com.josepedevs.domain.exceptions.PasswordNotValidException;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Psswrd {
		
	private String psswrd;
	
	//in the constructor we check all we want and throw personalized exceptions as required
    public Psswrd(String psswrd) {
        if (psswrd.length() <= 5) {
            throw new PasswordNotValidException("Password should have more than 5 characters", "Password");
        }
        if (!psswrd.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,}$")) {
            throw new PasswordNotValidException("Password should have at least one number, "
            		+ "one lowercase letter, one uppercase letter, one special character and no spaces", "Password");
        }
        this.psswrd = psswrd;
    }
   

}
