package com.fmi.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(schema = "event_manager", name="users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "profile_picture_url")
    private String profile_picture_url;

    @Column(name = "date_of_birth")
    private Date date_of_birth;

    @Column(name = "address")
    private String address;

    @ManyToMany(mappedBy = "id",  cascade = CascadeType.REMOVE)
    private Set<Task> tasks;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private Set<EventUser> eventUsers;

   @Column(name = "created_date")
    private Timestamp created_date;

    @Column(name = "last_modified_date")
    private Timestamp last_modified_date;

    @Column(name="version")
    @Version
    private Long version;


    //TODO: overrride hashCode and equals!!!
}
