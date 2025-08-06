package com.huseyinsen.service;

import com.huseyinsen.dto.AuthenticationRequest;
import com.huseyinsen.dto.AuthenticationResponse;
import com.huseyinsen.dto.RegisterRequest;

public interface IAuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshToken(String oldToken);
    void logout(String token);
}