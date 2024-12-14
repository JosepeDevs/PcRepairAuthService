package com.josepedevs.domain.mapper;

import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.request.PatchUserRoleRequest;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AuthDataMapper {

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "psswrd", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "currentToken", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping( target="idUser", source="id")
    AuthenticationData map(PatchUserRoleRequest patchUserRoleRequest);

}
