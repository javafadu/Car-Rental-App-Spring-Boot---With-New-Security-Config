package com.ascarrent.mapper;

import com.ascarrent.domain.Role;
import com.ascarrent.domain.User;
import com.ascarrent.dto.UserDTO;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-06T18:35:10+0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.13 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        Set<Role> set = user.getRoles();
        if ( set != null ) {
            userDTO.setRoles( new LinkedHashSet<Role>( set ) );
        }
        userDTO.setId( user.getId() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setPhoneNumber( user.getPhoneNumber() );
        userDTO.setAddress( user.getAddress() );
        userDTO.setZipCode( user.getZipCode() );
        userDTO.setBirthDate( user.getBirthDate() );
        userDTO.setBuiltIn( user.getBuiltIn() );

        return userDTO;
    }

    @Override
    public List<UserDTO> userListToUserDTOList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( users.size() );
        for ( User user : users ) {
            list.add( userToUserDTO( user ) );
        }

        return list;
    }
}
