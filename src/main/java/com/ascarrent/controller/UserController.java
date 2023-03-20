package com.ascarrent.controller;

import com.ascarrent.dto.UserDTO;
import com.ascarrent.dto.request.UpdatePasswordRequest;
import com.ascarrent.dto.request.UserUpdateRequest;
import com.ascarrent.dto.response.ACRResponse;
import com.ascarrent.dto.response.ResponseMessage;
import com.ascarrent.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @GetMapping("/auth/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsersWithPage(
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size,
            @RequestParam(required = false, value = "sort", defaultValue = "id") String prop,
            @RequestParam(required = false, value = "direction", defaultValue = "ASC") Sort.Direction direction)
     {
         Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
         Page<UserDTO> userDTOPage =  userService.getAllUsersWithPage(pageable);
         return ResponseEntity.ok(userDTOPage);

    }

    // -- Get a User With Id --
    @GetMapping("/auth/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserWithId(@PathVariable Long id) { //@PathVariable("id") Long id
        return ResponseEntity.ok(userService.getUserWithId(id)) ;

    }

    // -- Update password (logged in user) --
    @PatchMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or asRole('CUSTOMER')")
    public ResponseEntity<ACRResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatePassword(updatePasswordRequest);

        ACRResponse response = new ACRResponse();
        response.setMessage(ResponseMessage.PASSWORD_CHANGED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }

    // -- Update User (Authenticated user update own info)
    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or asRole('CUSTOMER')")
    public ResponseEntity<ACRResponse> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(userUpdateRequest);

        ACRResponse response = new ACRResponse();
        response.setMessage(ResponseMessage.USER_UPDATE_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }


}
