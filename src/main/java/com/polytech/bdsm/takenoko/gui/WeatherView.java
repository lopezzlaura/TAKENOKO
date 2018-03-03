package com.polytech.bdsm.takenoko.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public enum WeatherView {

    SUN("Sun", Color.YELLOW),
    CLOUDS("Clouds", Color.GREY),
    RAIN("Rain", Color.BLUE),
    STORM("Storm", Color.VIOLET),
    WIND("Wind", Color.WHITE),
    NONE("", Color.gray(0, 0));

    private String name;
    private Color color;

    WeatherView(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public void draw(GraphicsContext gc, double width, double edgeSize) {
        gc.setFont(Font.font(edgeSize * 0.6));
        gc.setFill(Color.WHITE);
        gc.fillText("Current weather", width - edgeSize * 5, edgeSize);

        gc.setFill(this.color);
        gc.fillOval(width - edgeSize * 3.5, edgeSize * 1.5, edgeSize * 1.2, edgeSize * 1.2);
        gc.fillText(this.name, width - edgeSize * 3.8, edgeSize * 3.5);
    }
}
