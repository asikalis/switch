package com.switchapp.controller;

import com.switchapp.model.Role;
import com.switchapp.service.RoleService;
import com.switchapp.util.ResponseJson;
import com.switchapp.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<ResponseJson> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return RestUtil.response(HttpStatus.OK, "Roles fetched", roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseJson> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        if (role != null) {
            return RestUtil.response(HttpStatus.OK, "Role found", role);
        }
        return RestUtil.response(HttpStatus.NOT_FOUND, "Role not found", null);
    }

    @PostMapping
    public ResponseEntity<ResponseJson> createRole(@RequestBody Role role) {
        Role created = roleService.createRole(role);
        return RestUtil.response(HttpStatus.CREATED, "Role created", created);
    }

    @PutMapping("/{roleName}")
    public ResponseEntity<ResponseJson> updateRole(@PathVariable String roleName, @RequestBody Role role) {
        Role updated = roleService.updateRole(roleName, role);
        if (updated != null) {
            return RestUtil.response(HttpStatus.OK, "Role updated", updated);
        }
        return RestUtil.response(HttpStatus.NOT_FOUND, "Role not found", null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseJson> deleteRole(@PathVariable Long id) {
        boolean deleted = roleService.deleteRole(id);
        if (deleted) {
            return RestUtil.response(HttpStatus.OK, "Role deleted", null);
        }
        return RestUtil.response(HttpStatus.NOT_FOUND, "Role not found", null);
    }
}
