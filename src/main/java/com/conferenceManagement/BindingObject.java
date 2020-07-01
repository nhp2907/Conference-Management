package com.conferenceManagement;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class BindingObject<T>{
    ObjectProperty<T> main = new SimpleObjectProperty<>();
    ObjectProperty<T> sup = new SimpleObjectProperty<>();

    public BindingObject(){
        sup.addListener((observableValue, t, t1) -> {
            main.set(t1);
        });
        main.addListener((observableValue, t, t1) -> {
            sup.set(t1);
        });
    }
    public BindingObject(T v){
        main.set(v);
        sup.set(v);
        sup.addListener((observableValue, t, t1) -> {
            main.set(t1);
        });
        main.addListener((observableValue, t, t1) -> {
            sup.set(t1);
        });
    }

    public void bind(BindingObject<T> bindingObject){
        if (bindingObject.main.get() == null){
            bindingObject.main.set(this.get());
        }
        sup.bindBidirectional(bindingObject.main);
    }

    public void set(T t){
        main.set(t);
    }

    public T get(){
        return main.get();
    }

    public void addListener(ChangeListener<T> changeListener){
        main.addListener(changeListener);
    }



}
