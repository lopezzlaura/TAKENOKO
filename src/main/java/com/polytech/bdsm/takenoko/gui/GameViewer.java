package com.polytech.bdsm.takenoko.gui;

import com.polytech.bdsm.takenoko.Main;
import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.GardenerStrategy;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.ai.strategies.ParcelStrategy;
import com.polytech.bdsm.takenoko.ai.strategies.RandomStrategy;
import com.polytech.bdsm.takenoko.engine.Game;
import com.polytech.bdsm.takenoko.engine.actions.weather.WeatherDice;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.player.Player;
import com.polytech.bdsm.takenoko.engine.player.Turn;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.*;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class GameViewer extends Application implements Observer {

    /**
     * Main method for the IHM
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }


    private Game game;
    private BoardViewer viewer;
    private int timeInterval;

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    public void init() throws Exception {
        super.init();

        Map<String, String> params = this.getParameters().getNamed();

        if (params.containsKey("interval") && !params.get("interval").isEmpty()) {
            this.timeInterval = Integer.parseInt(params.get("interval"));
        } else {
            this.timeInterval = 100;
        }

        if (params.containsKey("size") && !params.get("size").isEmpty()) {
            this.viewer = new BoardViewer(Integer.parseInt(params.get("size")), () -> game.pause(), () -> game.resume(), (observable, oldValue, newValue) -> this.setTimeInterval(newValue.intValue()));
        } else {
            this.viewer = new BoardViewer(40, () -> game.pause(), () -> game.resume(), (observable, oldValue, newValue) -> this.setTimeInterval(newValue.intValue()));
        }

        Board board = new Board();

        Player sherlock = new Bot("Sherlock",   board, new PandaStrategy(), 183, board.getGlobalRandom());
        Player moriarty = new Bot("Moriarty",   board, new GardenerStrategy(), 173, board.getGlobalRandom());
        Player watson   = new Bot("Watson",     board, new ParcelStrategy(), board.getGlobalRandom());
        Player mycroft  = new Bot("Mycroft",    board, new RandomStrategy(true), board.getGlobalRandom());

        this.game = new Game(board, 1, sherlock, moriarty, watson, mycroft);
        game.addObserver(this);

        // Add the players views
        List<PlayerView> players = new ArrayList<>();
        players.addAll(Arrays.asList(
                new PlayerView(1, sherlock.getName(), this.viewer.getDefaultParcelEdgeSize()),
                new PlayerView(2, moriarty.getName(), this.viewer.getDefaultParcelEdgeSize()),
                new PlayerView(3, watson.getName(), this.viewer.getDefaultParcelEdgeSize()),
                new PlayerView(4, mycroft.getName(), this.viewer.getDefaultParcelEdgeSize())
        ));
        this.viewer.setPlayers(players);


        this.initMenuBar(this.viewer.getMenuBar());
    }

    private void initMenuBar(MenuBar menuBar) {
        menuBar.setFileMenuEvents(this.game,
                event -> {
                        Game newGame = (Game) Main.load(new FileChooser().showOpenDialog(null));
                        viewer =  new BoardViewer(viewer.getDefaultParcelEdgeSize(), newGame::pause, newGame::resume, (observable, oldValue, newValue) -> this.setTimeInterval(newValue.intValue()));
                },
                event -> System.exit(0));

        LoggerOutput loggerOutput = new LoggerOutput();
        menuBar.setHelpMenuEvents(loggerOutput);
        this.game.setLoggerStreamOutPut(loggerOutput);
    }

    /**
     * Start the JavaFX application window
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BDSM - Takenoko Game Viewer");
        primaryStage.setScene(this.viewer);
        primaryStage.show();

        try {
            new Thread(game).start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * When a player finished an action,
     * an event is called and the board view is updated
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Game) {
            if (arg instanceof Board) {
                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                BoardAdapter.updateBoardViewer(this.viewer, (Board) arg);
            } else if (arg instanceof Turn) {
                Turn t = (Turn) arg;
                this.viewer.setCurrentTurn(t.getTurnNumber());

                Game game = (Game) o;
                for (int i = 0; i < game.getPlayers().size(); i++) {
                    if (game.getPlayers().get(i).equals(t.getPlayer())) {
                        this.viewer.setPlayerTurn(i + 1);
                        break;
                    }
                }
            } else if (arg instanceof List) {
                new GameOverView(arg.toString() + " win : " + ((Player)((List) arg).get(0)).getScore() + " points",
                        this.viewer.getDefaultParcelEdgeSize(),
                        new Point2D(this.viewer.getDefaultParcelEdgeSize(), this.viewer.getHeight() - this.viewer.getDefaultParcelEdgeSize())).draw(this.viewer.getCanvas().getGraphicsContext2D());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (arg instanceof WeatherDice) {
                this.viewer.setCurrentWeather(BoardAdapter.weatherToWeatherView((WeatherDice) arg));
            }
        }
    }
}
