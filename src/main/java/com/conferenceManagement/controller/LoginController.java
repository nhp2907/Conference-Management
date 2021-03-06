package com.conferenceManagement.controller;

import com.conferenceManagement.App;
import com.conferenceManagement.model.UserFunction;
import com.conferenceManagement.service.IUserService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends ControllerBase {
    @FXML
    GridPane root;
    @FXML
    JFXButton loginButton;
    @FXML
    JFXButton cancelButton;
    @FXML
    JFXTextField userNameTextField;
    @FXML
    JFXPasswordField passwordField;

    @FXML
    Label registerLabel;

    @FXML
    Label logInFailLabel;

    IUserService userService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                loginButton.fire();
                ev.consume();
            }
        });

        //add button click action
        loginButton.setOnAction(mouseEvent -> {
            logInFailLabel.setVisible(false);
            var username = userNameTextField.getText();
            var password = passwordField.getText();
            var user = userService.getUserByUserName(username);

            if (userService.checkLogin(username, password)) {
                System.out.println(user.isAvailable());
                if (user.isAvailable()) {
                    App.setUser(user);
                } else {
                    var alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Người dùng đã bị chặn");
                    alert.showAndWait();
                    return;
                }

            } else {
                logInFailLabel.setVisible(true);
                return;
            }

            if (returnDataFunction != null) {
                returnDataFunction.returnData(user);
            }

            /* close stage if log in success */
            var source = (JFXButton) mouseEvent.getSource();
            var stage = (Stage) source.getScene().getWindow();
            stage.close();
        });

        /* handle sign up function */
        registerLabel.setOnMouseClicked(mouseEvent -> {
            UserFunction signUpUF = null;
            try {
                signUpUF = new UserFunction("SignUp");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            var controller = (SignUpController) signUpUF.getController();
            controller.setUserService(userService);

            Stage stage = new Stage();
            stage.setScene(new Scene(signUpUF.getView()));
            stage.setTitle("Đăng ký tài khoản");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        });

        /* cancel button clicked */
        cancelButton.setOnMouseClicked(mouseEvent -> {
            var source = (JFXButton) mouseEvent.getSource();
            var stage = (Stage) source.getScene().getWindow();
            stage.close();
        });


    }

    void setUserService(IUserService userService) {
        this.userService = userService;
    }

}



