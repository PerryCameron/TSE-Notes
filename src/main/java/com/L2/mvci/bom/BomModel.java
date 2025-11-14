package com.L2.mvci.bom;

import com.L2.dto.bom.ComponentDTO;
import com.L2.dto.bom.ComponentXML;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.StackPane;

public class BomModel {
    StringProperty searchComponent = new SimpleStringProperty();
    StringProperty searchInBom = new SimpleStringProperty();
    TreeTableView<ComponentDTO> treeTable;
    TreeItem<ComponentDTO> root;
    ComponentDTO selectedComponent = new ComponentDTO();
    ObjectProperty<Integer[]> levels = new SimpleObjectProperty<>();
    ObjectProperty<StackPane> stackPane = new SimpleObjectProperty<>();
    ObservableList<ComponentDTO> searchedComponents;
    TableView<ComponentDTO> componentTable;

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

    public void copyToSelectedComponent(ComponentDTO newComponent) {
        selectedComponent.itemProperty().set(newComponent.itemProperty().get());
        selectedComponent.itemIdProperty().set(newComponent.itemIdProperty().get());
        selectedComponent.levelProperty().set(newComponent.levelProperty().get());
        selectedComponent.descriptionProperty().set(newComponent.descriptionProperty().get());
        selectedComponent.revisionProperty().set(newComponent.revisionProperty().get());
        selectedComponent.uomProperty().set(newComponent.uomProperty().get());
        selectedComponent.quantityProperty().set(newComponent.quantityProperty().get());
        selectedComponent.itemTypeProperty().set(newComponent.itemTypeProperty().get());
        selectedComponent.refDesProperty().set(newComponent.refDesProperty().get());
    }

    public Integer[] getLevels() {
        return levels.get();
    }

    public ObjectProperty<Integer[]> levelsProperty() {
        return levels;
    }

    public void setLevels(Integer[] levels) {
        this.levels.set(levels);
    }

    public String getSearchInBom() {
        return searchInBom.get();
    }

    public StringProperty searchInBomProperty() {
        return searchInBom;
    }

    public void setSearchInBom(String searchInBom) {
        this.searchInBom.set(searchInBom);
    }

    public StackPane getStackPane() {
        return stackPane.get();
    }

    public ObjectProperty<StackPane> stackPaneProperty() {
        return stackPane;
    }

    public void setStackPane(StackPane stackPane) {
        this.stackPane.set(stackPane);
    }

    public ObservableList<ComponentDTO> getSearchedComponents() {
        return searchedComponents;
    }

    public void setSearchedComponents(ObservableList<ComponentDTO> searchedComponents) {
        this.searchedComponents = searchedComponents;
    }

    public TableView<ComponentDTO> getComponentTable() {
        return componentTable;
    }

    public void setComponentTable(TableView<ComponentDTO> componentTable) {
        this.componentTable = componentTable;
    }
}
