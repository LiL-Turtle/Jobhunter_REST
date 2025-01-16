package vn.lilturtle.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.lilturtle.jobhunter.domain.Skill;
import vn.lilturtle.jobhunter.domain.Subscriber;
import vn.lilturtle.jobhunter.service.SubscriberService;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    boolean existsByEmail(String email);

    List<Skill> findByIdIn(List<Long> id);

    Subscriber findByEmail(String email);
}
