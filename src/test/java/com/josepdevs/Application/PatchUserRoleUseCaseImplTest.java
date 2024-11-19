package com.josepdevs.Application;

import com.josepedevs.application.service.GetUserFromTokenIdService;
import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.application.service.JwtTokenValidations;
import com.josepedevs.application.usecase.user.PatchUserRoleUseCaseImpl;
import com.josepedevs.domain.entity.AuthDataMapper;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.PatchUserRoleRequest;
import com.josepedevs.domain.request.UpdateRoleRequest;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatchUserRoleUseCaseImplTest {

    private AuthenticationDataRepository repository = mock(AuthenticationDataRepository.class);
    private GetUserFromTokenUsernameService getUserFromTokenUsernameService = mock(GetUserFromTokenUsernameService.class);
    private GetUserFromTokenIdService getUserFromTokenIdService = mock(GetUserFromTokenIdService.class);
    private JwtTokenDataExtractorService jwtReaderService = mock(JwtTokenDataExtractorService.class);
    private JwtTokenValidations jwtValidations = mock(JwtTokenValidations.class);

    private final EasyRandom easyRandom = new EasyRandom();

    @InjectMocks
    private PatchUserRoleUseCaseImpl useCase;

    @Test
    void pathRole_ShouldUpdateRoleCorrectly() {

        String jwtToken = "jwtToken";
        String roleToBeSetted = "EDITOR";
        UUID idUser = UUID.randomUUID();
        UUID idAdmin = UUID.randomUUID();
        boolean tokenIsFromAdmin = true;

        UpdateRoleRequest request = UpdateRoleRequest.builder().id(idUser.toString()).role(roleToBeSetted).build();
        final var loggedAdmin = AuthenticationData.builder().role("ADMIN").idUser(idAdmin).username("usernameOfAnAdmin").build();
        final var userToBeModified = AuthenticationData.builder().role("USER").idUser(idUser).username("usernameOfUserToBeModified").build();
        final var request2 = this.easyRandom.nextObject(PatchUserRoleRequest.class);

        AuthenticationDataEntity authData = AuthDataMapper.INSTANCE.mapToAuthData(request);

        when(jwtReaderService.extractUsername(jwtToken)).thenReturn(loggedAdmin.getRole());
        when(getUserFromTokenIdService.getUserFromTokenId(idUser)).thenReturn(userToBeModified);
        when(jwtValidations.isAdminTokenCompletelyValidated(jwtToken)).thenReturn(tokenIsFromAdmin);
        when(repository.registerUserAuthData(userToBeModified, jwtToken)).thenReturn(userToBeModified);

        when(repository.patchRole(userToBeModified, roleToBeSetted)).thenReturn(true);

        boolean finalResult = useCase.apply(request2);
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

        AuthenticationDataEntity loggedAdmin = AuthenticationDataEntity.builder().role("ADMIN").idUser(idAdmin).username("usernameOfAnAdmin").build();
        AuthenticationDataEntity userToBeModified = AuthenticationDataEntity.builder().role("USER").idUser(idUser).username("usernameOfUserToBeModified").build();

        //Actual call to mapstruct to map (and assert results and mapping is being done correctly)
        AuthenticationDataEntity authData = AuthDataMapper.INSTANCE.mapToAuthData(request);

        when(jwtReaderService.extractUsername(jwtToken)).thenReturn(loggedAdmin.getRole());
        when(getUserFromTokenIdService.getUserFromTokenId(idUser)).thenReturn(userToBeModified);
        when(jwtValidations.isAdminTokenCompletelyValidated(jwtToken)).thenReturn(tokenIsFromAdmin);
        when(repository.registerUserAuthData(userToBeModified, jwtToken)).thenReturn(userToBeModified);

        when(repository.patchRole(userToBeModified, roleToBeSetted)).thenReturn(true);

        boolean finalResult = useCase.apply(jwtToken,request);

        assertNotNull( authData );//check mapstruct is working
        assertThat( authData.getIdUser() ).isEqualTo(idUser ); //check mapstruct is working
        assertFalse( finalResult );

    }

}