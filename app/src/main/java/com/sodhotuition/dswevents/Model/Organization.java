package com.sodhotuition.dswevents.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.annotations.NotNull;

@IgnoreExtraProperties
public class Organization {

    public String name;
    public String email;
    public String password;
    public String phone;
    public String members;
    public String description;
    private String key;

    public Organization(){

    }

    public Organization(String name, String email, String password, String phone, String members, String description) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.members = members;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Organization{" +
              "name='" + name + '\'' +
              ", email='" + email + '\'' +
              ", password='" + password + '\'' +
              ", phone='" + phone + '\'' +
              ", members='" + members + '\'' +
              ", description='" + description + '\'' +
              '}';
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

