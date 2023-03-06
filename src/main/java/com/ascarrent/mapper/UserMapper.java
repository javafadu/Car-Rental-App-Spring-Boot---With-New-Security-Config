package com.ascarrent.mapper;

import com.ascarrent.domain.User;
import com.ascarrent.dto.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") // I can inject any class
public interface UserMapper {

    UserDTO userToUserDTO(User user);
    List<UserDTO> userListToUserDTOList(List<User> users);

}
