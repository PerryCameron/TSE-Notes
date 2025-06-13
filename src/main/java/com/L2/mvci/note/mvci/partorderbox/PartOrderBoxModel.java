package com.L2.mvci.note.mvci.partorderbox;

import com.L2.dto.PartFx;
import com.L2.dto.PartOrderFx;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class PartOrderBoxModel {
    private final NoteModel noteModel;
    private final NoteView noteView;
    private VBox root;
    private final Map<PartOrderFx, VBox> partOrderMap = new HashMap<>();
    private TableView<PartFx> tableView;
    private final BooleanProperty flash = new SimpleBooleanProperty(false);
    private final BooleanProperty RefreshFields = new SimpleBooleanProperty(false);
    private final ObjectProperty<PartOrderBoxMessage> message = new SimpleObjectProperty<>(PartOrderBoxMessage.NONE);
    private PartFinderController partController;
    // this is used for the Part Finder MVCI
    private SparesDTO spare;

    public PartOrderBoxModel(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

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
    public PartFinderController getPartController() {
        return partController;
    }
    public void setPartController(PartFinderController partController) {
        this.partController = partController;
    }
    public NoteModel getNoteModel() {
        return noteModel;
    }
    public PartOrderBoxMessage getMessage() {
        return message.get();
    }
    public ObjectProperty<PartOrderBoxMessage> messageProperty() {
        return message;
    }
    public void setMessage(PartOrderBoxMessage message) {
        this.message.set(message);
    }
    public SparesDTO getSpare() {
        return spare;
    }
    public void setSpare(SparesDTO spare) {
        this.spare = spare;
    }
    public NoteView getNoteView() {
        return noteView;
    }
}
