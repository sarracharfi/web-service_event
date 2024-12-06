package com.event.Service;

import com.event.Model.Notification;
import com.event.Repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification>  getByUserIdAndIsReadFalseOrderByTimestampDesc (long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByTimestampDesc(userId);
    }
    public List<Notification> getByUserId (long userId) {
        return notificationRepository.findByUserId(userId);
    }
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }
    public void markAsRead(long notificationId) {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setRead(true);
            notificationRepository.save(notification);
        } else {
            throw new EntityNotFoundException("Notification not found with ID: " + notificationId);

        }
    }
}