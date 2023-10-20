package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.UserDto;
import innowise.khorsun.carorderservice.entity.User;
import innowise.khorsun.carorderservice.mapper.UserMapper;
import innowise.khorsun.carorderservice.model.UserUpdateModel;
import innowise.khorsun.carorderservice.repositorie.UserRepository;
import innowise.khorsun.carorderservice.service.UserService;
import innowise.khorsun.carorderservice.util.error.user.UserNotFoundException;
import innowise.khorsun.carorderservice.util.error.user.UserCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new UserNotFoundException("User not found", new Date()));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository
                .findAll().stream()
                .map(userMapper::userToUserDto).toList();
    }

    @Override
    @Transactional
    public void addUser(UserDto userDto) {
        if (!isUserEmailUnique(userDto.getEmail())) {
            throw new UserCustomerException("User with this email is already exist", new Date());
        }
        if (!isUserPhoneNumberUnique(userDto.getPhoneNumber())) {
            throw new UserCustomerException("User with this phone number is already exist", new Date());
        }
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
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setFirstName(userUpdateModel.getFirstName());
            updatedUser.setLastName(userUpdateModel.getLastName());
        } else {
            throw new UserNotFoundException("User not found", new Date());
        }
    }

    public boolean isUserEmailUnique(String email) {
        return userRepository.findUserByEmail(email).isEmpty();
    }

    public boolean isUserPhoneNumberUnique(String phoneNumber) {
        return userRepository.findUserByPhoneNumber(phoneNumber).isEmpty();
    }
}