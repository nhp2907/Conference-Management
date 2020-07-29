package com.conferenceManagement.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "ConferenceAttendance")
@IdClass(ConferenceAttendanceCompositeID.class)
public class ConferenceAttendance {
    @Id
    @ManyToOne
    @JoinColumn(name = "conferenceID")
    private Conference conference;

    @Id
    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @Column(name = "isAccepted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isAccepted;

    public ConferenceAttendance() {

    }

    public Conference getConference() {
        return conference;
    }


    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
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