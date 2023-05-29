package com.fmi.project.model;

import com.fmi.project.enums.Category;
import com.fmi.project.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "event_manager", name = "events_users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;


}
