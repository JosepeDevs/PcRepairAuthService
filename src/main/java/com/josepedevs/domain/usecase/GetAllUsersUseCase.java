package com.josepedevs.domain.usecase;

import com.josepedevs.domain.entity.AuthenticationData;

import java.util.List;
import java.util.function.Function;

public interface GetAllUsersUseCase extends Function<String, List<AuthenticationData>> {
}
