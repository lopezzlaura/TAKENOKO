package com.polytech.bdsm.takenoko.gui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class ParcelView {

    private int edgeSize;
    private Point2D position;
    private Color color;
    private int bamboos = 0;
    private FacilityView facility;

    public ParcelView(int x, int y, int edgeSize, Color color, FacilityView facility) {
        this.position = new Point2D(x, y);
        this.edgeSize = edgeSize;
        this.color = color;
        this.facility = facility;
    }

    public int getEdgeSize() {
        return edgeSize;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setBamboos(int bamboos) {
        this.bamboos = bamboos;
    }

    public int getBamboos() {
        return bamboos;
    }

    public void draw(GraphicsContext gc) {
        /*
        * x = radius * sin(angle)
        * y = -(radius * cos(angle))
        */

        double x1, y1;
        double[] xPoints = new double[6];
        double[] yPoints = new double[6];
        for (double i = 0, angle = 90; i < xPoints.length || angle < 450; angle += 60, i++) {
            x1 = this.position.getX() + (edgeSize * Math.sin(Math.toRadians(angle)));
            y1 = this.position.getY() - (edgeSize * Math.cos(Math.toRadians(angle)));
            xPoints[(int) i] = x1;
            yPoints[(int) i] = y1;
        }

        gc.setFill(this.color);
        gc.fillPolygon(xPoints, yPoints, 6);

        // Draw the bamboos
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(this.edgeSize / 2));
        gc.fillText(Integer.toString(this.bamboos), this.position.getX() - this.edgeSize * 0.9, this.position.getY() + this.edgeSize * 0.15);

        // Draw the facility
        gc.setFill(this.facility.getColor());
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(this.edgeSize * 0.05);
        gc.fillRect(this.position.getX() - this.edgeSize * 0.45, this.position.getY() + this.edgeSize * 0.5, this.edgeSize * 0.9, this.edgeSize * 0.3);

        if (this.facility != FacilityView.NO_FACILITY) {
            gc.strokeRect(this.position.getX() - this.edgeSize * 0.45, this.position.getY() + this.edgeSize * 0.5, this.edgeSize * 0.9, this.edgeSize * 0.3);
        }
    }
}
