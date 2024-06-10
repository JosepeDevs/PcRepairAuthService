package com.josepdevs.Domain.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.UserNotFoundException;
import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.repository.AuthRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtTokenValidations {
	
	JwtTokenDataExtractorService jwtTokenDataExtractorService;
	private final AuthRepository repository;

	/**
	 * Gets the expiration of the token and checks current time to return true if is still valid
	 * @param jwtToken
	 * @return boolean : true if is valid, false if not.
	 */
	public boolean isTokenExpired(String jwtToken) {
		return jwtTokenDataExtractorService.extractExpiration(jwtToken).before(new Date());
	}
	
	/**
	 * Extracts the user for which it was issued, if not exists throws error, if exists and its token is invalidated returns true, if token is still valid returns FALSE
	 * @param jwtToken
	 * @return boolean : true if the token was invalidated, FALSE IF token is STILL VALID.
	 */
	public boolean isTokenInvalidated(String jwtToken) {
		String username = jwtTokenDataExtractorService.extractUsername(jwtToken);
		Optional<AuthenticationData> user = repository.findByUsername(username);
		AuthenticationData existingUser = user.orElseThrow( () ->  new UserNotFoundException("user was not found", "Username"));
		String token = existingUser.getCurrentToken();
		if(token == "invalid" || token == null) {
			return true;
		} else {
			return false;
		}
	}
	
	

	
	/**
	 * check that user details in token is equal to userDetails and that the token is not expired
	 * @param jwtToken
	 * @param userdetails
	 * @return
	 */
	public boolean isTokenUserDataValidAndNotExpired(String jwtToken, UserDetails userDetails) {
		final String username = jwtTokenDataExtractorService.extractUsername(jwtToken);
		return (username.equals(userDetails.getUsername()) &&  ! isTokenExpired(jwtToken) );
	}

	

}
