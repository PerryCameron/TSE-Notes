package com.L2.mvci.bom.components;

import com.L2.mvci.bom.BomModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.util.Builder;

import java.util.ArrayList;
import java.util.List;

public class LevelPieChart implements Builder<PieChart> {

    private final BomModel bomModel;

    public LevelPieChart(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    @Override
    public PieChart build() {
        PieChart pieChart = new PieChart();
        pieChart.setPrefHeight(200);
        pieChart.setPrefWidth(400);
        pieChart.setLegendVisible(true); // Shows level labels with colors; set false if unwanted
        pieChart.setLegendSide(Side.LEFT);
        pieChart.setLabelsVisible(false); // Shows percentage on slices
        pieChart.setLabelLineLength(10); // Adjust for readability
        pieChart.setAnimated(true);

        bomModel.levelsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillPieChart(pieChart);
            }
        });
        return pieChart;
    }

    private void fillPieChart(PieChart pieChart) {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        Integer[] levels = bomModel.levelsProperty().get();
        double total = 0;
        List<Integer> nonZeroLevels = new ArrayList<>();
        for (int level : levels) {
            if (level != 0) {
                nonZeroLevels.add(level);
                total += level;
            }
        }

        int displayLevel = 1;
        for (int i = 0; i < nonZeroLevels.size(); i++) {
            int count = nonZeroLevels.get(i);
            double percentage = (count / total) * 100;
            PieChart.Data data = new PieChart.Data("Level " + displayLevel + " (" + String.format("%.1f%%", percentage) + ")", count);
            pieData.add(data);

            // Add tooltip with absolute count
            final int currentLevel = displayLevel;
            data.nodeProperty().addListener((obs, old, newNode) -> {
                if (newNode != null) {
                    Tooltip.install(newNode, new Tooltip("Level " + currentLevel + ": " + count + " parts"));
                }
            });
            displayLevel++;
        }

        pieChart.setData(pieData);
    }
}