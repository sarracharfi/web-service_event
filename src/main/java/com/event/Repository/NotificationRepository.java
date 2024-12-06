package com.event.Repository;

import com.event.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndIsReadFalseOrderByTimestampDesc(Long userId);

    List<Notification> findByUserId(Long userId);

    List<Notification> findByEventId(Long eventId);
}
