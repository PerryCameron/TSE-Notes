package com.L2.interfaces;

import javafx.scene.control.Alert;

import java.sql.SQLException;

public abstract class AlertController<T extends Enum<T>> {

    public abstract Alert getView();
    public abstract void action(T actionEnum) throws SQLException;
}
