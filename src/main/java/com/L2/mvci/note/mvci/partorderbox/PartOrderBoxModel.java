package com.L2.mvci.note.mvci.partorderbox;

import com.L2.dto.PartFx;
import com.L2.dto.PartOrderFx;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class PartOrderBoxModel {
    private VBox root;
    private final Map<PartOrderFx, VBox> partOrderMap = new HashMap<>();
    private TableView<PartFx> tableView;
    private final BooleanProperty flash = new SimpleBooleanProperty(false);
    private final BooleanProperty RefreshFields = new SimpleBooleanProperty(false);

    public VBox getRoot() {
        return root;
    }

    public void setRoot(VBox root) {
        this.root = root;
    }

    public Map<PartOrderFx, VBox> getPartOrderMap() {
        return partOrderMap;
    }

    public TableView<PartFx> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<PartFx> tableView) {
        this.tableView = tableView;
    }

    public BooleanProperty getFlash() {
        return flash;
    }

    public void flash() {
        flash.set(true);
        flash.set(false);
    }

    public BooleanProperty getRefreshFields() {
        return RefreshFields;
    }

    public void refreshFields() {
        RefreshFields.set(true);
        RefreshFields.set(false);
    }
}
