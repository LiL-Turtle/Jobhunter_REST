package vn.lilturtle.jobhunter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createNewUser(
            @RequestBody User postManUser) {
        User binUser = this.userService.handleCreateUser(postManUser);
        return binUser;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleleUser(id);
        return "Delete user " + id + " success";
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") long id) {
        User user = this.userService.fetchUserById(id);
        return user;
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        List<User> listUser = this.userService.fetchAllUsers();
        return listUser;
    }

    @PutMapping("/user")
    public User udpateUser(@RequestBody User updateUser) {
        updateUser = this.userService.handleCreateUser(updateUser);
        return updateUser;
    }

}
