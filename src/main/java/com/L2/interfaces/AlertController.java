package com.L2.interfaces;

import javafx.scene.control.Alert;

public abstract class AlertController<T extends Enum<T>> {

    public abstract Alert getView();
    public abstract void action(T actionEnum);
}
