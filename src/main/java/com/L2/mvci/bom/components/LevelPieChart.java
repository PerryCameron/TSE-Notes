package com.L2.mvci.bom.components;

import com.L2.mvci.bom.BomModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.util.ArrayList;
import java.util.List;

public class LevelPieChart implements Builder<PieChart> {

    private final BomModel bomModel;
    private final List<Color> colorPalette = List.of(
            Color.web("#1f77b4"), Color.web("#ff7f0e"), Color.web("#2ca02c"), Color.web("#d62728"),
            Color.web("#9467bd"), Color.web("#8c564b"), Color.web("#e377c2"), Color.web("#7f7f7f"),
            Color.web("#bcbd22"), Color.web("#17becf")
    ); // Optional: For custom colors if defaults aren't enough

    public LevelPieChart(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    @Override
    public PieChart build() {
        PieChart pieChart = new PieChart();
//        pieChart.setTitle("Parts Distribution per Level");
        pieChart.setPrefHeight(200);
        pieChart.setPrefWidth(400);
        pieChart.setLegendVisible(true); // Shows level labels with colors; set false if unwanted
        pieChart.setLabelsVisible(true); // Shows percentage on slices
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

            // Optional: Customize color if defaults don't suffice
            final int index = i;
            data.nodeProperty().addListener((obs, old, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-pie-color: " + colorPalette.get(index % colorPalette.size()).toString().replace("0x", "#") + ";");
                }
            });

            // Add tooltip with absolute count
            Tooltip.install(data.getNode(), new Tooltip("Level " + displayLevel + ": " + count + " parts"));
            displayLevel++;
        }

        pieChart.setData(pieData);
    }
}