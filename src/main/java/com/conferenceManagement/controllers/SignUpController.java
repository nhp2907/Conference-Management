package com.conferenceManagement.controllers;

import com.conferenceManagement.BindingObject;
import com.conferenceManagement.models.DAOs.UserDAO;
import com.conferenceManagement.models.Guest;
import com.conferenceManagement.models.User;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController extends ControllerBase {
    @FXML
    JFXButton registerButton;
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
    ObjectProperty<User> userProperty = new SimpleObjectProperty<>();

    BindingObject<User> bindingObject = new BindingObject<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerButton.setDefaultButton(true);
        registerButton.setOnAction(mouseEvent -> {
            User user = new User();

            user.setUserName(userNameTextField.getText());
            user.setName(nameTextField.getText());
            user.setPassword(passwordField.getText());
            user.setEmail(emailTextField.getText());

            UserDAO.save(user);
            bindingObject.set(user);
            var source = (JFXButton)mouseEvent.getSource();
            var stage = (Stage)source.getScene().getWindow();
            stage.close();
        });

        userNameTextField.textProperty().addListener((observableValue, s, t1) -> {

        });

        cancelButton.setOnMouseClicked(mouseEvent -> {
            var source = (JFXButton)mouseEvent.getSource();
            var stage = (Stage)source.getScene().getWindow();
            stage.close();
        });

    }
}


