package com.fmi.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(schema = "event_manager", name="Events")
@Getter
public class Event {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private Date date;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy="event")
    private Set<Task> tasks;

   @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created_date;

    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp last_modified_date;

    @Column(name="version")
    @Version
    private Long version;
}
