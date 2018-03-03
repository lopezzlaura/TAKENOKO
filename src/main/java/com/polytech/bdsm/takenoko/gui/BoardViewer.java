package com.polytech.bdsm.takenoko.gui;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
class BoardViewer extends Scene implements Serializable {

    private Canvas canvas;
    private TopMenu topMenu;
    private MenuBar menuBar;

    private int defaultParcelEdgeSize;
    private List<ParcelView> parcels;
    private PNJView gardener, panda;
    private List<PlayerView> players;
    private List<IrrigationView> irrigations;
    private WeatherView currentWeather;
    private int currentTurn;

    BoardViewer(int parcelEdgeSize, PauseListener pauseListener, ResumeListener resumeListener, ChangeListener<? super Number> event) {
        super(new Group(), 20 * parcelEdgeSize, 20 * parcelEdgeSize, Color.WHITE);

        this.currentWeather = WeatherView.NONE;
        ParcelView initialParcel = new ParcelView(20 * parcelEdgeSize / 2, 20 * parcelEdgeSize / 2, parcelEdgeSize, Color.BLUE, FacilityView.NO_FACILITY);

        this.defaultParcelEdgeSize = parcelEdgeSize;
        this.canvas = initCanvas();

        this.parcels = new ArrayList<>();
        this.parcels.add(initialParcel);

        this.irrigations = new ArrayList<>();

        this.gardener = new PNJView(new Image(getClass().getResourceAsStream("/images/gardener.png")), initialParcel.getPosition(), parcelEdgeSize / 2);
        this.panda = new PNJView(new Image(getClass().getResourceAsStream("/images/panda.png")), initialParcel.getPosition(), parcelEdgeSize / 2);

        BorderPane borderPane = new BorderPane();
        this.topMenu = new TopMenu(pauseListener, resumeListener);
        this.topMenu.setOnChangeSpeed(event);

        this.menuBar = new MenuBar();

        borderPane.setCenter(this.canvas);
        borderPane.setTop(this.topMenu);

        BorderPane globalPane = new BorderPane();
        globalPane.setCenter(borderPane);
        globalPane.setTop(this.menuBar);

        this.setRoot(globalPane);
        //((Group) this.getRoot()).getChildren().add(canvas);
    }

    private Canvas initCanvas() {
        final Canvas canvas = new Canvas(this.getWidth(), this.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        return canvas;
    }

    public List<ParcelView> getParcels() {
        return parcels;
    }

    public List<IrrigationView> getIrrigations() {
        return new ArrayList<>(irrigations);
    }

    ParcelView findParcel(Point2D point2D) {
        for (ParcelView view : this.parcels) {
            if (view.getPosition().equals(point2D)) {
                return view;
            }
        }

        return null;
    }

    /* GETTERS */

    public Canvas getCanvas() {
        return canvas;
    }

    public void clear() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());

        ParcelView initialParcel = new ParcelView(20 * defaultParcelEdgeSize / 2, 20 * defaultParcelEdgeSize / 2, defaultParcelEdgeSize, Color.BLUE, FacilityView.NO_FACILITY);
        this.parcels = new ArrayList<>();
        this.parcels.add(initialParcel);

        this.irrigations = new ArrayList<>();
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public WeatherView getCurrentWeather() {
        return currentWeather;
    }

    public PNJView getGardener() {
        return gardener;
    }

    public PNJView getPanda() {
        return panda;
    }

    int getDefaultParcelEdgeSize() {
        return defaultParcelEdgeSize;
    }

    public List<PlayerView> getPlayers() {
        return players;
    }

    ParcelView getCenterParcel() {
        return this.parcels.isEmpty() ? new ParcelView((int) canvas.getWidth() / 2, (int) canvas.getHeight() / 2, defaultParcelEdgeSize, Color.BLUE, FacilityView.NO_FACILITY) : this.parcels.get(0);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    /* SETTERS */
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
        this.updateView();
    }

    public void setCurrentWeather(WeatherView currentWeather) {
        this.currentWeather = currentWeather;
        this.updateView();
    }

    void setPlayerTurn(int num) {
        for (PlayerView playerView : players) {
            if (playerView.getNum() == num) playerView.setPlaying(true);
            else playerView.setPlaying(false);
        }
        this.updateView();
    }

    void setPlayers(List<PlayerView> players) {
        this.players = players;
    }

    void addIrrigation(IrrigationView irrigationView) {
        this.irrigations.add(irrigationView);
        this.updateView();
    }

    void addParcelView(ParcelView parcelView) {
        this.parcels.add(parcelView);
        this.updateView();
    }

    void movePanda(Point2D newPosition) {
        this.panda.move(newPosition);
        this.updateView();
    }

    void moveGardener(Point2D newPosition) {
        this.gardener.move(newPosition);
        this.updateView();
    }


    /**
     * Draw all the parcels in the canvas
     */
    private void updateView() {
        this.canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.canvas.getGraphicsContext2D().setFill(Color.BLACK);
        this.canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Turn
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(defaultParcelEdgeSize / 1.5));
        gc.fillText("Turn: " + this.currentTurn, this.canvas.getWidth() / 2.2, defaultParcelEdgeSize);
        
        // Weather
        this.currentWeather.draw(gc, canvas.getWidth(), defaultParcelEdgeSize);

        // Update players
        for (PlayerView playerView : players) {
            playerView.draw(gc);
        }

        // Update Parcels
        for (ParcelView view : parcels) {
            view.draw(gc);
        }

        // Update Irrigations
        for (IrrigationView irrigation : irrigations) {
            irrigation.draw(gc);
        }

        // Update Panda and Gardener
        this.panda.drawWithImage(gc, 0);
        this.gardener.drawWithImage(gc, defaultParcelEdgeSize / 3);
    }
}
