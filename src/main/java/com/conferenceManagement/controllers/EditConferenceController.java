package com.conferenceManagement.controllers;

import com.conferenceManagement.models.Conference;
import com.conferenceManagement.models.DAOs.ConferenceAttendenceDAO;
import com.conferenceManagement.models.DAOs.ConferenceDAO;
import com.conferenceManagement.models.DAOs.PlaceDAO;
import com.conferenceManagement.models.Place;
import com.jfoenix.controls.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.ResourceBundle;

public class EditConferenceController extends ControllerBase {
    @FXML
    JFXButton updateButton;
    @FXML
    JFXButton exitButton;
    @FXML
    JFXTextField nameTextField;
    @FXML
    JFXTextField shortDescriptionTextField;
    @FXML
    JFXTextField detailDescriptionTextField;
    @FXML
    JFXDatePicker startDatePicker;
    @FXML
    JFXDatePicker endDatePicker;
    @FXML
    JFXTimePicker startTimePicker;
    @FXML
    JFXTimePicker endTimePicker;

    @FXML
    JFXComboBox<Place> placeComboBox;

    @FXML
    Label invalidTimeLabel;
    @FXML
    JFXTimePicker jfxTimePicker;
    @FXML
    JFXDatePicker jfxDatePicker;

    Conference conference;
    ObservableList<Place> places = FXCollections.observableArrayList(PlaceDAO.getAll());

    IReturnDataFunction<Conference> returnDataFunction = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        placeComboBox.setItems(places);

        updateButton.setOnMouseClicked(mouseEvent -> {
            conference.setName(nameTextField.getText());
            conference.setShortDescription(shortDescriptionTextField.getText());
            conference.setDetailDescription(detailDescriptionTextField.getText());
            conference.setHoldPlace(placeComboBox.getSelectionModel().getSelectedItem());

            var startLDT = LocalDateTime.of(startDatePicker.getValue(), startTimePicker.getValue());
            var startDate =  Date.from(startLDT.atZone(ZoneId.systemDefault()).toInstant());

            var endLDT = LocalDateTime.of(endDatePicker.getValue(), endTimePicker.getValue());

            var endDate =  Date.from(endLDT.atZone(ZoneId.systemDefault()).toInstant());


            conference.setHoldTime(startDate);
            conference.setEndTime(endDate);


            ConferenceDAO.update(conference);
            updateButton.setDisable(true);
        });

        nameTextField.textProperty().addListener((observable, s, t1) -> {
            if (!conference.getName().equals(t1))
                updateButton.setDisable(false);
            else {
                updateButton.setDisable(true);
            }
        });

        placeComboBox.selectionModelProperty().addListener((observableValue, s, t1) -> {
            if (!conference.getHoldPlace().equals(t1))
                updateButton.setDisable(false);
            else {
                updateButton.setDisable(true);
            }
        });

        shortDescriptionTextField.textProperty().addListener((observableValue, s, t1) -> {
            if (!conference.getShortDescription().equals(t1))
                updateButton.setDisable(false);
            else {
                updateButton.setDisable(true);
            }

        });

        detailDescriptionTextField.textProperty().addListener((observableValue, s, t1) -> {
            if (!conference.getDetailDescription().equals(t1))
                updateButton.setDisable(false);
            else {
                updateButton.setDisable(true);
            }
        });

        exitButton.setOnMouseClicked(mouseEvent -> {
            var source = (JFXButton) mouseEvent.getSource();
            var stage = (Stage) source.getScene().getWindow();
            stage.close();
        });

        startDatePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
            var cDate = conference.getHoldTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (!t1.equals(cDate)) {
                updateButton.setDisable(false);
            } else {
                updateButton.setDisable(true);
            }
        });

    }

    public void updateConfenceInfo(Conference conference) {
        this.conference = conference;
        nameTextField.setText(conference.getName());

        startTimePicker.setValue(conference.getHoldTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalTime());
        endTimePicker.setValue(conference.getEndTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalTime());

        startDatePicker.setValue(conference.getHoldTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        endDatePicker.setValue(conference.getEndTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());


        var places = PlaceDAO.getAll();
        placeComboBox.setItems(FXCollections.observableArrayList(places));
        places.forEach(place -> {
            if (place.equals(conference.getHoldPlace())) {
                placeComboBox.getSelectionModel().select(place);
            }
        });


        detailDescriptionTextField.setText(conference.getDetailDescription());
        shortDescriptionTextField.setText(conference.getShortDescription());
    }
}
