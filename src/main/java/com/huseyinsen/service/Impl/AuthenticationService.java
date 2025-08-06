package com.huseyinsen.service.Impl;

import com.huseyinsen.dto.AuthenticationRequest;
import com.huseyinsen.dto.AuthenticationResponse;
import com.huseyinsen.dto.DtoRegisterRequest;
import com.huseyinsen.entity.Role;
import com.huseyinsen.entity.User;
import com.huseyinsen.entity.UserStatus;
import com.huseyinsen.exception.InvalidCredentialsException;
import com.huseyinsen.exception.UserAlreadyExistsException;
import com.huseyinsen.repository.RoleRepository;
import com.huseyinsen.repository.UserRepository;
import com.huseyinsen.security.JwtService;
import com.huseyinsen.service.IAuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(DtoRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use: " + request.getEmail());
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse refreshToken(String oldToken) {
        // Basit bir versiyon, refreshToken mantığını genişletebilirsin.
        String email = jwtService.extractUsername(oldToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(oldToken, user)) {
            throw new RuntimeException("Invalid token");
        }

        String newToken = jwtService.generateToken(user);
        return new AuthenticationResponse(newToken);
    }

    @Override
    public void logout(String token) {
        // Eğer token blacklist gibi bir yapı kuracaksan, burada yapılır.
        // Şu anda stateless JWT kullanıldığı için herhangi bir işlem yapılmıyor.
    }
}