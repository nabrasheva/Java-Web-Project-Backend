package com.fmi.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(schema = "event_manager", name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false, name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "profile_picture_url")
    private String profile_picture_url;

    @Column(name = "date_of_birth")
    @Past
    private Date date_of_birth;

    @Column(name = "address")
    private String address;

    @ManyToMany(mappedBy = "assignees", cascade = CascadeType.REMOVE)
    private Set<Task> tasks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<EventUser> eventUsers;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Timestamp created_date;

    @Column(name = "last_modified_date", nullable = false)
    @UpdateTimestamp
    private Timestamp last_modified_date;
}
