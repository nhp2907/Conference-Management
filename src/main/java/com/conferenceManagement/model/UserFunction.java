package com.conferenceManagement.model;

import com.conferenceManagement.App;
import com.conferenceManagement.controller.ControllerBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class UserFunction {
    private Parent view;
    private ControllerBase controller;
    private  String name;
    private Parent previousView;

    public UserFunction(String fxml) throws IOException {
        this.name = fxml;
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        view = loader.load();
        controller = loader.getController();
    }

    public Parent getView() {
        return view;
    }

    public ControllerBase getController() {
        return controller;
    }

    public void setPreviousView(Parent previousView) {
        this.previousView = previousView;
    }
}
