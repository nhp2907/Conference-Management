package com.conferenceManagement.controller;

import com.conferenceManagement.App;
import com.conferenceManagement.model.*;
import com.conferenceManagement.dao.ConferenceAttendanceDAO;
import com.conferenceManagement.service.UserService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
    TableView<ConferenceAttendance> tableView;
    JFXTextField textField;

    TableColumn<ConferenceAttendance, Boolean> statusColumn;

    public Parent previousView;

    List<ConferenceAttendance> attendances = FXCollections.observableArrayList();
    Conference conference = null;

    UserRegistrationConferenceController userRegistrationConferenceController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set conference info

        //set register button status
        /* create table view */
        createTableView();
        /* set register button clicked */
        registerButton.setOnAction(mouseEvent -> {
            /* If user is Guest need to register account first */
            if (App.getUser() instanceof Guest) {
                //login if did not
                try {
                    var loginUF = new UserFunction("Login");
                    LoginController controller = (LoginController) loginUF.getController();
                    controller.setUserService(new UserService());

                    controller.returnDataFunction = user -> {
//                        var confAt = new ConferenceAttendance();
//                        confAt.setConference(conference);
//                        confAt.setUser((User) user);
//
//                        ConferenceAttendanceDAO.save(confAt);
//
//                        registerButton.setText("Đang đợi duyệt");
//                        registerButton.setDisable(true);
//                        registerButton.setStyle("-fx-background-color:green");
                    };


                    Stage stage = new Stage();
                    stage.setScene(new Scene(loginUF.getView()));

                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else { //
                var confAt = new ConferenceAttendance();
                confAt.setConference(conference);
                confAt.setUser(App.getUser());

                ConferenceAttendanceDAO.save(confAt);

                registerButton.setText("Đang đợi duyệt");
                registerButton.setDisable(true);
                registerButton.setStyle("-fx-background-color:green");
            }

        });

        App.userProperty().addListener((observableValue, user, t1) -> {
            System.out.println("User listener at ConferenceDetail; " + user.getClass());
            if (t1 == null) {
                return;
            }

            System.out.println("User listener at ConferenceDetail; New user: " + t1.getName());

            if (t1 instanceof Guest) {
                return;
            }

            /* update register button status corresponding to the binding user */
            var attendanceOptional = this.attendances.stream().filter(c -> c.getUser().equals(App.getUser())).findFirst();
            if (!attendanceOptional.isEmpty()) {
                var attendance = attendanceOptional.get();
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
                tableView.getColumns().add(statusColumn);
            } else {
                tableView.getColumns().remove(statusColumn);
            }

        });

        //back
        backButton.setOnMouseClicked(mouseEvent -> {
            var button = (HBox) mouseEvent.getSource();
            var root = (BorderPane) button.getParent().getParent().getParent();
            root.setCenter(previousView);
        });
    }

    private void createTableView() {
        /* create tableView column */
        var idColumn = new TableColumn<ConferenceAttendance, Long>("ID");
        idColumn.setCellValueFactory(cellData -> {
            return new SimpleLongProperty(cellData.getValue().getConference().getId()).asObject();
        });

        var nameColumn = new TableColumn<ConferenceAttendance, String>("Tên");
        nameColumn.setPrefWidth(250);
        nameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getUser().getName());
        });

        var emailColumn = new TableColumn<ConferenceAttendance, String>("Email");
        emailColumn.setPrefWidth(250);
        emailColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getUser().getEmail());
        });

        statusColumn = new TableColumn("Trạng thái");
        statusColumn.setCellValueFactory(cellData -> {
            return new SimpleBooleanProperty(cellData.getValue().isAccepted());
        });

        statusColumn.setPrefWidth(150);

        statusColumn.setCellFactory(cellData -> {
            return new TableCell<ConferenceAttendance, Boolean>() {
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
                                    ConferenceAttendanceDAO.update(item);

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
        statusColumn.setStyle("-fx-alignment: center");

        tableView.setPlaceholder(new Label("Chưa có đăng ký"));
        if (App.getUser() instanceof Admin)
            tableView.getColumns().addAll(idColumn, nameColumn, emailColumn, statusColumn);
        else
            tableView.getColumns().addAll(idColumn, nameColumn, emailColumn);
    }

    void setConferenceData(Conference conference) {
        //set conference
        this.conference = conference;

        //get user attended to this conference
        this.attendances = ConferenceAttendanceDAO.getUsersByConferenceID(conference.getId());

        /* change register button status */
        var attendance = attendances.stream().filter(a -> a.getUser().equals(App.getUser())).findFirst();
        if (attendance.isEmpty()) {
            //chưa đăng ký
        } else if (attendance.get().getUser().equals(App.getUser())) {
            if (attendance.get().isAccepted()) {
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
        detailDescriptionLabel.setText("Chi tiết: " + conference.getDetailDescription());
    }
}

