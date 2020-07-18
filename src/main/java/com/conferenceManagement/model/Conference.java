package com.conferenceManagement.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Conferences")
public class Conference extends RecursiveTreeObject<Conference> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String shortDescription;

    private String detailDescription;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "holdPlace")
    private Place holdPlace;

    @OneToMany(mappedBy = "conference", fetch = FetchType.EAGER)
    private Set<ConferenceAttendance> conferenceAttendances = new HashSet<>();

    public Conference(){

    }
    public Set<ConferenceAttendance> getConferenceAttendances() {
        return conferenceAttendances;
    }

    public void setConferenceAttendances(Set<ConferenceAttendance> conferenceAttendances) {
        this.conferenceAttendances = conferenceAttendances;
    }


    public Conference(int id, String name, String shortDescription, Date holdTime){

    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /*--------Name---------*/
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*-------shortDescription---------*/
    public String getShortDescription() {
        return shortDescription;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /*-------detailDescription---------*/
    public String getDetailDescription() {
        return detailDescription;
    }
    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    /*-------holdTime---------*/
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }


    /*-------holdPlace---------*/
    public Place getHoldPlace() {
        return holdPlace;
    }
    public void setHoldPlace(Place holdPlace) {
        this.holdPlace = holdPlace;
    }


}
