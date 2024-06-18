package com.josepedevs.Infra.output;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.josepedevs.Domain.entities.AuthenticationData;

public interface AuthJpaRepository extends JpaRepository<AuthenticationData, UUID> {
//sin implementación salvvo que se quiera algo personalizado, solo para poder llamar a los métodos de JPARepo

	public Optional<AuthenticationData> findByUsername(String username);
	public Optional<AuthenticationData> findByEmail(String email);
	
}
