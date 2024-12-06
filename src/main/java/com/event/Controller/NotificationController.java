package com.event.Controller;


import com.event.Model.Event;

import com.event.Model.Notification;
import com.event.Service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notification")
public class NotificationController {


    @Autowired
    private NotificationService notificationService;


    @GetMapping("/all/{userId}")
    public List<Notification> getAllNotifications(@PathVariable int userId) {
        return notificationService.getByUserId(userId);
    }


    @GetMapping("/unread/{userId}")
    public List<Notification>  getByUserIdAndIsReadFalseOrderByTimestampDesc (@PathVariable long userId) {
        return notificationService.getByUserIdAndIsReadFalseOrderByTimestampDesc(userId);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(@PathVariable("id") long notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok("Notification marked as read.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while marking the notification as read.");
        }
    }



}
