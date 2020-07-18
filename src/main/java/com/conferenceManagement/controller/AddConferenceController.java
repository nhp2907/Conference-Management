package com.conferenceManagement.controller;

import com.conferenceManagement.model.Conference;
import com.conferenceManagement.dao.ConferenceDAO;
import com.conferenceManagement.dao.PlaceDAO;
import com.conferenceManagement.model.Place;
import com.conferenceManagement.model.UserFunction;
import com.jfoenix.controls.*;
import com.jfoenix.validation.IntegerValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.xml.validation.Validator;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AddConferenceController extends ControllerBase {
    @FXML
    JFXButton addButton;
    @FXML
    JFXButton exitButton;
    @FXML
    JFXTextField nameTextField;
    @FXML
    JFXTextField shortDescriptionTextField;
    @FXML
    JFXTextField detailDescriptionTextField;
    @FXML
    JFXDatePicker startDatePicker;
    @FXML
    JFXDatePicker endDatePicker;
    @FXML
    JFXTimePicker startTimePicker;
    @FXML
    JFXTimePicker endTimePicker;

    @FXML
    JFXComboBox<Place> placeComboBox;

    @FXML
    Label errorLabel;

    @FXML
    ImageView addPlaceImageButton;

    ObservableList<Place> places = FXCollections.observableArrayList(PlaceDAO.getAll());

    public IReturnDataFunction<Conference> returnDataFunction = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        placeComboBox.setItems(places);
        if (places.size() > 0) {
            placeComboBox.getSelectionModel().select(0);
        }

        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
        startTimePicker.setValue(LocalTime.of(7, 30));
        endTimePicker.setValue(LocalTime.of(17, 30));

        addButton.setOnMouseClicked(mouseEvent -> {

            var startLDT = LocalDateTime.of(startDatePicker.getValue(), startTimePicker.getValue());
            var endLDT = LocalDateTime.of(endDatePicker.getValue(), endTimePicker.getValue());

            List<Conference> conferences = ConferenceDAO.getConferenceByPlace(placeComboBox.getValue());

            conferences.forEach(conference -> {
                System.out.println();
            });
            System.out.println("Compare: " + startLDT.compareTo(LocalDateTime.now()));
            var isPlaceValid = conferences.stream().allMatch(conference -> {
                System.out.println(endLDT.compareTo(conference.getStartDateTime()));
                System.out.println(startLDT.compareTo(conference.getEndDateTime()));

                return endLDT.compareTo(conference.getStartDateTime()) <= 0
                        || startLDT.compareTo(conference.getEndDateTime()) >= 0;
            });

            System.out.println("isPlaceValid: " + isPlaceValid);

            var errorMessage = "";
            if (nameTextField.getText().length() < 1) {
                errorMessage = "*tên không được để trống";
            } else if (shortDescriptionTextField.getText().length() < 1) {
                errorMessage = "*mô tả ngắn gọn không được để trống";
            } else if (detailDescriptionTextField.getText().length() < 1) {
                errorMessage = "*mô tả chi tiết không được để trống";
            } else if (startLDT.compareTo(LocalDateTime.now()) <= 0) {
                errorMessage = "*ngày bắt đầu phải sau ngày hôm nay";
            } else if (endLDT.compareTo(startLDT) <= 0) {
                errorMessage = "*ngày kết thúc phải sau ngày bắt đầu";
            } else if (!isPlaceValid) {
                errorMessage = "*địa điểm này đã có hội nghị khác diễn ra";
            } else {
                Conference conference = new Conference();
                conference.setName(nameTextField.getText());
                conference.setShortDescription(shortDescriptionTextField.getText());
                conference.setDetailDescription(detailDescriptionTextField.getText());
                conference.setHoldPlace(placeComboBox.getSelectionModel().getSelectedItem());
                conference.setStartDateTime(startLDT);
                conference.setEndDateTime(endLDT);

                if (returnDataFunction != null) {
                    returnDataFunction.returnData(conference);
                }

                ConferenceDAO.save(conference);
                var stage = (Stage) addButton.getScene().getWindow();
                stage.close();

                errorMessage = "";
            }

            errorLabel.setText(errorMessage);

        });

        exitButton.setOnAction(event -> {
            var button = (JFXButton) event.getSource();
            var stage = (Stage) button.getScene().getWindow();
            stage.close();
        });

        addPlaceImageButton.setOnMouseClicked(mouseEvent -> {
            try {
                var addPlaceUF = new UserFunction("AddPlace");

                var stage = new Stage();
                stage.setScene(new Scene(addPlaceUF.getView()));

                var controller = (AddPlaceController) addPlaceUF.getController();
                controller.setReturnDataFunction(o -> {
                    var place = (Place) o;
                    this.places.add(place);
                });

                stage.setTitle("Add new place");
                stage.setResizable(false);
                stage.show();


            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var dateConverter = new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        };
        startDatePicker.setConverter(dateConverter);
        endDatePicker.setConverter(dateConverter);

//        nameTextField.textProperty().addListener((observableValue, s, t1) -> {
//
//        });
//
//        shortDescriptionTextField.textProperty().addListener((observableValue, s, t1) -> {
//
//        });
//
//        detailDescriptionTextField.textProperty().addListener((observableValue, s, t1) -> {
//
//        });
//
//        startDatePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
//
//        });
//
//        startTimePicker.valueProperty().addListener((observableValue, localTime, t1) -> {
//
//        });
//
//
//        endDatePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
//
//        });
//
//        endTimePicker.valueProperty().addListener((observableValue, localTime, t1) -> {
//
//        });
//        placeComboBox.valueProperty().addListener((observableValue, place, t1) -> {
//
//        });
//        placeComboBox.selectionModelProperty().addListener((observableValue, placeSingleSelectionModel, t1) -> {
//        });
    }


}


