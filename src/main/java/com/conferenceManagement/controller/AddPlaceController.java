package com.conferenceManagement.controller;

import com.conferenceManagement.dao.PlaceDAO;
import com.conferenceManagement.model.Place;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.IntegerValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddPlaceController extends ControllerBase {
    @FXML
    JFXButton addButton;
    @FXML
    JFXButton cancelButton;

    @FXML
    JFXTextField nameTextField;
    @FXML
    JFXTextField addressTextField;
    @FXML
    JFXTextField capacityTextField;
    @FXML
    Label errorLabel;

    BooleanProperty nameValid = new SimpleBooleanProperty(false);
    BooleanProperty addressValid = new SimpleBooleanProperty(false);
    BooleanProperty capacityValid = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addButton.setOnAction(event -> {
            String errorMessage = "";
            if (!nameValid.get()) {
                errorMessage = "* tên không được để trống";
            } else if (!addressValid.get()) {
                errorMessage = "* địa chỉ không được để trống";
            } else if (!capacityValid.get()) {
                errorMessage = "* số lượng không hợp lệ";
            } else {
                var place = new Place();
                place.setName(nameTextField.getText());
                place.setAddress(addressTextField.getText());
                place.setCapacity(Integer.parseInt(capacityTextField.getText()));


                PlaceDAO.save(place);

                /* return data to previous view*/
                if (returnDataFunction!= null){
                    returnDataFunction.returnData(place);
                }

                var stage = (Stage)addButton.getScene().getWindow();
                stage.close();

                errorMessage = "";
            }

            errorLabel.setText(errorMessage);
        });

        nameTextField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.length() < 1) {
                nameValid.set(false);
            } else {
                nameValid.set(true);
            }
        });

        addressTextField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.length() < 0) {
                addressValid.set(false);
            } else {
                addressValid.set(true);
            }
        });

        capacityTextField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                capacityTextField.setText(t1.replaceAll("[^\\d]", ""));
            }


            int number = 0;
            try {
                number = Integer.parseInt(capacityTextField.getText());

                if (number < 1) {
                    capacityValid.set(false);
                } else
                    capacityValid.set(true);
            } catch (Exception ex) {
                capacityValid.set(false);
//                ex.printStackTrace();
            }
        });

        cancelButton.setOnAction(event -> {
            var stage = (Stage)cancelButton.getScene().getWindow();
            stage.close();
        });
    }
}
