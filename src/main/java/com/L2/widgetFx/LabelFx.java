package com.L2.widgetFx;

import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class LabelFx {

    public static Label of(String title) {
        Label label = new Label(title);
        label.setPadding(new Insets(0, 0, 0, 5));
        return label;
    }
}
