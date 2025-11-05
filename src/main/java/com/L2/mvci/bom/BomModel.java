package com.L2.mvci.bom;

import com.L2.dto.bom.ComponentDTO;
import com.L2.dto.bom.ComponentXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

public class BomModel {
    StringProperty searchComponent = new SimpleStringProperty();
    TreeTableView<ComponentDTO> treeTable;
//    ComponentXML xmlRoot = null;
    TreeItem<ComponentDTO> root;


    public StringProperty searchComponentProperty() {
        return searchComponent;
    }

//    public void setXmlRoot(ComponentXML root) {
//        this.xmlRoot = root;
//    }
//
//    public ComponentXML getXmlRoot() {
//        return xmlRoot;
//    }

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
}
