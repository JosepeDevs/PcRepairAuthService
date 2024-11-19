package com.josepedevs.application.usecase.user;

import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenIssuerService;
import com.josepedevs.domain.exceptions.TokenNotValidException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.AuthenticationRequest;
import com.josepedevs.domain.request.AuthenticationResponse;
import com.josepedevs.domain.usecase.LoginUserUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginUserUseCaseImpl implements LoginUserUseCase {

	private final AuthenticationManager authenticationManager;
	private final AuthenticationDataRepository repository;
	private final JwtTokenIssuerService jwtService;
	private final Logger logger = LoggerFactory.getLogger(LoginUserUseCaseImpl.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;


	public AuthenticationResponse apply(AuthenticationRequest request) {
		String username = request.getUsername();
    	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPsswrd()));
		final var userDataAuth = getUserFromTokenUsernameService.getUserFromTokenUsername(username);
	    
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

