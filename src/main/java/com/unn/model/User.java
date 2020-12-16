package com.unn.model;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User implements UserDetails {
    private static final long serialVersionUID = 6602651258712907377L;

    private String username;
    @JsonIgnore
    private String password;

    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
}
