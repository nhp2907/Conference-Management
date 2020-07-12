package com.conferenceManagement.controllers;

import com.conferenceManagement.App;
import com.conferenceManagement.models.*;
import com.conferenceManagement.models.DAOs.ConferenceDAO;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.*;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    ObjectProperty<Conference> conferenceProperty = new SimpleObjectProperty<>();

    public ObservableList<Conference> getConferences() {
        return conferences;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /* check admin */
        if (App.getUser() instanceof Admin) {
            addConferenceButton.setVisible(true);
        }

        App.userProperty().addListener((observableValue, user, t1) -> {
            if (t1 instanceof Admin){
                addConferenceButton.setVisible(true);
            } else {
                addConferenceButton.setVisible(false);
            }
        });

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
                    tableView.refresh();
                };
                addStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        initTable();

        MenuItem editItem = new MenuItem("Sữa thông tin");
        editItem.setOnAction(event -> {
            var selectedItem = tableView.getSelectionModel().getSelectedItem();

            try {
                var editConferenceUF = new UserFunction("EditConference");
                var stage = new Stage();

                var controller = (EditConferenceController) editConferenceUF.getController();
                controller.updateConfenceInfo(selectedItem);

                controller.returnDataFunction = (conference) -> {
                    tableView.refresh();
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


    void initTable(){
        var idColumn = new TableColumn<Conference, Long>("ID");
        idColumn.setCellValueFactory(t -> {
            return new SimpleLongProperty(t.getValue().getId()).asObject();
        });

        var nameColumn = new TableColumn<Conference, String>("Tên ");
        nameColumn.setPrefWidth(230);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        var addressColumn = new TableColumn<Conference, String>("Địa điểm");
        addressColumn.setPrefWidth(250);
        addressColumn.setCellValueFactory(t -> {
            return new SimpleStringProperty(t.getValue().getHoldPlace().toString());
        });

        var timeColumn = new TableColumn<Conference, String>("Thời gian bắt đầu");
        timeColumn.setPrefWidth(150);
        timeColumn.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getStartDateTime();
            var formater = DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm a");
            return new SimpleStringProperty(formater.format(date));
        });

        var endTimeColum = new TableColumn<Conference, String>("Thời gian kết thúc");
        endTimeColum.setPrefWidth(150);
        endTimeColum.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getEndDateTime();
            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm a");
            return new SimpleStringProperty(formatter.format(date));
        });
        var descriptionColumn = new TableColumn<Conference, String>("Mô tả");
        descriptionColumn.setPrefWidth(250);
        descriptionColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getShortDescription());
        });

        var stateColumn = new TableColumn<Conference, Boolean>("Trạng thái");
        stateColumn.setCellValueFactory( cellData -> {
            if (cellData.getValue().getStartDateTime().compareTo(LocalDateTime.now()) <= 0){
                return new SimpleBooleanProperty(true);
            }else {
                return new SimpleBooleanProperty(false);
            }
        });
        stateColumn.setCellFactory(TableColumn -> {
            return new TableCell<Conference, Boolean>(){
                @Override
                protected void updateItem(Boolean state, boolean b) {
                    super.updateItem(state, b);

                    if (state == null || b){
                        setGraphic(null);
                        return;
                    } else {
                        var lb = new Label();
                    }
                }
            };
        });


        idColumn.setStyle("-fx-alignment: center");

        tableView.setItems(conferences);
        tableView.setEditable(true);
        tableView.getColumns().setAll(idColumn, nameColumn, addressColumn, timeColumn,endTimeColum, descriptionColumn);
        tableView.setStyle("-fx-selection-bar: #9AD9D4; -fx-selection-bar-non-focused: #A7CCC9;");

        tableView.setRowFactory(tv -> {
            TableRow<Conference> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                //load the detail view
                try {
                    var conferenceDetailUF = new UserFunction("ConferenceDetail");

                    if (mouseEvent.getClickCount() >=2 && !row.isEmpty()) {
                        //get selected conference base on the selected row.
                        var selectedItem = tableView.getSelectionModel().getSelectedItem();
                        var conferenceDetailController = (ConferenceDetailController) conferenceDetailUF.getController();
                        conferenceDetailController.previousView = vbox;
                        conferenceDetailController.setConferenceData(selectedItem);

                        var source = (TableRow) mouseEvent.getSource();
                        var borderPane = (BorderPane) source.getTableView().getParent().getParent().getParent();
                        borderPane.setCenter(conferenceDetailUF.getView());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            return row;
        });
    }


}
