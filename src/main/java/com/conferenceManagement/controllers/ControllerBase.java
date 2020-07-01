package com.conferenceManagement.controllers;

import com.conferenceManagement.models.User;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

public abstract class ControllerBase implements Initializable {

    public static interface IReturnDataFunction<T>{
        void backFunction(T t);
    }

    private Parent rootView;
    public void setRootView(Parent view){
        this.rootView = view;
    }
    public Parent getRootView(){
        return this.rootView;
    }

}
