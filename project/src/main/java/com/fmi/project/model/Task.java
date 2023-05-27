package com.fmi.project.model;

import com.fmi.project.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Table(schema = "event_manager", name = "Tasks")
@Getter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

   /* @Column(name="due_date")
    @Temporal(TemporalType.DATE)
    private Date due_date;*/

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "creator_username")
    private String creator_username;

    @ManyToMany()
    @JoinTable(
            name = "event_users_tasks",
            joinColumns = @JoinColumn(name = "event_user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<EventUser> assignees;

   /* @Column(name="event")
    private Set<EventUser> assignees;*/

    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private Event event;

   /* @Column(name="created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created_date;

    @Column(name="last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp last_modified_date;*/

    @Column(name = "version")
    @Version
    private Long version;
}
