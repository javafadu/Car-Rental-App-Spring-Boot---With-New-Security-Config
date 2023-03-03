package com.ascarrent.controller;

import com.ascarrent.dto.request.UserRegisterRequest;
import com.ascarrent.dto.response.ACRResponse;
import com.ascarrent.dto.response.ResponseMessage;
import com.ascarrent.security.jwt.JwtUtils;
import com.ascarrent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJwtController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;


    // --User Register--
    @PostMapping("/register")
    public ResponseEntity<ACRResponse> registerUser(@Valid
                                                        @RequestBody UserRegisterRequest userRegisterRequest) {
    userService.registerUser(userRegisterRequest);
    ACRResponse response = new ACRResponse();
    response.setMessage(ResponseMessage.USER_REGISTER_RESPONSE_MESSAGE);
    response.setSuccess(true);

    return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

}
