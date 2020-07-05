package com.conferenceManagement.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;

@Entity
@Table(name = "Conferences")
public class Conference extends RecursiveTreeObject<Conference> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String shortDescription;

    private String detailDescription;

    @Temporal(TemporalType.TIMESTAMP)
    private Date holdTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @OneToOne
    @JoinColumn(name = "holdPlace")
    private Place holdPlace;

    public Conference(){

    }

    public Conference(int id, String name, String shortDescription, Date holdTime){

    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
    public Date getHoldTime() {
        return holdTime;
    }
    public void setHoldTime(Date holdTime) {
        this.holdTime = holdTime;
    }


    /*-------holdPlace---------*/
    public Place getHoldPlace() {
        return holdPlace;
    }
    public void setHoldPlace(Place holdPlace) {
        this.holdPlace = holdPlace;
    }


}
