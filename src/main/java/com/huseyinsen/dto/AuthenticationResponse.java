package com.huseyinsen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;

    public AuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}