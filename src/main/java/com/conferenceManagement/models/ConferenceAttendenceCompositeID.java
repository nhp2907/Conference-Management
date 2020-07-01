package com.conferenceManagement.models;

import java.io.Serializable;
import java.util.Objects;

public class ConferenceAttendenceCompositeID implements Serializable {
    private Long conferenceID;
    private Long userID;

    ConferenceAttendenceCompositeID(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConferenceAttendenceCompositeID that = (ConferenceAttendenceCompositeID) o;
        return Objects.equals(conferenceID, that.conferenceID) &&
                Objects.equals(userID, that.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conferenceID, userID);
    }
}
