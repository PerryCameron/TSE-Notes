package com.L2.mvci.bom;

import com.L2.dto.bom.ComponentDTO;
import com.L2.dto.bom.ComponentXML;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

public class BomModel {
    StringProperty searchComponent = new SimpleStringProperty();
    TreeTableView<ComponentDTO> treeTable;
    TreeItem<ComponentDTO> root;
    ComponentDTO selectedComponent = new ComponentDTO();
    ObjectProperty<ComponentDTO> selectedComponentProperty = new SimpleObjectProperty<>();
    Integer[] levels = new Integer[15];




    public StringProperty searchComponentProperty() {
        return searchComponent;
    }

    public TreeTableView<ComponentDTO> getTreeTable() {
        return treeTable;
    }

    public void setTreeTable(TreeTableView<ComponentDTO> treeTable) {
        this.treeTable = treeTable;
    }

    public void setRoot(TreeItem<ComponentDTO> root) {
        this.root = root;
    }

    public TreeItem<ComponentDTO> getRoot() {
        return root;
    }

    public ComponentDTO getSelectedComponent() {
        return selectedComponent;
    }

    public void setSelectedComponent(ComponentDTO selectedComponent) {
        this.selectedComponent = selectedComponent;
    }

    public Integer[] getLevels() {
        return levels;
    }

    public void setLevels(Integer[] levels) {
        this.levels = levels;
    }

    public void resetLevels() {
        for (int i = 0; i < levels.length; i++) {
            levels[i] = 0;
        }
    }
}
