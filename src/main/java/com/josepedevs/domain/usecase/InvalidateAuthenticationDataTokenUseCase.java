package com.josepedevs.domain.usecase;

import com.josepedevs.domain.request.InvalidateTokenRequest;

import java.util.function.Function;

public interface InvalidateAuthenticationDataTokenUseCase extends Function<InvalidateTokenRequest, Boolean> {
}
