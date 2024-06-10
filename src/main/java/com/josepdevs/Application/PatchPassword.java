package com.josepdevs.Application;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.UserNotFoundException;
import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.repository.AuthRepository;
import com.josepdevs.Domain.service.JwtTokenDataExtractorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatchPassword {

	private final AuthRepository repository;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final PasswordEncoder passwordEncoder;
	private final Logger logger = LoggerFactory.getLogger(PatchPassword.class);

	public boolean patchPassword(String jwtToken, String newpassword) {
		//here is being throw the error
		String username = jwtReaderService.extractUsername(jwtToken);
		Optional<AuthenticationData> userDataAuth = repository.findByUsername(username); 
		AuthenticationData existingUser = userDataAuth.orElseThrow( () -> {
			logger.error("User was not found with username: "+username);
			throw new UserNotFoundException("ha intentado cambiuar la contraseña de un usuario que "
				+ "no existe o el token con las credenciales no lo contenia.", "username");
			} 
		);	
		String hashedPassword = passwordEncoder.encode(newpassword);
		boolean isUserSavedWithChanges = repository.patchPassword(existingUser, hashedPassword);
		if( isUserSavedWithChanges ) {
			logger.info("User updated password correctly.");
			return true;
		} else { 
			logger.info("User could not update password or no changes were detected");
			return false;
		}
	}
	

}
