package com.conferenceManagement.controllers;

import com.conferenceManagement.App;
import com.conferenceManagement.models.DAOs.UserDAO;
import com.conferenceManagement.models.User;
import com.conferenceManagement.models.UserFunction;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    TextField userNameTextField;
    @FXML
    PasswordField passwordField;

    @FXML
    Label registerLabel;

    @FXML
    Label logInFailLabel;

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
            User user = UserDAO.getUserByUsername(username);

            if (user != null && user.getPassword().equals(password)) {
                App.setUser(user);
            } else  {
                logInFailLabel.setVisible(true);
                System.out.println("can not get user name with username = " + username);
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

            var controller = (SignUpController)signUpUF.getController();
            controller.setReturnDataFunction(signUpScecess -> {
                if ((boolean) signUpScecess){
                    var p = (GridPane)mouseEvent.getSource();
                    var stage = (Stage)p.getScene().getWindow();
                    stage.close();
                }
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(signUpUF.getView()));
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

}



