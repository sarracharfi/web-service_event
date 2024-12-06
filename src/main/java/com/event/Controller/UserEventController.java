package com.event.Controller;

import com.event.Model.Category;
import com.event.Model.Event;
import com.event.Model.Status;
import com.event.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class UserEventController {

    @Autowired
    private EventService eventService;


    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/category/{category}")
    public List<Event> getEventsByCategory(@PathVariable Category category) {
        return eventService.getAllEventsByCategory(category);
    }

    @GetMapping("/status/{status}")
    public List<Event> getEventsByStatus(@PathVariable Status status) {
        return eventService.getAllEventsByEtat(status);
    }

    @GetMapping("/{eventId}")
    public Optional<Event> getEventById(@PathVariable Long eventId) {
        return eventService.getEventById(eventId);
    }

    @PostMapping("/{eventId}/subscribe")
    public String subscribeToEvent(@RequestParam Long userId, @PathVariable Long eventId) {
        try {
            eventService.subscribeToEvent(userId, eventId);
            return "User subscribed to event successfully!";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    @DeleteMapping("/{eventId}/unsubscribe")
    public String unsubscribeFromEvent(@RequestParam Long userId, @PathVariable Long eventId) {
        try {
            eventService.unsubscribeFromEvent(userId, eventId);
            return "User unsubscribed from event successfully!";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

}
