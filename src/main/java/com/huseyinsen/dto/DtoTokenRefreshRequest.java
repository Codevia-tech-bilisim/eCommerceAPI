package com.huseyinsen.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DtoTokenRefreshRequest {
    @NotBlank
    private String refreshToken;
}