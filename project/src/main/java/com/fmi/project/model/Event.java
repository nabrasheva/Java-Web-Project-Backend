package com.fmi.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(schema = "event_manager", name = "events")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false, name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    @Size(min = 1, max = 32)
    private String name;

    @Column(name = "date")
    @Future
    private LocalDate date;

    @Column(name = "location")
    @Size(min = 5, max = 64)
    private String location;

    @Column(name = "description")
    @Size(max = 128)
    private String description;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private Set<Task> tasks;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private Set<EventUser> eventUsers;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "last_modified_date", nullable = false)
    @UpdateTimestamp
    private Timestamp lastModifiedDate;
}
