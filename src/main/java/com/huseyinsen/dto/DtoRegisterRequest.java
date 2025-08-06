package com.huseyinsen.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoRegisterRequest {

    @NotBlank(message = "Email field can't be blank")
    @Email(message = "Please enter a valid email!")
    private String email;

    @NotBlank(message = "Password field can't be blank")
    @Size(min = 6, message = "The password must be minimum 6 characters!")
    private String password;

    @NotBlank(message = "Firstname field can't be blank")
    private String firstName;

    @NotBlank(message = "Lastname field can't be blank")
    private String lastName;
}