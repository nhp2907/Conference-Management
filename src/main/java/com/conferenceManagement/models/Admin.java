package com.conferenceManagement.models;

import javax.persistence.*;

@Entity
@Table(name = "Admin")
@PrimaryKeyJoinColumn(name="userID")
public class Admin extends User {

    Admin(){

    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
