package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.AuthDataFinder;
import com.josepedevs.application.service.JwtMasterValidator;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.TokenNotValidException;
import com.josepedevs.domain.mapper.AuthDataMapper;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.PatchUserRoleRequest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatchAuthenticationDataRoleUseCaseImplTest {

    @InjectMocks
    private PatchAuthenticationDataRoleUseCaseImpl useCase;
    @Mock
    private  AuthenticationDataRepository repository;
    @Mock
    private  AuthDataFinder authDataFinder;
    @Mock
    private  JwtMasterValidator jwtMasterValidator;
    @Mock
    private  AuthDataMapper mapper;


    @Test
    void patchRole_ShouldUpdateRoleCorrectly() {

        String jwtToken = "jwtToken";
        String roleToBeSetted = "EDITOR";
        UUID idUser = UUID.randomUUID();

        final var request = PatchUserRoleRequest.builder().jwtToken(jwtToken).id(idUser.toString()).role(roleToBeSetted).build();
        final var userToBeModified = AuthenticationData.builder().role("USER").idUser(idUser).username("usernameOfUserToBeModified").build();
        final var userModified = AuthenticationData.builder().role(roleToBeSetted).idUser(idUser).username("usernameOfUserToBeModified").build();

        when(repository.registerUserAuthData(userToBeModified, jwtToken)).thenReturn(userModified);
        when(authDataFinder.findById(userToBeModified.getIdUser())).thenReturn(userToBeModified);
        when(mapper.map(request)).thenReturn(userToBeModified);
        when(jwtMasterValidator.isAdminTokenCompletelyValidated(jwtToken)).thenReturn(true);

        boolean finalResult = useCase.apply(request);
        assertTrue(finalResult);
    }

    @Test
    void patchRole_ShouldRejectUpdateIfLoggedTokenIsNotFromAdmin() {

        String jwtToken = "jwtToken";
        String roleToBeSetted = "EDITOR";
        UUID idUser = UUID.randomUUID();

        final var request = PatchUserRoleRequest.builder().jwtToken(jwtToken).id(idUser.toString()).role(roleToBeSetted).build();

        when(jwtMasterValidator.isAdminTokenCompletelyValidated(jwtToken)).thenReturn(false);

        // Act & Assert
        assertThrows(TokenNotValidException.class, () -> {
            useCase.apply(request);
        });

    }

}