package com.josepedevs.infra.output.persistence.jpa.postgresql;

import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.entity.valueobjects.Role;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.mapper.JpaAuthenticationDataMapper;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.repository.JpaAuthenticationDataRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaAuthenticationDataPostgreSqlAdapterTest {

	@InjectMocks
	private JpaAuthenticationDataPostgreSqlAdapter adapter;

	@Mock
	private JpaAuthenticationDataRepository repository;

	@Mock
	private JpaAuthenticationDataMapper mapper;

	private final EasyRandom easyRandom = new EasyRandom();

	@Test
	void registerUserAuthData_shouldSaveCorrectlyData() {

		String jwtToken ="tokenValue";
		String username ="specificUserName";
		UUID id = UUID.randomUUID();
		AuthenticationData authenticationDataEntity = AuthenticationData.builder()
				  .email("hola@adios.com")
				  .username(username)
				  .role("not admin")
				  .psswrd(username)
				  .currentToken(jwtToken)
				  .active(true)
				  .build();

		AuthenticationData authenticationDataEntityWithId = AuthenticationData.builder()
				  .idUser(id)
				  .email("hola@adios.com")
				  .username(username)
				  .role("not admin")
				  .psswrd(username)
				  .currentToken(jwtToken)
				  .active(true)
				  .build();

		AuthenticationData finalResult = adapter.registerUserAuthData(authenticationDataEntity, jwtToken);
		
		assertNotNull(finalResult);
		assertNull(finalResult.getIdUser());

		AuthenticationData finalResultWithExpectedId = adapter.registerUserAuthData(authenticationDataEntityWithId, jwtToken);

	 	 assertNotNull(finalResultWithExpectedId.getIdUser());
	 	 finalResult.setIdUser(id);
	 	 assertEquals(finalResultWithExpectedId, finalResult);
	}

	@Test
	void  findByUsername_shouldReturnUserIfFound() {
		final var authData = easyRandom.nextObject(AuthenticationData.class);
		final var authenticationDataEntityWithId = AuthenticationDataEntity.builder()
				  .idUser(authData.getIdUser())
				  .email(authData.getEmail())
				  .username(authData.getUsername())
				  .role(authData.getRole())
				  .psswrd(authData.getPsswrd())
				  .currentToken(authData.getCurrentToken())
				  .active(authData.isActive())
				  .build();
		when(repository.findByUsername(authData.getUsername())).thenReturn(Optional.of(authenticationDataEntityWithId));
		when(mapper.map(authenticationDataEntityWithId)).thenReturn(authData);

		final var finalResult = adapter.findByUsername(authData.getUsername());

		assertNotNull(finalResult);
		assertNotNull(Optional.of(authenticationDataEntityWithId));
	    assertTrue(finalResult.isPresent());
	}

	@Test
	void  findByUsername_shouldReturnEmptyOptionalIUsernamefNotFound() {
		String username ="specificUsername";
		Optional<AuthenticationDataEntity> emptyOptionaluser =  Optional.empty();
		when(repository.findByUsername(username)).thenReturn(emptyOptionaluser);

		Optional<AuthenticationData> finalResult = adapter.findByUsername(username);

	    assertTrue(finalResult.isEmpty());
	}

	@Test
	void login_GivenFoundAuthDataAndSuccessfulOperation_ThenReturnsTrue() {
		// Arrange
	   	final var authData = easyRandom.nextObject(AuthenticationData.class);
		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.username(authData.getUsername())
				.role(authData.getRole())
				.psswrd(authData.getPsswrd())
				.currentToken(authData.getCurrentToken())
				.active(authData.isActive())
				.build();
	   	when(repository.findById(authData.getIdUser())).thenReturn(Optional.of(authEntity));
	   	when(repository.save(authEntity)).thenReturn(authEntity);
		when(mapper.map(authData)).thenReturn(authEntity);
		// Act
		final var actual = this.adapter.updateToken(authData, authData.getCurrentToken());
		// Assert
		assertTrue(actual);
	}

	@Test
	void login_GivenEmptyAuthData_ThenReturnsFalse() {
		// Arrange
		final var authData = easyRandom.nextObject(AuthenticationData.class);
		when(repository.findById(authData.getIdUser())).thenReturn(Optional.empty());
		// Act
		final var actual = this.adapter.updateToken(authData, authData.getCurrentToken());
		// Assert
		assertFalse(actual);
	}

	@Test
	void login_GivenFoundAuthDataAndFailedOperation_ThenReturnsFalse() {
		// Arrange
		final var authData = easyRandom.nextObject(AuthenticationData.class);
		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.username(authData.getUsername())
				.role(authData.getRole())
				.psswrd(authData.getPsswrd())
				.currentToken("other token")
				.active(authData.isActive())
				.build();
		when(repository.findById(authData.getIdUser())).thenReturn(Optional.of(authEntity));
		when(repository.save(authEntity)).thenReturn(authEntity);
		when(mapper.map(authData)).thenReturn(authEntity);
		// Act
		final var actual = this.adapter.updateToken(authData, authData.getCurrentToken());
		// Assert
		assertFalse(actual);
	}

	@Test
	void patchPassword_SavedCorrectly_ThenReturnsTrue() {
		final var authData = easyRandom.nextObject(AuthenticationData.class);
		final var newPassword = this.easyRandom.nextObject(String.class);

		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.psswrd(authData.getPsswrd())
				.username(authData.getUsername())
				.role(authData.getRole())
				.currentToken(authData.getCurrentToken())
				.active(authData.isActive())
				.build();

		final var authWithNewPassword = authData.toBuilder().psswrd(newPassword).build();

		when(mapper.map(authEntity)).thenReturn(authWithNewPassword);
		when(repository.save(authEntity)).thenReturn(authEntity);
		when(mapper.map(authWithNewPassword)).thenReturn(authEntity);

		final var actual = this.adapter.patchPassword(authData, newPassword);

		assertTrue(actual);
	}

	@Test
	void patchPassword_GivenFailedUpdate_ThenReturnsFalse() {
		final var authData = easyRandom.nextObject(AuthenticationData.class);

		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.psswrd(authData.getPsswrd())
				.username(authData.getUsername())
				.role(authData.getRole())
				.currentToken(authData.getCurrentToken())
				.active(authData.isActive())
				.build();


		when(mapper.map(authEntity)).thenReturn(authData);
		when(repository.save(authEntity)).thenReturn(authEntity);
		when(mapper.map(authData)).thenReturn(authEntity);

		final var actual = this.adapter.patchPassword(authData, authData.getPsswrd());

		assertFalse(actual);
	}

	@Test
	void patchRole_SavedCorrectly_ThenReturnsAuthData() {
		final var authData = easyRandom.nextObject(AuthenticationData.class);
		final var newRole = this.easyRandom.nextObject(Role.class);

		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.psswrd(authData.getPsswrd())
				.username(authData.getUsername())
				.role(authData.getRole())
				.currentToken(authData.getCurrentToken())
				.active(authData.isActive())
				.build();

		final var authWithNewRole = authData.toBuilder().role(newRole.toString()).build();

		when(mapper.map(authEntity)).thenReturn(authWithNewRole);
		when(repository.save(authEntity)).thenReturn(authEntity);
		when(mapper.map(authWithNewRole)).thenReturn(authEntity);

		final var actual = this.adapter.patchRole(authData, newRole.toString());

		assertEquals(authWithNewRole, actual);
	}

	@Test
	void patchRole_GivenFailedUpdate_ThenReturnsAuthData() {
		final var authData = easyRandom.nextObject(AuthenticationData.class);
		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.psswrd(authData.getPsswrd())
				.username(authData.getUsername())
				.role(authData.getRole())
				.currentToken(authData.getCurrentToken())
				.active(authData.isActive())
				.build();

		when(mapper.map(authEntity)).thenReturn(authData);
		when(repository.save(authEntity)).thenReturn(authEntity);
		when(mapper.map(authData)).thenReturn(authEntity);

		final var actual = this.adapter.patchRole(authData, authData.getRole());

		assertEquals(authData, actual);
	}

	@Test
	void getAll_FindsAlls_ThenReturnsFilledList() {
		final var authData = easyRandom.nextObject(AuthenticationData.class);
		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.psswrd(authData.getPsswrd())
				.username(authData.getUsername())
				.role(authData.getRole())
				.currentToken(authData.getCurrentToken())
				.active(authData.isActive())
				.build();
		final var authList = List.of(authEntity);

		when(mapper.map(authEntity)).thenReturn(authData);
		when(repository.findAll()).thenReturn(authList);

		final var actual = adapter.getAll();

		assertEquals(List.of(authData), actual);

	}

	@Test
	void getAll_FindsAlls_ThenReturnsEmptyList() {
		when(repository.findAll()).thenReturn(List.of());

		final var actual = adapter.getAll();

		assertEquals(List.of(), actual);
	}

	@Test
	void invalidateToken_SavedCorrectly_ThenReturnsTrue() {
		final var authData = easyRandom.nextObject(AuthenticationData.class);
		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.psswrd(authData.getPsswrd())
				.username(authData.getUsername())
				.role(authData.getRole())
				.currentToken(authData.getCurrentToken())
				.active(authData.isActive())
				.build();

		final var authWithChanges = authData.toBuilder().currentToken("invalidated").build();

		when(mapper.map(authEntity)).thenReturn(authWithChanges);
		when(repository.save(authEntity)).thenReturn(authEntity);
		when(mapper.map(authWithChanges)).thenReturn(authEntity);

		final var actual = this.adapter.invalidateToken(authData);

		assertTrue(actual);
	}

	@Test
	void invalidateToken_GivenFailedUpdate_ThenReturnsFalse() {
		final var authData = easyRandom.nextObject(AuthenticationData.class).toBuilder().currentToken("invalidated").build();
		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.psswrd(authData.getPsswrd())
				.username(authData.getUsername())
				.role(authData.getRole())
				.currentToken(authData.getCurrentToken())
				.active(authData.isActive())
				.build();

		when(mapper.map(authEntity)).thenReturn(authData);
		when(repository.save(authEntity)).thenReturn(authEntity);
		when(mapper.map(authData)).thenReturn(authEntity);

		final var actual = this.adapter.invalidateToken(authData);

		assertFalse(actual);
	}

	@Test
	void deleteHard_GivenDeleted_ThenReturnsTrue() {
		final var authData = easyRandom.nextObject(AuthenticationData.class).toBuilder().currentToken("invalidated").build();

		when(this.adapter.findById(authData.getIdUser())).thenReturn(Optional.empty());

		final var actual = this.adapter.deleteHard(authData.getIdUser());

		assertTrue(actual);
	}
	@Test
	void deleteHard_GivenFailedDeleted_ThenReturnsFalse() {
		final var authData = easyRandom.nextObject(AuthenticationData.class).toBuilder().currentToken("invalidated").build();

		when(this.adapter.findById(authData.getIdUser())).thenReturn(Optional.of(authData));

		final var actual = this.adapter.deleteHard(authData.getIdUser());

		assertFalse(actual);
	}

	@Test
	void findById_GivenFoundAuthData_ThenReturnsOptional() {
		final var authData = easyRandom.nextObject(AuthenticationData.class).toBuilder().currentToken("invalidated").build();
		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.psswrd(authData.getPsswrd())
				.username(authData.getUsername())
				.role(authData.getRole())
				.currentToken(authData.getCurrentToken())
				.active(authData.isActive())
				.build();

		when(this.repository.findById(authData.getIdUser())).thenReturn(Optional.of(authEntity));
		when(this.mapper.map(authEntity)).thenReturn(authData);

		final var actual = this.adapter.findById(authData.getIdUser());

		assertEquals(Optional.of(authData), actual);
	}

	@Test
	void findById_GivenNotFoundAuthData_ThenReturnsEmptyOptional() {
		final var authData = easyRandom.nextObject(AuthenticationData.class).toBuilder().currentToken("invalidated").build();

		when(this.repository.findById(authData.getIdUser())).thenReturn(Optional.empty());

		final var actual = this.adapter.findById(authData.getIdUser());

		assertEquals(Optional.empty(), actual);
	}

	@Test
	void findByEmail_GivenFoundAuthData_ThenReturnsOptional() {
		final var authData = easyRandom.nextObject(AuthenticationData.class).toBuilder().currentToken("invalidated").build();
		final var authEntity = AuthenticationDataEntity.builder()
				.idUser(authData.getIdUser())
				.email(authData.getEmail())
				.psswrd(authData.getPsswrd())
				.username(authData.getUsername())
				.role(authData.getRole())
				.currentToken(authData.getCurrentToken())
				.active(authData.isActive())
				.build();

		when(this.repository.findByEmail(authData.getEmail())).thenReturn(Optional.of(authEntity));
		when(this.mapper.map(authEntity)).thenReturn(authData);

		final var actual = this.adapter.findByEmail(authData.getEmail());

		assertEquals(Optional.of(authData), actual);
	}

	@Test
	void findByEmail_GivenNotFoundAuthData_ThenReturnsEmptyOptional() {
		final var authData = easyRandom.nextObject(AuthenticationData.class).toBuilder().currentToken("invalidated").build();

		when(this.repository.findByEmail(authData.getEmail())).thenReturn(Optional.empty());

		final var actual = this.adapter.findByEmail(authData.getEmail());

		assertEquals(Optional.empty(), actual);
	}

}