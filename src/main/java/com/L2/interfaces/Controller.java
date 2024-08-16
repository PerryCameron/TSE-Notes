package com.L2.interfaces;

import javafx.scene.layout.Region;

public abstract class Controller<T extends Enum<T>> {

    public abstract Region getView();
    public abstract void action(T actionEnum);
}
