package com.example.managementsystem.authentication;

import com.example.managementsystem.clientmanagement.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

public class AuthenticationUser extends User {

    private UUID client;
    private String role;

    public AuthenticationUser(UserEntity user) {
        this(user.getUsername(), user.getPassword(), user.isEnabled(), user.isEnabled(), user.isEnabled(), user.isEnabled(), user.getRole().getAuthorities());
        this.client = user.getClient().getId();
        this.role = user.getRole().getCode();
    }

    public AuthenticationUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthenticationUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UUID getClient() {
        return client;
    }

    public void setClient(UUID client) {
        this.client = client;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
