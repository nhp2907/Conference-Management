package com.conferenceManagement.controller;

import javafx.fxml.Initializable;
import javafx.scene.Parent;

public abstract class ControllerBase implements Initializable {

    public static interface IReturnDataFunction<T>{
        void returnData(T t);
    }

    IReturnDataFunction returnDataFunction = null;
    void setReturnDataFunction(IReturnDataFunction returnDataFunction){
        this.returnDataFunction = returnDataFunction;
    }



    private Parent rootView;
    public void setRootView(Parent view){
        this.rootView = view;
    }
    public Parent getRootView(){
        return this.rootView;
    }

}
