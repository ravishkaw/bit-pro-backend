package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// repository layer of task
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // search task
    @Query("SELECT t FROM Task t WHERE " +
            "(LOWER(t.assignedTo.fullName) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) OR " +
            "(LOWER(t.description) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) OR " +
            "(LOWER(t.taskType.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) OR " +
            "(LOWER(t.targetType.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) OR " +
            "(LOWER(t.taskStatus.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
    Page<Task> searchTasks(@Param("searchQuery") String searchQuery, Pageable pageable);

    // check if there is any tasks for the room or venue
    @Query("SELECT COUNT(t)>0 FROM Task t WHERE " +
            "t.targetId=?1 AND t.targetType.id =?2 AND t.taskType.id = ?3 AND " +
            "t.taskStatus.name NOT IN ('Cancelled', 'Completed')")
    boolean checkIfTasksExists(Long targetId, Integer targetTypeId, Integer taskTypeId);

    @Query("SELECT COUNT(t)>0 FROM Task t WHERE " +
            "t.targetId=?1 AND t.targetType.id =?2 AND t.id != ?4 AND t.taskType.id = ?3 AND " +
            "t.taskStatus.name NOT IN ('Cancelled', 'Completed')")
    boolean checkIfTasksExistsExcludingId(Long targetId, Integer targetTypeId, Integer taskTypeId, Long taskId);
}
