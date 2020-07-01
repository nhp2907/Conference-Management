package com.conferenceManagement.controllers;

import com.conferenceManagement.models.Conference;
import com.conferenceManagement.models.DAOs.ConferenceAttendenceDAO;
import com.conferenceManagement.models.DAOs.ConferenceDAO;
import com.conferenceManagement.models.DAOs.PlaceDAO;
import com.conferenceManagement.models.Place;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EditConferenceController extends ControllerBase {
    @FXML
    JFXButton updateButton;
    @FXML
    JFXButton exitButton;
    @FXML
    TextField nameTextField;
    @FXML
    TextField shortDescriptionTextField;
    @FXML
    TextField detailDescriptionTextField;
    @FXML
    TextField timeTextField;
    @FXML
    DatePicker datePicker;

    @FXML
    JFXComboBox<Place> placeComboBox;

    @FXML
    Label invalidTimeLabel;

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


            SimpleDateFormat formter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            try {
                var pickedDate = datePicker.getValue();
                var string = String.format("%d-%d-%d ", pickedDate.getYear(), pickedDate.getMonth().getValue(), pickedDate.getDayOfMonth());

                var date = formter.parse(string + timeTextField.getText());
                conference.setHoldTime(date);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }


            ConferenceDAO.update(conference);
            updateButton.setDisable(true);
        });

        /* check valid value */
        timeTextField.textProperty().addListener((observableValue, s, t1) -> {
            SimpleDateFormat formter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                var pickedDay = datePicker.getValue();
                var date = formter.parse("2020-01-01 " + t1);

                SimpleDateFormat checkFomater = new SimpleDateFormat("hh:mm");
                if (checkFomater.format(conference.getHoldTime()).equals(checkFomater.format(date)))
                    updateButton.setDisable(true);
                else {
                    updateButton.setDisable(false);
                }


                invalidTimeLabel.setVisible(false);
                System.out.println(date.toString());

            } catch (ParseException e) {
                updateButton.setDisable(true);
                e.printStackTrace();
                System.out.println("Could not parse time");
                invalidTimeLabel.setVisible(true);
            }

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
            var source = (JFXButton)mouseEvent.getSource();
            var stage = (Stage)source.getScene().getWindow();
            stage.close();
        });

        datePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
            var cDate = conference.getHoldTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (!t1.equals(cDate)){
                updateButton.setDisable(false);
            } else {
                updateButton.setDisable(true);
            }
        });

    }

    public void updateConfenceInfo(Conference conference) {
        this.conference = conference;
        nameTextField.setText(conference.getName());

        SimpleDateFormat timeFormater = new SimpleDateFormat("hh:mm");
        timeTextField.setText(timeFormater.format(conference.getHoldTime()));


        datePicker.setValue(conference.getHoldTime().toInstant()
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
