package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.GetUserFromTokenIdService;
import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.application.service.JwtTokenValidations;
import com.josepedevs.domain.mapper.AuthDataMapper;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.PatchUserRoleRequest;
import com.josepedevs.domain.usecase.PatchAuthenticationDataRoleUseCase;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PatchAuthenticationDataRoleUseCaseImpl implements PatchAuthenticationDataRoleUseCase {

	private final AuthenticationDataRepository repository;
	private final Logger logger = LoggerFactory.getLogger(PatchAuthenticationDataRoleUseCaseImpl.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;
	private final GetUserFromTokenIdService getUserFromTokenIdService;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final JwtTokenValidations jwtValidations;
	private final AuthDataMapper authDataMapper;

	@Override
	public Boolean apply(PatchUserRoleRequest patchUserRoleRequest) {

		final var  authData = authDataMapper.map(patchUserRoleRequest.getUpdateRoleRequest());

		final var id = authData.getIdUser();
		final var role = authData.getRole();

		//check if user declared in token exists
		final var existingUserToBeChanged = getUserFromTokenIdService.getUserFromTokenId(id);
		boolean isAdminOrExceptionWillBeThrown = jwtValidations.isAdminTokenCompletelyValidated(patchUserRoleRequest.getJwtToken());

		if( ! isAdminOrExceptionWillBeThrown ){
			//This should not be executed, since checking for admin and finding that is not an admin will  throw an exception,
			logger.error("The token's user was not admin, as it was expected.");
			return false;

		} else {

			final var rolInicial = existingUserToBeChanged.getRole();
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
