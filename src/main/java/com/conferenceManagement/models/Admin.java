package com.conferenceManagement.models;

import javax.persistence.*;

@Entity
@Table(name = "Admin")
@PrimaryKeyJoinColumn(name = "userID")
public class Admin extends User {

    public Admin() {

    }

    @Override
    public User clone() {
       var user = super.clone();
       Admin admin = new Admin();
       admin.setName(user.getName());
       admin.setUserName(user.getUserName());
       admin.setEmail(user.getEmail());
       admin.setPassword(user.getPassword());
       return admin;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
