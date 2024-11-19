package com.josepedevs.application.usecase.user;

import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.application.service.JwtTokenValidations;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.usecase.GetAllUsersUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllusersUseCaseImpl implements GetAllUsersUseCase {

	private final AuthenticationDataRepository repository;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final JwtTokenValidations jwtValidations;
	private final Logger logger = LoggerFactory.getLogger(GetAllusersUseCaseImpl.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;

	public List<AuthenticationData> apply(String jwtToken) {
		
		String username = jwtReaderService.extractUsername(jwtToken);
		final var user = getUserFromTokenUsernameService.getUserFromTokenUsername(username);
		boolean isUserAnAdmin = jwtValidations.isAdminTokenCompletelyValidated(jwtToken);
		
		if(isUserAnAdmin){
			logger.info("Returning all authentication data.");
			return repository.getAll();
		} else {
			//anyway if not admin it will throw exception
            return List.of();
		}
	}
}
