package com.L2.mvci.note.mvci.partorderbox.mvci.parteditor;


import com.L2.mvci.note.NoteView;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class PartEditorView implements Builder<Region> {
    private static final Logger logger = LoggerFactory.getLogger(PartEditorView.class);

    private Consumer<PartEditorMessage> action;
    private NoteView noteView;
    private PartEditorModel partEditorModel;

    public PartEditorView(NoteView noteView, PartEditorModel partEditorModel, Consumer<PartEditorMessage> message) {
        this.noteView = noteView;
        this.partEditorModel = partEditorModel;
        this.action = message;
    }

    @Override
    public Region build() {
        return null;
    }
}
