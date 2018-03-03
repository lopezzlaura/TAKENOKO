package com.polytech.bdsm.takenoko.gui;


import com.polytech.bdsm.takenoko.Main;
import com.polytech.bdsm.takenoko.engine.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class MenuBar extends javafx.scene.control.MenuBar {

    private Menu file, edit, view, window, help;

    //  File Menu Items
    private MenuItem save, load, close;

    //  Help Menu Items
    private MenuItem help_menu, logs, about;

    public MenuBar() {
        super();
        init();
    }

    private void init() {
        this.file    = new Menu("File");
        this.edit    = new Menu("Edit");
        this.view    = new Menu("View");
        this.window  = new Menu("Window");
        this.help    = new Menu("Help");

        //  File Menu
        this.save = new MenuItem("Save");
        this.save.setAccelerator(KeyCombination.valueOf("Ctrl + s"));
        this.load = new MenuItem("Load");
        this.close  = new MenuItem("Close");
        this.file.getItems().addAll(save, load, close);

        //  Help Menu
        this.help_menu = new MenuItem("Help");
        this.logs = new MenuItem("Show Game Logs");
        this.about = new MenuItem("About us");
        this.help.getItems().addAll(help_menu, logs, about);


        this.setBackground(new Background(new BackgroundFill(Paint.valueOf("#fff"),null, null)));

        this.getMenus().addAll(file, edit, view, window, help);
    }

    public void setFileMenuEvents(Game game, EventHandler<ActionEvent> loadEvent, EventHandler<ActionEvent> closeEvent) {
        FileChooser fileChooser = new FileChooser();
        this.save.setOnAction(event -> Main.save(game, fileChooser.showSaveDialog(null).getAbsolutePath()));
        this.load.setOnAction(loadEvent);
        this.close.setOnAction(closeEvent);
    }

    public void setHelpMenuEvents(LoggerOutput output) {
        this.help_menu.setOnAction(event -> {
            Dialog dialog = new Dialog();
            dialog.setTitle("Help");
            dialog.setHeaderText("Board Legend");
            dialog.initStyle(StageStyle.UTILITY);
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Facility :"), 0, 0);
            grid.add(new Label("Paddock :"), 1, 1);
            grid.add(new Label("Red"), 2, 1);
            grid.add(new Label("Pond :"), 1, 2);
            grid.add(new Label("Blue"), 2, 2);
            grid.add(new Label("Fertilizer :"), 1, 3);
            grid.add(new Label("Green"), 2, 3);

            grid.add(new Label("Parcel :"), 0, 4);
            grid.add(new Label("Pond Parcel :"), 1, 5);
            grid.add(new Label("Blue"), 2, 5);
            grid.add(new Label("Pink Parcel :"), 1, 6);
            grid.add(new Label("Pink"), 2, 6);
            grid.add(new Label("Green Parcel :"), 1, 7);
            grid.add(new Label("Green"), 2, 7);
            grid.add(new Label("Yellow Parcel :"), 1, 8);
            grid.add(new Label("Yellow"), 2, 8);
            grid.add(new Label("Number on the side :"), 1, 9);
            grid.add(new Label("Number of Bamboo"), 2, 9);

            dialog.getDialogPane().setContent(grid);

            dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
            dialog.show();
        });
        this.logs.setOnAction(event -> {
            Dialog dialog = new Dialog();
            dialog.setTitle("Logs");
            dialog.setHeaderText("The Game Logs");
            dialog.initStyle(StageStyle.UNIFIED);

            Label label = new Label("Games Logs:");

            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            textArea.setWrapText(true);

            output.setTextArea(textArea);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            dialog.getDialogPane().setExpandableContent(expContent);

            dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
            dialog.show();
        });
        this.about.setOnAction(event -> {
            Dialog dialog = new Dialog();
            dialog.setTitle("About Us");
            dialog.initStyle(StageStyle.UTILITY);
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("BDSM - Bureau de Sébastien Mosser"), 0, 0);
            grid.add(new Label("Version 8.0"), 0, 1);
            dialog.getDialogPane().setContent(grid);


            dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
            dialog.show();
        });
    }

}
