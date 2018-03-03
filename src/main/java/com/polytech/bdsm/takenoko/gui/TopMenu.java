package com.polytech.bdsm.takenoko.gui;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class TopMenu extends BorderPane {

    private Button pauseButton;
    private Button resumeButton;
    private Slider speedSlider;

    public TopMenu(PauseListener pauseListener, ResumeListener resumeListener) {
        super();
        this.init(pauseListener, resumeListener);
    }

    private void init(PauseListener pauseListener, ResumeListener resumeListener) {
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        this.pauseButton = new Button("Pause");
        this.pauseButton.setOnMouseClicked(event -> pauseListener.pauseAction());

        this.resumeButton = new Button("Resume MOSSER FUCKER");
        this.resumeButton.setOnMouseClicked(event -> resumeListener.resumeAction());

        this.speedSlider = new Slider(5, 1500, 500);
        this.speedSlider.setOrientation(Orientation.HORIZONTAL);
        this.speedSlider.setBlockIncrement(5);
        this.speedSlider.setShowTickLabels(true);
        this.speedSlider.setShowTickMarks(true);
        this.speedSlider.setMajorTickUnit(500);

        this.setRight(this.pauseButton);
        this.setCenter(this.speedSlider);
        this.setLeft(this.resumeButton);
    }

    public void setOnChangeSpeed(ChangeListener<? super Number> event) {
        this.speedSlider.valueProperty().addListener(event);
    }
}
