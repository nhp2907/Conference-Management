package com.conferenceManagement.controller;

import com.conferenceManagement.App;
import com.conferenceManagement.dao.UserDAO;
import com.conferenceManagement.model.Guest;
import com.conferenceManagement.model.User;
import com.conferenceManagement.service.UserService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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
    JFXTextField userNameTextField;
    @FXML
    JFXTextField nameTextField;
    @FXML
    JFXTextField emailTextField;
    @FXML
    JFXPasswordField passwordField;
    @FXML
    JFXPasswordField retypePasswordField;
    @FXML
    Label wrongPasswordLabel;
    @FXML
    Label userNameExistedLabel;
    @FXML
    Label notLoggedInLabel;
    @FXML
    Label visibleLabel;
    User user = null;

    UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        updateButton.setOnMouseClicked(mouseEvent -> {
            user.setName(nameTextField.getText());
            user.setUserName(userNameTextField.getText());
            user.setPassword(passwordField.getText());
            user.setEmail(emailTextField.getText());

            userService.update(this.user);

            if (returnDataFunction != null)
                returnDataFunction.returnData(user);
            updateButton.setDisable(true);
        });

        App.userProperty().addListener((observableValue, user1, t1) -> {
            if (t1 != null) {
                user = t1;
                updateInfo(t1);
            }
        });
        logOutButton.setOnAction(event -> {
            visibleLabel.setVisible(false);
            notLoggedInLabel.setVisible(true);
            App.setUser(new Guest());
        });




//        userNameTextField.textProperty().addListener((observableValue, aBoolean, t1) -> {
//            var temp = UserDAO.getUserByUsername(t1);
//            if (t1.equals(user.getUserName())) {
//                userNameExistedLabel.setVisible(false);
//                updateButton.setDisable(true);
//            } else if (temp != null) {
//                userNameExistedLabel.setVisible(true);
//                updateButton.setDisable(true);
//            } else {
//                userNameExistedLabel.setVisible(false);
//                updateButton.setDisable(false);
//            }
//        });
//
//        /* binding textField textProperty */
//        nameTextField.textProperty().addListener((observableValue, s, t1) -> {
//
//            if (t1.equals(user.getName()) || t1.length() == 0) {
//                updateButton.setDisable(true);
//            } else {
//                updateButton.setDisable(false);
//            }
//        });
//
//        emailTextField.textProperty().addListener((observableValue, s, t1) -> {
//
//            if (t1.equals(user.getEmail())) {
//                updateButton.setDisable(true);
//            } else {
//                updateButton.setDisable(false);
//            }
//
//        });
//
//
//        passwordField.textProperty().addListener((observableValue, aBoolean, t1) -> {
//            if (t1.equals(user.getPassword())) {
//                updateButton.setDisable(true);
//            } else {
//                if (!t1.equals(retypePasswordField.getText())) {
//                    wrongPasswordLabel.setVisible(true);
//                    updateButton.setDisable(true);
//                } else {
//                    wrongPasswordLabel.setVisible(false);
//                    updateButton.setDisable(false);
//                }
//            }
//
//        });
//
//        retypePasswordField.textProperty().addListener((observableValue, s, t1) -> {
//
//            if (!t1.equals(passwordField.getText())) {
//                wrongPasswordLabel.setVisible(true);
//                updateButton.setDisable(true);
//            } else {
//                wrongPasswordLabel.setVisible(false);
//                if (t1.equals(user.getPassword()))
//                    updateButton.setDisable(true);
//                else
//                    updateButton.setDisable(false);
//            }
//
//        });

        updateInfo(App.getUser());

    } //end initialize

    public void updateInfo(User user) {
        if (user instanceof Guest) {
            //every thing is invisible
            return;
        }

        visibleLabel.setVisible(true);
        notLoggedInLabel.setVisible(false);
        nameTextField.setText(user.getName());
        userNameTextField.setText(user.getUserName());
        emailTextField.setText(user.getEmail());
        passwordField.setText(user.getPassword().substring(0, 8));
        retypePasswordField.setText(user.getPassword().substring(0,8));
    }


}
