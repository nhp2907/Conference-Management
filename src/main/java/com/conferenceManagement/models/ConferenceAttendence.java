package com.conferenceManagement.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "ConferenceAttendence")
@IdClass(ConferenceAttendenceCompositeID.class)
public class ConferenceAttendence {
    @Id
    @OneToOne
    @JoinColumn(name = "conferenceID")
    private Conference conference;

    @Id
    @OneToOne
    @JoinColumn(name = "userID")
    private User user;

    @Column(name = "isAccepted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isAccepted;

    public ConferenceAttendence() {

    }

    public Conference getConference() {
        return conference;
    }


    public void setAccepted(boolean isAccepted) {
        isAccepted = isAccepted;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}