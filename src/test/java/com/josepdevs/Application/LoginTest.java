package com.josepdevs.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.TokenNotValidException;
import com.josepdevs.Domain.dto.AuthenticationRequest;
import com.josepdevs.Domain.dto.AuthenticationResponse;
import com.josepdevs.Domain.repository.AuthRepository;
import com.josepdevs.Domain.service.JwtTokenIssuerService;

import lombok.RequiredArgsConstructor;
/*
@Service
@RequiredArgsConstructor
public class LoginTest {

	private final AuthenticationManager authenticationManager;
	private final AuthRepository repository;
	private final JwtTokenIssuerService jwtService;
	private final Logger logger = LoggerFactory.getLogger(Login.class);

	public AuthenticationResponse login(AuthenticationRequest request) {
    	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPsswrd()));
	    var userDataAuth = repository.findByUsername(request.getUsername())
    		.orElseThrow( () -> {
			    logger.error("username was not found in our database: "+ request.getUsername());
			    throw new NoSuchElementException("The element was not found");
    		}
			);
	    
	    Map<String, Object> extraClaims = new HashMap<>();
	    extraClaims.put("authorities", userDataAuth.getAuthorities()); 

	    var jwtToken = jwtService.generateToken(extraClaims, userDataAuth);
	    //update current token to this
	    boolean success = repository.login(userDataAuth, jwtToken);
	    if( ! success) {
	    	logger.error("The token was not saved correctly to currentToken");
	    	throw new TokenNotValidException("The generated token could not be saved to the user or the login failed for other reason", "TokenNotValidException");
	    }        	
    	logger.trace("Returning token");

	    return AuthenticationResponse.builder()
	           .token(jwtToken)
	           .build();
	}
}

*/