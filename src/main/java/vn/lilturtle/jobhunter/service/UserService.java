package vn.lilturtle.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public void handleDeleleUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public List<User> fetchAllUsers() {
        return this.userRepository.findAll();
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.fetchUserById(user.getId());
        if (currentUser != null) {

            if (user.getName() != null && !user.getName().equals(currentUser.getName())) {
                currentUser.setName(user.getName());
            } else if (user.getEmail() != null && !user.getEmail().equals(currentUser.getEmail())) {
                currentUser.setEmail(user.getEmail());
            } else if (currentUser.getPassword() != null && !user.getPassword().equals(currentUser.getPassword())) {
                currentUser.setPassword(user.getPassword());
            }

            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUserName(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username);
    }

}