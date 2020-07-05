package com.conferenceManagement.controllers;

import com.conferenceManagement.models.Conference;
import com.conferenceManagement.models.DAOs.PlaceDAO;
import com.conferenceManagement.models.Place;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class AddConferenceController extends ControllerBase {
    @FXML
    JFXButton addButton;
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

    ObservableList<Place> places = FXCollections.observableArrayList(PlaceDAO.getAll());

    public IReturnDataFunction<Conference> returnDataFunction = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        placeComboBox.setItems(places);

        addButton.setOnMouseClicked(mouseEvent -> {
            Conference conference = new Conference();
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


            returnDataFunction.returnData(conference);

            var source = (JFXButton) mouseEvent.getSource();
            var stage = (Stage) source.getScene().getWindow();
            stage.close();

        });


    }

}


