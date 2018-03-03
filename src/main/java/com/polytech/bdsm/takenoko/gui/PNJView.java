package com.polytech.bdsm.takenoko.gui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;


/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class PNJView {

    private Point2D currentPosition;
    private double size;
    private Character initial;
    private Image icon;

    public PNJView(Character initial, Point2D currentPosition, double size) {
        this.initial = initial;
        this.currentPosition = currentPosition;
        this.size = size;
    }

    public PNJView(Image icon, Point2D currentPosition, double size) {
        this.icon = icon;
        this.currentPosition = currentPosition;
        this.size = size;
    }

    public void move(Point2D newPosition) {
        this.currentPosition = newPosition;
    }

    public Point2D getCurrentPosition() {
        return currentPosition;
    }

    public void drawWithImage(GraphicsContext gc, int offset) {
        gc.drawImage(this.icon, currentPosition.getX() - size + offset, currentPosition.getY() - size, size * 1.5, size * 2);
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(currentPosition.getX() - size / 2, currentPosition.getY() - size / 2, size, size);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.WHITE);
        gc.fillText(initial.toString(), currentPosition.getX(), currentPosition.getY());
    }
}
