package com.josepedevs.application.config;

import com.josepedevs.domain.repository.AuthenticationDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @InjectMocks
    private ApplicationConfig config;

    @Mock
    private AuthenticationDataRepository repository;

    @Test
    void testUserDetailsServiceBeanNotNull() {
        UserDetailsService bean = config.userDetailsService();
        assertNotNull(bean);
    }

    @Test
    void testPasswordEncoderBeanNotNull() {
        PasswordEncoder bean = config.passwordEncoder();
        assertNotNull(bean);
    }

    @Test
    void testAuthenticationProviderBeanNotNull() {
        AuthenticationProvider bean = config.authenticationProvider();
        assertNotNull(bean);
    }

}