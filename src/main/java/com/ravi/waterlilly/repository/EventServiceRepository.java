package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// repository layer of event service
@Repository
public interface EventServiceRepository extends JpaRepository<EventService, Integer> {

    // search event services
    @Query("SELECT es FROM EventService es WHERE " +
            "LOWER(es.name) LIKE LOWER(CONCAT('%', :searchQuery,'%')) OR " +
            "LOWER(es.description) LIKE LOWER(CONCAT('%', :searchQuery,'%'))")
    Page<EventService> searchEventServices(@Param("searchQuery") String searchQuery, Pageable pageable);

    // find event service by name
    @Query("SELECT es FROM EventService es WHERE es.name=?1")
    EventService findByName(String name);
}
