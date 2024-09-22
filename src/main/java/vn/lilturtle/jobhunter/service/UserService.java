package vn.lilturtle.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleleUser(long id){
        this.userRepository.deleteById(id);
    }






}