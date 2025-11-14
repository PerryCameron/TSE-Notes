package com.L2.mvci.bom.components;

import com.L2.dto.bom.ComponentDTO;
import com.L2.interfaces.Component;
import com.L2.mvci.bom.BomModel;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class BomTreeTableView implements Component<Node> {
    private final BomModel bomModel;

    @Override
    public void flash() {

    }

    @Override
    public void refreshFields() {

    }

    public BomTreeTableView(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    @Override
    public Node build() {
        bomModel.setTreeTable(new TreeTableView<>());
        VBox.setVgrow(bomModel.getTreeTable(), Priority.ALWAYS);

        TreeTableView.TreeTableViewSelectionModel<ComponentDTO> selectionModel = bomModel.getTreeTable().getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                bomModel.copyToSelectedComponent(newSelection.getValue());
                System.out.println("set: " +bomModel.getSelectedComponent());
            }
        });

        // Columns
        TreeTableColumn<ComponentDTO, String> colItem = new TreeTableColumn<>("Item");
        colItem.setCellValueFactory(p -> p.getValue().getValue().itemProperty());
        colItem.prefWidthProperty().bind(bomModel.getTreeTable().widthProperty().multiply(0.30));


//        TreeTableColumn<ComponentDTO, Number> colItemId = new TreeTableColumn<>("Item ID");
//        colItemId.setCellValueFactory(p -> p.getValue().getValue().itemIdProperty());
//        colItemId.setPrefWidth(80);

        TreeTableColumn<ComponentDTO, Number> colLevel = new TreeTableColumn<>("Level");
        colLevel.setCellValueFactory(p -> p.getValue().getValue().levelProperty());
        colLevel.prefWidthProperty().bind(bomModel.getTreeTable().widthProperty().multiply(0.10));
        // how do I align cells to center content?


        TreeTableColumn<ComponentDTO, String> colDesc = new TreeTableColumn<>("Description");
        colDesc.setCellValueFactory(p -> p.getValue().getValue().descriptionProperty());
        colDesc.prefWidthProperty().bind(bomModel.getTreeTable().widthProperty().multiply(0.30));

//        TreeTableColumn<ComponentDTO, String> colRev = new TreeTableColumn<>("Rev");
//        colRev.setCellValueFactory(p -> p.getValue().getValue().revisionProperty());
//        colRev.setPrefWidth(60);

        TreeTableColumn<ComponentDTO, String> colUom = new TreeTableColumn<>("UOM");
        colUom.setCellValueFactory(p -> p.getValue().getValue().uomProperty());
        colUom.prefWidthProperty().bind(bomModel.getTreeTable().widthProperty().multiply(0.10));

        TreeTableColumn<ComponentDTO, Number> colQty = new TreeTableColumn<>("Quantity");
        colQty.setCellValueFactory(p -> p.getValue().getValue().quantityProperty());
        colQty.prefWidthProperty().bind(bomModel.getTreeTable().widthProperty().multiply(0.10));
        colQty.setStyle("-fx-alignment: CENTER-RIGHT;");

        TreeTableColumn<ComponentDTO, String> colType = new TreeTableColumn<>("Type");
        colType.setCellValueFactory(p -> p.getValue().getValue().itemTypeProperty());
        colType.prefWidthProperty().bind(bomModel.getTreeTable().widthProperty().multiply(0.10));

//        TreeTableColumn<ComponentDTO, String> colRef = new TreeTableColumn<>("Ref Des");
//        colRef.setCellValueFactory(p -> p.getValue().getValue().refDesProperty());
//        colRef.setPrefWidth(150);

        bomModel.getTreeTable().getColumns().addAll(Arrays.asList(
                colItem, colLevel, colDesc,
                colUom, colQty, colType)
        );

        colItem.setCellFactory(tc -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                // Get the ComponentDTO from the row
                TreeTableRow<ComponentDTO> row = getTableRow();
                if (row == null || row.getItem() == null) {
                    setText(item);
                    setStyle("");
                    return;
                }

                int level = row.getItem().levelProperty().get();
                String fontWeight = level == 1 ? "bold" : "normal";

                String color = switch (level) {
                    case 1 -> "-fx-component-color1";
                    case 2 -> "-fx-component-color2";
                    case 3 -> "-fx-component-color3";
                    case 4 -> "-fx-component-color4";
                    case 5 -> "-fx-component-color5";
                    case 6 -> "-fx-component-color6";
                    case 7 -> "-fx-component-color7";
                    case 8 -> "-fx-component-color8";
                    case 9 -> "-fx-component-color9";
                    default -> "black"; // fallback
                };

                setText(item);
                setStyle("-fx-font-weight: " + fontWeight + "; -fx-text-fill: " + color + ";");
            }
        });

        bomModel.getTreeTable().setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        return bomModel.getTreeTable();
    }
}
