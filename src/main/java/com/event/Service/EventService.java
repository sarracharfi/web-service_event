package com.event.Service;

import com.event.Model.*;
import com.event.Repository.EventRepository;
import com.event.Repository.UserRepository;
import com.event.ws.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationHandler notificationHandler;
    @Autowired
    private NotificationService notificationService;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getAllEventsByCategory(Category category) {
        return eventRepository.findEventsByCategory(category);
    }

    public List<Event> getAllEventsByEtat(Status status) {
        return eventRepository.findEventsByStatus(status);
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public void saveEvent(Event item) {
        eventRepository.save(item);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public void subscribeToEvent(Long userId, Long eventId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (userOptional.isPresent() && eventOptional.isPresent()) {
            User user = userOptional.get();
            Event event = eventOptional.get();

            user.getSubscribedEvents().add(event);

            event.getUsersSubscribed().add(user);

            userRepository.save(user);
            eventRepository.save(event);
        } else {
            throw new RuntimeException("User or Event not found");
        }
    }

    public void unsubscribeFromEvent(Long userId, Long eventId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (userOptional.isPresent() && eventOptional.isPresent()) {
            User user = userOptional.get();
            Event event = eventOptional.get();

            user.getSubscribedEvents().remove(event);

            event.getUsersSubscribed().remove(user);

            userRepository.save(user);
            eventRepository.save(event);
        } else {
            throw new RuntimeException("User or Event not found");
        }
    }

    public void startEvent(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            event.setStatus(Status.ONGOING);

            eventRepository.save(event);

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
                        "The event '" + event.getTitle() + "' has started!", String.valueOf(user.getId()));
                System.out.println("The event '" + event.getTitle() + "' has started!");
            }

        } else {
            throw new RuntimeException("Event not found");
        }
    }

    public void cancelEvent(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            event.setStatus(Status.CANCELLED);

            Set<User> subscribedUsers = event.getUsersSubscribed();
            for (User user : subscribedUsers) {
                Notification notification = new Notification();
                notification.setUser(user);
                notification.setEvent(event);
                notification.setMessage("The event '" + event.getTitle() + "' has been canceled!");
                notification.setRead(false);
                notification.setTimestamp(LocalDateTime.now());
                notificationService.save(notification);
                notificationHandler.sendNotification("The event '" + event.getTitle() + "' has been canceled!",String.valueOf(user.getId()));
            }
            eventRepository.save(event);


        } else {
            throw new RuntimeException("Event not found");
        }
    }
}
