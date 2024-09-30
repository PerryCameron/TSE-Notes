package com.L2.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;


public class EntitlementDTO  {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty includes = new SimpleStringProperty();
    private StringProperty notIncludes = new SimpleStringProperty();

    public EntitlementDTO(EntitlementDTO original) {
        this.id.set(original.getId());
        this.name.set(original.getName());
        this.includes.set(original.getIncludes());
        this.notIncludes.set(original.getNotIncludes());
    }

    public EntitlementDTO(Integer id) {
        this.id.set(id);
        this.name.set("New Entitlement");
        this.includes.set("");
        this.notIncludes.set("");
    }

    public EntitlementDTO() {
        this.id.set(0);
        this.name.set("");
        this.includes.set("");
        this.notIncludes.set("");
    }

    public EntitlementDTO(Integer id, String name, String includes, String notIncludes) {
        this.id.set(id);
        this.name.set(name);
        this.includes.set(includes);
        this.notIncludes.set(notIncludes);
    }

    // Getters and Setters for id
    public int getId() {
        return id.get();
    }


    public void setId(int id) {
        this.id.set(id);
    }


    public IntegerProperty idProperty() {
        return id;
    }


    // Getters and Setters for name
    public String getName() {
        return name.get();
    }


    public void setName(String name) {
        this.name.set(name);
    }


    public StringProperty nameProperty() {
        return name;
    }


    // Getters and Setters for includes
    public String getIncludes() {
        return includes.get();
    }


    public void setIncludes(String includes) {
        this.includes.set(includes);
    }


    public StringProperty includesProperty() {
        return includes;
    }


    // Getters and Setters for notIncludes
    public String getNotIncludes() {
        return notIncludes.get();
    }


    public void setNotIncludes(String notIncludes) {
        this.notIncludes.set(notIncludes);
    }


    public StringProperty notIncludesProperty() {
        return notIncludes;
    }

    public void clear() {
        id.set(0);
        name.set("");
        includes.set("");
        notIncludes.set("");
    }

    public void copy(EntitlementDTO original) {
        id.set(original.getId());
        name.set(original.getName());
        includes.set(original.getIncludes());
        notIncludes.set(original.getNotIncludes());
    }

    public String toFancyString() {
        return "EntitlementDTO{" +
                "id=" + id +
                ", name=" + name +
                ", includes=" + includes +
                ", notIncludes=" + notIncludes +
                '}';
    }

    @Override
    public String toString() {
        return name.get(); // This will be displayed in the ComboBox
    }
}


