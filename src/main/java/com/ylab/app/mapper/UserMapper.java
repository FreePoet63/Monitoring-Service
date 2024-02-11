package com.ylab.app.mapper;

/**
 * UserMapper interface
 *
 * @author razlivinsky
 * @since 07.02.2024
 */
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * UserMapper interface
 *
 * @author razlivinsky
 * @since 07.02.2024
 */
@Mapper
public interface UserMapper {
    /**
     * The constant INSTANCE.
     */
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    /**
     * User to user dto user dto.
     *
     * @param user the user
     * @return the user dto
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    UserDto userToUserDto(User user);

    /**
     * User dto to user user.
     *
     * @param userDto the user dto
     * @return the user
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    User userDtoToUser(UserDto userDto);
}
