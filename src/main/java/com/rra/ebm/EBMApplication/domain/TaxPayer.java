package com.rra.ebm.EBMApplication.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class TaxPayer {
    @Id
    private int tin;
    private String firstName;
    private String LastName;
    private String email;
    private String phoneNumber;

    public TaxPayer() {
    }

    public TaxPayer(int tin, String firstName, String lastName, String email, String phoneNumber) {
        this.tin = tin;
        this.firstName = firstName;
        LastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getTin() {
        return tin;
    }

    public void setTin(int tin) {
        this.tin = tin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}