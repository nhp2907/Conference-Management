module com.conferenceManagement {
    requires java.naming;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.base;
    requires java.persistence;
    requires java.sql;
    requires net.bytebuddy;
    requires org.hibernate.orm.core;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.fasterxml.classmate;
    requires java.xml.bind;
    requires com.jfoenix;

    exports com.conferenceManagement;

    opens com.conferenceManagement.controller to javafx.fxml;
    exports com.conferenceManagement.controller;

    opens com.conferenceManagement.model;
    exports com.conferenceManagement.model;
}