package com.careconnect.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name = "users")
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(nullable = false) private String firstName;
  @Column(nullable = false) private String lastName;
  @Column(nullable = false, unique = true) private String email;
  @JsonIgnore @Column(nullable = false) private String password;
  @Column(nullable = false) private String role = "PATIENT";
  private LocalDate dateOfBirth;
  protected User() {}
  public User(String firstName, String lastName, String email, String password, LocalDate dateOfBirth) { this.firstName=firstName; this.lastName=lastName; this.email=email; this.password=password; this.dateOfBirth=dateOfBirth; }
  public User(String firstName, String lastName, String email, String password, LocalDate dateOfBirth, String role) { this(firstName,lastName,email,password,dateOfBirth); this.role=role; }
  public void updateProfile(String firstName, String lastName, LocalDate dateOfBirth){this.firstName=firstName;this.lastName=lastName;this.dateOfBirth=dateOfBirth;}
  public LocalDate getDateOfBirth(){return dateOfBirth;}
  public Long getId(){return id;} public String getFirstName(){return firstName;} public String getLastName(){return lastName;} public String getEmail(){return email;} public String getPassword(){return password;} public String getRole(){return role;}
}
