package com.josepedevs.domain.usecase;

import com.josepedevs.domain.request.DeleteHardUserRequest;

import java.util.function.Function;

public interface DeleteHardUserUseCase extends Function<DeleteHardUserRequest, Boolean> {
}
