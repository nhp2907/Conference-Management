package com.conferenceManagement.controller;

import com.conferenceManagement.App;
import com.conferenceManagement.dao.UserDAO;
import com.conferenceManagement.model.User;
import com.conferenceManagement.service.IUserService;
import com.conferenceManagement.service.UserService;
import com.conferenceManagement.util.PasswordAuthentication;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.security.KeyStore;
import java.util.ResourceBundle;

public class SignUpController extends ControllerBase {
    @FXML
    JFXButton registerButton;
    @FXML
    JFXButton cancelButton;
    @FXML
    JFXTextField userNameTextField;
    @FXML
    JFXTextField nameTextField;
    @FXML
    JFXTextField emailTextField;
    @FXML
    JFXPasswordField passwordField;
    @FXML
    JFXPasswordField retypePasswordField;

    IUserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerButton.setDefaultButton(true);
        registerButton.setOnAction(mouseEvent -> {
            User user = new User();

            user.setUserName(userNameTextField.getText());
            user.setName(nameTextField.getText());
            user.setPassword(PasswordAuthentication.hash(passwordField.getText()));
            user.setEmail(emailTextField.getText());

            userService.save(user);
            App.setUser(user);

            if (returnDataFunction != null) {
                returnDataFunction.returnData(user);
            }

            var source = (JFXButton) mouseEvent.getSource();
            var stage = (Stage) source.getScene().getWindow();
            stage.close();
        });

        userNameTextField.textProperty().addListener((observableValue, s, t1) -> {

        });

        cancelButton.setOnMouseClicked(mouseEvent -> {
            var stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}


