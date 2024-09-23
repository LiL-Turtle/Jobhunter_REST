package vn.lilturtle.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {
        User binUser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(binUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleleUser(id);
        return ResponseEntity.noContent().build();
        // return ResponseEntity.ok("Delete user " + id + " success");
        // return ResponseEntity.status(HttpStatus.OK).body("Delete user " + id + "
        // success");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User user = this.userService.fetchUserById(id);
        return ResponseEntity.ok(user);
        // return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> listUser = this.userService.fetchAllUsers();
        return ResponseEntity.ok(listUser);
        // return ResponseEntity.status(HttpStatus.OK).body(listUser);
    }

    @PutMapping("/users")
    public ResponseEntity<User> udpateUser(@RequestBody User updateUser) {
        updateUser = this.userService.handleUpdateUser(updateUser);
        return ResponseEntity.ok(updateUser);
        // return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateUser);
    }

}
