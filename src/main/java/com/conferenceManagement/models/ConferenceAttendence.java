package com.conferenceManagement.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "ConferenceAttendence")
@IdClass(ConferenceAttendenceCompositeID.class)
public class ConferenceAttendence {
    @Id
    private Long conferenceID;
    @Id
    private Long userID;

    ConferenceAttendence(){

    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public Long getUserID() {
        return userID;
    }
}
