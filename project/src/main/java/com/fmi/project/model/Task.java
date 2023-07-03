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
import java.util.HashSet;
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

    @Column(name = "name", nullable = false, unique = true)
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
    //@Size(max = 16)
    private Status status;

    @Column(name = "creator_email", nullable = false)
    @Size(max = 64)
    private String creatorEmail;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "users_tasks",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> assignees = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private Event event;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "last_modified_date", nullable = false)
    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    public void addUser(User user) {
        assignees.add(user);
        user.getTasks().add(this);
    }

    public void removeUser(User user) {
        assignees.remove(user);
        user.getTasks().remove(this);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//
//        if (!(o instanceof Task)) return false;
//
//        return id != null && id.equals(((Task) o).getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }
}
