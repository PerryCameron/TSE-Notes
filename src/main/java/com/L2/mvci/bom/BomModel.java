package com.L2.mvci.bom;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BomModel {
    StringProperty searchComponent = new SimpleStringProperty();

    public StringProperty searchComponentProperty() {
        return searchComponent;
    }

}
