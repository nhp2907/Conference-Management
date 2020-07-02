package com.conferenceManagement.controllers;

import com.conferenceManagement.BindingObject;
import com.conferenceManagement.models.*;
import com.conferenceManagement.models.DAOs.ConferenceDAO;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ConferenceListForGuestController extends ControllerBase {
    @FXML
    VBox vbox;
    @FXML
    TableView<Conference> tableView;
    @FXML
    HBox adminHbox;
    @FXML
    JFXButton addConferenceButton;


    ObservableList<Conference> conferences = FXCollections.observableArrayList(ConferenceDAO.getAll());
    BindingObject<User> userBindingObject = new BindingObject<>();
    ObjectProperty<Conference> conferenceProperty = new SimpleObjectProperty<>();

    public ObservableList<Conference> getConferences() {
        return conferences;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /* check admin */
        if (userBindingObject.get() instanceof Admin) {
            addConferenceButton.setVisible(true);
        }

        addConferenceButton.setOnMouseClicked(mouseEvent -> {
            try {
                var addConferenceUF = new UserFunction("AddConference");

                /* show stage */
                var addStage = new Stage();
                addStage.setScene(new Scene(addConferenceUF.getView()));
                addStage.initModality(Modality.APPLICATION_MODAL);

                /* update data back */
                var controller = (AddConferenceController) addConferenceUF.getController();
                controller.returnDataFunction = conference -> {
                    this.conferences.add(conference);
                    ConferenceDAO.save(conference);
                };
                addStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var idColumn = new TableColumn<Conference, Long>("ID");
        idColumn.setCellValueFactory(t -> {
            return new SimpleLongProperty(t.getValue().getId()).asObject();
        });

        var nameColumn = new TableColumn<Conference, String>("Tên ");
        nameColumn.setPrefWidth(250);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        var addressColumn = new TableColumn<Conference, String>("Địa điểm");
        addressColumn.setPrefWidth(250);
        addressColumn.setCellValueFactory(t -> {
            return new SimpleStringProperty(t.getValue().getHoldPlace().toString());
        });

        var timeColumn = new TableColumn<Conference, String>("Thời gian");
        timeColumn.setPrefWidth(140);
        timeColumn.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getHoldTime();
            var formater = new SimpleDateFormat("dd-MM-yyyy, hh:mm");
            return new SimpleStringProperty(formater.format(date));
        });

        var descriptionColum = new TableColumn<Conference, String>("Mô tả");
        descriptionColum.setPrefWidth(250);
        descriptionColum.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getShortDescription());
        });

        idColumn.setStyle("-fx-alignment: center");

//
//        timeColumn.setCellFactory(new MyTableCell<>());
//        idColumn.setCellFactory(new MyTableCell<>(Pos.CENTER));
//        nameColumn.setCellFactory(new MyTableCell<>());
//        addressColumn.setCellFactory(new MyTableCell<>());

        tableView.setItems(conferences);
        tableView.setEditable(true);
        tableView.getColumns().setAll(idColumn, nameColumn, addressColumn, timeColumn, descriptionColum);
        tableView.setStyle("-fx-selection-bar: #9AD9D4; -fx-selection-bar-non-focused: #A7CCC9;");

        tableView.setRowFactory(tv -> {
            TableRow<Conference> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                //load the detail view
                System.out.println(mouseEvent.getClickCount() + " ");
                try {
                    var conferenceDetailUF = new UserFunction("ConferenceDetail");

                    if (mouseEvent.getClickCount() >=2 && !row.isEmpty()) {
                        //get selected conference base on the selected row.
                        var selectedItem = tableView.getSelectionModel().getSelectedItem();
                        var conferenceDetailController = (ConferenceDetailController) conferenceDetailUF.getController();
                        conferenceDetailController.previousView = vbox;
                        conferenceDetailController.setConferenceData(selectedItem);

                        this.userBindingObject.bind(conferenceDetailController.userBindingObject);

                        var source = (TableRow) mouseEvent.getSource();
                        var borderPane = (BorderPane) source.getTableView().getParent().getParent().getParent();
                        System.out.println(borderPane);
                        borderPane.setCenter(conferenceDetailUF.getView());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            return row;
        });

        MenuItem editItem = new MenuItem("Sữa thông tin");
        editItem.setOnAction(event -> {
            var selectedItem = tableView.getSelectionModel().getSelectedItem();

            try {
                var editConferenceUF = new UserFunction("EditConference");
                var stage = new Stage();

                var controller = (EditConferenceController) editConferenceUF.getController();
                controller.updateConfenceInfo(selectedItem);

                controller.returnDataFunction = (conference) -> {
                    ConferenceDAO.update(conference);
                };

                stage.setScene(new Scene(editConferenceUF.getView()));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(editItem);

        tableView.setContextMenu(contextMenu);

    }

}
