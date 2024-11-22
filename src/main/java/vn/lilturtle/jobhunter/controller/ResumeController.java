package vn.lilturtle.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.lilturtle.jobhunter.domain.Resume;
import vn.lilturtle.jobhunter.domain.response.ResUpdateUserDTO;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.domain.response.Resume.ResCreateResumeDTO;
import vn.lilturtle.jobhunter.domain.response.Resume.ResFetchResumeDTO;
import vn.lilturtle.jobhunter.domain.response.Resume.ResUpdateResumeDTO;
import vn.lilturtle.jobhunter.service.ResumeService;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;
import vn.lilturtle.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        //check id exist
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isIdExist) {
            throw new IdInvalidException(
                    "User or Job id is not exist"
            );
        }
        ResCreateResumeDTO resResume = this.resumeService.handleCreateResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(resResume);
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        Resume currentResume = this.resumeService.fetchById(resume.getId());
        if (currentResume == null) {
            throw new IdInvalidException(
                    "Resume with id " + resume.getId() + " does not exist"
            );
        }

        ResUpdateResumeDTO resResume = this.resumeService.handleUpdateResume(resume);

        return ResponseEntity.ok(resResume);
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) throws IdInvalidException {
        Resume resume = this.resumeService.fetchById(id);
        if (resume == null) {
            throw new IdInvalidException(
                    "Resume with id " + id + " does not exist"
            );
        }
        this.resumeService.deleteResume(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch a resume")
    public ResponseEntity<ResFetchResumeDTO> fetchAResume(@PathVariable Long id) throws IdInvalidException {
        Resume resume = this.resumeService.fetchById(id);
        if (resume == null) {
            throw new IdInvalidException(
                    "Resume with id " + id + " does not exist"
            );
        }
        ResFetchResumeDTO currentResume = this.resumeService.getResume(resume);
        return ResponseEntity.ok(currentResume);
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resume with paginate")
    public ResponseEntity<ResultPaginationDTO> fetchAll(
            @Filter Specification<Resume> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchAllResume(spec, pageable));
    }

}
