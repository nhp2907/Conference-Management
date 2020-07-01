package com.conferenceManagement.controllers;

import com.conferenceManagement.BindingObject;
import com.conferenceManagement.models.Conference;
import com.conferenceManagement.models.DAOs.PlaceDAO;
import com.conferenceManagement.models.Place;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.InterfaceAddress;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class AddConferenceController extends ControllerBase {
    @FXML
    JFXButton addButton;
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


            SimpleDateFormat formter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            try {
                var pickedDate = datePicker.getValue();
                var string = String.format("%d-%d-%d ", pickedDate.getYear(),pickedDate.getMonth().getValue(),pickedDate.getDayOfMonth());

                var date = formter.parse(string + timeTextField.getText());
                conference.setHoldTime(date);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }

            returnDataFunction.backFunction(conference);

            var source = (JFXButton)mouseEvent.getSource();
            var stage = (Stage)source.getScene().getWindow();
            stage.close();

        });

        /* check valid value */
        timeTextField.textProperty().addListener((observableValue, s, t1) -> {
            SimpleDateFormat formter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                var pickedDay = datePicker.getValue();
                var date = formter.parse("2020-01-01 " + t1);

                invalidTimeLabel.setVisible(false);
                System.out.println(date.toString());

            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("Could not parse time");
                invalidTimeLabel.setVisible(true);
            }
        });
    }

}


