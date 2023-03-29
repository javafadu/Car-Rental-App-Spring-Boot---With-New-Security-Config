package com.ascarrent.service;

import com.ascarrent.domain.Role;
import com.ascarrent.domain.User;
import com.ascarrent.domain.enums.RoleType;
import com.ascarrent.dto.UserDTO;
import com.ascarrent.dto.request.UpdatePasswordRequest;
import com.ascarrent.dto.request.UserRegisterRequest;
import com.ascarrent.dto.request.UserUpdateByAdminRequest;
import com.ascarrent.dto.request.UserUpdateRequest;
import com.ascarrent.exception.BadRequestException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final ReservationService reservationService;

    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, RoleService roleService, UserMapper userMapper, ReservationService reservationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.reservationService = reservationService;
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


    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = getCurrentLoggedInUser();

        // check1 : user is built-in or not
        if(user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // check2 : control current passwords are same in DB and in requested form
        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(),user.getPassword())) {
            throw  new BadRequestException(ErrorMessages.PASSWORD_NOT_MATCHED_MESSAGE);
        }

        // encode the string password
        String hashedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());

        // change current password with the new password and save it
        user.setPassword(hashedPassword);
        userRepository.save(user);

    }

    @Transactional // like a rollback, to secure for all related repository queries
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        User user = getCurrentLoggedInUser();

        // check1 : user is built-in or not
        if(user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // check2 : email control
        Boolean emailExist =  userRepository.existsByEmail(userUpdateRequest.getEmail());

        // new email is exist and not equal to user's e-mail (means updated email is another else's email)
        if(emailExist && !userUpdateRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(String.format(ErrorMessages.EMAIL_ALREADY_EXIST_MESSAGE,userUpdateRequest.getEmail()));
        }

        userRepository.update(user.getId(),
                userUpdateRequest.getFirstName(),
                userUpdateRequest.getLastName(),
                userUpdateRequest.getEmail(),
                userUpdateRequest.getPhoneNumber(),
                userUpdateRequest.getAddress(),
                userUpdateRequest.getZipCode(),
                userUpdateRequest.getBirthDate());

    }

    public void updateUserByAdmin(Long id, UserUpdateByAdminRequest userUpdateByAdminRequest) {
        // check1: control user with id is exist or not
        User user = getById(id);

        // check2: control builtIn
        if(user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // check3: email control
        Boolean emailExist =  userRepository.existsByEmail(userUpdateByAdminRequest.getEmail());
        if(emailExist && !userUpdateByAdminRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(String.format(ErrorMessages.EMAIL_ALREADY_EXIST_MESSAGE,userUpdateByAdminRequest.getEmail()));
        }

        // check4: password control if it is null or not
        if(userUpdateByAdminRequest.getPassword()==null) {
            userUpdateByAdminRequest.setPassword(user.getPassword());
        } else {
            String encodedPassword = passwordEncoder.encode(userUpdateByAdminRequest.getPassword());
            userUpdateByAdminRequest.setPassword(encodedPassword);
        }

        // check5: roles -> convert ROLE_ADMIN or ROLE_CUSTOMER from Administrator or Customer
        // pRoles = {"Customer", "Administrator"}
        Set<String> userStrRoles =  userUpdateByAdminRequest.getRoles();
        Set<Role> roles =  convertRoles(userStrRoles);


        // Set all items
        user.setFirstName(userUpdateByAdminRequest.getFirstName());
        user.setLastName(userUpdateByAdminRequest.getLastName());
        user.setEmail(userUpdateByAdminRequest.getEmail());
        user.setPassword(userUpdateByAdminRequest.getPassword());
        user.setPhoneNumber(userUpdateByAdminRequest.getPhoneNumber());
        user.setAddress(userUpdateByAdminRequest.getAddress());
        user.setZipCode(userUpdateByAdminRequest.getZipCode());
        user.setBirthDate(userUpdateByAdminRequest.getBirthDate());
        user.setBuiltIn(userUpdateByAdminRequest.getBuiltIn());
        user.setRoles(roles);

        userRepository.save(user);


    }

    public void removeUserById(Long id) {
        // check1: control user with id is exist or not
        User user = getById(id);

        // check2: control builtIn
        if(user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // check3: control if this user has a reservation in the reservation table db
        boolean isExist = reservationService.existsByUser(user);
        if(isExist) {
            throw new BadRequestException(ErrorMessages.USER_CANNOT_BE_DELETED);
        }

        userRepository.deleteById(id);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }


    // Supporting method to be used any method in this class
    public User getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessages.RESOURCE_NOT_FOUND_EXCEPTION,id)));

        return user;
    }


    private Set<Role> convertRoles(Set<String> pRoles) { // pRoles = {"Customer", "Administrator"}
        Set<Role> roles = new HashSet<>();
        if(pRoles==null) {
            Role userRole = roleService.findByType(RoleType.ROLE_CUSTOMER);
            roles.add(userRole);
        } else {
            pRoles.forEach(roleStr-> {
                if(roleStr.equals(RoleType.ROLE_ADMIN.getName())) {
                    Role adminRole = roleService.findByType(RoleType.ROLE_ADMIN);
                    roles.add(adminRole);
                } else {
                    Role userRole = roleService.findByType(RoleType.ROLE_CUSTOMER);
                    roles.add(userRole);
                }
            });
        }
        return roles;
    }



}
