package com.conferenceManagement.controllers;

import com.conferenceManagement.models.DAOs.UserDAO;
import com.conferenceManagement.models.User;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class UserListController extends ControllerBase {
    @FXML
    TableView<User> tableView;

    ObservableList<User> users = FXCollections.observableArrayList(UserDAO.getAll());


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        var idColumn = new TableColumn<User, Long>("ID");
        idColumn.setStyle("-fx-alignment: center");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        var nameColumn = new TableColumn<User, String>("Tên");
        nameColumn.setPrefWidth(250);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        var userNameColumn = new TableColumn<User, String>("User name");
        userNameColumn.setPrefWidth(250);
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        var emailColumn = new TableColumn<User, String>("Email");
        emailColumn.setPrefWidth(200);
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
                        System.out.println("Value factory");
                        var lb = new Button();
                        lb.setText(s ? "Cho phép" : "Đã chặn");

                        lb.setOnAction(mouseEvent -> {
                            System.out.println("clicked");
                            var index = getIndex();
                            var user = getTableView().getItems().get(getIndex());
                            if (user.isStatus() == true) {
                                users.get(index).setStatus(false);
                                UserDAO.update(user);
                                lb.setText("Đã chặn");
                            } else {
                                users.get(index).setStatus(true);
                                lb.setText("Cho phép");
                                UserDAO.update(user);
                            }
                        });

                        lb.setStyle(" -fx-background-color: #2B2B2B;\n" +
                                "    -fx-background-radius:5;\n" +
                                "    -fx-text-fill:white;\n" +
                                "    -fx-padding: 5;");

                        lb.setPrefWidth(90);
                        lb.setAlignment(Pos.CENTER);
                        setAlignment(Pos.CENTER);

                        setGraphic(lb);
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
