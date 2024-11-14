package vn.lilturtle.jobhunter.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.lilturtle.jobhunter.domain.Job;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.lilturtle.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.lilturtle.jobhunter.repository.JobRepository;
import vn.lilturtle.jobhunter.service.JobService;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;
import vn.lilturtle.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final JobRepository jobRepository;
    private final JobService jobService;

    public JobController(JobRepository jobRepository, JobService jobService) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a Job")
    public ResponseEntity<ResCreateJobDTO> postCreateJob(@Valid @RequestBody Job reqJob) {
        ResCreateJobDTO job = this.jobService.handleCreateJob(reqJob);
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @PutMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job reqJob) throws IdInvalidException {
        Optional<Job> jobOptional = this.jobRepository.findById(reqJob.getId());
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException(
                    "Job with id " + reqJob.getId() + " it not exists"
            );
        }
        ResUpdateJobDTO res = this.jobService.handleUpdateJob(reqJob);
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @DeleteMapping("jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable long id) throws IdInvalidException {
        Optional<Job> jobOptional = this.jobRepository.findById(id);
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException(
                    "Job with id " + id + " it not exists"
            );
        }
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("jobs/{id}")
    @ApiMessage("Get a job by id")
    public ResponseEntity<Job> getAJob(@PathVariable long id) throws IdInvalidException {
        Optional<Job> jobOptional = this.jobRepository.findById(id);
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException(
                    "Job with id " + id + " it not exists"
            );
        }
        Job job = jobOptional.get();
        return ResponseEntity.ok(jobOptional.get());
    }

    @GetMapping("/jobs")
    @ApiMessage("Get all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(
            @Filter
            Specification<Job> spec,
            Pageable pageable
    ) {
        ResultPaginationDTO job = this.jobService.fetchAll(spec, pageable);
        return ResponseEntity.ok().body(job);
    }
}
