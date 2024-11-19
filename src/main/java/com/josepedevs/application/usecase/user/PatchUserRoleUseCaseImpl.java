package com.josepedevs.application.usecase.user;

import com.josepedevs.application.service.GetUserFromTokenIdService;
import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.application.service.JwtTokenValidations;
import com.josepedevs.domain.entity.AuthDataMapper;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.PatchUserRoleRequest;
import com.josepedevs.domain.usecase.PatchUserRoleUseCase;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class PatchUserRoleUseCaseImpl implements PatchUserRoleUseCase {

	private final AuthenticationDataRepository repository;
	private final Logger logger = LoggerFactory.getLogger(PatchUserRoleUseCaseImpl.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;
	private final GetUserFromTokenIdService getUserFromTokenIdService;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final JwtTokenValidations jwtValidations;

	@Override
	public Boolean apply(PatchUserRoleRequest patchUserRoleRequest) {

		//Using MAPSTRUCT we map from request class to domain class
		AuthenticationDataEntity authData = AuthDataMapper.INSTANCE.mapToAuthData(patchUserRoleRequest.getUpdateRoleRequest());

		UUID id = authData.getIdUser();
		String role = authData.getRole();

		//check if user declared in token exists
		String username = jwtReaderService.extractUsername(patchUserRoleRequest.getJwtToken());
		final var existingUserToBeChanged = getUserFromTokenIdService.getUserFromTokenId(id);
		boolean isAdminOrExceptionWillBeThrown = jwtValidations.isAdminTokenCompletelyValidated(patchUserRoleRequest.getJwtToken());

		if( ! isAdminOrExceptionWillBeThrown ){
			//This should not be executed, since checking for admin and finding that is not an admin will  throw an exception,
			logger.error("The token's user was not admin, as it was expected.");
			return false;

		} else {

			String rolInicial = existingUserToBeChanged.getRole();
			existingUserToBeChanged.setRole(role);
			final var savedUser = repository.registerUserAuthData(existingUserToBeChanged, patchUserRoleRequest.getJwtToken());
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
