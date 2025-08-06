package com.huseyinsen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoJwtResponse {
    private String accessToken;
    private String refreshToken;
    private DtoUser dtoUser;
}