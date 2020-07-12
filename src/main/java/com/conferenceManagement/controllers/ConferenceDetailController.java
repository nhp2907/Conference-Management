package com.conferenceManagement.controllers;

import com.conferenceManagement.App;
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
import javafx.scene.text.Font;
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

        var statusColum = new TableColumn<ConferenceAttendence, Boolean>("Trạng thái");
        statusColum.setCellValueFactory(cellData -> {
            return new SimpleBooleanProperty(cellData.getValue().isAccepted());
        });

        statusColum.setPrefWidth(150);

        statusColum.setCellFactory(cellData -> {
            return new TableCell<ConferenceAttendence, Boolean>() {
                @Override
                protected void updateItem(Boolean isAccepted, boolean b) {
                    super.updateItem(isAccepted, b);
                    if (b || isAccepted == null) {
                        setGraphic(null);
                    } else {
                        var button = new Button();
                        if (isAccepted) {
                            button.setText("Đã duyệt");
                            button.setStyle("-fx-background-radius: 5;" +
                                    "-fx-background-color: #72C156;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-padding: 5;");
                        } else {
                            button.setText("Chưa duyệt");
                            button.setStyle("-fx-background-radius: 5;" +
                                    "-fx-background-color: #F68567;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-padding: 5;");
                        }

                        button.setPrefWidth(100);
                        button.setFont(new Font(14));
                        button.setAlignment(Pos.CENTER);

                        button.setOnMouseClicked(event -> {
                            if (App.getUser() instanceof Admin) {

                                var item = getTableView().getItems().get(getIndex());
                                if (item.isAccepted() == false) {
                                    item.setAccepted(true);
                                    ConferenceAttendenceDAO.update(item);

                                    button.setText("Đã duyệt");
                                    button.setStyle("-fx-background-color: #72C156;" +
                                            "-fx-text-fill: white;");

                                }
                            }
                        });

                        setGraphic(button);
                    }
                    setAlignment(Pos.CENTER);

                }
            };
        });
        statusColum.setStyle("-fx-alignment: center");

        tableView.setPlaceholder(new Label("Chưa có đăng ký"));
        if (App.getUser() instanceof Admin)
            tableView.getColumns().addAll(idColumn, nameColumn, emailColumn, statusColum);
        else
            tableView.getColumns().addAll(idColumn, nameColumn, emailColumn);

        /* set register button clicked */
        registerButton.setOnAction(mouseEvent -> {
            /* If user is Guest need to register account first */
            if (App.getUser() instanceof Guest) {
                //show dialog login
                try {
                    var loginUF = new UserFunction("Login");
                    LoginController controller = (LoginController) loginUF.getController();

                    controller.returnDataFunction = user -> {
                        var confAt = new ConferenceAttendence();
                        confAt.setConference(conference);
                        confAt.setUser((User) user);

                        ConferenceAttendenceDAO.save(confAt);

                        registerButton.setText("Đang đợi duyệt");
                        registerButton.setDisable(true);
                        registerButton.setStyle("-fx-background-color:green");
                    };


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
                confAt.setUser(App.getUser());

                ConferenceAttendenceDAO.save(confAt);

                registerButton.setText("Đang đợi duyệt");
                registerButton.setDisable(true);
                registerButton.setStyle("-fx-background-color:green");
            }

        });

        App.userProperty().addListener((observableValue, user, t1) -> {
            System.out.println("New usser: " + t1.getName());

            /* update register button status corresponding to the binding user */
            var attendenceOptional = this.attendances.stream().filter(c -> c.getUser().equals(App.getUser())).findFirst();
            if (!attendenceOptional.isEmpty()) {
                var attendance = attendenceOptional.get();
                registerButton.setDisable(true);
                registerButton.setStyle("-fx-background-color:green");

                if (attendance.isAccepted()) {
                    registerButton.setText("Đã đăng ký");
                } else {
                    registerButton.setText("Đang duyệt");
                }
            }

            if (App.getUser() instanceof Admin) {
                System.out.println("Admin can accept user register");
                tableView.getColumns().add(statusColum);
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
        this.attendances = ConferenceAttendenceDAO.getUsersByConferenceID(conference.getId());

        /* change register button status */
        var attendence = attendances.stream().filter(a -> a.getUser().equals(App.getUser())).findFirst();
        if (attendence.isEmpty()) {
            //chưa đăng ký
        } else if (attendence.get().getUser().equals(App.getUser())) {
            if (attendence.get().isAccepted()) {
                registerButton.setText("Đã đăng ký");
            } else {
                registerButton.setText("Đang đợi duyệt");
            }

            registerButton.setDisable(true);
            registerButton.setStyle("-fx-background-color:green");
        }


        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(attendances));

        nameLabel.setText(conference.getName());
        timeLabel.setText("Thời gian: " + conference.getStartDateTime().

                toString());
        placeLabel.setText("Địa điểm: " + conference.getHoldPlace().

                toString());
        descriptionLabel.setText("Mô tả: " + conference.getShortDescription());
        descriptionLabel.setText("Mô tả: " + conference.getShortDescription());
        detailDescriptionLabel.setText("Mô tả: " + conference.getDetailDescription());
    }
}

