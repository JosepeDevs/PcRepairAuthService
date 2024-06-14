package com.josepdevs.Application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.repository.AuthRepository;
import com.josepdevs.Domain.service.GetUserFromTokenUsernameService;
import com.josepdevs.Domain.service.JwtTokenDataExtractorService;
import com.josepdevs.Domain.service.JwtTokenValidations;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAllRegisteredUseCase {

	private final AuthRepository repository;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final JwtTokenValidations jwtValidations;
	private final Logger logger = LoggerFactory.getLogger(GetAllRegisteredUseCase.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;

	public List<AuthenticationData> getAll(String jwtToken) {
		
		String username = jwtReaderService.extractUsername(jwtToken);
		AuthenticationData user = getUserFromTokenUsernameService.getUserFromTokenUsername(username);
		boolean isUserAnAdmin = jwtValidations.isAdminTokenCompletelyValidated(jwtToken);
		
		if(isUserAnAdmin){
			logger.info("Returning all authentication data.");
			return repository.getAll();
		} else {
			//anyway if not admin it will throw exception
			List<AuthenticationData> empty = List.of();
			return empty;
		}
	}
}
