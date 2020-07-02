package com.conferenceManagement.controllers;

import com.conferenceManagement.App;
import com.conferenceManagement.BindingObject;
import com.conferenceManagement.models.DAOs.UserDAO;
import com.conferenceManagement.models.Guest;
import com.conferenceManagement.models.User;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditInfoController extends ControllerBase {
    @FXML
    JFXButton updateButton;
    @FXML
    JFXButton logOutButton;
    @FXML
    TextField userNameTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField emailTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    PasswordField retypePasswordField;
    @FXML
    Label wrongPasswordLabel;
    @FXML
    Label userNameExistedLabel;
    @FXML
    Label notLoggedInLabel;
    @FXML
    Label visibleLabel;

    User user = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = App.getUser();
        updateButton.setOnMouseClicked(mouseEvent -> {
            UserDAO.update(user);
            App.setUser(user);
        });
        App.userProperty().addListener((observableValue, user1, t1) -> {
            updateInfo(App.getUser());
        });
        logOutButton.setOnAction(event -> {
            visibleLabel.setVisible(false);
            notLoggedInLabel.setVisible(true);
            App.setUser(new Guest());
        });

        /* binding textField textProperty */
        nameTextField.focusedProperty().addListener((observableValue, s, t1) -> {
            if (!t1) {
                if (user.getName().equals(nameTextField.getText())) {
                    updateButton.setDisable(true);
                } else {
                    updateButton.setDisable(false);
                    user.setName(nameTextField.getText());
                }
            }
        });

        emailTextField.focusedProperty().addListener((observableValue, s, t1) -> {
            if (!t1) {
                if (emailTextField.getText().equals(user.getEmail())) {
                    updateButton.setDisable(true);
                } else {
                    updateButton.setDisable(false);
                    user.setEmail(emailTextField.getText());
                }
            }
        });

        userNameTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                var temp = UserDAO.getUserByUsername(userNameTextField.getText());
                if (temp != null) {
                    userNameExistedLabel.setDisable(false);
                    updateButton.setDisable(true);
                } else if (userNameTextField.getText().equals(user.getUserName())) {
                    updateButton.setDisable(true);
                } else {
                    updateButton.setDisable(false);
                    user.setUserName(userNameTextField.getText());
                }
            }
        });

        passwordField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                if (passwordField.getText().equals(user.getPassword())) {
                    updateButton.setDisable(true);
                } else if (!retypePasswordField.getText().equals(passwordField.getText())) {
                    wrongPasswordLabel.setDisable(false);
                    updateButton.setDisable(true);
                } else {
                    updateButton.setDisable(false);
                    wrongPasswordLabel.setVisible(false);
                }
            }
        });

        retypePasswordField.focusedProperty().addListener((observableValue, s, t1) -> {
            if (!t1) {
                if (!retypePasswordField.getText().equals(passwordField.getText())) {
                    wrongPasswordLabel.setVisible(true);
                    updateButton.setDisable(true);
                } else {
                    wrongPasswordLabel.setVisible(false);
                    if (retypePasswordField.getText().equals(user.getPassword()))
                        updateButton.setDisable(true);
                    else
                        updateButton.setDisable(false);
                }
            }
        });

        updateInfo(App.getUser());

    } //end initialize

    public void updateInfo(User user) {
        this.user = user;
        if (user instanceof Guest) {
            //every thing is invisible
            return;
        }

        nameTextField.setText(user.getName());
        userNameTextField.setText(user.getUserName());
        emailTextField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        retypePasswordField.setText(user.getPassword());
    }


}
