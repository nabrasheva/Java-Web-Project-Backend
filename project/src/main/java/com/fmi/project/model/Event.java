package com.fmi.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.sql.Date;
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

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    @Future
    private Date date;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private Set<Task> tasks;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private Set<EventUser> eventUsers;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Timestamp created_date;

    @Column(name = "last_modified_date", nullable = false)
    @UpdateTimestamp
    private Timestamp last_modified_date;
}
