package com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.repository;

import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAuthenticationDataRepository extends JpaRepository<AuthenticationDataEntity, UUID> {

    Optional<AuthenticationDataEntity> findByUsername(String username);

    Optional<AuthenticationDataEntity> findByEmail(String email);

}
