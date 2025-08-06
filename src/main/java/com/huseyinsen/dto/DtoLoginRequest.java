package com.huseyinsen.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoLoginRequest {

    @NotBlank(message = "Invalid email format.")
    @Email(message = "Please enter a valid email form.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    private String password;
}