package vn.lilturtle.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.lilturtle.jobhunter.domain.Skill;
import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.service.SkillService;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;
import vn.lilturtle.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> postCreateSkill(@Valid @RequestBody Skill reqSkill) throws IdInvalidException {
        Boolean isNameExist = this.skillService.isNameExist(reqSkill.getName());

        if (isNameExist) {
            throw new IdInvalidException(
                    "Name " + reqSkill.getName() + " already exists!"
            );
        }

        Skill skill = this.skillService.handleSaveSkill(reqSkill);
        return ResponseEntity.status(HttpStatus.CREATED).body(skill);
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> postUpdateSkill(@Valid @RequestBody Skill reqSkill) throws IdInvalidException {
        Boolean isNameExist = this.skillService.isNameExist(reqSkill.getName());
        boolean isIdExist = this.skillService.isIdExist(reqSkill.getId());

        if (!isIdExist) {
            throw new IdInvalidException(
                    "Id " + reqSkill.getId() + " is not exists!"
            );
        }

        if (isNameExist) {
            throw new IdInvalidException(
                    "Name " + reqSkill.getName() + " already exists!"
            );
        }
        Skill skill = this.skillService.handleUpdateSkill(reqSkill);
        return ResponseEntity.ok(skill);
    }

    @GetMapping("/skills/{id}")
    @ApiMessage("Fetch a skill")
    public ResponseEntity<Skill> getSkill(@PathVariable("id") long id) throws IdInvalidException {
        boolean isIdExist = this.skillService.isIdExist(id);
        if (!isIdExist) {
            throw new IdInvalidException(
                    "Id " + id + " is not exists!"
            );
        }
        Skill skill = this.skillService.fetchSkillById(id);
        return ResponseEntity.ok(skill);
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(
            @Filter
            Specification<Skill> spec,
            Pageable pageable
    ) {
        ResultPaginationDTO rs = this.skillService.fetchAllSkills(spec, pageable);
        return ResponseEntity.ok(rs);
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.fetchSkillById(id);
        if (currentSkill == null) {
            throw new IdInvalidException(
                    "Skill id " + id + " is not exists!"
            );
        }
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.ok().body(null);

    }

}
