package com.conferenceManagement.controllers;

import com.conferenceManagement.App;
import com.conferenceManagement.BindingObject;
import com.conferenceManagement.models.Guest;
import com.conferenceManagement.models.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditInfoController extends ControllerBase {
    @FXML
    JFXButton updateButton;
    @FXML
    JFXButton cancelButton;
    @FXML
    TextField userNameTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField emailTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label loginFailLabel;

    BindingObject<User> bindingObject = new BindingObject<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton.setOnMouseClicked(mouseEvent -> {
            //get input data

            //notify user change
        });

    }

    public void updateInfo(User user) {
        if (user instanceof Guest){
            loginFailLabel.setVisible(true);
            return;
        }

        loginFailLabel.setVisible(false);

        nameTextField.setText(user.getName());
        userNameTextField.setText(user.getUserName());
        emailTextField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
    }

}
