package com.josepedevs.application.usecase.user;

import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.DeleteHardUserRequest;
import com.josepedevs.domain.usecase.DeleteHardUserUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteHardUseCaseImpl implements DeleteHardUserUseCase {
	
	private final AuthenticationDataRepository repository;
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final Logger logger = LoggerFactory.getLogger(PatchUserPasswordUseCaseImpl.class);

	@Override
	public Boolean apply(DeleteHardUserRequest deleteHardUserRequest) {

		boolean wasDeletedCorrectly = repository.deleteHard(deleteHardUserRequest.getUserId());
		
		if( wasDeletedCorrectly ) {
			logger.info("User was deleted correctly.");
			return true;
		} else { 
			logger.info("User could not be deleted");
			return false;
		}
	}

}
