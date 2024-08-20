package com.L2.widgetFx;

import javafx.scene.layout.Region;

public class RegionFx {

    public static Region regionHeightOf(double height) {
        Region region = new Region();
        region.setPrefHeight(height);
        return region;
    }

    public static Region regionWidthOf(double width) {
        Region region = new Region();
        region.setPrefHeight(width);
        return region;
    }

    public static Region regionOf(double width, double height) {
        Region region = new Region();
        region.setPrefHeight(width);
        region.setPrefHeight(height);
        return region;
    }
}
