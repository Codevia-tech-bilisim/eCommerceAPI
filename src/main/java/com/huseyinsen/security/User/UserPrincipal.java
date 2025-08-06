package com.huseyinsen.security.User;

import com.huseyinsen.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Eğer rollerin varsa burada maplersin, şu an boş liste dönüyoruz
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Login için email kullanılıyor
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // İstersen User’dan okuyabilirsin
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
        return user.isEnabled(); // Kullanıcı aktif mi kontrolü
    }

    public User getUser() {
        return user;
    }
}