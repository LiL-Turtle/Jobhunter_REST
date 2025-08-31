package vn.lilturtle.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.lilturtle.jobhunter.domain.Company;
import vn.lilturtle.jobhunter.domain.Role;
import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.domain.response.*;
import vn.lilturtle.jobhunter.repository.UserRepository;

import javax.swing.text.html.Option;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, CompanyService companyService, RoleService roleService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    public User handleCreateUser(User user) {
        //check company
        if (user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }

        //check role
        if (user.getRole() != null) {
            Role role = this.roleService.fetchRoleById(user.getRole().getId());
            user.setRole(role != null ? role : null);
        }

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

    public ResultPaginationDTO fetchAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageUser.getSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        //remove sentitive data
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.fetchUserById(user.getId());
        if (currentUser != null) {

            if (user.getName() != null && !user.getName().equals(currentUser.getName())) {
                currentUser.setName(user.getName());
            }
            if (user.getEmail() != null && !user.getEmail().equals(currentUser.getEmail())) {
                currentUser.setEmail(user.getEmail());
            }
            if (user.getAge() != currentUser.getAge()) {
                currentUser.setAge(user.getAge());
            }
            if (user.getAddress() != null && !user.getAddress().equals(currentUser.getAddress())) {
                currentUser.setAddress(user.getAddress());
            }
            if (user.getGender() != null && !user.getGender().equals(currentUser.getGender())) {
                currentUser.setGender(user.getGender());
            }
            if (user.getCompany() != null && !user.getCompany().equals(currentUser.getCompany())) {
                Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
                currentUser.setCompany(companyOptional.orElse(null));
            }
            if (user.getRole() != null && !user.getRole().equals(currentUser.getRole())) {
                Role role = this.roleService.fetchRoleById(user.getRole().getId());
                currentUser.setRole(role);
            }

            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUserName(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailIsExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.companyUser com = new ResCreateUserDTO.companyUser();
        ResCreateUserDTO.roleUser role = new ResCreateUserDTO.roleUser();

        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setAddress(user.getAddress());

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }
        if (user.getRole() != null) {
            role.setId(user.getRole().getId());
            role.setName(user.getRole().getName());
            res.setRole(role);
        }

        return res;
    }

    public boolean isIdIsExist(long id) {
        return this.userRepository.existsById(id);
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.companyUser com = new ResUpdateUserDTO.companyUser();
        ResUpdateUserDTO.roleUser role = new ResUpdateUserDTO.roleUser();

        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setAddress(user.getAddress());

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }
        if (user.getRole() != null) {
            role.setId(user.getRole().getId());
            role.setName(user.getRole().getName());
            res.setRole(role);
        }


        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();
        ResUserDTO.RoleUser role = new ResUserDTO.RoleUser();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }

        if (user.getRole() != null) {
            role.setId(user.getRole().getId());
            role.setName(user.getRole().getName());
            res.setRole(role);
        }

        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUserName(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}