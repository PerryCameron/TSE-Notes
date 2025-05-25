package com.L2.static_tools;

import com.L2.dto.global_spares.RangesFx;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Filter {
    public static void copyRange(RangesFx fromRange, TextField range, TextField rangeType, TextArea family) {
        range.setText(fromRange.getRange());
        rangeType.setText(fromRange.getRangeType());
        family.setText(family.getText());
    }
}
