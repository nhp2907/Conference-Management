package com.conferenceManagement.controller;

import com.conferenceManagement.App;
import com.conferenceManagement.model.*;
import com.conferenceManagement.dao.ConferenceDAO;
import com.conferenceManagement.util.UnicodeStringComparator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import java.util.stream.Collectors;

public class ConferenceListForGuestController extends ControllerBase {
    @FXML
    VBox vbox;
    @FXML
    TableView<Conference> tableView;
    @FXML
    HBox adminHbox;
    @FXML
    JFXButton addConferenceButton;
    @FXML
    JFXTextField searchTextField;

    ContextMenu contextMenu;

    ObservableList<Conference> conferences = FXCollections.observableArrayList(ConferenceDAO.getAll());
    ObservableList<Conference> searchResult = null;

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
            if (t1 instanceof Admin) {
                addConferenceButton.setVisible(true);
                tableView.setContextMenu(contextMenu);
            } else {
                addConferenceButton.setVisible(false);
                tableView.setContextMenu(null);
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
        initContextMenu();

        searchTextField.textProperty().addListener((observableValue, s, t1) -> {
            var lowerCaseText = t1.toLowerCase();
            searchResult = FXCollections.observableArrayList(conferences.stream()
                    .filter(c ->
                            UnicodeStringComparator.contains(c.getName().toLowerCase(), lowerCaseText)
                                    || UnicodeStringComparator.contains(c.getHoldPlace().getAddress().toLowerCase(), lowerCaseText)
                                    || UnicodeStringComparator.contains(c.getHoldPlace().getName().toLowerCase(), lowerCaseText))
                    .collect(Collectors.toList()));
            tableView.setItems(searchResult);
            tableView.refresh();
        });

        searchTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
//            if (!t1) {
//                tableView.setItems(conferences);
//                tableView.refresh();
//            }
        });

    }


    void initTable() {
        var idColumn = new TableColumn<Conference, Long>("ID");
        idColumn.setPrefWidth(50);
        idColumn.setCellValueFactory(t -> new SimpleLongProperty(t.getValue().getId()).asObject());

        var nameColumn = new TableColumn<Conference, String>("Tên ");
        nameColumn.setPrefWidth(230);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        var addressColumn = new TableColumn<Conference, String>("Địa điểm");
        addressColumn.setPrefWidth(250);
        addressColumn.setCellValueFactory(t -> {
            return new SimpleStringProperty(t.getValue().getHoldPlace().toString());
        });

        var startTimeColumn = new TableColumn<Conference, LocalDateTime>("Thời gian bắt đầu");
        startTimeColumn.setPrefWidth(170);
        startTimeColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getStartDateTime());
        });
        startTimeColumn.setCellFactory(column ->
                new TableCell<>() {
                    @Override
                    protected void updateItem(LocalDateTime localDateTime, boolean b) {
                        super.updateItem(localDateTime, b);
                        if (b || localDateTime == null) {
                            setGraphic(null);
                        } else {
                            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm a");
                            setText(formatter.format(column.getTableView().getItems().get(getIndex()).getStartDateTime()));
                        }
                    }
                });

        var endTimeColum = new TableColumn<Conference, LocalDateTime>("Thời gian kết thúc");
        endTimeColum.setCellFactory(column ->
                new TableCell<>() {
                    @Override
                    protected void updateItem(LocalDateTime localDateTime, boolean b) {
                        super.updateItem(localDateTime, b);
                        if (b || localDateTime == null) {
                            setGraphic(null);
                        } else {
                            setText(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm a")
                                    .format(column.getTableView().getItems().get(getIndex()).getEndDateTime()));
                        }
                    }
                });

        endTimeColum.setPrefWidth(170);
        endTimeColum.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getEndDateTime()));

        var descriptionColumn = new TableColumn<Conference, String>("Mô tả");
        descriptionColumn.setPrefWidth(250);
        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getShortDescription()));

        var countColumn = new TableColumn<Conference, Integer>("Số lượng");
        countColumn.setPrefWidth(70);
        countColumn.setCellValueFactory(cellData -> {
            int number = (int) cellData.getValue().getConferenceAttendances()
                    .stream().filter(ConferenceAttendance::isAccepted)
                    .count();
            return new SimpleIntegerProperty(number).asObject();
        });


        startTimeColumn.setStyle("-fx-alignment: center");
        endTimeColum.setStyle("-fx-alignment: center");
        idColumn.setStyle("-fx-alignment: center");
        countColumn.setStyle("-fx-alignment: center");

        tableView.setItems(conferences);
        tableView.setEditable(true);
        tableView.getColumns().setAll(idColumn, nameColumn, addressColumn, startTimeColumn, endTimeColum, descriptionColumn, countColumn);
        tableView.setStyle("-fx-selection-bar: #9AD9D4; -fx-selection-bar-non-focused: #A7CCC9;");

        tableView.setRowFactory(tv -> {
            final TableRow<Conference> row = new TableRow<>();

            row.setOnMouseClicked(mouseEvent -> {
                //load the detail view
                try {
                    var conferenceDetailUF = new UserFunction("ConferenceDetail");

                    if (mouseEvent.getClickCount() >= 2 && !row.isEmpty()) {
                        //get selected conference base on the selected row.
                        var selectedItem = tableView.getSelectionModel().getSelectedItem();
                        var conferenceDetailController = (ConferenceDetailController) conferenceDetailUF.getController();
                        conferenceDetailController.previousView = vbox;
                        conferenceDetailController.returnDataFunction = (data) -> {
                            updateConferenceList();
                        };
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

    private void updateConferenceList() {
        tableView.refresh();
    }

    void initContextMenu() {
        MenuItem editItem = new MenuItem("Sữa thông tin");
        editItem.setOnAction(event -> {
            var selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem.getStartDateTime().compareTo(LocalDateTime.now()) <= 0) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Hội nghị đã diễn ra không được sữa đổi thông tin",
                        ButtonType.OK);
                a.show();
                return;
            }
            try {
                var editConferenceUF = new UserFunction("EditConference");
                var stage = new Stage();

                var controller = (EditConferenceController) editConferenceUF.getController();
                controller.updateConferenceInfo(selectedItem);

                controller.returnDataFunction = conference -> tableView.refresh();

                stage.setScene(new Scene(editConferenceUF.getView()));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        contextMenu = new ContextMenu();
        contextMenu.getItems().add(editItem);

        if (App.getUser() instanceof Admin) {
            tableView.setContextMenu(contextMenu);
        }
    }

}
