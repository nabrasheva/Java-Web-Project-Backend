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
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name="due_date", nullable = false)
    private Date due_date;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "creator_username", nullable = false)
    private String creator_username;

    @ManyToMany()
    @JoinTable(
            name = "users_tasks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<User> assignees;

    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private Event event;

    @Column(name="created_date", nullable = false)
    private Timestamp created_date;

    @Column(name="last_modified_date", nullable = false)
    private Timestamp last_modified_date;

    @Column(name = "version", nullable = false)
    @Version
    private Long version;
}
