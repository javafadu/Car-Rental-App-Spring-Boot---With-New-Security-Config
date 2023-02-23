package com.ascarrent.dto.request;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageRequest {


    @Size(min = 1,max = 50, message = "Your Name '${validatedValue}' must be between {min} and {max} chars long")
    @NotNull(message = "Please provide your name")
    @NotBlank(message = "Please provide your name")
    private String senderName;

    @Email(message="Provide a valid email")
    @NotBlank(message = "Please provide your e-mail")
    private String email;

    private String phone;

    @Size(min = 5,max = 120, message = "Your Subject '${validatedValue}' must be between {min} and {max} chars long")
    @NotNull(message = "Please provide message subject")
    @NotBlank(message = "Please provide message subject")
    private String subject;

    @Size(min = 20,max = 300, message = "Your Body '${validatedValue}' must be between {min} and {max} chars long")
    @NotNull(message = "Please provide message body")
    private String messageBody;

}
