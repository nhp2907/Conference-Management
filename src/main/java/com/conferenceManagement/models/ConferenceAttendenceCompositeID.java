package com.conferenceManagement.models;

import java.io.Serializable;
import java.util.Objects;

public class ConferenceAttendenceCompositeID implements Serializable {
    private Conference conference;
    private User user;

    ConferenceAttendenceCompositeID(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConferenceAttendenceCompositeID that = (ConferenceAttendenceCompositeID) o;
        return Objects.equals(conference, that.conference) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conference, user);
    }
}
