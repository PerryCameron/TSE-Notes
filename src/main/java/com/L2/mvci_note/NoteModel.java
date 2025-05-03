package com.L2.mvci_note;

import com.L2.dto.*;
import com.L2.dto.global_spares.ProductToSparesDTO;
import com.nikialeksey.hunspell.Hunspell;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.Subscription;

import java.util.Collection;

public class NoteModel {
    private ObservableList<NoteDTO> notes = FXCollections.observableArrayList();
    // this is a NoteDTO that never moves, it just copies and pastes its values to the real notes
    private final ObjectProperty<NoteDTO> boundNote = new SimpleObjectProperty<>();
    // you can only select one part at a time, so simpler to keep here.
    private final ObjectProperty<PartDTO> selectedPart = new SimpleObjectProperty<>();
    private final ObservableList<ProductToSparesDTO> searchedPart = FXCollections.observableArrayList();;
    // you can only have one part order focused at time, so simpler to keep here as well
    private final ObjectProperty<PartOrderDTO> selectedPartOrder = new SimpleObjectProperty<>();
    private ObservableList<EntitlementDTO> entitlements = FXCollections.observableArrayList();
    private final ObjectProperty<EntitlementDTO> currentEntitlement = new SimpleObjectProperty<>();
    private final ObjectProperty<VBox> PlanDetailsBox = new SimpleObjectProperty<>();
    // allow this many records to be displayed
    private final IntegerProperty pageSize = new SimpleIntegerProperty(50);
    // skip the first N records
    private final IntegerProperty offset = new SimpleIntegerProperty(0);
    // shouldn't this be under main controller???????
    private final StringProperty statusLabel = new SimpleStringProperty();
    private final ObjectProperty<UserDTO> user = new SimpleObjectProperty<>();
    private final BooleanProperty clearCalled  = new SimpleBooleanProperty(false);
    private final BooleanProperty refreshBoundNote = new SimpleBooleanProperty(false);
    private final BooleanProperty refreshEntitlements = new SimpleBooleanProperty(false);
    private final BooleanProperty openNoteTab = new SimpleBooleanProperty(false);
    private final ObjectProperty<Hunspell> hunspell = new SimpleObjectProperty<>();
    private final ObjectProperty<Subscription> spellCheckSubscription = new SimpleObjectProperty<>();
    private final ObjectProperty<CodeArea> issueArea = new SimpleObjectProperty<>();
    private final ObjectProperty<CodeArea> finishArea = new SimpleObjectProperty<>();
    private final ObjectProperty<CodeArea> subjectArea = new SimpleObjectProperty<>();
    private final StringProperty newWord = new SimpleStringProperty();
    private final ObjectProperty<ScrollPane> noteScrollPane = new SimpleObjectProperty<>();
    private final ObjectProperty<ContextMenu> contextMenu = new SimpleObjectProperty<>();
    private final ObjectProperty<StyleSpans<Collection<String>>> subjectSpansProperty =
            new SimpleObjectProperty<>(null);
    private final ObjectProperty<StyleSpans<Collection<String>>> issueSpansProperty =
            new SimpleObjectProperty<>(null);
    private final ObjectProperty<StyleSpans<Collection<String>>> finishSpansProperty =
            new SimpleObjectProperty<>(null);
    private final StringProperty searchWord = new SimpleStringProperty();





    public IntegerProperty pageSizeProperty() {
        return pageSize;
    }
    public IntegerProperty offsetProperty() {
        return offset;
    }
    public ObjectProperty<PartOrderDTO> selectedPartOrderProperty() {
        return selectedPartOrder;
    }
    public ObjectProperty<PartDTO> selectedPartProperty() {
        return selectedPart;
    }
    public ObservableList<ProductToSparesDTO> getSearchedParts() { return searchedPart;}
    public BooleanProperty refreshBoundNoteProperty() {
        return refreshBoundNote;
    }
    public BooleanProperty clearCalledProperty() {
        return clearCalled;
    }
    public ObservableList<NoteDTO> getNotes() {
        return notes;
    }
    public void setNotes(ObservableList<NoteDTO> notes) { this.notes = notes; }
    public ObjectProperty<UserDTO> userProperty() {
        return user;
    }
    public StringProperty statusLabelProperty() {
        return statusLabel;
    }
    public ObservableList<EntitlementDTO> getEntitlements() {
        return entitlements;
    }
    public void setEntitlements(ObservableList<EntitlementDTO> entitlements) {
        this.entitlements = entitlements;
    }
    public ObjectProperty<NoteDTO> boundNoteProperty() {
        return boundNote;
    }
    public BooleanProperty refreshEntitlementsProperty() {
        return refreshEntitlements;
    }
    public ObjectProperty<EntitlementDTO> currentEntitlementProperty() {
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
}
