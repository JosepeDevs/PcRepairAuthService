package com.josepdevs.Application;

import com.josepedevs.Application.PatchRoleUseCase;
import com.josepedevs.Domain.dto.AuthDataMapper;
import com.josepedevs.Domain.dto.UpdateRoleRequest;
import com.josepedevs.Domain.entities.AuthenticationData;
import com.josepedevs.Domain.repository.AuthRepository;
import com.josepedevs.Domain.service.GetUserFromTokenIdService;
import com.josepedevs.Domain.service.GetUserFromTokenUsernameService;
import com.josepedevs.Domain.service.JwtTokenDataExtractorService;
import com.josepedevs.Domain.service.JwtTokenValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatchRoleUseCaseTest {

    AuthRepository repository = mock(AuthRepository.class);
    GetUserFromTokenUsernameService getUserFromTokenUsernameService = mock(GetUserFromTokenUsernameService.class);
    GetUserFromTokenIdService getUserFromTokenIdService = mock(GetUserFromTokenIdService.class);
    JwtTokenDataExtractorService jwtReaderService = mock(JwtTokenDataExtractorService.class);
    JwtTokenValidations jwtValidations = mock(JwtTokenValidations.class);

    @InjectMocks
    PatchRoleUseCase useCase;

    @Test
    void pathRole_ShouldUpdateRoleCorrectly() {

        String jwtToken = "jwtToken";
        String roleToBeSetted = "EDITOR";
        UUID idUser = UUID.randomUUID();
        UUID idAdmin = UUID.randomUUID();
        boolean tokenIsFromAdmin = true;

        UpdateRoleRequest request = UpdateRoleRequest.builder().id(idUser.toString()).role(roleToBeSetted).build();
        ;
        AuthenticationData loggedAdmin = AuthenticationData.builder().role("ADMIN").idUser(idAdmin).username("usernameOfAnAdmin").build();
        AuthenticationData userToBeModified = AuthenticationData.builder().role("USER").idUser(idUser).username("usernameOfUserToBeModified").build();


        AuthenticationData authData = AuthDataMapper.INSTANCE.mapToAuthData(request);

        when(jwtReaderService.extractUsername(jwtToken)).thenReturn(loggedAdmin.getRole());
        when(getUserFromTokenIdService.getUserFromTokenId(idUser)).thenReturn(userToBeModified);
        when(jwtValidations.isAdminTokenCompletelyValidated(jwtToken)).thenReturn(tokenIsFromAdmin);
        when(repository.registerUserAuthData(userToBeModified, jwtToken)).thenReturn(userToBeModified);

        when(repository.patchRole(userToBeModified, roleToBeSetted)).thenReturn(true);

        boolean finalResult = useCase.patchRole( jwtToken,request);
        assertNotNull( authData );//check mapstruct is working
        assertThat( authData.getIdUser() ).isEqualTo(idUser );//check mapstruct is working
        assertTrue( finalResult );
        assertThat( userToBeModified.getRole() ).isEqualTo(roleToBeSetted );

    }

    @Test
    void pathRole_ShouldRejectUpdateIfLoggedTokenIsNotFromAdmin() {

        String jwtToken = "jwtToken";
        String roleToBeSetted = "EDITOR";
        UUID idUser = UUID.randomUUID();
        UUID idAdmin = UUID.randomUUID();
        boolean tokenIsFromAdmin = false;

        UpdateRoleRequest request = UpdateRoleRequest.builder().id(idUser.toString()).role(roleToBeSetted).build();

        AuthenticationData loggedAdmin = AuthenticationData.builder().role("ADMIN").idUser(idAdmin).username("usernameOfAnAdmin").build();
        AuthenticationData userToBeModified = AuthenticationData.builder().role("USER").idUser(idUser).username("usernameOfUserToBeModified").build();

        //Actual call to mapstruct to map (and assert results and mapping is being done correctly)
        AuthenticationData authData = AuthDataMapper.INSTANCE.mapToAuthData(request);

        when(jwtReaderService.extractUsername(jwtToken)).thenReturn(loggedAdmin.getRole());
        when(getUserFromTokenIdService.getUserFromTokenId(idUser)).thenReturn(userToBeModified);
        when(jwtValidations.isAdminTokenCompletelyValidated(jwtToken)).thenReturn(tokenIsFromAdmin);
        when(repository.registerUserAuthData(userToBeModified, jwtToken)).thenReturn(userToBeModified);

        when(repository.patchRole(userToBeModified, roleToBeSetted)).thenReturn(true);

        boolean finalResult = useCase.patchRole( jwtToken,request);

        assertNotNull( authData );//check mapstruct is working
        assertThat( authData.getIdUser() ).isEqualTo(idUser ); //check mapstruct is working
        assertFalse( finalResult );

    }

}