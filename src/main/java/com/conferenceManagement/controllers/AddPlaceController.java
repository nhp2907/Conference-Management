package com.conferenceManagement.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class AddPlaceController extends ControllerBase {
    @FXML
    JFXButton addButton;
    @FXML
    JFXButton cancelButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setOnAction(event -> {



        });
    }
}
