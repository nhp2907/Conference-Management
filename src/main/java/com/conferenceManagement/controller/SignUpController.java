package com.conferenceManagement.controller;

import com.conferenceManagement.model.User;
import com.conferenceManagement.service.IUserService;
import com.conferenceManagement.service.UserService;
import com.conferenceManagement.util.PasswordUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    @FXML
    Label errorLabel;

    IUserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerButton.setDefaultButton(true);
        registerButton.setOnAction(mouseEvent -> {


            var pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            var matcher = pattern.matcher(emailTextField.getText());

            var temp = userService.getUserByUserName(userNameTextField.getText());
            String errorMessage;
            if (temp != null) {
                errorMessage = "Tên đăng nhập đã tồn tại";
            } else if (userNameTextField.getText().length() < 6) {
                errorMessage = "Username tối thiểu phải có 6 ký tự";
            } else if (passwordField.getText().length() < 8) {
                errorMessage = "Mật khẩu tối thiểu phải có 8 ký tự";
            } else if (!passwordField.getText().equals(retypePasswordField.getText())) {
                errorMessage = "Mật khẩu không trùng khớp";
            } else if (nameTextField.getText().length() <= 0) {
                errorMessage = "Tên không được để trống";
            } else if (!matcher.find()) {
                errorMessage = "Email không hợp lệ";
            } else {
                User user = new User();

                user.setUserName(userNameTextField.getText());
                user.setName(nameTextField.getText());
                user.setPassword(PasswordUtil.hash(passwordField.getText()));
                user.setEmail(emailTextField.getText());
                user.setAvailable(true);
                errorMessage = "";
                userService.save(user);


                if (returnDataFunction != null) {
                    returnDataFunction.returnData(user);
                }

                var source = (JFXButton) mouseEvent.getSource();
                var stage = (Stage) source.getScene().getWindow();
                stage.close();
            }

            System.out.println(errorMessage);
            errorLabel.setText(errorMessage);
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


