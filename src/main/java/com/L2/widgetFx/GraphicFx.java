package com.L2.widgetFx;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class GraphicFx {

    public static RadialGradient greenCircle() {
        // Define the radial gradient: lighter green center to darker green edge
        return new RadialGradient(
                0, // focusAngle
                0.1, // focusDistance
                0.5, // centerX (relative to circle)
                0.5, // centerY (relative to circle)
                1.0, // radius (relative to circle)
                true, // proportional (coordinates are relative to circle size)
                CycleMethod.NO_CYCLE, // No repeating gradient
                new Stop(0.0, Color.LIGHTGREEN), // Center: Light green
                new Stop(1.0, Color.DARKGREEN) // Edge: Dark green
        );
    }

    public static RadialGradient redCircle() {
        return new RadialGradient(
                0, // focusAngle
                0.1, // focusDistance
                0.5, // centerX (relative to circle)
                0.5, // centerY (relative to circle)
                1.0, // radius (relative to circle)
                true, // proportional (coordinates are relative to circle size)
                CycleMethod.NO_CYCLE, // No repeating gradient
                new Stop(0.0, Color.RED), // Center: Light green
                new Stop(1.0, Color.DARKRED) // Edge: Dark green
        );
    }
}
