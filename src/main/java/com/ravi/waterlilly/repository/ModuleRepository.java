package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Module;
import com.ravi.waterlilly.payload.module.ModuleWithPrivilegeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository to get module
@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {

    // get modules without privileges for specific role
    @Query("SELECT m FROM Module m WHERE m.id NOT IN (" +
            "SELECT p.module.id from Privilege p WHERE p.role.id = ?1)"
    )
    List<Module> findModulesWithoutPrivilegesForRole(Integer roleId);

    // Get module names of a user that has privileges
    @Query(value = "SELECT m.name FROM module AS m WHERE m.id IN (" +
            "SELECT p.module_id FROM privilege AS p WHERE p.role_id IN (" +
            "SELECT uhr.role_id FROM user_has_role AS uhr WHERE uhr.user_id = (" +
            "SELECT u.id FROM user AS u WHERE u.username = ?1))" +
            "AND (p.select_op = 1 OR p.insert_op = 1 OR p.update_op = 1 OR p.delete_op = 1))", nativeQuery = true)
    List<String> findAccessibleModulesForUser(String username);

    // Get modules of a user with their respective privileges
    @Query(value = "SELECT m.name AS module_name, MAX(p.select_op) AS select_privilege, MAX(p.insert_op) AS insert_privilege, MAX(p.update_op) AS update_privilege, MAX(p.delete_op) AS delete_privilege " +
            "FROM module AS m JOIN privilege AS p ON m.id = p.module_id " +
            "JOIN user_has_role AS uhr ON p.role_id = uhr.role_id " +
            "JOIN user AS u ON uhr.user_id = u.id " +
            "WHERE u.username = ?1 " +
            "GROUP BY m.name", nativeQuery = true)
    List<ModuleWithPrivilegeDTO> findModulePrivilegesForUser(String username);
}
