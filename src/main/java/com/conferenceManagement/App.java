package com.conferenceManagement;

import com.conferenceManagement.model.Guest;
import com.conferenceManagement.model.User;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private static Parent parent;
    private static ObjectProperty<User> user = new SimpleObjectProperty(new Guest());

    public static User getUser() {
        return user.get();
    }

    public static ObjectProperty<User> userProperty() {
        return user;
    }

    public static void setUser(User user) {
        App.user.set(user);
    }

    @Override
    public void start(Stage stage) throws IOException {
        //init data

        //load view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        stage.setScene(scene);

        stage.setTitle("Quản Lý Hội Nghị");
//        stage.setMaximized(true);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    public static void setCenter(Parent p){
        ((BorderPane) scene.getRoot()).setCenter(p);
    }
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}