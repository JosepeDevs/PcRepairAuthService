package com.josepedevs;

import com.josepedevs.application.usecase.authenticationdata.LoginAuthenticationDataUseCaseImpl;
import com.josepedevs.infra.input.rest.authenticationdata.RestLoginController;
import com.josepedevs.infra.output.persistence.jpa.postgresql.JpaAuthenticationDataPostgreSqlAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthManagerAppTest {

	@Autowired
	private RestLoginController controller;

	@Autowired
	private LoginAuthenticationDataUseCaseImpl useCase;

	@Autowired
	private JpaAuthenticationDataPostgreSqlAdapter adapter;

	@Test
	void contextLoads() {
		assertNotNull(controller);
		assertNotNull(useCase);
		assertNotNull(adapter);
	}

}
