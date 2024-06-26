package com.josepedevs.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.josepedevs.Domain.dto.AuthenticationRequest;
import com.josepedevs.Domain.dto.AuthenticationResponse;
import com.josepedevs.Domain.entities.AuthenticationData;
import com.josepedevs.Domain.exceptions.TokenNotValidException;
import com.josepedevs.Domain.repository.AuthRepository;
import com.josepedevs.Domain.service.GetUserFromTokenUsernameService;
import com.josepedevs.Domain.service.JwtTokenIssuerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Login {

	private final AuthenticationManager authenticationManager;
	private final AuthRepository repository;
	private final JwtTokenIssuerService jwtService;
	private final Logger logger = LoggerFactory.getLogger(Login.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;


	public AuthenticationResponse login(AuthenticationRequest request) {
		String username = request.getUsername();
    	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPsswrd()));
		AuthenticationData userDataAuth = getUserFromTokenUsernameService.getUserFromTokenUsername(username);
	    
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

