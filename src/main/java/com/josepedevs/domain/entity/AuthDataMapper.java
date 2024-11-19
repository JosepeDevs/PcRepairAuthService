package com.josepedevs.domain.entity;

import com.josepedevs.domain.request.UpdateRoleRequest;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthDataMapper {

    AuthDataMapper INSTANCE = Mappers.getMapper(AuthDataMapper.class);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "psswrd", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "currentToken", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping( target="idUser", source="id")
    AuthenticationDataEntity mapToAuthData(UpdateRoleRequest updateRoleRequest);

}
