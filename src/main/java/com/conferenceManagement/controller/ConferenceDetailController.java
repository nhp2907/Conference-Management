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
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConferenceDetailController extends ControllerBase {

    @FXML
    JFXButton registerButton;
    @FXML
    JFXButton cancelButton;
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

    @FXML
    HBox approvingHbox;

    TableColumn<ConferenceAttendance, Boolean> statusColumn;

    public Parent previousView;

    List<ConferenceAttendance> attendances = FXCollections.observableArrayList();
    Conference conference = null;

    UserRegistrationConferenceController userRegistrationConferenceController;

    BooleanProperty isRegistered = new SimpleBooleanProperty(false);
    BooleanProperty conferenceHasTaken = new SimpleBooleanProperty(false);
    BooleanProperty isConferenceFull = new SimpleBooleanProperty(false);
    BooleanProperty isApproved = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set conference info
        registerButton.visibleProperty().bind(isRegistered.not().or(conferenceHasTaken));
        registerButton.disableProperty().bind(isRegistered.or(conferenceHasTaken).or(isConferenceFull));
        cancelButton.visibleProperty().bind(isRegistered.and(conferenceHasTaken.not()));
        approvingHbox.visibleProperty().bind(isRegistered.and(isApproved.not()));

        //set register button status
        /* create table view */
        createTableView();
        /* set register button clicked */
        cancelButton.setOnAction(event -> {
            if (conference.getStartDateTime().compareTo(LocalDateTime.now()) > 0) {
                ConferenceAttendanceDAO.deleteAttendanceRegistrationById(conference, App.getUser());
                isRegistered.set(false);
                isApproved.set(false);
                isConferenceFull.set(false);

                registerButton.setText("Đăng ký tham dự");
                attendances.removeIf(a -> a.getUser().equals(App.getUser()));
            }
        });
        registerButton.setOnAction(mouseEvent -> {
            /* If user is Guest need to register account first */
            if (App.getUser() instanceof Guest) {
                //login if did not
                try {
                    var loginUF = new UserFunction("Login");
                    LoginController controller = (LoginController) loginUF.getController();
                    controller.setUserService(new UserService());

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
                isRegistered.set(true);
            }
        });

        App.userProperty().addListener((observableValue, user, t1) -> {
            isRegistered.set(false);
            isApproved.set(false);

            System.out.println("User listener at ConferenceDetail; " + user.getClass());
            if (t1 == null || t1 instanceof Guest) {
                return;
            }

            System.out.println("User listener at ConferenceDetail; New user: " + t1.getName());

            /* update register button status corresponding to the binding user */
            var attendanceOptional = this.attendances.stream().filter(c -> c.getUser().equals(App.getUser())).findFirst();

            if (!attendanceOptional.isEmpty()) {
                isRegistered.set(true);

                var attendance = attendanceOptional.get();

                if (attendance.isAccepted()) {
                    registerButton.setText("Đã đăng ký");
                    isApproved.set(true);
                } else {
                    registerButton.setText("Đang duyệt");
                }
            }


            //show the status column if user is admin
            if (App.getUser() instanceof Admin) {
                System.out.println("Admin can accept user register");
                tableView.getColumns().add(statusColumn);
                this.attendances = ConferenceAttendanceDAO.getConferencesByConference(conference);
            } else {
                tableView.getColumns().remove(statusColumn);
            }
            System.out.println(isRegistered.get());
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
        idColumn.setPrefWidth(50);
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
        idColumn.setStyle("-fx-alignment: center");

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
        attendances = ConferenceAttendanceDAO.getConferencesByConference(conference);
        int count = attendances.size();

        if (count >= conference.getHoldPlace().getCapacity()) {
            isConferenceFull.set(true);
            registerButton.setText("Đã hết chỗ");
        } else {
        }

        if (App.getUser() instanceof Guest) {

        } else {
            System.out.println("not guest");
            /* check that logged in user had registered this conference */
            var attendance = attendances.stream().filter(a -> a.getUser().equals(App.getUser())).findAny();
            isRegistered.set(false);
            isApproved.set(false);
            if (!attendance.isEmpty()) {
                System.out.println("not empty");
                isRegistered.set(true);
                if (attendance.get().isAccepted()) {
                    registerButton.setText("Đã đăng ký");
                    isApproved.set(true);
                } else {
                    System.out.println("Đang đợi duyệt");
                    registerButton.setText("Đang đợi duyệt");
                }

            }
        }

        if (conference.getEndDateTime().compareTo(LocalDateTime.now()) <= 0) {
            System.out.println("Conference had taken!");
            conferenceHasTaken.set(true);
            registerButton.setText("Đã diễn ra");
        }

        if (!(App.getUser() instanceof Admin)) {
            attendances.removeIf(Predicate.not(ConferenceAttendance::isAccepted));
        }

        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(attendances));

        nameLabel.setText(conference.getName());
        timeLabel.setText("Thời gian: " + conference.getStartDateTime().toString());
        placeLabel.setText("Địa điểm: " + conference.getHoldPlace().toString());
        descriptionLabel.setText("Mô tả: " + conference.getShortDescription());
        detailDescriptionLabel.setText("Chi tiết: " + conference.getDetailDescription());
    }
}

