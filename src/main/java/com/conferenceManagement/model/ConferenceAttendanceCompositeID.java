package com.conferenceManagement.model;

import java.io.Serializable;
import java.util.Objects;

public class ConferenceAttendanceCompositeID implements Serializable {
    private Conference conference;
    private User user;

    ConferenceAttendanceCompositeID(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConferenceAttendanceCompositeID that = (ConferenceAttendanceCompositeID) o;
        return Objects.equals(conference, that.conference) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conference, user);
    }
}
