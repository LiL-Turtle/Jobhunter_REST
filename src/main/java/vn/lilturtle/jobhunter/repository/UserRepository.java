package vn.lilturtle.jobhunter.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.lilturtle.jobhunter.domain.Company;
import vn.lilturtle.jobhunter.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    @Override
    List<User> findAll(Specification<User> spec);

    boolean existsByEmail(String email);

    boolean existsById(Long id);

    User findByRefreshTokenAndEmail(String refreshToken, String email);

    List<User> findByCompany(Company company);
}
