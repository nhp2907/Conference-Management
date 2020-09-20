package com.conferenceManagement.controller;

import com.conferenceManagement.App;
import com.conferenceManagement.dao.ConferenceAttendanceDAO;
import com.conferenceManagement.model.Conference;
import com.conferenceManagement.model.ConferenceAttendance;
import com.conferenceManagement.model.UserFunction;
import com.conferenceManagement.service.IConferenceService;
import com.conferenceManagement.util.UnicodeStringComparator;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UserAttendedConferenceController extends ControllerBase {
    @FXML
    TableView<Conference> tableView;
    @FXML
    VBox vbox;
    @FXML
    TextField searchTextField;


    /* Data */
    ObservableList<Conference> conferences = null;
    ObservableList<Conference> searchResult = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getData();
        System.out.println(conferences.size());
        initTable();

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

    IConferenceService conferenceService = null;

    void getData() {
        this.conferences = FXCollections.observableArrayList(ConferenceAttendanceDAO.getConferenceByUserId(App.getUser()));
    }


    void initTable() {
        var idColumn = new TableColumn<Conference, Long>("IDHN");
        idColumn.setPrefWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
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
                                    .format(column.getTableView().getItems().get(getIndex()).getStartDateTime()));
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

        var unregisterColumn = new TableColumn<Conference, ConferenceAttendance>("Huỷ đăng ký");
        unregisterColumn.setPrefWidth(110);
        unregisterColumn.setCellValueFactory(cellData -> {
            var conference = cellData.getValue();
            var ca = ConferenceAttendanceDAO.getConferenceById(conference, App.getUser());
            return new SimpleObjectProperty<>(ca);
        });
        unregisterColumn.setCellFactory(tableColumn -> new TableCell<Conference, ConferenceAttendance>() {
            @Override
            protected void updateItem(ConferenceAttendance ca, boolean b) {
                super.updateItem(ca, b);
                if (ca == null || b) {
                    setGraphic(null);
                } else {
                    var button = new Button("Bỏ tham dự");
                    button.setStyle("-fx-background-radius:5;\n" +
                            "    -fx-text-fill:white;\n" +
                            "    -fx-padding: 5;");
                    button.setStyle("-fx-background-color: #72C156");

                    button.setPrefWidth(90);
                    button.setAlignment(Pos.CENTER);

                    if (!ca.isAccepted()) {
                        button.setText("Bỏ đăng ký");
                        button.setStyle("-fx-background-color: #F68567");
                    }

                    button.setOnAction(event -> {
                        if (ca.getConference().isTaken()) {
                            var alert = new Alert(Alert.AlertType.WARNING);
                            alert.setHeaderText("Hội nghị đã diễn ra!");
                            alert.setContentText("Không thể bỏ đăng ký hội nghị đã diễn ra!");
                            alert.show();
                        } else {
                            var alert = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận " + button.getText());
                            var res = alert.showAndWait();
                            if (res.get() == ButtonType.OK) {
                                ConferenceAttendanceDAO.cancelRegistration(ca);
                                setGraphic(null);
                                getTableView().getItems().remove(getIndex());
                                tableView.refresh();
                            }
                        }
                    });

                    setGraphic(button);
                }
            }
        });


        startTimeColumn.setStyle("-fx-alignment: center");
        endTimeColum.setStyle("-fx-alignment:  center ");
        idColumn.setStyle(" -fx-alignment:  center");
        countColumn.setStyle("-fx-alignment :  center");
        unregisterColumn.setStyle("-fx-alignment :  center");

        tableView.setItems(conferences);
        tableView.setEditable(true);
        tableView.getColumns().setAll(idColumn, nameColumn, addressColumn, startTimeColumn, descriptionColumn, countColumn, unregisterColumn);
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

    public void setConferenceService(IConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }
}
