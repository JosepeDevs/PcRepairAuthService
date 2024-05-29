package com.josepdevs.Domain.dto;


import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.josepdevs.Domain.dto.valueobjects.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@Table(name="authentication_data")
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class AuthenticationData implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7466891722159308882L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_user")
	@Id
	private UUID idUser;

	@Column(name = "username")
	private String username;
	
	@Column(name = "email")
	private String email;

	@Column(name = "psswrd")
	private String psswrd;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@Column(name = "psswrd_salt")
	private String psswrdSalt;

	@Column(name = "registration_token")
	private String registrationToken;
	
	@Column(name = "psswrdchange_token")
	private String psswrdChangeToken;
	
	@Column(name = "psswrdtoken_issuedate")
	private String 	psswrdTokenIssuedate;
	
	@Column(name = "active")
	private boolean	active;


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.toString()));
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public String getPassword() {
		return psswrd;
	}


}
