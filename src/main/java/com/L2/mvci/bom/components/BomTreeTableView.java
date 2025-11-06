package com.L2.mvci.bom.components;


import com.L2.dto.bom.ComponentDTO;
import com.L2.interfaces.Component;
import com.L2.mvci.bom.BomModel;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class BomTreeTableView implements Component {
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

        // Columns
        TreeTableColumn<ComponentDTO, String> colItem = new TreeTableColumn<>("Item");
        colItem.setCellValueFactory(p -> p.getValue().getValue().itemProperty());
        colItem.setPrefWidth(120);

        TreeTableColumn<ComponentDTO, Number> colItemId = new TreeTableColumn<>("Item ID");
        colItemId.setCellValueFactory(p -> p.getValue().getValue().itemIdProperty());
        colItemId.setPrefWidth(80);

        TreeTableColumn<ComponentDTO, Number> colLevel = new TreeTableColumn<>("Level");
        colLevel.setCellValueFactory(p -> p.getValue().getValue().levelProperty());
        colLevel.setPrefWidth(60);

        TreeTableColumn<ComponentDTO, String> colDesc = new TreeTableColumn<>("Description");
        colDesc.setCellValueFactory(p -> p.getValue().getValue().descriptionProperty());
        colDesc.setPrefWidth(200);

        TreeTableColumn<ComponentDTO, String> colRev = new TreeTableColumn<>("Rev");
        colRev.setCellValueFactory(p -> p.getValue().getValue().revisionProperty());
        colRev.setPrefWidth(60);

        TreeTableColumn<ComponentDTO, String> colUom = new TreeTableColumn<>("UOM");
        colUom.setCellValueFactory(p -> p.getValue().getValue().uomProperty());
        colUom.setPrefWidth(60);

        TreeTableColumn<ComponentDTO, Number> colQty = new TreeTableColumn<>("Quantity");
        colQty.setCellValueFactory(p -> p.getValue().getValue().quantityProperty());
        colQty.setPrefWidth(80);
        colQty.setStyle("-fx-alignment: CENTER-RIGHT;");

        TreeTableColumn<ComponentDTO, String> colType = new TreeTableColumn<>("Type");
        colType.setCellValueFactory(p -> p.getValue().getValue().itemTypeProperty());
        colType.setPrefWidth(70);

        TreeTableColumn<ComponentDTO, String> colRef = new TreeTableColumn<>("Ref Des");
        colRef.setCellValueFactory(p -> p.getValue().getValue().refDesProperty());
        colRef.setPrefWidth(150);

        bomModel.getTreeTable().getColumns().addAll(Arrays.asList(
                colItem, colItemId, colLevel, colDesc, colRev,
                colUom, colQty, colType, colRef)
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
                String color;
                String fontWeight = level == 1 ? "bold" : "normal";

                color = switch (level) {
                    case 1 -> "#1976D2";
                    case 2 -> "#388E3C";
                    case 3 -> "#F57C00";
                    case 4 -> "#7B1FA2";
                    default -> "#000000"; // black for level 5+
                };

                setText(item);
                setStyle("-fx-font-weight: " + fontWeight + "; -fx-text-fill: " + color + ";");
            }
        });

        bomModel.getTreeTable().setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        return bomModel.getTreeTable();
    }


}
