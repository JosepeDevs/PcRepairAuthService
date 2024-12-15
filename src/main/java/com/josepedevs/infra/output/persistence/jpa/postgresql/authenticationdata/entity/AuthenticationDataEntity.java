package com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity;

import com.josepedevs.domain.entity.valueobjects.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="authentication_data")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AuthenticationDataEntity implements UserDetails{

    @Serial
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

    @Column(name = "role")
    private String role;

    @Column(name = "current_token")
    private String 	currentToken;

    @Column(name = "active")
    private boolean	active;


    public AuthenticationDataEntity(UUID idUser, String username, String email, String psswrd,
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
