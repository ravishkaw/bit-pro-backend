package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// User repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Find users by username
    @Query("SELECT u FROM User u WHERE u.username=?1")
    User findByUsername(String username);

    // Find users by email
    @Query("SELECT u FROM User u WHERE u.email=?1")
    User findByEmail(String email);

    // Search users
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.employee.fullName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<User> searchUsers(@Param("searchQuery") String searchQuery, Pageable pageable);

    // Find users by employee id
    @Query("SELECT u FROM User u WHERE u.employee.id =?1")
    User findUserByEmployeeId(Long employeeId);

    // count admins
    @Query("SELECT COUNT(u) FROM User u JOIN u.role r WHERE r.name = 'Admin'")
    Long countAdmins();

    // find users by role
    @Query("SELECT u FROM User u JOIN u.role r WHERE r.name = ?1")
    List<User> findUsersByRole(String role);
}
