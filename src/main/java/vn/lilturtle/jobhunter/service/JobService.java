package vn.lilturtle.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.lilturtle.jobhunter.domain.Company;
import vn.lilturtle.jobhunter.domain.Job;
import vn.lilturtle.jobhunter.domain.Skill;
import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.lilturtle.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.lilturtle.jobhunter.repository.JobRepository;
import vn.lilturtle.jobhunter.repository.SkillRepository;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResCreateJobDTO handleCreateJob(Job job) {
        if (job.getSkills() != null) {
            List<Long> reqskills = job.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqskills);
            job.setSkills(dbSkills);
        }
        // create job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResCreateJobDTO res = new ResCreateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLocation(currentJob.getLocation());
        res.setLevel(currentJob.getLevel());
        res.setStartDate(currentJob.getStartDate());
        res.setEndDate(currentJob.getEndDate());
        res.setActive(currentJob.isActive());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills().stream().map(x -> x.getName()).collect(Collectors.toList());
            res.setSkills(skills);
        }

        return res;
    }

    public Job fetchJobById(long id) {
        Optional<Job> optionalJob = this.jobRepository.findById(id);
        if (optionalJob.isPresent()) {
            return optionalJob.get();
        }
        return null;
    }

    public ResUpdateJobDTO handleUpdateJob(Job job) {
        // check skills
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }

        // update job
        Job currentJob = this.fetchJobById(job.getId());
        if (currentJob != null) {
            if (job.getName() != null && !job.getName().equals(currentJob.getName())) {
                currentJob.setName(job.getName());
            }
            if (job.getLocation() != null && !job.getLocation().equals(currentJob.getLocation())) {
                currentJob.setLocation(job.getLocation());
            }
            if (job.getSalary() != (currentJob.getSalary())) {
                currentJob.setSalary(job.getSalary());
            }
            if (job.getQuantity() != (currentJob.getQuantity())) {
                currentJob.setQuantity(job.getQuantity());
            }
            if (job.getLevel() != null && !job.getLevel().equals(currentJob.getLevel())) {
                currentJob.setLevel(job.getLevel());
            }
            if (job.getDescription() != null && !job.getDescription().equals(currentJob.getDescription())) {
                currentJob.setDescription(job.getDescription());
            }
            if (job.getStartDate() != null && !job.getStartDate().equals(currentJob.getStartDate())) {
                currentJob.setStartDate(job.getStartDate());
            }
            if (job.getEndDate() != null && !job.getEndDate().equals(currentJob.getEndDate())) {
                currentJob.setEndDate(job.getEndDate());
            }
            currentJob.setSkills(job.getSkills());
            currentJob.setActive(job.isActive());

            currentJob = this.jobRepository.save(currentJob);

        }


        // convert response
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setUpdatedAt(currentJob.getUpdatedAt());
        dto.setUpdatedBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }

        return dto;
    }

    public void handleDeleteJob(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageJob.getSize());

        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageJob.getContent());
        return rs;
    }
}
