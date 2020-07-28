package com.conferenceManagement.controller;

import com.conferenceManagement.dao.ConferenceAttendanceDAO;
import com.conferenceManagement.dao.UserDAO;
import com.conferenceManagement.model.ConferenceAttendance;
import com.conferenceManagement.model.User;
import com.conferenceManagement.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class UserListController extends ControllerBase {
    @FXML
    TableView<User> tableView;

    UserService userService = new UserService();

    ObservableList<User> users = FXCollections.observableArrayList(userService.getAll());


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        var idColumn = new TableColumn<User, Long>("ID");
        idColumn.setPrefWidth(50);
        idColumn.setStyle("-fx-alignment: center");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        var nameColumn = new TableColumn<User, String>("Tên");
        nameColumn.setPrefWidth(250);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        var userNameColumn = new TableColumn<User, String>("User name");
        userNameColumn.setPrefWidth(250);
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        var emailColumn = new TableColumn<User, String>("Email");
        emailColumn.setPrefWidth(250);
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        var stateColumn = new TableColumn<User, Boolean>("Trạng thái truy cập");
        stateColumn.setPrefWidth(200);
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        stateColumn.setCellFactory(treeTableColumn -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(Boolean s, boolean b) {
                    super.updateItem(s, b);
                    if (b || s == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        var button = new Button();
                        button.setText(s ? "Cho phép" : "Đã chặn");

                        button.setOnAction(mouseEvent -> {
                            var index = getIndex();
                            var user = getTableView().getItems().get(getIndex());
                            if (user.isStatus() == true) {
                                users.get(index).setStatus(false);
                                userService.update(user);
                                button.setText("Đã chặn");

                                //remove from attended conference
                                ConferenceAttendanceDAO.deleteAttendanceRegistrationByUser(user);
                            } else {
                                users.get(index).setStatus(true);
                                button.setText("Cho phép");
                                userService.update(user);
                            }
                        });

                        button.setStyle(" -fx-background-color: #72C156;\n" +
                                "    -fx-background-radius:5;\n" +
                                "    -fx-text-fill:white;\n" +
                                "    -fx-padding: 5;");

                        button.setPrefWidth(90);
                        button.setAlignment(Pos.CENTER);
                        setAlignment(Pos.CENTER);

                        setGraphic(button);
                    }
                }
            };
        });

        stateColumn.setStyle("-fx-alignment: center");
        idColumn.setCellFactory(a -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(Long along, boolean b) {
                    super.updateItem(along, b);

                    if (b || along == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Label lb = new Label();
                        lb.setText(along.toString());


                        setGraphic(lb);

                        setAlignment(Pos.CENTER);
                    }
                }
            };
        });
        tableView.setPlaceholder(new Label("Chưa có đăng ký"));


        tableView.setItems(users);
//        tableView.setShowRoot(false);
        tableView.setEditable(true);
        tableView.getColumns().setAll(idColumn, nameColumn, userNameColumn, emailColumn, stateColumn);
    }
}
