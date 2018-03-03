package com.polytech.bdsm.takenoko.gui;

import javafx.scene.paint.Color;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public enum FacilityView {

    NO_FACILITY(Color.gray(0, 0)),
    POND_FACILITY(Color.BLUE),
    FERTILIZER_FACILITY(Color.GREEN),
    PADDOCK_FACILITY(Color.RED);

    private Color color;

    FacilityView(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
