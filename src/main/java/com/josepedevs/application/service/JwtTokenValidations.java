package com.josepedevs.application.service;

import com.josepedevs.domain.exceptions.InadequateRoleException;
import com.josepedevs.domain.exceptions.TokenNotValidException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@AllArgsConstructor
public class JwtTokenValidations {
	
	private final JwtTokenDataExtractorService jwtTokenDataExtractorService;
	private final Logger logger = LoggerFactory.getLogger(JwtTokenValidations.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;

	/**
	 * Gets the expiration of the token and checks current time to return true if is still valid
	 * @param jwtToken
	 * @return boolean : true if is valid, false if not.
	 */
	public boolean isTokenExpired(String jwtToken) {
		if(jwtTokenDataExtractorService.extractExpiration(jwtToken).before(new Date())){
			throw new ExpiredJwtException(null, jwtTokenDataExtractorService.extractAllClaims(jwtToken), "The token was expired.");
		} else {
			return false;
		}
		
	}
	
	/**
	 * Extracts the user for which it was issued, if not exists throws error, if exists and its token is invalidated returns true, if token is still valid returns FALSE
	 * @param jwtToken
	 * @return boolean : true if the token was invalidated, FALSE IF token is STILL VALID.
	 */
	public boolean isTokenInvalidated(String jwtToken) {
		String username = jwtTokenDataExtractorService.extractUsername(jwtToken);
		final var user = getUserFromTokenUsernameService.getUserFromTokenUsername(username);
		String token = user.getCurrentToken();
		if(token == "invalid" || token == null) {
			throw new TokenNotValidException("Your token was invalidated and access was not granted", "Authentication token");
		} else {
			return false;
		}
	}
	
	/**
	 * check if token belongs to an admin
	 * @param jwtToken
	 * @return
	 */
	public boolean isTokenFromAnAdmin(String jwtToken) {
		final String username = jwtTokenDataExtractorService.extractUsername(jwtToken);
		final var user = getUserFromTokenUsernameService.getUserFromTokenUsername(username);
		if ( user.getRole().equals("ADMIN") ){
			return true;
		} else {
			throw new InadequateRoleException("You do not have the required authority to access this resource.", "Role");
		}
	}

	
	/**
	 * check that user details in token is equal to userDetails and that the token is not expired
	 * @param jwtToken
	 * @return
	 */
	public boolean isUserTokenCompletelyValidated(String jwtToken, UserDetails userDetails) {
		final String username = jwtTokenDataExtractorService.extractUsername(jwtToken);
		return (username.equals(userDetails.getUsername()) &&  ! isTokenExpired(jwtToken) );
	}

	public boolean isAdminTokenCompletelyValidated(String jwtToken) {
		Map<String,Object> rolesAndPermissions = jwtTokenDataExtractorService.extractRolesAndPermissions(jwtToken);
		logger.trace("Extracted data from token, it contained: "+ rolesAndPermissions.toString());
		final var isTokenInvalidated  = isTokenInvalidated(jwtToken);
		final var isTokenExpired = isTokenExpired(jwtToken);
		final var isTokenFromAdmin = isTokenFromAnAdmin(jwtToken);
		return ( ! isTokenInvalidated && ! isTokenExpired && isTokenFromAdmin );
	}

}
