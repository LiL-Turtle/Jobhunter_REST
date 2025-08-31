package vn.lilturtle.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.lilturtle.jobhunter.domain.Permission;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.repository.PermissionRepository;
import vn.lilturtle.jobhunter.service.PermissionService;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;
import vn.lilturtle.jobhunter.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) throws IdInvalidException {

        if (this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException(
                    "Permission already exists!"
            );
        }
        Permission per = this.permissionService.createPermission(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(per);
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        boolean isExist = this.permissionService.isIdExist(permission.getId());
        if (!isExist) {
            throw new IdInvalidException(
                    "Permission with id " + permission.getId() + " is not exist!"
            );
        }
        if (this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException(
                    "Permission already exist!"
            );
        }

        Permission udpatePer = this.permissionService.updatePermission(permission);

        return ResponseEntity.ok(udpatePer);
    }

    @GetMapping("/permissions/{id}")
    @ApiMessage("Fetch a permission")
    public ResponseEntity<Permission> getPermissionById(@PathVariable("id") long id) throws IdInvalidException {
        boolean isExist = this.permissionService.isIdExist(id);
        if (!isExist) {
            throw new IdInvalidException(
                    "Permission with id " + id + " is not exist!"
            );
        }
        Permission currentPer = this.permissionService.fetchPermission(id);
        return ResponseEntity.ok(currentPer);
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch all permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(
            @Filter Specification<Permission> spec, Pageable pageable) {
        ResultPaginationDTO rs = this.permissionService.fetchAllPermissions(spec, pageable);
        return ResponseEntity.ok(rs);
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete permission")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") long id) throws IdInvalidException {
        boolean isExist = this.permissionService.isIdExist(id);
        if (!isExist) {
            throw new IdInvalidException(
                    "Permission with id " + id + " is not exist!"
            );
        }
        this.permissionService.deletePermission(id);
        return ResponseEntity.ok().body(null);
    }
}
