package com.L2.mvci.bom.components;

import com.L2.mvci.bom.BomModel;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Builder;

import java.util.ArrayList;
import java.util.List;

public class LevelBarChart implements Builder<BarChart> {

    private final BomModel bomModel;

    public LevelBarChart(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    @Override
    public BarChart build() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Part Count");
        BarChart<String, Number> levelsChart = new BarChart<>(xAxis, yAxis);
        levelsChart.setTitle("Parts per Level");
        levelsChart.setPrefHeight(200); // Adjust height to fit typical number of levels comfortably
        levelsChart.setPrefWidth(400); // Compact width for the bar chart
        levelsChart.setCategoryGap(15); // Adjust spacing between category groups for better separation
        levelsChart.setBarGap(2); // Small gap between bars (though single series)
        levelsChart.setLegendVisible(false); // Single series, no need for legend
        levelsChart.setAnimated(true); // Optional: smooth animation on data changes

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        levelsChart.getData().add(series);
        bomModel.levelsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillLevelChart(series, xAxis); // Pass xAxis to the method
            }
        });
        return levelsChart;
    }

    private void fillLevelChart(XYChart.Series<String, Number> series, CategoryAxis xAxis) {
        series.getData().clear();
        Integer[] levels = bomModel.levelsProperty().get(); // Assuming this returns Integer[] or similar

        // Collect categories for explicit setting on the axis
        List<String> categories = new ArrayList<>();
        int displayLevel = 1; // Start labeling from Level 1 for better UX
        for (int level : levels) {
            if (level != 0) {
                String cat = "" + displayLevel++;
                series.getData().add(new XYChart.Data<>(cat, level));
                categories.add(cat);
            }
        }
        // Explicitly set categories on the axis to fix label stacking/overlap
        xAxis.setCategories(FXCollections.observableArrayList(categories));
    }
}
