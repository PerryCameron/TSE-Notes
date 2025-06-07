package com.L2.mvci.note.mvci.partorderbox.mvci.parts;

import com.L2.dto.PartFx;
import com.L2.dto.UpdatedByDTO;
import com.L2.dto.global_spares.ProductFamilyDTO;
import com.L2.dto.global_spares.RangesFx;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class PartModel {
    private TableView<PartFx> partsTableView;
    private NoteModel noteModel;
    private final SimpleBooleanProperty searchedBefore = new SimpleBooleanProperty(false);
    private NoteView noteView;
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    private final DialogPane dialogPane = new DialogPane();
    private Label rangeNumberLabel;
    private Label messageLabel;
    private TableView<SparesDTO> sparesTableView;
    private TreeView<Object> treeView;
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
    private ImageView imageView;
    private final BooleanProperty alertExtended = new SimpleBooleanProperty(false);
    private List<UpdatedByDTO> updatedByDTOs = new ArrayList<>();
    private List<ProductFamilyDTO> productFamilies = new ArrayList<>();
    private final ObjectProperty<SparesDTO> selectedSpare = new SimpleObjectProperty<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StringProperty comboBoxSelectedRange = new SimpleStringProperty();
    // from noteModel
    private final ObjectProperty<RangesFx> selectedRange;
    private final ObjectProperty<TextArea> partNote = new SimpleObjectProperty<>();
    private final ObjectProperty<TextArea> partKeyWords = new SimpleObjectProperty<>();
    // from noteModel
    private final ObservableList<RangesFx> ranges;
    private StackPane stackPane;
    private final BooleanProperty updatedNotes = new SimpleBooleanProperty(false);
    private final BooleanProperty updatedRanges = new SimpleBooleanProperty(false);
    private final BooleanProperty updatedKeywords = new SimpleBooleanProperty(false);
    private PartController partController;
    private Byte[] imageBytes;
//    private ObjectProperty<Image> image = new SimpleObjectProperty<>();

    public PartModel(NoteModel noteModel, TableView<PartFx> tableView) {
        this.ranges = noteModel.getRanges();
        this.partsTableView = tableView;
        this.selectedRange = noteModel.selectedRangeProperty();
        this.noteModel = noteModel;
    }

    public void setSelectedRange(RangesFx range) {
        selectedRange.setValue(range);
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
        return 800;
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
    public TreeView<Object> getTreeView() {
        return treeView;
    }
    public void setTreeView(TreeView<Object> treeView) {
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
    public List<ProductFamilyDTO> getProductFamilies() {
        return productFamilies;
    }
    public void setProductFamilies(List<ProductFamilyDTO> productFamilies) {
        this.productFamilies = new ArrayList<>(productFamilies); // Ensure mutable copy
    }
    public ObjectProperty<SparesDTO> selectedSpareProperty() {
        return selectedSpare;
    }
    public ObjectMapper getObjectMapper() { return objectMapper; }
    public StringProperty comboBoxSelectedRangeProperty() { return comboBoxSelectedRange; }
    public ObservableList<RangesFx> getRanges() {
        return ranges;
    }
    public StackPane getStackPane() {
        return stackPane;
    }
    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }
    public BooleanProperty updatedNotesProperty() {
        return updatedNotes;
    }
    public BooleanProperty updatedRangeProperty() {
        return updatedRanges;
    }
    public BooleanProperty getUpdatedKeywordsProperty() {
        return updatedKeywords;
    }
    public ObjectProperty<TextArea> partNoteProperty() {
        return partNote;
    }
    public ObjectProperty<TextArea> partKeyWordsProperty() {
        return partKeyWords;
    }
    public Byte[] getImageBytes() {
        return imageBytes;
    }
    public void setImageBytes(Byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
    public ImageView getImageView() {
        return imageView;
    }
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public List<UpdatedByDTO> getUpdatedByDTOs() {
        return updatedByDTOs;
    }

    public void setUpdatedByDTOs(List<UpdatedByDTO> updatedByDTOs) {
        this.updatedByDTOs = updatedByDTOs;
    }
}
