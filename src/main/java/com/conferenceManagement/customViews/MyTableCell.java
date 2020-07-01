package com.conferenceManagement.customViews;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.hibernate.sql.Update;

@FunctionalInterface
interface UpdateDataFI<T> {
    void updateData(Node node, T t);
}
public class MyTableCell<S,T> implements Callback<TableColumn<S,T>,TableCell<S,T>> {

    public MyTableCell(){

    }
    Pos pos;

    public MyTableCell(Pos pos){
        this.pos = pos;
    }


    @Override
    public TableCell<S, T> call(TableColumn<S, T> stTableColumn) {
        return new TableCell<>(){
            @Override
            protected void updateItem(T t, boolean b) {
                super.updateItem(t, b);

                if (b || t == null){
                    setGraphic(null);
                } else {
                    var lb = new Label();
                    lb.setText(t.toString());
                    lb.setPadding(new Insets(8));
                    lb.setFont(new Font(14));
                    lb.setAlignment(Pos.CENTER);
                    setAlignment(pos);
                    setGraphic(lb);
                }
            }
        };
    }
}
