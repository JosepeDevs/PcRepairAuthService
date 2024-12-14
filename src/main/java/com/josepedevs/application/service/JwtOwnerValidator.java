package com.josepedevs.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class JwtOwnerValidator {
	
	private final JwtDataExtractorService jwtDataExtractorService;

	/**
	 * check that user details in token is equal to userDetails and that the token is not expired
	 * @param jwtToken bearer token string
	 * @return boolean : true if is valid, false if not
	 */
	public boolean isTokenOwnerValid(String jwtToken, UserDetails userDetails) {
		return (jwtDataExtractorService.extractUsername(jwtToken).equals(userDetails.getUsername()));
	}

}
