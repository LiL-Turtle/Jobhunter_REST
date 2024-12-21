package vn.lilturtle.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.lilturtle.jobhunter.domain.Skill;
import vn.lilturtle.jobhunter.domain.Subscriber;
import vn.lilturtle.jobhunter.repository.SkillRepository;
import vn.lilturtle.jobhunter.repository.SubscriberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public boolean isEmailExist(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber handleSaveSubscriber(Subscriber subscriber) {

        if (subscriber.getSkills() != null) {
            List<Long> reqskills = subscriber.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqskills);
            subscriber.setSkills(dbSkills);
        }

        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber findById(Long id) {
        return this.subscriberRepository.findById(id).orElse(null);
    }

    public Subscriber handleUpdateSubscriber(Subscriber subsDB, Subscriber subsRequest) {
        //Check skill
        if (subsRequest.getSkills() != null) {
            List<Long> reqskills = subsRequest.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqskills);
            subsDB.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subsDB);
    }

}
