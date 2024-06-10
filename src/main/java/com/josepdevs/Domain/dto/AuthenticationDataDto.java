package com.josepdevs.Domain.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AuthenticationDataDto {
	 
	private String username;
    private String email;
    private String psswrd;
    private String psswrdSalt;
    private String registrationToken;
    private String psswrdChangeToken;
    private String psswrdChangeIssueDate;
    private String loginToken;
    private boolean active;
    
}
