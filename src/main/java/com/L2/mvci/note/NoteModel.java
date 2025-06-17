package com.L2.mvci.note;

import com.L2.dto.*;
import com.L2.dto.global_spares.RangesFx;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci.main.MainController;
import com.L2.mvci.note.mvci.partorderbox.PartOrderBoxController;
import com.nikialeksey.hunspell.Hunspell;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.Subscription;

import java.util.Collection;

public class NoteModel {

    private final ObjectProperty<NoteFx> boundNote = new SimpleObjectProperty<>();
    private final ObjectProperty<PartFx> selectedPart = new SimpleObjectProperty<>();
    private final ObjectProperty<RangesFx> selectedRange = new SimpleObjectProperty<>();
    private final ObjectProperty<PartOrderFx> selectedPartOrder = new SimpleObjectProperty<>();
    private final ObjectProperty<EntitlementFx> currentEntitlement = new SimpleObjectProperty<>();
    private final ObjectProperty<Hunspell> hunspell = new SimpleObjectProperty<>();
    private final ObjectProperty<Subscription> spellCheckSubscription = new SimpleObjectProperty<>();
    private final ObjectProperty<CodeArea> issueArea = new SimpleObjectProperty<>();
    private final ObjectProperty<CodeArea> finishArea = new SimpleObjectProperty<>();
    private final ObjectProperty<CodeArea> subjectArea = new SimpleObjectProperty<>();
    private final ObjectProperty<UserDTO> user = new SimpleObjectProperty<>();
    private final ObjectProperty<ScrollPane> noteScrollPane = new SimpleObjectProperty<>();
    private final ObjectProperty<ContextMenu> contextMenu = new SimpleObjectProperty<>();
    private final ObjectProperty<StyleSpans<Collection<String>>> subjectSpansProperty = new SimpleObjectProperty<>(null);
    private final ObjectProperty<StyleSpans<Collection<String>>> issueSpansProperty = new SimpleObjectProperty<>(null);
    private final ObjectProperty<StyleSpans<Collection<String>>> finishSpansProperty = new SimpleObjectProperty<>(null);
    private final ObjectProperty<Label> resultsLabel = new SimpleObjectProperty<>(new Label("Results"));
    private PartOrderBoxController partOrderBoxController;
    // lists
    private ObservableList<NoteFx> notes = FXCollections.observableArrayList();
    private final ObservableList<SparesDTO> searchedPart = FXCollections.observableArrayList();
    private final ObservableList<RangesFx> ranges = FXCollections.observableArrayList();
    private ObservableList<EntitlementFx> entitlements = FXCollections.observableArrayList();
    // integer properties
    private final IntegerProperty pageSize = new SimpleIntegerProperty(50);
    private final IntegerProperty offset = new SimpleIntegerProperty(0);
    private final IntegerProperty numberInRange = new SimpleIntegerProperty();
    // string properties
    private final StringProperty searchWord = new SimpleStringProperty();
    private final StringProperty statusLabel = new SimpleStringProperty();
    private final StringProperty newWord = new SimpleStringProperty();
    private final BooleanProperty clearCalled  = new SimpleBooleanProperty(false);
    private final BooleanProperty refreshBoundNote = new SimpleBooleanProperty(false);
    private final BooleanProperty refreshEntitlements = new SimpleBooleanProperty(false);
    private final BooleanProperty openNoteTab = new SimpleBooleanProperty(false);
    private MainController mainController = null;



    public ObjectProperty<PartOrderFx> selectedPartOrderProperty() {
        return selectedPartOrder;
    }
    public ObjectProperty<PartFx> selectedPartProperty() {
        return selectedPart;
    }
    public IntegerProperty pageSizeProperty() {
        return pageSize;
    }
    public IntegerProperty offsetProperty() {
        return offset;
    }
    public BooleanProperty refreshBoundNoteProperty() {
        return refreshBoundNote;
    }
    public BooleanProperty clearCalledProperty() {
        return clearCalled;
    }
    public ObservableList<SparesDTO> getSearchedParts() { return searchedPart;}
    public ObservableList<NoteFx> getNotes() {
        return notes;
    }
    public void setNotes(ObservableList<NoteFx> notes) { this.notes = notes; }
    public ObjectProperty<UserDTO> userProperty() {
        return user;
    }
    public StringProperty statusLabelProperty() {
        return statusLabel;
    }
    public ObservableList<EntitlementFx> getEntitlements() {
        return entitlements;
    }
    public void setEntitlements(ObservableList<EntitlementFx> entitlements) {
        this.entitlements = entitlements;
    }
    public ObjectProperty<NoteFx> boundNoteProperty() {
        return boundNote;
    }
    public BooleanProperty refreshEntitlementsProperty() {
        return refreshEntitlements;
    }
    public ObjectProperty<EntitlementFx> currentEntitlementProperty() {
        return currentEntitlement;
    }
    public ObjectProperty<Hunspell> hunspellProperty() {
        return hunspell;
    }
    public ObjectProperty<Subscription> spellCheckSubscriptionProperty() {
        return spellCheckSubscription;
    }
    public ObjectProperty<CodeArea> issueAreaProperty() { return issueArea; }
    public ObjectProperty<CodeArea> finishAreaProperty() { return finishArea; }
    public ObjectProperty<CodeArea> subjectAreaProperty() { return subjectArea; }
    public ObjectProperty<ContextMenu> contextMenuProperty() { return contextMenu; }
    public ObjectProperty<StyleSpans<Collection<String>>> subjectSpansProperty() {
        return subjectSpansProperty;
    }
    public ObjectProperty<StyleSpans<Collection<String>>> issueSpansProperty() {
        return issueSpansProperty;
    }
    public ObjectProperty<StyleSpans<Collection<String>>> finishSpansProperty() {
        return finishSpansProperty;
    }
    public StringProperty newWordProperty() {
        return newWord;
    }
    public ObjectProperty<ScrollPane> noteScrollPaneProperty() {
        return noteScrollPane;
    }
    public StringProperty searchWordProperty() { return searchWord; }
    public ObservableList<RangesFx> getRanges() { return ranges; }
    public ObjectProperty<RangesFx> selectedRangeProperty() { return selectedRange;}
    public ObjectProperty<Label> resultsLabelProperty() { return resultsLabel; }
    public IntegerProperty numberInRangeProperty() { return numberInRange; }
    public void refreshEntitlements() {
        refreshEntitlements.set(true);
        refreshEntitlements.set(false);
    }
    public void openNoteTab() {
        this.openNoteTab.set(true);
        this.openNoteTab.set(false);
    }
    public void clearBoundNoteFields() {
        boundNote.get().getPartOrders().clear();
        selectedPartOrderProperty().set(null);
        boundNote.get().clear();
        refreshBoundNote();
    }
    public void refreshBoundNote() {  // to refresh fields without bindings
        refreshBoundNote.set(true);
        refreshBoundNote.set(false);
    }
    public PartOrderBoxController getPartOrderBoxController() {
        return partOrderBoxController;
    }
    public void setPartOrderBoxController(PartOrderBoxController partOrderBoxController) {
        this.partOrderBoxController = partOrderBoxController;
    }
    public MainController getMainController() {
        return mainController;
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
