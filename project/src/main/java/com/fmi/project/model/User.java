package com.fmi.project.model;

import com.fmi.project.enums.UserAuthRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(schema = "event_manager", name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
//TODO: add private field called "enabled" of type boolean, in order to see if the user's account is enabled

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

    //@Enumerated(EnumType.STRING)
    //private UserAuthRole userAuthRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; //TODO: at first it might be false, because through the email message, the user will verify their email and the account will be enabled and this field will become true
    }
}
