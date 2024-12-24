package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.RegisterAuthenticationDataUseCaseImpl;
import com.josepedevs.domain.exceptions.BusyOrDownServerException;
import com.josepedevs.domain.request.RegisterRequest;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/noauth")
@RequiredArgsConstructor
@Slf4j
public class RestRegisterController {
	
	private final RegisterAuthenticationDataUseCaseImpl registerAuthenticationDataUseCaseImplUseCase;

	@PostMapping("/register")
	@Retry(name="registerRT",fallbackMethod="registerRetry")
	public ResponseEntity<UUID> register (@RequestBody RegisterRequest request){
		log.info("Registering user:"+request.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(registerAuthenticationDataUseCaseImplUseCase.apply(request));
	}

	public ResponseEntity<String> registerRetry (BusyOrDownServerException exception){
        log.info("Registration failed, retry to login will be done.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("The server cannot provide a response at the moment to your request. Please try again in some minutes ");

	}
}
