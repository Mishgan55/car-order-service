package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.UserDto;
import innowise.khorsun.carorderservice.entity.User;
import innowise.khorsun.carorderservice.mapper.UserMapper;
import innowise.khorsun.carorderservice.model.UserUpdateModel;
import innowise.khorsun.carorderservice.repositorie.UserRepository;
import innowise.khorsun.carorderservice.service.impl.UserServiceImpl;
import innowise.khorsun.carorderservice.util.error.user.UserCustomerException;
import innowise.khorsun.carorderservice.util.error.user.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//mvn clean test jacoco:report
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Test
    void testGetUserDtoById() {
        // Arrange
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        UserDto userDto = new UserDto();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(user)).thenReturn(userDto);

        // Act
        UserDto result = userService.getUserDtoById(userId);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDto, result);
    }
    @Test
    void testGetUserDtoById_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Вызываем метод и ожидаем исключение UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> userService.getUserDtoById(1));
    }
    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = List.of(new User(), new User());
        List<UserDto> userDto = List.of(new UserDto(), new UserDto());
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.userToUserDto(any())).thenReturn(userDto.get(0), userDto.get(1));

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }
    @Test
    void testAddUser() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setFirstName("Test");
        userDto.setLastName("Test");
        userDto.setEmail("test@example.com");
        userDto.setPhoneNumber("+123456789011");

        User user = new User();
        when(userRepository.findUserByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findUserByPhoneNumber(userDto.getPhoneNumber())).thenReturn(Optional.empty());
        when(userMapper.userDtoToUser(userDto)).thenReturn(user);

        // Act
        userService.addUser(userDto);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void testIsUserEmailUnique_UniqueEmail() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        boolean isUnique = userService.isUserEmailUnique("test@example.com");

        Assertions.assertTrue(isUnique);
    }

    @Test
    void testIsUserEmailUnique_DuplicateEmail() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(new User()));

        boolean isUnique = userService.isUserEmailUnique("test@example.com");

        Assertions.assertFalse(isUnique);
    }

    @Test
    void testIsUserPhoneNumberUnique_UniquePhoneNumber() {
        when(userRepository.findUserByPhoneNumber(anyString())).thenReturn(Optional.empty());

        boolean isUnique = userService.isUserPhoneNumberUnique("1234567890");

        Assertions.assertTrue(isUnique);
    }

    @Test
    void testIsUserPhoneNumberUnique_DuplicatePhoneNumber() {
        when(userRepository.findUserByPhoneNumber(anyString())).thenReturn(Optional.of(new User()));

        boolean isUnique = userService.isUserPhoneNumberUnique("1234567890");

        Assertions.assertFalse(isUnique);
    }


    @Test
    void testAddUser_DuplicatePhoneNumber() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByPhoneNumber(anyString())).thenReturn(Optional.of(new User()));

        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPhoneNumber("1234567890");

        assertThrows(UserCustomerException.class, () -> userService.addUser(userDto));
    }
    @Test
    void testAddUser_DuplicateEmail() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(new User()));

        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPhoneNumber("1234567890");

        assertThrows(UserCustomerException.class, () -> userService.addUser(userDto));
    }
    @Test
    void testRemoveUser() {
        // Arrange
        Integer userId = 1;
        //Используем doNothing(), чтобы задать пустое поведение для метода
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.removeUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
    @Test
    void testEditUser() {
        // Arrange
        Integer userId = 1;
        String newFirstName = "NewFirst";
        String newLastName = "NewLast";
        UserUpdateModel userUpdateModel = new UserUpdateModel();
        userUpdateModel.setFirstName(newFirstName);
        userUpdateModel.setLastName(newLastName);

        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.editUser(userId, userUpdateModel);

        // Assert
        // Проверяем, что метод findById был вызван ровно 1 раза с аргументом "userId"
        verify(userRepository, times(1)).findById(userId);
        Assertions.assertEquals(newFirstName, user.getFirstName());
        Assertions.assertEquals(newLastName, user.getLastName());
    }
    @Test
    void testEditUser_UserNotFound(){
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserUpdateModel userUpdateModel = new UserUpdateModel();

        assertThrows(UserNotFoundException.class,()->userService.editUser(1,userUpdateModel));
    }
}

