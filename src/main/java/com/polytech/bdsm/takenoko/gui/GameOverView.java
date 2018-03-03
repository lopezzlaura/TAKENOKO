package com.polytech.bdsm.takenoko.gui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class GameOverView {

    private String message;
    private Point2D position;
    private int edgeSize;

    public GameOverView(String message, int edgeSize, Point2D position) {
        this.message = message;
        this.edgeSize = edgeSize;
        this.position = position;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Impact", FontWeight.LIGHT, edgeSize));
        gc.fillText(message, this.position.getX(), this.position.getY());
    }
}
