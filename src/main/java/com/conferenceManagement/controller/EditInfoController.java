package com.conferenceManagement.controller;

import com.conferenceManagement.App;
import com.conferenceManagement.model.Guest;
import com.conferenceManagement.model.User;
import com.conferenceManagement.model.UserFunction;
import com.conferenceManagement.service.UserService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditInfoController extends ControllerBase {
    @FXML
    VBox logOutButton;
    @FXML
    JFXTextField userNameTextField;
    @FXML
    JFXTextField nameTextField;
    @FXML
    JFXTextField emailTextField;
    @FXML
    JFXPasswordField passwordField;
    @FXML
    Label notLoggedInLabel;
    @FXML
    Label visibleLabel;
    @FXML
    ImageView passwordEditButton;
    @FXML
    ImageView nameEditButton;
    @FXML
    ImageView emailEditButton;
    @FXML
    ImageView nameSaveImageButton;
    @FXML
    ImageView emailSaveImageButton;
    @FXML
    ImageView nameCancelButton;
    @FXML
    ImageView emailCancelButton;

    BooleanProperty nameEditing = new SimpleBooleanProperty(false);
    BooleanProperty emailEditing = new SimpleBooleanProperty(false);


    User user = null;

    UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameSaveImageButton.visibleProperty().bind(nameEditing);
        nameEditButton.visibleProperty().bind(nameEditing.not());
        emailSaveImageButton.visibleProperty().bind(emailEditing);
        emailEditButton.visibleProperty().bind(emailEditing.not());
        nameTextField.editableProperty().bind(nameEditing);
        emailTextField.editableProperty().bind(emailEditing);
        nameCancelButton.visibleProperty().bind(nameEditing);
        emailCancelButton.visibleProperty().bind(emailEditing);

        passwordEditButton.setOnMouseClicked(mouseEvent -> {
            try {
                var changePasswordUF =new UserFunction("ChangePassword");
                var stage = new Stage();
                stage.setScene(new Scene(changePasswordUF.getView()));
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        App.userProperty().addListener((observableValue, user1, t1) -> {
            if (t1 != null) {
                user = t1;
                updateInfo(t1);
            }
        });
        logOutButton.setOnMouseClicked(event -> {
            visibleLabel.setVisible(false);
            notLoggedInLabel.setVisible(true);
            App.setUser(new Guest());
        });

        nameEditButton.setOnMouseClicked(mouseEvent -> {
            nameEditing.set(true);
        });
        nameSaveImageButton.setOnMouseClicked(mouseEvent -> {
            //check data valid
            if (nameTextField.getText().length() >= 3){
                App.getUser().setName(nameTextField.getText());
                userService.update(App.getUser());
                nameEditing.set(false);
            }

        });
        emailEditButton.setOnMouseClicked(mouseEvent -> {
            emailEditing.set(true);
        });
        emailSaveImageButton.setOnMouseClicked(mouseEvent -> {
            //check data valid
            if (emailTextField.getText().length() >= 3){
                App.getUser().setEmail(emailTextField.getText());
                userService.update(App.getUser());
                emailEditing.set(false);
            }

        });

        nameCancelButton.setOnMouseClicked(mouseEvent -> {
            nameEditing.set(false);
            nameTextField.setText(App.getUser().getName());
        });
        emailCancelButton.setOnMouseClicked(mouseEvent -> {
            emailEditing.set(false);
            nameTextField.setText(App.getUser().getEmail());
        });

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
    }


}
