package com.conferenceManagement.controllers;

import com.conferenceManagement.BindingObject;
import com.conferenceManagement.models.*;
import com.conferenceManagement.models.DAOs.ConferenceAttendenceDAO;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
    TableView<ConferenceAttendence> tableView;

    @FXML
    TextFlow textFlow;

    BindingObject<User> userBindingObject = new BindingObject<>();
    public Parent previousView;

    List<ConferenceAttendence> attendances = null;
    Conference conference = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set conference info
        //set register button status

        /* create table view */



        /* create tableView column */
        var idColumn = new TableColumn<ConferenceAttendence, Long>("ID");
        idColumn.setCellValueFactory(cellData -> {
            return new SimpleLongProperty(cellData.getValue().getConference().getId()).asObject();
        });

        var nameColumn = new TableColumn<ConferenceAttendence, String>("Tên");
        nameColumn.setPrefWidth(250);
        nameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getUser().getName());
        });

        var emailColumn = new TableColumn<ConferenceAttendence, String>("Email");
        emailColumn.setPrefWidth(250);
        emailColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getUser().getEmail());
        });

        var statusColum = new TableColumn<ConferenceAttendence, String>("Trạng thái");
        statusColum.setCellValueFactory(cellData -> {
            if (cellData.getValue().isAccepted())
                return new SimpleStringProperty("Đã duyệt");
            else {
                return new SimpleStringProperty("Đang duyệt");
            }
        });

        statusColum.setCellFactory(cellData -> {
            return new TableCell<ConferenceAttendence, String>() {
                @Override
                protected void updateItem(String s, boolean b) {
                    super.updateItem(s, b);
                    if (b || s == null) {
                        setGraphic(null);
                    } else {
                        var button = new Button();
                        var selectedItem = tableView.getSelectionModel().getSelectedItem();
                        ConferenceAttendence attendance;

                        /* find corresponding attend to select user */
                        for (int i = 0; i < attendances.size(); i++) {
                            attendance = (ConferenceAttendence) attendances.get(i);

                            if (attendance.getUser().getId() == selectedItem.getUser().getId()) {
                                if (attendance.isAccepted()) { //get the status of attendance
                                    button.setText("Đã duyệt");
                                } else {
                                    button.setText("Đang duyệt");
                                }

                                break;
                            }
                        }

                        button.setStyle(" -fx-background-color: #39C8B0;\n" +
                                "    -fx-background-radius:5;\n" +
                                "    -fx-text-fill:white;\n" +
                                "    -fx-padding: 5;");

                        button.setPrefWidth(70);

                        button.setOnAction(event -> {
                            if (userBindingObject.get() instanceof Admin) {

                            }
                        });
                    }


                    setAlignment(Pos.CENTER);

                }
            };
        });

        tableView.setPlaceholder(new Label("Chưa có đăng ký"));
        tableView.getColumns().addAll(idColumn, nameColumn, emailColumn);

        /* set register button clicked */
        registerButton.setOnMouseClicked(mouseEvent -> {
            /* If user is Guest need to register account first */
            if (userBindingObject.get() instanceof Guest) {
                //show dialog login
                try {
                    var loginUF = new UserFunction("Login");
                    LoginController controller = (LoginController) loginUF.getController();

                    controller.returnDataFunction = user -> {
                        var confAt = new ConferenceAttendence();
                        confAt.setConference(conference);
                        confAt.setUser(userBindingObject.get());

                        ConferenceAttendenceDAO.save(confAt);

                        registerButton.setText("Đang đợi duyệt");
                        registerButton.setDisable(true);
                        registerButton.setStyle("-fx-background-color:green");
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
                var confAt = new ConferenceAttendence();
                confAt.setConference(conference);
                confAt.setUser(userBindingObject.get());

                ConferenceAttendenceDAO.save(confAt);

                registerButton.setText("Đang đợi duyệt");
                registerButton.setDisable(true);
                registerButton.setStyle("-fx-background-color:green");
            }

        });


        userBindingObject.addListener((observableValue, user, t1) -> {
            System.out.println("New usser: " + t1.getName());
            /* update register button status corresponding to the bindin user */

            for (int i = 0; i < attendances.size(); i++) {
                if (attendances.get(i).getUser().equals(userBindingObject.get())) {
                    if (attendances.get(i).isAccepted())
                        registerButton.setText("Đã đăng ký");
                    else
                        registerButton.setText("Đang đợi duyệt");

                    registerButton.setDisable(true);
                    registerButton.setStyle("-fx-background-color:green");

                    return;
                } else {
                    System.out.println("không cùng binding user");
                }
            }

            System.out.println("user did not register yet");

            if (userBindingObject.get() instanceof Admin) {

            }

        });

        //back
        backButton.setOnMouseClicked(mouseEvent -> {
            var button = (HBox) mouseEvent.getSource();
            var root = (BorderPane) button.getParent().getParent().getParent();
            root.setCenter(previousView);
        });
    }

    void setConferenceData(Conference conference) {
        //set conference
        this.conference = conference;

        //get user attended to this conference
        attendances = ConferenceAttendenceDAO.getUsersByConferenceID(conference.getId());
        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(attendances));

        /* change register button status */
        attendances.forEach(attendance -> {
            System.out.println("conference attendance: " + attendance.getConference().getId() + ": " + attendance.getUser().getId());
            if (attendance.getUser().equals(userBindingObject.get())) {

                if (attendance.isAccepted())
                    registerButton.setText("Đã đăng ký");
                else
                    registerButton.setText("Đang đợi duyệt");

                registerButton.setDisable(true);
                registerButton.setStyle("-fx-background-color:green");
            }
        });


        nameLabel.setText(conference.getName());
        timeLabel.setText("Thời gian: " + conference.getHoldTime().toString());
        placeLabel.setText("Địa điểm: " + conference.getHoldPlace().toString());
        descriptionLabel.setText("Mô tả: " + conference.getShortDescription());
        descriptionLabel.setText("Mô tả: " + conference.getShortDescription());
        detailDescriptionLabel.setText("Mô tả: " + conference.getDetailDescription());
    }
}

