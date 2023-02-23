package com.ascarrent.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_cmessages")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(length = 120, nullable = false)
    private String senderName;

    @Column(length = 180, nullable = false)
    private String email;

    @Column(length = 25)
    private String phone;

    @Column(length = 120, nullable = false)
    private String subject;

    @Column(length = 300)
    private String messageBody;


}
