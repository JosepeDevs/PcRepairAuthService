package com.josepdevs.Application;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.InadequateRoleException;
import com.josepdevs.Domain.Exceptions.UserNotFoundException;
import com.josepdevs.Domain.dto.valueobjects.Role;
import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.service.JwtTokenDataExtractorService;
import com.josepdevs.Infra.output.AuthJpaRepository;

import lombok.AllArgsConstructor;
/*
@AllArgsConstructor
@Service
public class PatchRoleUseCaseTest {
	
	private final AuthJpaRepository repository;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final Logger logger = LoggerFactory.getLogger(PatchRoleUseCase.class);

	public boolean patchRole(String jwtToken, UUID id, String role) {
		
		//check if user declared in token exists
		String username = jwtReaderService.extractUsername(jwtToken);
		Optional<AuthenticationData> authenticatedAdmin = repository.findByUsername(username); 
		AuthenticationData existingAdmin = authenticatedAdmin.orElseThrow( () ->
		new UserNotFoundException("You tried to access a user that was missing or not found in our systems.", "id") );	
		
		//check if user to be changed exists
		Optional<AuthenticationData> userToBeChanged = repository.findById(id); 
		AuthenticationData existingUserToBeChanged = userToBeChanged.orElseThrow( () -> {
            logger.error("Failed to find user with that id");
            throw new UserNotFoundException("You tried to access a user that was missing or not found in our systems.", "id");
        });

		//check if the token is from an ADMIN
		if(! existingAdmin.getRole().toString().equals("ADMIN")){
            logger.error("The role of the authenticated user was required to be admin, found: "+existingAdmin.getRole().toString());
			throw new InadequateRoleException("You do not have the required authority to access this resource.", "existingAdmin");
		} else {
			String rolInicial = existingUserToBeChanged.getRole();
			existingUserToBeChanged.setRole(role);
			AuthenticationData savedUser = repository.save(existingUserToBeChanged);
			if(rolInicial == savedUser.getRole()) {
	            logger.info("The role was not updated ");
				return false;
			} else {
				logger.info("Role updated correctly");
				return true;
			}
		}
	}
	
}
*/