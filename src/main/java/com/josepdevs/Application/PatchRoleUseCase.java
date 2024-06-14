package com.josepdevs.Application;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.service.GetUserFromTokenIdService;
import com.josepdevs.Domain.service.GetUserFromTokenUsernameService;
import com.josepdevs.Domain.service.JwtTokenDataExtractorService;
import com.josepdevs.Domain.service.JwtTokenValidations;
import com.josepdevs.Infra.output.AuthJpaRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PatchRoleUseCase {
	
	private final AuthJpaRepository repository;
	private final Logger logger = LoggerFactory.getLogger(PatchRoleUseCase.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;
	private final GetUserFromTokenIdService getUserFromTokenIdService;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final JwtTokenValidations jwtValidations;
	
	public boolean patchRole(String jwtToken, UUID id, String role) {
		
		//check if user declared in token exists
		String username = jwtReaderService.extractUsername(jwtToken);
		AuthenticationData existingAdmin = getUserFromTokenUsernameService.getUserFromTokenUsername(username);
		AuthenticationData existingUserToBeChanged = getUserFromTokenIdService.getUserFromTokenId(id);
		boolean isAdminOrExceptionWasThrown = jwtValidations.isAdminTokenCompletelyValidated(jwtToken);
		
		if( ! isAdminOrExceptionWasThrown ){
			//This should not be executed, since checking for admin in the token throws exception, 
			logger.error("The token's user was not admin, as it was expected.");
			return false;

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
