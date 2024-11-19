package com.josepedevs.infra.input.rest.user;

import com.josepedevs.application.usecase.user.RegisterUserUseCaseImpl;
import com.josepedevs.domain.exceptions.BusyOrDownServerException;
import com.josepedevs.domain.request.RegisterRequest;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class RegisterController {
	
	private final RegisterUserUseCaseImpl registerUserUseCaseImplUseCase;
	private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

	@PostMapping("/register")
	@Retry(name="registerRT",fallbackMethod="registerRetry")
	public ResponseEntity<UUID> register (@RequestBody RegisterRequest request){
		logger.info("Registering user:"+request.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(registerUserUseCaseImplUseCase.apply(request));
	}

	
	public ResponseEntity<String> registerRetry (BusyOrDownServerException exception){
        logger.info("Registration failed, retry to login will be done.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("The server cannot provide a response at the moment to your request. Please try again in some minutes ");

	}
}
