package com.josepdevs.Application;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.InadequateRoleException;
import com.josepdevs.Domain.Exceptions.TokenNotValidException;
import com.josepdevs.Domain.Exceptions.UserNotFoundException;
import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.repository.AuthRepository;
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

	public List<AuthenticationData> getAll(String jwtToken) {
		
		String username = jwtReaderService.extractUsername(jwtToken);
		Optional<AuthenticationData> userDataAuth = repository.findByUsername(username); 
		AuthenticationData existingUser = userDataAuth.orElseThrow( () -> {
			logger.error("The user that was searched by username was not found");
			throw new UserNotFoundException("The user was not found or the token does not containe the required data.", "Username");	
		});
		
		if(existingUser.getRole().toString().equals("ADMIN")){
			logger.info("Returning all authentication data");
			return repository.getAll();
		} else {
			logger.info("The role of the authenticated user was not correct and no return is made.");
			throw new InadequateRoleException("You do not have the required authority to access this resource.", "Role");
		}
	}
}
