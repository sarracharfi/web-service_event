package com.event.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String title;
    String description;
    LocalDate date;
    String location;

    @Enumerated(EnumType.STRING)
    Category category;

    @Enumerated(EnumType.STRING)
    Status status;
    @JsonIgnore
    @ManyToMany(mappedBy = "subscribedEvents")
    Set<User> usersSubscribed = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "event")
    Set<Notification> notifications = new HashSet<>();

    @JsonIgnoreProperties(ignoreUnknown = true,value = {"createdEvents" ,"email" ,"password","role","profile", "createdAt", "updatedAt", "enabled","authorities","accountNonLocked","accountNonExpired","credentialsNonExpired"
    })
    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;
}
