package com.ascarrent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Size(min=5, max=180)
    @Email(message = "Please provide a valid email")
    private String email;

    @Size(min=4, max=20, message = "Please provide correct size of password")
    @NotBlank(message = "Please provide an e-mail")
    private String password;
}
