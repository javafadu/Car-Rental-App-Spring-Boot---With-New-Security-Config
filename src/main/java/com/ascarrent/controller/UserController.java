package com.ascarrent.controller;

import com.ascarrent.dto.UserDTO;
import com.ascarrent.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // -- Get All Users --
    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
       List<UserDTO>  users = userService.getAllUsers();
       return ResponseEntity.ok(users);
    }

    // -- Get Authenticated (currently logged-in) User Info --
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or asRole('CUSTOMER')")
    public ResponseEntity<UserDTO> getAuthUser() {
        UserDTO userDTO = userService.getPrincipal();
        return ResponseEntity.ok(userDTO);
    }

    // -- Get All user With Paging --



}
