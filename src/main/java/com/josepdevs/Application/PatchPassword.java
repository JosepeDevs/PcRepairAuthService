package com.josepdevs.Application;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.dto.AuthenticationData;
import com.josepdevs.Domain.dto.AuthenticationRequest;
import com.josepdevs.Domain.service.JwtTokenReaderAndIssuerService;
import com.josepdevs.Infra.output.AuthJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatchPassword {
/*
	private final AuthenticationManager authenticationManager;
	private final AuthJpaRepository repository;
	private final JwtTokenReaderAndIssuerService jwtService;
	private final PasswordEncoder passwordEncoder;
	
	public String patchPassword(String newpassword) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPsswrd()));
		//if we arrive here the previous line is correct and the user got the correct password
		Optional<AuthenticationData> userDataAuth = repository.findByUsername(request.getUsername()).orElseThrow(); //todo que va a thorwear esto??
		userDataAuth.set
	}
	*/
	
}
