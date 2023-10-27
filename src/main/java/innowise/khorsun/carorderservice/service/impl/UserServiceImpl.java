package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.UserDto;
import innowise.khorsun.carorderservice.entity.User;
import innowise.khorsun.carorderservice.mapper.UserMapper;
import innowise.khorsun.carorderservice.model.UserUpdateModel;
import innowise.khorsun.carorderservice.repositorie.UserRepository;
import innowise.khorsun.carorderservice.service.UserService;
import innowise.khorsun.carorderservice.util.error.user.UserNotFoundException;
import innowise.khorsun.carorderservice.util.error.user.UserDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ResourceBundle resourceBundle;
    private static final String USER_NOT_FOUND="message.error.user_not_found";
    private static final String DUPLICATE_EMAIL_MESSAGE="message.error.duplicate_email";
    private static final String DUPLICATE_PHONE_MESSAGE="message.error.duplicate_phone";

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ResourceBundle resourceBundle) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public UserDto getUserDtoById(Integer id) {
        return userRepository
                .findById(id)
                .map(userMapper::userToUserDto)
                .orElseThrow(() -> new UserNotFoundException(resourceBundle.getString(USER_NOT_FOUND) + id, new Date()));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::userToUserDto)
                .toList();
    }

    @Override
    @Transactional
    public void addUser(UserDto userDto) {
        validateUserUniqueness(userDto);
        User user = userMapper.userDtoToUser(userDto);
        user.setCreationDate(new Date());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void editUser(Integer id, UserUpdateModel userUpdateModel) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(resourceBundle.getString(USER_NOT_FOUND) + id, new Date()));

        user.setFirstName(userUpdateModel.getFirstName());
        user.setLastName(userUpdateModel.getLastName());
    }

    private void validateUserUniqueness(UserDto userDto) {
        isUserEmailUnique(userDto);
        isUserPhoneNumberUnique(userDto);
    }

    private void isUserEmailUnique(UserDto userDto) {
        if (userRepository.findUserByEmail(userDto.getEmail()).isPresent()){
            throw new UserDuplicateException(resourceBundle.getString(DUPLICATE_EMAIL_MESSAGE) + userDto.getEmail(),
                    new Date());
        }
    }

    private void isUserPhoneNumberUnique(UserDto userDto) {
        if (userRepository.findUserByPhoneNumber(userDto.getPhoneNumber()).isPresent()){
            throw new UserDuplicateException(resourceBundle.getString(DUPLICATE_PHONE_MESSAGE) + userDto.getPhoneNumber(),
                    new Date());
        }
    }
}
