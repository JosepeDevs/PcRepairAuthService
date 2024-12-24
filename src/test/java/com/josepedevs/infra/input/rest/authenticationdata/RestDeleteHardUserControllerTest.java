//package com.josepedevs.infra.input.rest.authenticationdata;
//
//import com.josepedevs.application.usecase.authenticationdata.DeleteHardAuthenticationDataUseCaseImpl;
//import com.josepedevs.domain.request.DeleteHardUserRequest;
//import com.josepedevs.infra.input.rest.authenticationdata.mapper.RestAuthenticationDataMapper;
//import org.jeasy.random.EasyRandom;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class RestDeleteHardUserControllerTest {
//
//    @InjectMocks
//    private RestDeleteHardUserController controller;
//    @Mock
//    private  DeleteHardAuthenticationDataUseCaseImpl deleteUseCase;
//    @Mock
//    private  RestAuthenticationDataMapper mapper;
//
//    private final EasyRandom easyRandom = new EasyRandom();
//    @Test
//    void newPassword_GivenTokenAndId_ShouldReturnNoContent() {
//        final var tokenValue = "token";
//        final var token = "Bearer token";
//        final var request = this.easyRandom.nextObject(DeleteHardUserRequest.class).toBuilder().jwtToken(tokenValue).build();
//
//        final var actual = this.controller.newPassword(token, request.getUserId().toString());
//
//        when(this.mapper.map(tokenValue, request.getUserId())).thenReturn(request);
//        when(this.deleteUseCase.apply(request)).thenReturn(true);
//
//        verify(this.mapper).map(tokenValue, request.getUserId());
//        verify(deleteUseCase).apply(request);
//        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
//        assertEquals(true, actual.getBody());
//    }
//}