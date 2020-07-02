package com.conferenceManagement.controllers;

import com.conferenceManagement.BindingObject;
import com.conferenceManagement.models.*;
import com.jfoenix.controls.JFXToggleNode;
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

    /* User Function */
    UserFunction viewUserListUF = null;

    ArrayList<Conference> conferenceList;

    BindingObject<User> userFromConferenceRegister = new BindingObject<>(new Guest());
    BindingObject<User> userFromLoginView = new BindingObject<>(new Guest());

    UserFunction viewConferenceListUF = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            viewConferenceListUF = new UserFunction("ConferenceListForGuest");
            userFromConferenceRegister.bind(((ConferenceListForGuestController) viewConferenceListUF.getController()).userBindingObject);

        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Init User Function */
        try {
            viewUserListUF = new UserFunction("UserList");

        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Binding userProperty with viewconferenceList's user property */
        userFromConferenceRegister.addListener((observableValue, user, t1) -> {
            userFromLoginView.set(t1);
        });


        userFromLoginView.addListener((observableValue, user, t1) -> {
            /* Update user info to view */
            if (t1 instanceof Guest) {
                nameLabel.setText("Đăng nhập");
            } else
                nameLabel.setText(t1.getName());

            /* Update user for ConferenceListForGuestController */
            userFromConferenceRegister.set(t1);

        });
        //set toggle group selection change
        toggleGroup.selectedToggleProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            var toggleID = ((JFXToggleNode) newValue).getId();

            switch (toggleID) {
                case "AccountSettings":
                    if (userFromLoginView.get() instanceof Guest) {
                        UserFunction loginUF = null;
                        try {
                            loginUF = new UserFunction("Login");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        /* Binding userProperty with loginViewModel's user property */
                        userFromLoginView.bind(((LoginController) loginUF.getController()).bindingObject);
                        var view = loginUF.getView();

                        /* show dialog */
                        Stage stage = new Stage();
                        stage.setScene(new Scene(view));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();

                        toggleGroup.selectToggle(oldValue);

                    } else {
//                        var view = editInfoUF.getView();
//                        borderPane.setCenter(view);
                    }

                    break;
                case "UserList":
                    if (userFromLoginView.get() instanceof Admin) {
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

        avtStackPane.setOnMouseClicked(mouseEvent -> {
            if (userFromLoginView.get() instanceof Guest) {
                UserFunction loginUF = null;
                try {
                    loginUF = new UserFunction("Login");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /* Binding userProperty with loginViewModel's user property */
                userFromLoginView.bind(((LoginController) loginUF.getController()).bindingObject);
                var view = loginUF.getView();

                /* show dialog */
                Stage stage = new Stage();
                stage.setScene(new Scene(view));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

            } else {

                UserFunction editInfoUF = null;
                try {
                    editInfoUF = new UserFunction("EditInfo");
                    var controller = (EditInfoController) editInfoUF.getController();
                    controller.updateInfo(userFromLoginView.get());

                    controller.setReturnDataFunction(object -> {
                        userFromLoginView.set((User) object);
                    });
                    var view = editInfoUF.getView();
                    borderPane.setCenter(view);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        //show data
        borderPane.setCenter(viewConferenceListUF.getView());
    }


}
