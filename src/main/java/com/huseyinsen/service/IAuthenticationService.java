package com.huseyinsen.service;

import com.huseyinsen.dto.AuthenticationRequest;
import com.huseyinsen.dto.AuthenticationResponse;
import com.huseyinsen.dto.DtoRegisterRequest;
import jakarta.validation.Valid;

public interface IAuthenticationService {
    AuthenticationResponse register(@Valid DtoRegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshToken(String oldToken);
    void logout(String token);
}