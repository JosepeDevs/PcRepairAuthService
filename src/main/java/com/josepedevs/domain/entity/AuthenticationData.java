package com.josepedevs.domain.entity;


import com.josepedevs.domain.entity.valueobjects.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AuthenticationData implements UserDetails {

    @Serial
    private static final long serialVersionUID = -7466891722159308882L;

    private UUID idUser;

    private String username;

    private String email;

    private String psswrd;

    private String role;

    private String 	currentToken;

    private boolean	active;

    public AuthenticationData(UUID idUser, String username, String email, String psswrd,
                                    Role role, String currentToken, boolean active) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.psswrd = psswrd;
        this.role = role.name();
        this.currentToken = currentToken;
        this.active = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    /*//This would be used if more than one role is allowed
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList());
    }
     */
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return psswrd;
    }

}

