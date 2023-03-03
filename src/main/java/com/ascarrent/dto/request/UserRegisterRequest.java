package com.ascarrent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {


    @Size(max=70)
    @NotBlank(message = "Please provide your First Name")
    // @NotBlank = charSequence.toString().trim().length()>0
    // @NotBlank is only for String
    private String firstName;

    @Size(max=70)
    @NotBlank(message = "Please provide your Last Name")
    private String lastName;

    @Size(min=5, max=180)
    @Email(message = "Please provide a valid email")
    private String email;

    @Size(min=4, max=20, message = "Please provide correct size of password")
    @NotBlank(message = "Please provide an e-mail")
    private String password;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", // (555) 444-3333
            message = "Please provide valid phone number")
    @Size(min = 14, max = 14)
    @NotNull(message = "Please provide your phone number")
    private String phoneNumber;

    @Size(max=180)
    @NotBlank(message = "Please provide an address")
    private String address;

    @Size(min=2, max=15)
    @NotBlank(message = "Please provide a zip code")
    private String zipCode;



}
