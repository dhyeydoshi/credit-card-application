package com.creditcard.javaCreditCard.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String socialSecurityNumber;

    @NotNull
    private LocalDate dateOfBirth;

    @NotBlank
    private String streetAddress;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String postalCode;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CreditApplication> applications;
    public User() {}

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getID(){ return id; }
    public void setID(Long id) { this.id = id; }

    public String getEmail() {return email;}
    public void setEmail(String email){this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getFirstName() {return firstName;}
    public  void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public   void setLastName(String lastName) {this.lastName = lastName;}

    public  String getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getSocialSecurityNumber() {return socialSecurityNumber;}
    public void  setSocialSecurityNumber(String socialSecurityNumber) {this.socialSecurityNumber = socialSecurityNumber;}

    public  LocalDate getDateOfBirth() {return dateOfBirth;}
    public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth = dateOfBirth;}

    public String getStreetAddress() {return streetAddress;}
    public void setStreetAddress(String streetAddress) {this.streetAddress = streetAddress;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getPostalCode() {return postalCode;}
    public void  setPostalCode(String postalCode) {this.postalCode = postalCode;}

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }


    public List<CreditApplication> getApplications() { return applications; }
    public void setApplications(List<CreditApplication> applications) { this.applications = applications; }

}
