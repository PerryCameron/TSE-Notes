package com.L2.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;


public class EntitlementDTO implements Serializable {

    private static final long serialVersionUID = 1234567890123456789L;

    private transient IntegerProperty id = new SimpleIntegerProperty();
    private transient StringProperty name = new SimpleStringProperty();
    private transient StringProperty includes = new SimpleStringProperty();
    private transient StringProperty notIncludes = new SimpleStringProperty();
    // Default constructor
    public EntitlementDTO() {}
    // Copy Constructor
    public EntitlementDTO(EntitlementDTO original) {
        this.id.set(original.getId());
        this.name.set(original.getName());
        this.includes.set(original.getIncludes());
        this.notIncludes.set(original.getNotIncludes());
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
        System.out.println("Copying current entitlement");
        id.set(original.getId());
        name.set(original.getName());
        includes.set(original.getIncludes());
        notIncludes.set(original.getNotIncludes());
        System.out.println(toFancyString());
    }

    // Custom serialization logic
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(getId());
        out.writeUTF(getName());
        out.writeUTF(getIncludes());
        out.writeUTF(getNotIncludes());
    }


    // Custom deserialization logic
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        id = new SimpleIntegerProperty(in.readInt());
        name = new SimpleStringProperty(in.readUTF());
        includes = new SimpleStringProperty(in.readUTF());
        notIncludes = new SimpleStringProperty(in.readUTF());
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


