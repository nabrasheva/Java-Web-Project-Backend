package com.fmi.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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
  @Size(min = 1, max = 32)
  private String username;

  @Column(name = "email", nullable = false)
  @Email()
  @Size(max = 64)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "first_name")
  @Size(max = 32)
  private String firstName;

  @Column(name = "last_name")
  @Size(max = 32)
  private String lastName;

  @Column(name = "profile_picture_url")
  private String profilePictureUrl;

  @Column(name = "date_of_birth")
  @Past
  private Date dateOfBirth;

  @Column(name = "address")
  @Size(max = 64)
  private String address;

  @ManyToMany(mappedBy = "assignees", cascade = CascadeType.REMOVE)
  private Set<Task> tasks;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private Set<EventUser> eventUsers;

  @Column(name = "created_date", nullable = false)
  @CreationTimestamp
  private Timestamp createdDate;

  @Column(name = "last_modified_date", nullable = false)
  @UpdateTimestamp
  private Timestamp lastModifiedDate;
}
