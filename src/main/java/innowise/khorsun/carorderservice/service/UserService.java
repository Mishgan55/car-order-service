package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.UserDto;
import innowise.khorsun.carorderservice.model.UserUpdateModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto getUserDtoById(Integer id);
    List<UserDto> getAllUsers();
    void addUser(UserDto userDto);
    void removeUser(Integer id);
    void editUser(Integer id, UserUpdateModel userUpdateModel);
}
