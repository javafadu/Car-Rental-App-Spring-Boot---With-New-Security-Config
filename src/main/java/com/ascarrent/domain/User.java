package com.ascarrent.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tbl_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 70, nullable = false)
    private String firstName;

    @Column(length = 70, nullable = false)
    private String lastName;

    @Column(length = 180, nullable = false, unique = true)
    private String email;

    @Column(length = 120, nullable = false)
    private String password;

    @Column(length = 14, nullable = false)
    private String phoneNumber;

    @Column(length = 180, nullable = false)
    private String address;

    @Column(length = 15, nullable = false)
    private String zipCode;

    @Column(nullable = true)
    private LocalDate birthDate;


    @Column(nullable = false)
    private Boolean builtIn = false;

    // Hibername makes Eager as default for ending with One relations
    // Hibername makes Lazy as default for ending with Many relations
    @ManyToMany // LAZY
    @JoinTable(name="tbl_user_roles",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id") )
    private Set<Role> roles = new HashSet<>();




}
