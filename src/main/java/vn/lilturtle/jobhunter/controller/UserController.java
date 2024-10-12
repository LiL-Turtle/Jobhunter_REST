package vn.lilturtle.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.domain.dto.ResultPaginationDTO;
import vn.lilturtle.jobhunter.service.UserService;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;
import vn.lilturtle.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // create user
    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User binUser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(binUser);
    }

    // delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("ID must be less than 1500");
        }

        this.userService.handleDeleleUser(id);
        return ResponseEntity.ok("Delete user with id " + id + " success");
//         return ResponseEntity.ok("Delete user " + id + " success");
//         return ResponseEntity.status(HttpStatus.OK).body("Delete user " + id + "
//         success");
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User user = this.userService.fetchUserById(id);
        return ResponseEntity.ok(user);
        // return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    // fetch all users
    @GetMapping("/users")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter
            Specification<User> spec,
            Pageable pageable
    ) {
        ResultPaginationDTO rs = this.userService.fetchAllUsers(spec, pageable);
        return ResponseEntity.ok(rs);
        // return ResponseEntity.status(HttpStatus.OK).body(listUser);
    }

    // update user
    @PutMapping("/users")
    public ResponseEntity<User> udpateUser(@RequestBody User updateUser) {
        updateUser = this.userService.handleUpdateUser(updateUser);
        return ResponseEntity.ok(updateUser);
        // return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateUser);
    }

}
