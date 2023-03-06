package com.ascarrent.dto;

import com.ascarrent.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Convert User (POJO coming from repo) to UserDTO
public class UserDTO {


    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

    private String address;

    private String zipCode;

    private LocalDate birthDate;

    private Boolean builtIn;

    private Set<String> roles;


    public void setRoles(Set<Role> roles) {
        Set<String> roleStr = new HashSet<>();
        roles.forEach(r->{
            roleStr.add(r.getRoleType().getName()); // Customer, Administrator
        });

        this.roles=roleStr;
    }

}
