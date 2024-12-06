package com.event.Controller;

import com.event.Model.*;
import com.event.Service.EventService;
import com.event.Service.NotificationService;
import com.event.ws.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/organizer")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private NotificationHandler notificationHandler;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/events/create")
    public String createEvent(@RequestBody Event event) {
        try {
            eventService.saveEvent(event);
            return "Event created successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PutMapping("/events/{eventId}/update")
    public String updateEvent(@PathVariable Long eventId, @RequestBody Event event) {
        try {
            Optional<Event> existingEvent = eventService.getEventById(eventId);

            if (existingEvent.isPresent()) {
                event.setId(eventId);
                Set<User> subscribedUsers = event.getUsersSubscribed();
                for (User user : subscribedUsers) {
                    Notification notification = new Notification();
                    notification.setUser(user);
                    notification.setEvent(event);
                    notification.setMessage("The event '" + event.getTitle() + "' has started!");
                    notification.setRead(false);
                    notification.setTimestamp(LocalDateTime.now());
                    notificationService.save(notification);
                    System.out.println(user.getUsername());
                    notificationHandler.sendNotification(
                            "The event '" + event.getTitle() + "' has been Updated Check It Out!", String.valueOf(user.getId()));

                }

                eventService.saveEvent(event);
                return "Event updated successfully!";
            } else {
                return "Event not found!";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @DeleteMapping("/events/{eventId}/delete")
    public String deleteEvent(@PathVariable Long eventId) {
        try {
            eventService.deleteEvent(eventId);
            return "Event deleted successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/events/{eventId}/start")
    public String startEvent(@PathVariable Long eventId) {
        try {
            eventService.startEvent(eventId);
            return "Event started successfully!";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/events/{eventId}/cancel")
    public String cancelEvent(@PathVariable Long eventId) {
        try {
            eventService.cancelEvent(eventId);
            return "Event canceled successfully!";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }
}
