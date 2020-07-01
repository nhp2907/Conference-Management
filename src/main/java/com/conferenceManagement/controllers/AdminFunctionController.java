package com.conferenceManagement.controllers;

import com.conferenceManagement.BindingObject;
import com.conferenceManagement.models.Conference;
import com.conferenceManagement.models.DAOs.ConferenceDAO;
import com.conferenceManagement.models.UserFunction;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminFunctionController extends ControllerBase {

    @FXML
    JFXButton addButton;

    ObjectProperty<Conference> conferenceProperty = new SimpleObjectProperty<>();

    IAddConference addConferenceFI;
    Stage addStage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        conferenceProperty.addListener((observableValue, conference, t1) -> {
            System.out.println(t1.toString());
            /* save to database */
            ConferenceDAO.save(t1);

            /* update to view */
            if (addConferenceFI != null)
                addConferenceFI.addConference(t1);

            addStage.close();
        });
    }
}


@FunctionalInterface
interface IAddConference {
    void addConference(Conference conference);
}