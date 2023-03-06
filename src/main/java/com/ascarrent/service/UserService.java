package com.ascarrent.service;

import com.ascarrent.domain.Role;
import com.ascarrent.domain.User;
import com.ascarrent.domain.enums.RoleType;
import com.ascarrent.dto.UserDTO;
import com.ascarrent.dto.request.LoginRequest;
import com.ascarrent.dto.request.UserRegisterRequest;
import com.ascarrent.dto.response.LoginResponse;
import com.ascarrent.exception.ConflictException;
import com.ascarrent.exception.ResourceNotFoundException;
import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.mapper.UserMapper;
import com.ascarrent.repository.UserRepository;
import com.ascarrent.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder,@Lazy AuthenticationManager authenticationManager,JwtUtils jwtUtils, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND_EXCEPTION, email)));
        return user;
    }


    public void registerUser(UserRegisterRequest userRegisterRequest) {
        // check e-mail is exist or not
        if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessages.EMAIL_ALREADY_EXIST_MESSAGE, userRegisterRequest.getEmail()));
        }

        // set "Customer" for the new registered user as default
        Role role = roleService.findByRoleType(RoleType.ROLE_CUSTOMER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);


        // encode the requested password
        String encodedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());

        // set the related field of to be created user
        User user = new User();
        user.setFirstName(userRegisterRequest.getFirstName());
        user.setLastName(userRegisterRequest.getLastName());
        user.setEmail(userRegisterRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setPhoneNumber(userRegisterRequest.getPhoneNumber());
        user.setAddress(userRegisterRequest.getAddress());
        user.setZipCode(userRegisterRequest.getZipCode());
        user.setBirthDate(userRegisterRequest.getBirthDate());
        user.setRoles(roles);


        userRepository.save(user);

    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        // 1- validate username(email) and password via authentication manager
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        // 2- keep authenticated info in SecurityContext
        /* currently logged in user (authentication) information automatically kept in the SecurityContext*/

        // 3- generate token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtToken(userDetails);

        // 4- Send jwtToken to controller
        LoginResponse loginResponse = new LoginResponse(jwtToken);
        return loginResponse;

    }

    public List<UserDTO> getAllUsers() {
       List<User> users = userRepository.findAll();
       return userMapper.userListToUserDTOList(users);
    }
}
