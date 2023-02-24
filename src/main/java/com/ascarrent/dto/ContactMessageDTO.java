package com.ascarrent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageDTO {


    private Long id;
    private String senderName;
    private String email;
    private String phone;
    private String subject;
    private String messageBody;
    private String ipAddress;
    private LocalDateTime createdDateTime;


}
