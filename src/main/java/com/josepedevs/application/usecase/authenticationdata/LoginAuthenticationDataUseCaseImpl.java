package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.AuthDataFinder;
import com.josepedevs.application.service.JwtIssuerService;
import com.josepedevs.application.service.JwtRoleValidator;
import com.josepedevs.domain.exceptions.TokenNotValidException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.AuthenticationRequest;
import com.josepedevs.domain.request.AuthenticationResponse;
import com.josepedevs.domain.usecase.LoginAuthenticationDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginAuthenticationDataUseCaseImpl implements LoginAuthenticationDataUseCase {

	private final AuthenticationManager authenticationManager;
	private final AuthenticationDataRepository repository;
	private final JwtIssuerService jwtService;
	private final AuthDataFinder authDataFinder;

	public AuthenticationResponse apply(AuthenticationRequest request) {

		final var  username = request.getUsername();
		final var newPasswordAuthToken = new UsernamePasswordAuthenticationToken(username, request.getPsswrd());
    	authenticationManager.authenticate(newPasswordAuthToken);
		final var userDataAuth = authDataFinder.findByUsername(username);
	    
	    Map<String, Object> extraClaims = new HashMap<>();
	    extraClaims.put("authorities", userDataAuth.getAuthorities()); 

	    var jwtToken = jwtService.generateToken(extraClaims, userDataAuth);
	    boolean success = repository.login(userDataAuth, jwtToken);
	    if( ! success) {
	    	log.error("The token was not saved correctly to currentToken");
	    	throw new TokenNotValidException("The generated token could not be saved to the user or the login failed for other reason");
	    }        	
    	log.trace("Returning token.");

	    return AuthenticationResponse.builder()
	           .token(jwtToken)
	           .build();
	}
}

