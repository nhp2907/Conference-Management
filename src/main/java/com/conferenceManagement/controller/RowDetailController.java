package com.conferenceManagement.controller;

import com.conferenceManagement.model.Conference;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RowDetailController extends ControllerBase {

    ObjectProperty<Conference> conference;

    @FXML
    Label nameLabel;
    @FXML
    Label startTimeLabel;
    @FXML
    Label endTimeLabel;
    @FXML
    Label countLabel;
    @FXML
    Label addressLabel;
    @FXML
    Label shortDescriptionLabel;
    @FXML
    Label detailLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //
    }

    public void setConference(ObjectProperty<Conference> conferenceObjectProperty) {
        this.conference = conferenceObjectProperty;

        var conference = conferenceObjectProperty.get(); //variable shadow, it's okay
        System.out.println(conference);
        DateTimeFormatter ft =DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");
        nameLabel.setText(conference.getName());
        startTimeLabel.setText(ft.format(conference.getStartDateTime()));
        endTimeLabel.setText(ft.format(conference.getEndDateTime()));
        countLabel.setText("conferenceAttendance.size()");
        shortDescriptionLabel.setText(conference.getShortDescription());
        detailLabel.setText(conference.getDetailDescription());
    }
}
