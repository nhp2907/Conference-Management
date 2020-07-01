package com.conferenceManagement.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "ConferenceAttendence")
@IdClass(ConferenceAttendenceCompositeID.class)
public class ConferenceAttendence {
    @Id
    private Long conferenceID;
    @Id
    private Long userID;

    @Column(name = "isAccepted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isAccepted;

    public ConferenceAttendence(){

    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public Long getUserID() {
        return userID;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
