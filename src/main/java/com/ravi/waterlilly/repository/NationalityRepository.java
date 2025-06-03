package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repository layer of civil Nationality entity
@Repository
public interface NationalityRepository extends JpaRepository<Nationality, Integer> {
    
    // Find nationality by name
    @Query("SELECT n FROM Nationality n WHERE n.name=?1")
    Nationality findByNationalityName(String nationality);
}
