package com.L2.interfaces;

import javafx.util.Builder;

public interface Component<T> extends Builder<T> {
    public void flashBorder();
    public void populateFields();
}
