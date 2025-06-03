package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of notification type
@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Integer> {
}
