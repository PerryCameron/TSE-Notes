package com.L2.mvci.bom;

import com.L2.mvci.note.NoteInteractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BomInteractor {
    private static final Logger logger = LoggerFactory.getLogger(BomInteractor.class);
    private final BomModel bomModel;

    public BomInteractor(BomModel bomModel) {
        this.bomModel = bomModel;
    }
}
