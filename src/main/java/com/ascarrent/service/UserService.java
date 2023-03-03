package com.ascarrent.service;

import com.ascarrent.domain.Role;
import com.ascarrent.domain.User;
import com.ascarrent.domain.enums.RoleType;
import com.ascarrent.dto.request.UserRegisterRequest;
import com.ascarrent.exception.ConflictException;
import com.ascarrent.exception.ResourceNotFoundException;
import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService,@Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND_EXCEPTION, email)));
        return user;
    }


    public void registerUser(UserRegisterRequest userRegisterRequest) {
        // check e-mail is exist or not
        if(userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessages.EMAIL_ALREADY_EXIST_MESSAGE,userRegisterRequest.getEmail()));
        }

        // set "Customer" for the new registered user as default
        Role role = roleService.findByRoleType(RoleType.ROLE_CUSTOMER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);


        // encode the requested password
        String encodedPassword =  passwordEncoder.encode(userRegisterRequest.getPassword());

        // set the related field of to be created user
        User user = new User();
        user.setFirstName(userRegisterRequest.getFirstName());
        user.setLastName(userRegisterRequest.getLastName());
        user.setEmail(userRegisterRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setPhoneNumber(userRegisterRequest.getPhoneNumber());
        user.setAddress(userRegisterRequest.getAddress());
        user.setZipCode(userRegisterRequest.getZipCode());
        user.setRoles(roles);

        userRepository.save(user);

    }
}
