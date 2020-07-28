package com.conferenceManagement.controller;

import com.conferenceManagement.App;
import com.conferenceManagement.model.User;
import com.conferenceManagement.service.IUserService;
import com.conferenceManagement.service.UserService;
import com.conferenceManagement.util.PasswordUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController extends ControllerBase {
    @FXML
    CheckBox showOldPassWordCheckBox;
    @FXML
    CheckBox newPasswordShowCheckBox;
    @FXML
    CheckBox retypeNewPasswordShowCheckBox;
    @FXML
    JFXPasswordField oldPasswordField;
    @FXML
    JFXPasswordField newPasswordField;
    @FXML
    JFXPasswordField retypePasswordField;

    @FXML
    JFXTextField oldTextField;
    @FXML
    JFXTextField newTextField;
    @FXML
    JFXTextField retypeTextField;
    @FXML
    Label errorLabel;

    @FXML
    JFXButton changeButton;
    @FXML
    JFXButton cancelButton;
    User user;

    IUserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PasswordField p;
        showOldPassWordCheckBox.setGraphic(createImageView());
        newPasswordShowCheckBox.setGraphic(createImageView());
        retypeNewPasswordShowCheckBox.setGraphic(createImageView());

        oldPasswordField.visibleProperty().bind(showOldPassWordCheckBox.selectedProperty().not());
        newPasswordField.visibleProperty().bind(newPasswordShowCheckBox.selectedProperty().not());
        retypePasswordField.visibleProperty().bind(retypeNewPasswordShowCheckBox.selectedProperty().not());

        oldPasswordField.textProperty().bindBidirectional(oldTextField.textProperty());
        newPasswordField.textProperty().bindBidirectional(newTextField.textProperty());
        retypePasswordField.textProperty().bindBidirectional(retypeTextField.textProperty());

        user = userService.getUserByUserName(App.getUser().getUserName());

        changeButton.setOnAction(event -> {
            String errorMessage = "";
            System.out.println(oldPasswordField.getText());
            System.out.println(newPasswordField.getText());
            System.out.println(retypePasswordField.getText());
            if (!PasswordUtil.authenticate(oldPasswordField.getText(),user.getPassword())){
                errorMessage = "Mật khẩu hiện tại không đúng";
            }else if (newPasswordField.getText().length() < 3){
                errorMessage = "Mật khẩu phải có ít nhất 8 ký tự";
            } else if (PasswordUtil.authenticate(newPasswordField.getText(), user.getPassword())){
                errorMessage = "Mật khẩu trùng với mật khẩu củ";
            } else if (!retypePasswordField.getText().equals(newPasswordField.getText())) {
                errorMessage = "Mật khẩu không trùng khớp";
            } else {
                user.setPassword(PasswordUtil.hash(newPasswordField.getText()));
                userService.update(user);
                var stage  = (Stage)cancelButton.getScene().getWindow();
                stage.close();
            }

            errorLabel.setText(errorMessage);
        });
        cancelButton.setOnAction(event -> {
            var stage  = (Stage)cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    ImageView createImageView() {
        ImageView imageView = new ImageView(new Image(App.class.getResource("images/icons8_eye_32.png").toString()));
        imageView.setFitWidth(18);
        imageView.setFitHeight(18);
        return imageView;
    }

}
