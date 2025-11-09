package com.L2.mvci.bom;

import com.L2.dto.bom.LevelDTO;
import com.L2.mvci.bom.components.BomTreeTableView;
import com.L2.widgetFx.TextFieldFx;
import com.L2.widgetFx.TitleBarFx;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BomView implements Builder<Region> {
    private final Consumer<BomMessage> action;
    private final BomModel bomModel;
    private static final Logger logger = LoggerFactory.getLogger(BomView.class);

    public BomView(BomModel bomModel, Consumer<BomMessage> action) {
        this.bomModel = bomModel;
        this.action = action;
    }

    @Override
    public Region build() {
        VBox root = new VBox(10);
        root.getStyleClass().add("base-vbox");
        root.setPadding(new Insets(10, 10, 0, 10));
        root.getChildren().addAll(navigation(), new BomTreeTableView(bomModel).build());
        return root;
    }

    //    private Node navigation() {
//        VBox vBox = new VBox();
//        HBox hBox = new HBox(10);
//        vBox.getStyleClass().add("decorative-hbox");
//        Button[] buttons = new Button[]{};
//        vBox.getChildren().addAll(TitleBarFx.of("Part/Model number", buttons), hBox);
//        hBox.setPadding(new Insets(5, 0, 5, 5));
//        hBox.setAlignment(Pos.CENTER_LEFT);
//
//        // Create BarChart for levels visualization
//        CategoryAxis xAxis = new CategoryAxis();
////        xAxis.setLabel("Levels");
//        NumberAxis yAxis = new NumberAxis();
//        yAxis.setLabel("Part Count");
//        BarChart<String, Number> levelsChart = new BarChart<>(xAxis, yAxis);
//        levelsChart.setTitle("Parts per Level");
//        levelsChart.setPrefHeight(200); // Adjust height to fit typical number of levels comfortably
//        levelsChart.setPrefWidth(400); // Compact width for the bar chart
//        levelsChart.setCategoryGap(15); // Adjust spacing between category groups for better separation
//        levelsChart.setBarGap(2); // Small gap between bars (though single series)
//        levelsChart.setLegendVisible(false); // Single series, no need for legend
//        levelsChart.setAnimated(true); // Optional: smooth animation on data changes
//
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        levelsChart.getData().add(series);
//
//        hBox.getChildren().addAll(searchBox(), searchButton(), levelsChart);
//
//        // Listener to populate the chart when levels change
//        bomModel.levelsProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                fillLevelChart(series);
//            }
//        });
//
//        return vBox;
//    }
//
//    // Updated method to populate the BarChart
//    private void fillLevelChart(XYChart.Series<String, Number> series) {
//        series.getData().clear();
//        Integer[] levels = bomModel.levelsProperty().get(); // Assuming this returns Integer[] or similar
//        int displayLevel = 1; // Start labeling from Level 1 for better UX
//        for (int level : levels) {
//            if (level != 0) {
//                series.getData().add(new XYChart.Data<>("" + displayLevel++, level)); // for some reason these are all stacked on top of each other instead of being under their respective columns
//            }
//        }
//    } // when this navigation menu gets built everything works fine except the numbers for level displayed along the x axis are all stacked on top of each other.
    private Node navigation() {
        VBox vBox = new VBox();
        HBox hBox = new HBox(10);
        vBox.getStyleClass().add("decorative-hbox");
        Button[] buttons = new Button[]{};
        vBox.getChildren().addAll(TitleBarFx.of("Part/Model number", buttons), hBox);
        hBox.setPadding(new Insets(5, 0, 5, 5));
        hBox.setAlignment(Pos.CENTER_LEFT);

        // Create BarChart for levels visualization
        CategoryAxis xAxis = new CategoryAxis();
//        xAxis.setAnimated(false); // Disable animation to fix dynamic update rendering issues on labels

        // xAxis.setLabel("Levels");
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

        hBox.getChildren().addAll(searchBox(), searchButton(), levelsChart);

        // Listener to populate the chart when levels change
        bomModel.levelsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillLevelChart(series, xAxis); // Pass xAxis to the method
            }
        });

        return vBox;
    }

    // Updated method to populate the BarChart
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

    private Node searchBox() {
        TextField textField = TextFieldFx.of(200, "Find BOM");
        textField.textProperty().bindBidirectional(bomModel.searchComponentProperty());
        return textField;
    }

    private Node searchButton() {
        Button button = new Button("Search");
        button.setOnAction(event -> {
            if (!bomModel.searchComponentProperty().get().isEmpty())
                action.accept(BomMessage.SEARCH);
        });
        return button;
    }
}
