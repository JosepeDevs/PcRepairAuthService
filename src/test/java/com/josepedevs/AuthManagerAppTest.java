package com.josepedevs;

import com.josepedevs.application.usecase.authenticationdata.LoginAuthenticationDataUseCaseImpl;
import com.josepedevs.infra.input.rest.authenticationdata.RestLoginController;
import com.josepedevs.infra.output.persistence.jpa.postgresql.JpaAuthenticationDataPostgreSqlAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthManagerAppTest {

	@Autowired
	private RestLoginController controler;

	@Autowired
	private LoginAuthenticationDataUseCaseImpl useCase;

	@Autowired
	private JpaAuthenticationDataPostgreSqlAdapter adapter;

	@Test
	void contextLoads() {
		assertNotNull(controler);
		assertNotNull(useCase);
		assertNotNull(adapter);
	}

}
