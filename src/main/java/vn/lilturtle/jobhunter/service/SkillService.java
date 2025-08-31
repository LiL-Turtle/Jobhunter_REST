package vn.lilturtle.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.lilturtle.jobhunter.domain.Skill;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.repository.SkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleSaveSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    public boolean isIdExist(long id) {
        return this.skillRepository.existsById(id);
    }

    public Skill fetchSkillById(long id) {
        Optional<Skill> optionalSkill = this.skillRepository.findById(id);
        if (optionalSkill.isPresent()) {
            return optionalSkill.get();
        }
        return null;
    }

    public Skill handleUpdateSkill(Skill skill) {
        Skill currentSkill = this.fetchSkillById(skill.getId());

        if (currentSkill != null) {
            if (!skill.getName().equals(currentSkill.getName())) {
                currentSkill.setName(skill.getName());
            }
        }

        return this.skillRepository.save(currentSkill);
    }

    public ResultPaginationDTO fetchAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageSkill.getContent());

        return rs;
    }

    public void handleDeleteSkill(long id) {

        // delete job (inside job_skill table)
        Optional<Skill> optionalSkill = this.skillRepository.findById(id);
        Skill currentSkill;
        if (optionalSkill.isPresent()) {
            currentSkill = optionalSkill.get();
        } else {
            currentSkill = null;
        }
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        //delete subscriber (inside subscriber_skill_table)
        currentSkill.getSubscribers().forEach(sub -> sub.getSkills().remove(currentSkill));

        //delete skill
        this.skillRepository.delete(currentSkill);
    }
}
