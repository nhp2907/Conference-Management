package com.conferenceManagement.controllers;

import com.conferenceManagement.BindingObject;
import com.conferenceManagement.models.*;
import com.conferenceManagement.models.DAOs.ConferenceAttendenceDAO;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConferenceDetailController extends ControllerBase {

    @FXML
    JFXButton registerButton;
    @FXML
    HBox backButton;
    @FXML
    Label nameLabel;
    @FXML
    Label timeLabel;
    @FXML
    Label placeLabel;
    @FXML
    Label detailDescriptionLabel;
    @FXML
    Label descriptionLabel;
    @FXML
    TableView tableView;

    @FXML
    TextFlow textFlow;

    BindingObject<User> userBindingObject = new BindingObject<>();
    public Parent previousView;

    ObservableList<User> attendedUser = null;
    ObjectProperty<Conference> conference = new SimpleObjectProperty<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //make sure register button is not disabled
        registerButton.setDisable(false);

        /* get user attend conference */
        conference.addListener((observableValue, conference1, t1) -> {
            attendedUser = FXCollections.observableArrayList(ConferenceAttendenceDAO.getUsersByConferenceID(conference.get().getId()));
            tableView.getItems().clear();
            tableView.setItems(attendedUser);

            /* change register button status register button */
            attendedUser.forEach(iUser -> {
                if (iUser.equals(userBindingObject.get())){
                    registerButton.setText("Đã đăng ký");
                    registerButton.setStyle("-fx-background-color:green");
                }
            });

        });

        /* create tableView column */
        var idColumn = new TableColumn<User, Long>("ID");
        idColumn.setCellValueFactory(cellData -> {
            return new SimpleLongProperty(cellData.getValue().getId()).asObject();
        });

        var nameColumn = new TableColumn<User, String>("Tên");
        nameColumn.setPrefWidth(250);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        var emailColumn = new TableColumn<User, String>("Email");
        emailColumn.setPrefWidth(250);
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        tableView.setPlaceholder(new Label("Chưa có đăng ký"));
        tableView.getColumns().addAll(idColumn,nameColumn,emailColumn);

        /* set register button clicked */
        registerButton.setOnMouseClicked(mouseEvent -> {
            /* If user is Guest need to register account first */
            if (userBindingObject.get() instanceof Guest) {
                //show dialog login
                try {
                    var loginUF = new UserFunction("Login");
                    LoginController controller = (LoginController) loginUF.getController();

                    controller.iLoginBackAction = user -> {
                        ConferenceAttendenceDAO.save(conference.get().getId(), userBindingObject.get().getId());
                        registerButton.setDisable(true);
                    };

                    userBindingObject.bind(controller.bindingObject);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(loginUF.getView()));

                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ConferenceAttendenceDAO.save(conference.get().getId(), userBindingObject.get().getId());
            }


        });

        userBindingObject.addListener((observableValue, user, t1) -> {
            attendedUser.forEach(iUser -> {
                if (iUser.equals(t1)){
                    registerButton.setText("Đã đăng ký");
                    registerButton.setStyle("-fx-background-color:green");
                }
            });
        });

        //back
        backButton.setOnMouseClicked(mouseEvent -> {
            var button = (HBox)mouseEvent.getSource();
            var root = (BorderPane)button.getParent().getParent().getParent();
            root.setCenter(previousView);
        });
    }

    void setConferenceData(Conference conference) {
        this.conference.set(conference);

        nameLabel.setText(conference.getName());
        timeLabel.setText("Thời gian: " + conference.getHoldTime().toString());
        placeLabel.setText("Địa điểm: " + conference.getHoldPlace().toString());
        descriptionLabel.setText("Mô tả: " + conference.getShortDescription());
        descriptionLabel.setText("Mô tả: " + conference.getShortDescription());
        detailDescriptionLabel.setText("Mô tả: " + conference.getDetailDescription());
    }
}

