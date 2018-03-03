package com.polytech.bdsm.takenoko.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class PlayerView {

    private String name;
    private int num;
    private int size;
    private boolean isPlaying = false;

    public PlayerView(int num, String name, int size) {
        this.name = name;
        this.num = num;
        this.size = size / 2;
    }

    public int getNum() {
        return num;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void draw(GraphicsContext gc) {
        gc.setFont(Font.font(size));
        gc.setFill(Color.WHITE);
        gc.fillText("" + num + " : " + name + " \t" + (isPlaying ? "PLAYING" : ""), size / 2, size / 2 + (size * num));
    }
}
