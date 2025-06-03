package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Privilege;
import com.ravi.waterlilly.payload.privilege.PrivilegeOfModuleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Privilege repository
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    // get privileges without admin ones
    @Query("SELECT p FROM Privilege p WHERE p.role.name != 'Admin'")
    Page<Privilege> findAllNonAdminPrivileges(Pageable pageable);

    // find privilege by module id and role id
    @Query("SELECT p FROM Privilege p WHERE p.role.id=?1 AND p.module.id=?2")
    Privilege findPrivilegeByRoleAndModule(Integer roleId, Integer moduleId);

    // get privilege of a module by the user
    @Query(value = "SELECT MAX(p.select_op) AS select_privilege, " +
            "MAX(p.insert_op) AS insert_privilege, " +
            "MAX(p.update_op) AS update_privilege, " +
            "MAX(p.delete_op) AS delete_privilege " +
            "FROM module AS m JOIN privilege AS p ON m.id = p.module_id " +
            "JOIN user_has_role AS uhr ON p.role_id = uhr.role_id " +
            "JOIN user AS u ON uhr.user_id = u.id " +
            "WHERE u.username = ?1 and m.name= ?2", nativeQuery = true)
    PrivilegeOfModuleDTO findUserPrivilegesForModule(String username, String moduleName);

    // Search Privileges ( filter admin )
    @Query("SELECT p FROM Privilege p WHERE " +
            "(LOWER(p.role.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(p.module.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) AND " +
            "p.role.name != 'Admin'")
    Page<Privilege> searchNonAdminPrivileges(@Param("searchQuery") String searchQuery, Pageable pageable);
}
