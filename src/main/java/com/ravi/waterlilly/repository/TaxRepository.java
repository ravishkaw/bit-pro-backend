package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// repository layer of tax
@Repository
public interface TaxRepository extends JpaRepository<Tax, Integer> {

    // find tax by name
    @Query("SELECT t FROM Tax t WHERE t.name=?1")
    Tax findTaxByName(String name);
}
