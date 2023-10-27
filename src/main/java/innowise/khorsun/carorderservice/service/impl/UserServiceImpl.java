package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.UserDto;
import innowise.khorsun.carorderservice.entity.User;
import innowise.khorsun.carorderservice.mapper.UserMapper;
import innowise.khorsun.carorderservice.model.UserUpdateModel;
import innowise.khorsun.carorderservice.repositorie.UserRepository;
import innowise.khorsun.carorderservice.service.UserService;
import innowise.khorsun.carorderservice.util.PropertyUtil;
import innowise.khorsun.carorderservice.util.error.user.UserNotFoundException;
import innowise.khorsun.carorderservice.util.error.user.UserDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto getUserDtoById(Integer id) {
        return userRepository
                .findById(id)
                .map(userMapper::userToUserDto)
                .orElseThrow(() -> new UserNotFoundException(PropertyUtil.USER_NOT_FOUND + id, new Date()));
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
                .orElseThrow(() -> new UserNotFoundException(PropertyUtil.USER_NOT_FOUND + id, new Date()));

        user.setFirstName(userUpdateModel.getFirstName());
        user.setLastName(userUpdateModel.getLastName());
    }

    private void validateUserUniqueness(UserDto userDto) {
        isUserEmailUnique(userDto);
        isUserPhoneNumberUnique(userDto);
    }

    private void isUserEmailUnique(UserDto userDto) {
        if (userRepository.findUserByEmail(userDto.getEmail()).isPresent()){
            throw new UserDuplicateException(PropertyUtil.DUPLICATE_EMAIL_MESSAGE + userDto.getEmail(),
                    new Date());
        }
    }

    private void isUserPhoneNumberUnique(UserDto userDto) {
        if (userRepository.findUserByPhoneNumber(userDto.getPhoneNumber()).isPresent()){
            throw new UserDuplicateException(PropertyUtil.DUPLICATE_PHONE_MESSAGE + userDto.getPhoneNumber(),
                    new Date());
        }
    }
}
