package com.rra.ebm.EBMApplication.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "user_login")
public class Users {
    @Id
    private int tin;
    private String email;
    private String password;
    private boolean isDefault;
    private String roles;
    private Date changedAt;
    
    // Field for reset password token
    private String resetPasswordToken;

    public Users() {
    }

    public Users(int tin, String email, String password, boolean isDefault, String roles, Date changedAt, String resetPasswordToken) {
        this.tin = tin;
        this.email = email;
        this.password = password;
        this.isDefault = isDefault;
        this.roles = roles;
        this.changedAt = changedAt;
        this.resetPasswordToken = resetPasswordToken;
    }

    // Getters and setters
    public int getTin() {
        return tin;
    }

    public void setTin(int tin) {
        this.tin = tin;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }
}
