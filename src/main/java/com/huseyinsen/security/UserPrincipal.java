package com.huseyinsen.security;

import com.huseyinsen.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Rol sistemin yoksa şimdilik boş liste dönebiliriz.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // User entity içindeki password
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // User entity içindeki username (veya email)
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; // Eğer sisteminde aktiflik durumu varsa burada kontrol edebilirsin
    }
}