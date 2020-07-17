package com.conferenceManagement.controllers;

import com.conferenceManagement.App;
import com.conferenceManagement.models.*;
import com.jfoenix.controls.JFXToggleNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController extends ControllerBase {
    @FXML
    BorderPane borderPane;
    @FXML
    ToggleGroup toggleGroup;

    @FXML
    Label accountLabel;
    @FXML
    Label nameLabel;
    @FXML
    StackPane avtStackPane;
    @FXML
    JFXToggleNode emptyToggleNode;

    /* User Function */
    UserFunction viewUserListUF = null;
    UserFunction viewConferenceListUF = null;
    UserFunction editInfoUF = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* Init User Function */
        try {
            editInfoUF = new UserFunction("EditInfo");
            viewUserListUF = new UserFunction("UserList");
            viewConferenceListUF = new UserFunction("ConferenceListForGuest");

            var editInfoController = (EditInfoController) editInfoUF.getController();
            editInfoController.setReturnDataFunction(user -> {
                if (user instanceof Guest) {
                    nameLabel.setText("Đăng nhập");
                } else
                    nameLabel.setText(((User)user).getName());
            });
        } catch (IOException e) {

            e.printStackTrace();
        }

        App.userProperty().addListener((observableValue, user, t1) -> {
            System.out.println("App.user changed from " + user.getName() + " to " + t1.getName());
            if (t1 instanceof Guest) {
                nameLabel.setText("Đăng nhập");
            } else
                nameLabel.setText(t1.getName());
        });


        //set toggle group selection change
        toggleGroup.selectedToggleProperty().addListener((observableValue, oldValue, newValue) -> {
            String toggleID = null;
            if (newValue == null) {
                toggleID =  ((JFXToggleNode) oldValue).getId();
            } else {
                toggleID =  ((JFXToggleNode) newValue).getId();
            }


            switch (toggleID) {
                case "AvtImage":
                case "NameLabel":
                    if (App.getUser() instanceof Guest) {
                        UserFunction loginUF = null;
                        try {
                            loginUF = new UserFunction("Login");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        var controller = (LoginController) loginUF.getController();
                        controller.setReturnDataFunction(user -> {
//                            borderPane.setCenter(editInfoUF.getView());
                        });

                        var view = loginUF.getView();

                        /* show dialog */
                        Stage stage = new Stage();
                        stage.setScene(new Scene(view));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.show();

                        toggleGroup.selectToggle(emptyToggleNode);

                    } else {
                        var view = editInfoUF.getView();
                        borderPane.setCenter(view);
                    }


                    break;
                case "UserList":
                    if (App.getUser() instanceof Admin) {
                        var view = viewUserListUF.getView();
                        borderPane.setCenter(view);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Chức năng này chỉ dành cho Admin");
                        alert.show();
                    }
                    break;
                case "ConferenceList":
                    var view = viewConferenceListUF.getView();
                    borderPane.setCenter(view);
                    break;
                default:

            }
        });


        //show data
        borderPane.setCenter(viewConferenceListUF.getView());
    }


}
