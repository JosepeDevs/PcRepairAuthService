package com.josepedevs.Application;

import java.util.UUID;

import com.josepedevs.Domain.dto.AuthDataMapper;
import com.josepedevs.Domain.dto.UpdateRoleRequest;
import com.josepedevs.Domain.repository.AuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.josepedevs.Domain.entities.AuthenticationData;
import com.josepedevs.Domain.service.GetUserFromTokenIdService;
import com.josepedevs.Domain.service.GetUserFromTokenUsernameService;
import com.josepedevs.Domain.service.JwtTokenDataExtractorService;
import com.josepedevs.Domain.service.JwtTokenValidations;
import com.josepedevs.Infra.output.AuthJpaRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PatchRoleUseCase {
	
	private final AuthRepository repository;
	private final Logger logger = LoggerFactory.getLogger(PatchRoleUseCase.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;
	private final GetUserFromTokenIdService getUserFromTokenIdService;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final JwtTokenValidations jwtValidations;
	
	public boolean patchRole(String jwtToken, UpdateRoleRequest request) {

		//Using MAPSTRUCT we map from request class to domain class
		AuthenticationData authData = AuthDataMapper.INSTANCE.mapToAuthData(request);

		UUID id = authData.getIdUser();
		String role = authData.getRole();

		//check if user declared in token exists
		String username = jwtReaderService.extractUsername(jwtToken);
		AuthenticationData existingUserToBeChanged = getUserFromTokenIdService.getUserFromTokenId(id);
		boolean isAdminOrExceptionWillBeThrown = jwtValidations.isAdminTokenCompletelyValidated(jwtToken);
		
		if( ! isAdminOrExceptionWillBeThrown ){
			//This should not be executed, since checking for admin and finding that is not an admin will  throw an exception,
			logger.error("The token's user was not admin, as it was expected.");
			return false;

		} else {
			
			String rolInicial = existingUserToBeChanged.getRole();
			existingUserToBeChanged.setRole(role);
			AuthenticationData savedUser = repository.registerUserAuthData(existingUserToBeChanged, jwtToken);
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
