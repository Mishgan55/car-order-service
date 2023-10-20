package innowise.khorsun.carorderservice.mapper;

import innowise.khorsun.carorderservice.dto.UserDto;
import innowise.khorsun.carorderservice.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    User userDtoToUser(UserDto userDto);
    UserDto userToUserDto(User user);
}
