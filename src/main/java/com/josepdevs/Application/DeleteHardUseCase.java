package com.josepdevs.Application;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.repository.AuthRepository;
import com.josepdevs.Domain.service.GetUserFromTokenUsernameService;
import com.josepdevs.Domain.service.JwtTokenDataExtractorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteHardUseCase {
	
	private final AuthRepository repository;
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final Logger logger = LoggerFactory.getLogger(PatchPassword.class);

	public boolean deleteHard(String jwtToken, UUID userId) {

		boolean wasDeletedCorrectly = repository.deleteHard(userId);
		
		if( wasDeletedCorrectly ) {
			logger.info("User was deleted correctly.");
			return true;
		} else { 
			logger.info("User could not be deleted");
			return false;
		}
	}
	

}
