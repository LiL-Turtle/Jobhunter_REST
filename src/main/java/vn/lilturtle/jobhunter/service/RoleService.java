package vn.lilturtle.jobhunter.service;

import com.turkraft.springfilter.transformer.processor.CurrentDateFunctionExpressionProcessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.lilturtle.jobhunter.domain.Permission;
import vn.lilturtle.jobhunter.domain.Role;
import vn.lilturtle.jobhunter.domain.Skill;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.repository.PermissionRepository;
import vn.lilturtle.jobhunter.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentDateFunctionExpressionProcessor currentDateFunctionExpressionProcessor;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository, CurrentDateFunctionExpressionProcessor currentDateFunctionExpressionProcessor) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.currentDateFunctionExpressionProcessor = currentDateFunctionExpressionProcessor;
    }

    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public boolean existById(Long id) {
        return this.roleRepository.existsById(id);
    }

    public Role fetchRoleById(Long id) {
        Optional<Role> role = this.roleRepository.findById(id);
        if (role.isPresent()) {
            return role.get();
        }
        return null;
    }


    public Role createRole(Role role) {
        // check permission
        if (role.getPermissions() != null) {
            List<Long> reqPer = role.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());

            List<Permission> dbPer = this.permissionRepository.findByIdIn(reqPer);
            role.setPermissions(dbPer);
        }

        // create Role
        Role currentRole = this.roleRepository.save(role);

        return this.roleRepository.save(currentRole);
    }

    public Role updateRole(Role role) {
        // check permission
        if (role.getPermissions() != null) {
            List<Long> reqPer = role.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());

            List<Permission> dbPer = this.permissionRepository.findByIdIn(reqPer);
            role.setPermissions(dbPer);
        }

        Role currentRole = this.fetchRoleById(role.getId());
        if (currentRole != null) {
            if (role.getName() != null && !role.getName().equals(currentRole.getName())) {
                currentRole.setName(role.getName());
            }
            if (role.getDescription() != null && !role.getDescription().equals(currentRole.getDescription())) {
                currentRole.setDescription(role.getDescription());
            }
            if (role.getDescription() != null && !role.getDescription().equals(currentRole.getDescription())) {
                currentRole.setDescription(role.getDescription());
            }
            if (role.getDescription() != null && !role.getDescription().equals(currentRole.getDescription())) {
                currentRole.setDescription(role.getDescription());
            }
            currentRole.setPermissions(role.getPermissions());
            currentRole.setActive(role.isActive());

            currentRole = this.roleRepository.save(currentRole);
        }
        return currentRole;
    }

    public ResultPaginationDTO fetchAllRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pPermissions = this.roleRepository.findAll(spec, pageable);
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

    public void deleteRole(long id) {
        Role currentRole = this.fetchRoleById(id);
        this.roleRepository.delete(currentRole);
    }

}
