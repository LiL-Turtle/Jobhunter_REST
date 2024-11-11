package vn.lilturtle.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.domain.response.ResCreateUserDTO;
import vn.lilturtle.jobhunter.domain.response.ResUpdateUserDTO;
import vn.lilturtle.jobhunter.domain.response.ResUserDTO;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
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
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser) throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailIsExist(postManUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email" + postManUser.getEmail() + " is already in use"
            );
        }

        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User binUser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(binUser));
    }

    // delete user
    @DeleteMapping("/users/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        boolean isIdExist = this.userService.isIdIsExist(id);

        if (!isIdExist) {
            throw new IdInvalidException(
                    "User with id " + id + " is not exist"
            );
        }

        this.userService.handleDeleleUser(id);
        return ResponseEntity.ok().build();
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        boolean isIdExist = this.userService.isIdIsExist(id);

        if (!isIdExist) {
            throw new IdInvalidException(
                    "Id " + id + " is not exist"
            );
        }

        User user = this.userService.fetchUserById(id);

        return ResponseEntity.ok(this.userService.convertToResUserDTO(user));
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
    }

    // update user
    @PutMapping("/users")
    @ApiMessage("update a user")
    public ResponseEntity<ResUpdateUserDTO> udpateUser(@RequestBody User updateUser) throws IdInvalidException {

        boolean isIdExist = this.userService.isIdIsExist(updateUser.getId());

        if (!isIdExist) {
            throw new IdInvalidException(
                    "Id " + updateUser.getId() + " is not exist"
            );
        }
        updateUser = this.userService.handleUpdateUser(updateUser);
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(updateUser));
    }

}
