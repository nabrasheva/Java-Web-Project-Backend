package com.fmi.project.model;

import com.fmi.project.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.Set;


@Entity
@Table(schema = "event_manager", name = "tasks")
@Getter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name="due_date")
    private Date due_date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "creator_username")
    private String creator_username;

    @ManyToMany()
    @JoinTable(
            name = "events_users_tasks",
            joinColumns = @JoinColumn(name = "event_user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<EventUser> assignees;

    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private Event event;

    @Column(name="created_date")
    private Timestamp created_date;

    @Column(name="last_modified_date")
    private Timestamp last_modified_date;

    @Column(name = "version")
    @Version
    private Long version;
}
