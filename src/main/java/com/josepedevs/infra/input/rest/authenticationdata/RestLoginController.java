package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.LoginAuthenticationDataUseCaseImpl;
import com.josepedevs.domain.exceptions.BusyOrDownServerException;
import com.josepedevs.domain.request.AuthenticationRequest;
import com.josepedevs.domain.request.AuthenticationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

@RestController

@RequestMapping("api/v1/noauth")

@RequiredArgsConstructor
@Slf4j
public class RestLoginController {

	private final LoginAuthenticationDataUseCaseImpl loginAuthenticationDataUseCaseImplUseCase;

	@PostMapping("/authenticate")
	@Retry(name="loginRT",fallbackMethod="loginRetry")
	@RateLimiter(name="loginRL",fallbackMethod="loginRateLimiter")
	@CircuitBreaker(name="loginCB",fallbackMethod="loginRateLimiter")
	public ResponseEntity<AuthenticationResponse> login (@RequestBody AuthenticationRequest request){
		log.info("Logging in user: "+ request.toString());
		return ResponseEntity.ok(loginAuthenticationDataUseCaseImplUseCase.apply(request));
	}
	
	public ResponseEntity<String> loginRetry (ResourceAccessException exception){
        log.info("Login failed, retry to login will be done.");
        return ResponseEntity.status(HttpStatus.PROCESSING)
                .body("The server cannot provide a response at the moment to your request. Please try again in some minutes ");
	}
	
	public ResponseEntity<String> loginRateLimiter (BusyOrDownServerException exception){
		log.info("Rate limiter of login, server will provide a response as soon as possible.");
		return ResponseEntity.status(HttpStatus.PROCESSING)
				.body("Login in process, please, wait while the server processes your request, this might take some time.");
	}
	
}

