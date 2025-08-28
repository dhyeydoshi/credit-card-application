package com.creditcard.javaCreditCard.dataObject;

import jakarta.validation.constraints.*;
import java.time.*;

public class userRegistrationDataObject {

    @NotBlank(message="First name is required")
    private String firstName;

    @NotBlank(message="Last name is required")
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotBlank(message="SSN is required")
    private String socialSecurityNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank
    private String streetAddress;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String postalCode;

    public userRegistrationDataObject(){}

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



}
