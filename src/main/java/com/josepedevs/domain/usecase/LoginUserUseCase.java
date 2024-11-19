package com.josepedevs.domain.usecase;

import com.josepedevs.domain.request.AuthenticationRequest;
import com.josepedevs.domain.request.AuthenticationResponse;

import java.util.function.Function;

public interface LoginUserUseCase extends Function<AuthenticationRequest, AuthenticationResponse> {
}
