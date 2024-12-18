package com.josepedevs.domain.usecase;

import com.josepedevs.domain.request.RegisterRequest;

import java.util.UUID;
import java.util.function.Function;

public interface RegisterAuthenticationDataUseCase extends Function<RegisterRequest, UUID> {
}
