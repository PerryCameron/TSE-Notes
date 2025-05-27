package com.L2.mvci.parts;

import com.L2.dto.PartFx;
import com.L2.dto.ProductFamilyFx;
import com.L2.dto.global_spares.RangesFx;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PartModel {
    private static final Logger logger = LoggerFactory.getLogger(PartModel.class);
    private TableView<PartFx> partsTableView;
    private NoteModel noteModel;
    private SimpleBooleanProperty searchedBefore = new SimpleBooleanProperty(false);
    private NoteView noteView;
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    private final DialogPane dialogPane = new DialogPane();
    private final double width = 800;
    private Label rangeNumberLabel;
    private Label messageLabel;
    private TableView<SparesDTO> sparesTableView;
    private TreeView<String> treeView;
    private HBox resultsLabelHbox;
    private HBox buttonBox;
    private HBox partContainerButtonBox;
    private HBox moreInfoHbox;
    private VBox cancelHbox;
    private VBox partContainer;
    private VBox content;
    private TextField searchField;
    private Button cancelButton;
    private Button searchButton;
    private final BooleanProperty alertExtended = new SimpleBooleanProperty(false);
    List<ProductFamilyFx> productFamilies = List.of();
    private ObjectProperty<SparesDTO> selectedSpare = new SimpleObjectProperty<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private StringProperty selectedRange = new SimpleStringProperty();
    private final ObservableList<RangesFx> ranges;



    public PartModel(NoteModel noteModel, TableView<PartFx> tableView) {
        this.ranges = noteModel.getRanges();
        this.partsTableView = tableView;
    }

    public TableView<PartFx> getPartsTableView() {
        return partsTableView;
    }

    public void setPartsTableView(TableView<PartFx> partsTableView) {
        this.partsTableView = partsTableView;
    }

    public NoteModel getNoteModel() {
        return noteModel;
    }

    public void setNoteModel(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public boolean isSearchedBefore() {
        return searchedBefore.get();
    }

    public SimpleBooleanProperty searchedBeforeProperty() {
        return searchedBefore;
    }

    public void setSearchedBefore(boolean searchedBefore) {
        this.searchedBefore.set(searchedBefore);
    }

    public NoteView getNoteView() {
        return noteView;
    }

    public void setNoteView(NoteView noteView) {
        this.noteView = noteView;
    }

    public Alert getAlert() {
        return alert;
    }

    public DialogPane getDialogPane() {
        return dialogPane;
    }

    public double getWidth() {
        return width;
    }

    public Label getRangeNumberLabel() {
        return rangeNumberLabel;
    }

    public void setRangeNumberLabel(Label rangeNumberLabel) {
        this.rangeNumberLabel = rangeNumberLabel;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public void setMessageLabel(Label messageLabel) {
        this.messageLabel = messageLabel;
    }

    public TableView<SparesDTO> getSparesTableView() {
        return sparesTableView;
    }

    public void setSparesTableView(TableView<SparesDTO> sparesTableView) {
        this.sparesTableView = sparesTableView;
    }

    public TreeView<String> getTreeView() {
        return treeView;
    }

    public void setTreeView(TreeView<String> treeView) {
        this.treeView = treeView;
    }

    public HBox getResultsLabelHbox() {
        return resultsLabelHbox;
    }

    public void setResultsLabelHbox(HBox resultsLabelHbox) {
        this.resultsLabelHbox = resultsLabelHbox;
    }

    public HBox getButtonBox() {
        return buttonBox;
    }

    public void setButtonBox(HBox buttonBox) {
        this.buttonBox = buttonBox;
    }

    public HBox getPartContainerButtonBox() {
        return partContainerButtonBox;
    }

    public void setPartContainerButtonBox(HBox partContainerButtonBox) {
        this.partContainerButtonBox = partContainerButtonBox;
    }

    public HBox getMoreInfoHbox() {
        return moreInfoHbox;
    }

    public void setMoreInfoHbox(HBox moreInfoHbox) {
        this.moreInfoHbox = moreInfoHbox;
    }

    public VBox getCancelHbox() {
        return cancelHbox;
    }

    public void setCancelHbox(VBox cancelHbox) {
        this.cancelHbox = cancelHbox;
    }

    public VBox getPartContainer() {
        return partContainer;
    }

    public void setPartContainer(VBox partContainer) {
        this.partContainer = partContainer;
    }

    public VBox getContent() {
        return content;
    }

    public void setContent(VBox content) {
        this.content = content;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public void setSearchField(TextField searchField) {
        this.searchField = searchField;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    public boolean isAlertExtended() {
        return alertExtended.get();
    }

    public BooleanProperty alertExtendedProperty() {
        return alertExtended;
    }

    public List<ProductFamilyFx> getProductFamilies() {
        return productFamilies;
    }

    public void setProductFamilies(List<ProductFamilyFx> productFamilies) {
        this.productFamilies = productFamilies;
    }

    public ObjectProperty<SparesDTO> selectedSpareProperty() {
        return selectedSpare;
    }

    public ObjectMapper getObjectMapper() { return objectMapper; }

    public StringProperty selectedRangeProperty() { return selectedRange; }

    public ObservableList<RangesFx> getRanges() {
        return ranges;
    }
}
