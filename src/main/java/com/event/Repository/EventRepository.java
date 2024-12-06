package com.event.Repository;

import com.event.Model.Category;
import com.event.Model.Status;
import com.event.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findEventsByCategory(Category category);
    List<Event> findEventsByStatus(Status status);
}
