package com.L2.widgetFx;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class ToolTipFx {
    public static Tooltip of(String tip) {
        Tooltip toolTip = new Tooltip(tip);
        toolTip.setShowDelay(Duration.seconds(.5));
        toolTip.setWrapText(true);
        toolTip.setShowDuration(Duration.seconds(2));
        toolTip.setHideDelay(Duration.seconds(.2));
        return toolTip;
    }
}
