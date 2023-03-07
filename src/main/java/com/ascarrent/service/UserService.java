package com.ascarrent.service;

import com.ascarrent.domain.Role;
import com.ascarrent.domain.User;
import com.ascarrent.domain.enums.RoleType;
import com.ascarrent.dto.UserDTO;
import com.ascarrent.dto.request.UserRegisterRequest;
import com.ascarrent.exception.ConflictException;
import com.ascarrent.exception.ResourceNotFoundException;
import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.mapper.UserMapper;
import com.ascarrent.repository.UserRepository;
import com.ascarrent.security.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, RoleService roleService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
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
        Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);
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


    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.userListToUserDTOList(users);
    }


    public UserDTO getPrincipal() {
        return userMapper.userToUserDTO(getCurrentLoggedInUser());
    }


    // Get Current Logged-in User
    public User getCurrentLoggedInUser() {
        String email = SecurityUtils.getCurrentLoggedInUser().orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessages.PRINCIPAL_NOT_FOUND_MESSAGE));
        return getUserByEmail(email);
    }


    public Page<UserDTO> getAllUsersWithPage(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return convertPageUserToPageUserDTO(userPage);
    }


    // Convert Page<User> - Page<UserDTO>
    private Page<UserDTO> convertPageUserToPageUserDTO(Page<User> userPage) {
        return userPage.map(
                user -> userMapper.userToUserDTO(user)
        );
    }

    public UserDTO getUserWithId(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessages.RESOURCE_NOT_FOUND_EXCEPTION,id)));
        return userMapper.userToUserDTO(user);
    }
}
