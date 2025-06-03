package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// title repository
@Repository
public interface TitleRepository extends JpaRepository<Title, Integer> {
}
