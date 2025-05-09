package com.L2.widgetFx;

import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci_note.NoteModel;
import javafx.scene.control.TableView;

public class SparesTableViewFx {
    public static TableView<SparesDTO> createTableView(NoteModel noteModel) {
        TableView<SparesDTO> tableView = TableViewFx.of(SparesDTO.class);
        tableView.setItems(noteModel.getSearchedParts());



        return tableView;
    }

}
