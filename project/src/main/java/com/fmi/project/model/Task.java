package com.fmi.project.model;

import com.fmi.project.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Column(insertable = false, updatable = false, name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(min = 1, max = 32)
    private String name;

    @Column(name = "description")
    @Size(max = 128)
    private String description;

    @Column(name="due_date", nullable = false)
    @Future
    private Date due_date;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Size(max = 16)
    private Status status;

    @Column(name = "creator_username", nullable = false)
    @Size(max = 32)
    private String creatorUsername;

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

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "last_modified_date", nullable = false)
    @UpdateTimestamp
    private Timestamp lastModifiedDate;
}
