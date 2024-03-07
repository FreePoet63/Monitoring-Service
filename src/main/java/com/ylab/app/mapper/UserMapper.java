package com.ylab.app.mapper;

import com.ylab.app.model.User;
import com.ylab.app.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The UserMapper interface provides methods for mapping User objects to UserDto objects and vice versa.
 *
 * @author razlivinsky
 * @since 07.02.2024
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Maps a User object to a UserDto object.
     *
     * @param user the user object to be mapped
     * @return the mapped UserDto object
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    UserDto userToUserDto(User user);

    /**
     * Maps a UserDto object to a User object.
     *
     * @param userDto the userDto object to be mapped
     * @return the mapped User object
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    User userDtoToUser(UserDto userDto);
}
