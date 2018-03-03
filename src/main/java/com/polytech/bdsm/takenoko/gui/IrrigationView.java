package com.polytech.bdsm.takenoko.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class IrrigationView {

    private ParcelView leftParcel;
    private ParcelView rightParcel;

    public IrrigationView(ParcelView leftParcel, ParcelView rightParcel) {
        this.leftParcel = leftParcel;
        this.rightParcel = rightParcel;
    }

    public ParcelView getLeftParcel() {
        return leftParcel;
    }

    public ParcelView getRightParcel() {
        return rightParcel;
    }

    public void draw(GraphicsContext gc) {
        double xA = leftParcel.getPosition().getX();
        double yA = leftParcel.getPosition().getY();
        double xB = rightParcel.getPosition().getX();
        double yB = rightParcel.getPosition().getY();

        double xI = (xA + xB) / 2.;
        double yI = (yA + yB) / 2.;

        double R = rightParcel.getEdgeSize();
        double d = R / 2.;

        double a = -(xB - xA) / (yB - yA);
        double b = yI - a * xI;

        // Final coordinates
        double xV = xI - Math.sqrt((Math.pow(d, 2.) / (1. + a * a)));

        double xW = xI + Math.sqrt((Math.pow(d, 2.) / (1. + a * a)));

        double yV = a * xV + b;
        double yW = a * xW + b;

        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(0.2 * R);
        gc.strokeLine(xV, yV, xW, yW);
    }
}
