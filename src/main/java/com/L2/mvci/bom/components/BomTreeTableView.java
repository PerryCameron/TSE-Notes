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
                bomModel.setSelectedComponent(newSelection.getValue());
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

        TreeTableColumn<ComponentDTO, String> colDesc = new TreeTableColumn<>("Description");
        colDesc.setCellValueFactory(p -> p.getValue().getValue().descriptionProperty());
        colDesc.prefWidthProperty().bind(bomModel.getTreeTable().widthProperty().multiply(0.40));

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

//        TreeTableColumn<ComponentDTO, String> colType = new TreeTableColumn<>("Type");
//        colType.setCellValueFactory(p -> p.getValue().getValue().itemTypeProperty());
//        colType.prefWidthProperty().bind(bomModel.getTreeTable().widthProperty().multiply(0.10));

//        TreeTableColumn<ComponentDTO, String> colRef = new TreeTableColumn<>("Ref Des");
//        colRef.setCellValueFactory(p -> p.getValue().getValue().refDesProperty());
//        colRef.setPrefWidth(150);

        bomModel.getTreeTable().getColumns().addAll(Arrays.asList(
                colItem, colLevel, colDesc,
                colUom, colQty)
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
                String styleClass;
                String fontWeight = level == 1 ? "bold" : "normal";

                styleClass = switch (level) {
                    case 1 -> "-fx-text-fill: #1976D2;";
                    case 2 -> "-fx-text-fill: #388E3C;";
                    case 3 -> "-fx-text-fill: #F57C00;";
                    case 4 -> "-fx-text-fill: #7B1FA2;";
                    case 5 -> "-fx-text-fill: #efcc17;";
                    case 6 -> "-fx-text-fill: #5c1200;";
                    case 7 -> "-fx-text-fill: #44e3e3;";
                    case 8 -> "-fx-text-fill: #023bf8;";
                    case 9 -> "-fx-text-fill: #e34469;";
                    default -> "#000000"; // black for level 5+
                };

                setText(item);
                getStyleClass().add(styleClass);
                setStyle("-fx-font-weight: " + fontWeight + ";" + styleClass);
            }
        });

        bomModel.getTreeTable().setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        return bomModel.getTreeTable();
    }
}
