package innowise.khorsun.carorderservice.controller;

import innowise.khorsun.carorderservice.dto.UserDto;
import innowise.khorsun.carorderservice.model.UserUpdateModel;
import innowise.khorsun.carorderservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/add")
    public void addUser(@RequestBody @Valid UserDto userDto) {
        userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable("id") Integer id) {
        userService.removeUser(id);
    }

    @PatchMapping("/{id}/edit")
    public void editUser(@PathVariable("id") Integer id,
                         @RequestBody @Valid UserUpdateModel userUpdateModel) {
        userService.editUser(id, userUpdateModel);
    }
}
