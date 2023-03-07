package com.ascarrent.controller;

import com.ascarrent.dto.request.LoginRequest;
import com.ascarrent.dto.request.UserRegisterRequest;
import com.ascarrent.dto.response.ACRResponse;
import com.ascarrent.dto.response.LoginResponse;
import com.ascarrent.dto.response.ResponseMessage;
import com.ascarrent.security.jwt.JwtUtils;
import com.ascarrent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJwtController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

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

    // --User Login--
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        // Validate user with username(email) and password

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        // envolop our username and password to be used by spring security

        Authentication authentication =  authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        // the user has been validated and ready for creating token
        // user information is also stored in the SecurityContext

        // currently logged-in user information
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // create token here
        String jwtToken =  jwtUtils.generateJwtToken(userDetails);

        LoginResponse loginResponse = new LoginResponse(jwtToken);

        return new ResponseEntity<>(loginResponse,HttpStatus.OK);

    }

}