package com.L2.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PartOrderFx {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty noteId = new SimpleIntegerProperty();
    private final StringProperty orderNumber = new SimpleStringProperty();
    private final BooleanProperty showType = new SimpleBooleanProperty();
    private final ListProperty<PartFx> parts = new SimpleListProperty<>(FXCollections.observableArrayList());

    public PartOrderFx(Integer id, Integer noteId, String orderNumber, Boolean showType) {
        this.id.set(id);
        this.noteId.set(noteId);
        this.orderNumber.set(orderNumber);
        this.showType.set(showType);
    }

    public PartOrderFx(int noteId) {
        this.id.set(0);
        this.noteId.set(noteId);
        this.orderNumber.set("");
        this.showType.set(false);
    }

    public String getOrderNumber() {
        return orderNumber.get();
    }

    public StringProperty orderNumberProperty() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber.set(orderNumber);
    }

    public ObservableList<PartFx> getParts() {
        return parts.get();
    }

    public ListProperty<PartFx> partsProperty() {
        return parts;
    }

    public void setParts(ObservableList<PartFx> parts) {
        this.parts.set(parts);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getNoteId() {
        return noteId.get();
    }

    public IntegerProperty noteIdProperty() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId.set(noteId);
    }

    public boolean showType() {
        return showType.get();
    }

    public BooleanProperty showTypeProperty() {
        return showType;
    }

    @Override
    public String toString() {
        return "PartOrderDTO{" +
                "id=" + id +
                ", noteId=" + noteId +
                ", orderNumber=" + orderNumber +
                '}';
    }
}
