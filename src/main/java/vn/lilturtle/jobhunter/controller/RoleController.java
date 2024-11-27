package vn.lilturtle.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.lilturtle.jobhunter.domain.Role;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.service.RoleService;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;
import vn.lilturtle.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {

        //check role exist
        boolean isExist = this.roleService.existByName(role.getName());
        if (isExist) {
            throw new IdInvalidException(
                    "Role with name " + role.getName() + " already exists"
            );
        }
        Role currentRole = this.roleService.createRole(role);

        return ResponseEntity.status(HttpStatus.CREATED).body(currentRole);
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
        boolean isNameExist = this.roleService.existByName(role.getName());
        boolean isIdExist = this.roleService.existById(role.getId());

        if (isNameExist) {
            throw new IdInvalidException(
                    "Role with name " + role.getName() + " already exists"
            );
        }
        if (!isIdExist) {
            throw new IdInvalidException(
                    "Role with id " + role.getId() + " does not exist"
            );
        }
        Role currentRole = this.roleService.updateRole(role);
        return ResponseEntity.ok(currentRole);
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch a role")
    public ResponseEntity<Role> getRole(@PathVariable long id) throws IdInvalidException {
        boolean isRoleExist = this.roleService.existById(id);
        if (!isRoleExist) {
            throw new IdInvalidException(
                    "Role with id " + id + " does not exist"
            );
        }

        Role role = this.roleService.fetchRoleById(id);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch all roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification<Role> spec, Pageable pageable) {
        ResultPaginationDTO rs = this.roleService.fetchAllRoles(spec, pageable);
        return ResponseEntity.ok(rs);
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete role")
    public ResponseEntity<Void> deleteRole(@PathVariable long id) throws IdInvalidException {
        boolean isRoleExist = this.roleService.existById(id);
        if (!isRoleExist) {
            throw new IdInvalidException(
                    "Role with id " + id + " does not exist"
            );
        }
        this.roleService.deleteRole(id);
        return ResponseEntity.ok().body(null);
    }


}
