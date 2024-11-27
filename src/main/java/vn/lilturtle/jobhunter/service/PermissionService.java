package vn.lilturtle.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.lilturtle.jobhunter.domain.Permission;
import vn.lilturtle.jobhunter.domain.Skill;
import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.domain.response.ResUserDTO;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.repository.PermissionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission permission) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(
                permission.getModule(), permission.getApiPath(), permission.getMethod()
        );
    }

    public Permission fetchPermission(Long id) {
        Optional<Permission> optionalPer = this.permissionRepository.findById(id);
        if (optionalPer.isPresent()) {
            return optionalPer.get();
        }
        return null;
    }

    public boolean isIdExist(Long id) {
        return this.permissionRepository.existsById(id);
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Permission permission) {
        Permission currentPer = this.fetchPermission(permission.getId());

        if (currentPer != null) {

            if (permission.getName() != null && !permission.getName().equals(currentPer.getName())) {
                currentPer.setName(permission.getName());
            }
            if (permission.getApiPath() != null && !permission.getApiPath().equals(currentPer.getApiPath())) {
                currentPer.setApiPath(permission.getApiPath());
            }
            if (permission.getMethod() != null && !permission.getMethod().equals(currentPer.getMethod())) {
                currentPer.setMethod(permission.getMethod());
            }
            if (permission.getModule() != null && !permission.getModule().equals(currentPer.getModule())) {
                currentPer.setModule(permission.getModule());
            }

            currentPer = this.permissionRepository.save(currentPer);
        }
        return currentPer;
    }

    public ResultPaginationDTO fetchAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pPermissions = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pPermissions.getTotalPages());
        mt.setTotal(pPermissions.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pPermissions.getContent());
        return rs;
    }

    public void deletePermission(Long id) {
        // delete job (inside job_skill table)
        Optional<Permission> optionalPer = this.permissionRepository.findById(id);
        Permission currentPer;
        if (optionalPer.isPresent()) {
            currentPer = optionalPer.get();
        } else {
            currentPer = null;
        }

        currentPer.getRoles().forEach(role -> role.getPermissions().remove(currentPer));

        this.permissionRepository.delete(currentPer);
    }
    
}
