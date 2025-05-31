package com.L2.mvci.note.components;

import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.note.NoteView;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.TitleBarFx;
import com.L2.widgetFx.ToolTipFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class PartOrderHeader implements Builder<Region> {

    private final NoteView noteView;

    public PartOrderHeader(NoteView noteView) {
        this.noteView = noteView;
    }

    @Override
    public Region build() {
        VBox root = VBoxFx.of(true,5.0, new Insets(3, 5, 3, 5));
        root.getStyleClass().add("decorative-header-box");
        Button newButton = ButtonFx.utilityButton( () -> {
            noteView.getAction().accept(NoteMessage.INSERT_PART_ORDER);
            noteView.getPartOrderBoxController().refreshFields();
        },"New Part Order", "/images/create-16.png");
        newButton.setTooltip(ToolTipFx.of("Create New Part Order"));
        Button[] buttons = new Button[] { newButton };
        root.getChildren().addAll(TitleBarFx.of("Part Orders", buttons));
        return root;
    }
}
