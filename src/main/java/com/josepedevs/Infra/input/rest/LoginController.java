package com.josepedevs.Infra.input.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import com.josepedevs.Application.Login;
import com.josepedevs.Domain.dto.AuthenticationRequest;
import com.josepedevs.Domain.dto.AuthenticationResponse;
import com.josepedevs.Domain.exceptions.BusyOrDownServerException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

@RestController

@RequestMapping("api/v1/noauth")

@RequiredArgsConstructor
public class LoginController {

	private final Login loginUseCase;
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@PostMapping("/authenticate")
	@Retry(name="loginRT",fallbackMethod="loginRetry")
	@RateLimiter(name="loginRL",fallbackMethod="loginRateLimiter")
	@CircuitBreaker(name="loginCB",fallbackMethod="loginRateLimiter")
	public ResponseEntity<AuthenticationResponse> login (@RequestBody AuthenticationRequest request){
		logger.info("Logging in user: "+ request.toString());
		return ResponseEntity.ok(loginUseCase.login(request));

	}
	
	public ResponseEntity<String> loginRetry (ResourceAccessException exception){
        logger.info("Login failed, retry to login will be done.");
        return ResponseEntity.status(HttpStatus.PROCESSING)
                .body("The server cannot provide a response at the moment to your request. Please try again in some minutes ");
	}
	
	public ResponseEntity<String> loginRateLimiter (BusyOrDownServerException exception){
		logger.info("Rate limiter of login, server will provide a response as soon as possible.");
		return ResponseEntity.status(HttpStatus.PROCESSING)
				.body("Login in process, please, wait while the server processes your request, this might take some time.");
	}
	
}

