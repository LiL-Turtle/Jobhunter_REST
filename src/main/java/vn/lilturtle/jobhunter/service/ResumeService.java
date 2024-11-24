package vn.lilturtle.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.lilturtle.jobhunter.domain.Job;
import vn.lilturtle.jobhunter.domain.Resume;
import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.domain.response.ResUpdateUserDTO;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.domain.response.Resume.ResCreateResumeDTO;
import vn.lilturtle.jobhunter.domain.response.Resume.ResFetchResumeDTO;
import vn.lilturtle.jobhunter.domain.response.Resume.ResUpdateResumeDTO;
import vn.lilturtle.jobhunter.repository.JobRepository;
import vn.lilturtle.jobhunter.repository.ResumeRepository;
import vn.lilturtle.jobhunter.repository.UserRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository,
                         UserRepository UserRepository,
                         JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = UserRepository;
        this.jobRepository = jobRepository;
    }

    public boolean checkResumeExistByUserAndJob(Resume resume) {
        // check user by id
        if (resume.getUser() == null)
            return false;
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty())
            return false;

        // check job by id
        if (resume.getJob() == null)
            return false;
        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty())
            return false;

        return true;
    }

    public Resume fetchById(long id) {
        Optional<Resume> optionalResume = this.resumeRepository.findById(id);
        if (optionalResume.isPresent()) {
            return optionalResume.get();
        }
        return null;
    }

    public ResCreateResumeDTO handleCreateResume(Resume resume) {

        resume = this.resumeRepository.save(resume);

        ResCreateResumeDTO res = new ResCreateResumeDTO();
        res.setId(resume.getId());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());

        return res;
    }

    public ResUpdateResumeDTO handleUpdateResume(Resume resume) {
        Resume currentResume = this.fetchById(resume.getId());
        if (currentResume != null) {
            if (resume.getStatus() != null && !resume.getStatus().equals(currentResume.getStatus())) {
                currentResume.setStatus(resume.getStatus());
            }
            currentResume = this.resumeRepository.save(currentResume);
        }


        ResUpdateResumeDTO res = new ResUpdateResumeDTO();
        res.setUpdatedAt(currentResume.getUpdatedAt());
        res.setUpdatedBy(currentResume.getUpdatedBy());
        res.setStatus(currentResume.getStatus());
        return res;
    }

    public void deleteResume(long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResume(Resume resume) {
        ResFetchResumeDTO res = new ResFetchResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());

        if (resume.getJob() != null) {
            res.setCompanyName(resume.getJob().getCompany().getName());
        }

        res.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));
        return res;

    }

    public ResultPaginationDTO fetchAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageUser = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResFetchResumeDTO> listResume = pageUser.getContent()
                .stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
    }


}
