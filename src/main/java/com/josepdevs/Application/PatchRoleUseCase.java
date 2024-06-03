package com.josepdevs.Application;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.InadequateRoleException;
import com.josepdevs.Domain.Exceptions.UserNotFoundException;
import com.josepdevs.Domain.dto.valueobjects.Role;
import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.service.JwtTokenReaderService;
import com.josepdevs.Infra.output.AuthJpaRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PatchRoleUseCase {
	
	private final AuthJpaRepository repository;
	private final JwtTokenReaderService jwtReaderService;
	
	public boolean patchRole(String jwtToken, UUID id, String role) {
		
		//check if user declared in token exists
		String username = jwtReaderService.extractUsername(jwtToken);
		Optional<AuthenticationData> authenticatedAdmin = repository.findByUsername(username); 
		AuthenticationData existingAdmin = authenticatedAdmin.orElseThrow( () ->
		new UserNotFoundException("You tried to access a user that was missing or not found in our systems.", id.toString()) );	
		
		//check if user to be changed exists
		Optional<AuthenticationData> userToBeChanged = repository.findById(id); 
		AuthenticationData existingUserToBeChanged = userToBeChanged.orElseThrow( () ->
		new UserNotFoundException("You tried to access a user that was missing or not found in our systems.", username) );	

		//check if the token is from an ADMIN
		if(! existingAdmin.getRole().toString().equals("ADMIN")){
			throw new InadequateRoleException("You do not have the required authority to access this resource.", existingAdmin.getRole());
		} else {
			String rolInicial = existingUserToBeChanged.getRole();
			existingUserToBeChanged.setRole(role);
			AuthenticationData savedUser = repository.save(existingUserToBeChanged);
			if(rolInicial == savedUser.getRole()) {
				return false;
			} else {
				return true;
			}
		}
	}
	
}
