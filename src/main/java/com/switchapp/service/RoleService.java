package com.switchapp.service;

import com.switchapp.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    Role getRoleById(Long id);

    Role createRole(Role role);

    Role updateRole(String roleName, Role role);

    boolean deleteRole(Long id);
}
