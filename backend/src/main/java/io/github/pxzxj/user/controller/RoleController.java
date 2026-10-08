package io.github.pxzxj.user.controller;

import io.github.pxzxj.user.entity.Role;
import io.github.pxzxj.user.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/api/role/page")
    public Page<Role> page(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }
}
