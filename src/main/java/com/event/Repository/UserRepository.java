package com.event.Repository;

import com.event.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    void deleteById(Long id);

    @Query("SELECT u FROM User u JOIN u.subscribedEvents e WHERE e.id = :eventId")
    Set<User> findUsersByEventId(@Param("eventId") Long eventId);
}