package vn.lilturtle.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("/user/create")
    @PostMapping("/user/create")
    public User createNewUser(
            @RequestBody User postManUser) {


        User binUser = this.userService.handleCreateUser(postManUser);

        return binUser;
    }
}
