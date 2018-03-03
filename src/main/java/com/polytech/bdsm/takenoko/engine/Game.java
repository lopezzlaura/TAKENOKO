package com.polytech.bdsm.takenoko.engine;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.*;
import com.polytech.bdsm.takenoko.engine.actions.Action;
import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionResult;
import com.polytech.bdsm.takenoko.engine.actions.params.ValidateGoalParams;
import com.polytech.bdsm.takenoko.engine.actions.weather.WeatherAction;
import com.polytech.bdsm.takenoko.engine.actions.weather.WeatherDice;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.goals.EmperorGoal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.player.Inventory;
import com.polytech.bdsm.takenoko.engine.player.Player;
import com.polytech.bdsm.takenoko.engine.player.Turn;
import com.polytech.bdsm.takenoko.logger.GameLogger;

import java.io.OutputStream;
import java.util.*;
import java.util.logging.Level;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 * @see GameLogger
 * @see Board
 * @see Bot
 */
public class Game extends Observable implements Observer, Runnable {

    // Logger to keep track of the game states and events
    private final static GameLogger LOGGER = new GameLogger();
    private final static int TURN_LIMITS = 500;
    private static int BUG_GAME = 0;
    private int nbGoal;

    private List<Player> players;
    private int turn_count = 1;
    private int nbGame;
    private Board board;

    private boolean pauseGame = false;

    /**
     * Normal constructor for a Game
     *
     * @param players The players of this game
     * @throws GameFailedException If there is more then 4 Player
     */
    public Game(Board board, Player... players) {
        if (players.length > 4) throw new GameFailedException("Game can't have more than 4 Players.");
        if (players.length < 2) throw new GameFailedException("Game can't have less than 2 Players.");
        this.board = board;
        this.nbGame = 1;
        this.players = Arrays.asList(players);
        this.nbGoal = 11 - players.length;
        for (Player p : players) p.getStat().setPlayer(p.getName());
    }


    /**
     * Normal Constructor for n Game
     *
     * @param players The players of those Game
     * @param nbGame  The number of Game that will be played
     */
    public Game(Board board, int nbGame, Player... players) {
        this(board, players);
        if (nbGame > 1) this.nbGame = nbGame;
    }

    /**
     * Getter for the players list
     *
     * @return The list of the players in this game
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Method to simulate turn through player by getting for each turn an Action and execute it
     *
     * @param player The Player that should play the turn
     * @see Player#getNextMove()
     * @see Action#execute()
     */
    public void nextTurn(Player player) {

        if ((board.getGlobalRandom().nextInt(Integer.MAX_VALUE)) < 100) LOGGER.logMessage(player.toString() + " say : \"I don't want to go !\"\n", Level.WARNING);

        LOGGER.logMessage(player.toString() + " turn to play");

        Turn turn = new Turn(player, turn_count);
        player.addObserver(turn);
        turn.addObserver(this);

        setChanged();
        notifyObservers(turn);

        if (!turn.playTurn()) {
            throw new GameFailedException("Game failed : The turn " + turn_count + " went wrong");
        }

        while (pauseGame) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        player.deleteObserver(turn);
    }

    /**
     * Start the Game
     *
     * @return The Player that won the Game
     * @see Player
     * @see Inventory
     */
    public List<Player> startGame() {
        players.sort((o1, o2) -> -Integer.compare(o1.getHeight(), o2.getHeight()));
        for (Player p : players) LOGGER.logMessage(p + " will be playing");
        boolean running = true;
        boolean stopGame = false;
        while (running && turn_count < TURN_LIMITS) {
            LOGGER.logMessage("Turn " + turn_count + ": ");
            for (Player p : players) {
                nextTurn(p);
                running = !(hasPlayerWon(p));
                if (!running) {
                    emperorVisit(p);
                    stopGame = true;
                }
            }
            if (stopGame) running = false;
            turn_count++;
        }
        if (turn_count >= TURN_LIMITS) {
            BUG_GAME++;
        }
        List<Player> winners = getCurrentWinners();
        for (Player p : players) p.endGame(winners.contains(p), winners.contains(p) && (winners.size() > 1));
        return winners;
    }

    /**
     * Method to check if the Player meet the condition necessary to win
     * The condition are very simple. The Player has to have enough accomplished goal
     * The threshold of accomplished goal is calculated like this :
     *
     *  threshold = 11 - number of player
     *
     * @param p The Player to check the condition
     * @return If the player have enough accomplished goal
     */
    public boolean hasPlayerWon(Player p) {
        return p.getInventory().getAccomplishedGoal() >= nbGoal;
    }

    /**
     * Getter for the current winner(s) of the Game
     *
     * @return The current winner(s) of the Game
     */
    public List<Player> getCurrentWinners() {
        if (players.size() < 1) return null;
        List<Player> sortedList = new ArrayList<>(this.players);
        Collections.sort(sortedList);
        List<Player> winners = new ArrayList<>();
        winners.add(sortedList.get(0));
        for (Player player : sortedList) {
            if (player.compareTo(winners.get(0)) == 0 && !(winners.contains(player))) winners.add(player);
        }
        return winners;
    }

    /**
     * Method to add Emperor Goal to the first {@code Player} to finish the {@code Game}
     *
     * @param firstToFinish The first {@code Player} to finish the {@code Game}
     */
    void emperorVisit(Player firstToFinish) {
        for (Player p : players) {
            if ((p.getInventory().getAccomplishedGoalCount(GoalType.EMPEROR) >= 1) && !(p.equals(firstToFinish)))
                return;
        }
        ValidateGoalParams params = (ValidateGoalParams) firstToFinish.getActionFactory().getAction(ActionType.VALIDATE_GOAL);
        params.setGoal(new EmperorGoal());
        firstToFinish.getActionList().add(ActionType.VALIDATE_GOAL);
        firstToFinish.getActionFactory().doAction(params, firstToFinish, new ActionResult());
        LOGGER.logMessage(firstToFinish.getName() + " is congratulated by the emperor and gets 2 points");

        List<Player> temp = new ArrayList<>(players);

        int index = players.indexOf(firstToFinish);

        if (index == -1) return;

        while (!temp.get(0).equals(firstToFinish)) {
            Player playerTemp = temp.remove(0);
            temp.add(playerTemp);
        }

        temp.remove(0);

        for (Player player : temp) {
            this.nextTurn(player);
        }
    }

    /**
     * Pause current Thread
     */
    public void pause() {
        this.pauseGame = true;
    }

    /**
     * Resume current Thread
     */
    public void resume() {
        this.pauseGame = false;
    }

    /**
     * Starting point of the Game
     *
     * @param args Command line arguments
     * @see Bot#Bot(String, Board, Strategy, Random)
     * @see GameLogger#logMessage(String)
     */
    public static void main(String[] args) {
        int iteration = 1;
        int withSeed = new Random().nextInt();
        List<Strategy> strategies = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-n")) {
                if (i +1 >= args.length) {
                    System.err.println("Error syntax (Expected Number) : [-n <number-of-game>]");
                    System.exit(1);
                }
                if (!args[i + 1].matches("[0-9]+")) {
                    System.err.println("Error syntax (Not a Number) : [-n <number-of-game>]");
                    System.exit(1);
                }
                iteration = Integer.parseInt(args[i + 1]);
            }

            if (args[i].equals("-nl") || args[i].equals("--no-logs")) {
                LOGGER.disableLogs();
            }

            if (args[i].equals("--seed")) {
                if (i + 1 >= args.length) {
                    System.err.println("Error syntax (Expected Number) : [--seed <seed-key>]");
                    System.exit(1);
                }
                if (!args[i + 1].matches("[0-9]+")) {
                    System.err.println("Error syntax (Not a Number) : [--seed <seed-key>]");
                    System.exit(1);
                }
                withSeed = Integer.parseInt(args[i + 1]);
            }

            if (args[i].equals("-p") || args[i].equals("--players")) {
                if (i + 1 >= args.length) {
                    System.err.println("Error syntax (Expected strategy type) : [(-p, --players) (panda, gardener, parcel, random, multiple)]");
                    System.exit(1);
                }
                int n = i + 1;
                while (n < args.length && args[n].matches("[a-zA-Z]+")) {
                    String strat = args[n++].toLowerCase();
                    switch (strat) {
                        case "panda":       strategies.add(new PandaStrategy());
                            break;
                        case "gardener":    strategies.add(new GardenerStrategy());
                            break;
                        case "parcel":      strategies.add(new ParcelStrategy());
                            break;
                        case "random":      strategies.add(new RandomStrategy(true));
                            break;
                        case "multiple":    strategies.add(new MultipleStrategy());
                            break;
                    }
                }
            }
        }

        Board board = new Board(withSeed);

        Game game;

        if (strategies.isEmpty()) {
            Player sherlock = new Bot("Sherlock", board, new PandaStrategy(), 183, board.getGlobalRandom());
            Player moriarty = new Bot("Moriarty", board, new GardenerStrategy(), 173, board.getGlobalRandom());
            Player watson = new Bot("Watson", board, new ParcelStrategy(), board.getGlobalRandom());
            Player mycroft = new Bot("Mycroft", board, new RandomStrategy(true), board.getGlobalRandom());

            game = new Game(board, iteration > 1 ? iteration : 1, sherlock, moriarty, watson, mycroft);

        } else {

            int size = strategies.size();
            Player sherlock = null, moriarty = null, watson = null, mycroft = null;

            switch (size) {
                case 4 : mycroft = new Bot("Mycroft", board, strategies.get(3), board.getGlobalRandom());
                case 3 : watson = new Bot("Watson", board, strategies.get(2), board.getGlobalRandom());
                case 2 : moriarty = new Bot("Moriarty", board, strategies.get(1), board.getGlobalRandom());
                case 1 : sherlock = new Bot("Sherlock", board, strategies.get(0), board.getGlobalRandom());
                    break;
                default: sherlock = new Bot("Sherlock", board, new PandaStrategy(), board.getGlobalRandom());
            }


            switch (size) {
                case 1 : game = new Game(board, iteration > 1 ? iteration : 1, sherlock); break;
                case 2 : game = new Game(board, iteration > 1 ? iteration : 1, sherlock, moriarty); break;
                case 3 : game = new Game(board, iteration > 1 ? iteration : 1, sherlock, moriarty, watson); break;
                case 4 : game = new Game(board, iteration > 1 ? iteration : 1, sherlock, moriarty, watson, mycroft); break;
                default: game = new Game(board, iteration > 1 ? iteration : 1, sherlock);
            }
        }

        try {
            Thread gameThread = new Thread(game);
            gameThread.start();
            gameThread.join();
        } catch (InterruptedException | GameFailedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Game thread method to execute n Game
     */
    @Override
    public void run() {
        long time = System.nanoTime();
        int currentProgress;

        for (int i = 1; i <= nbGame; i++) {
            LOGGER.logMessage("The game " + i + " is on");
            try {
                List<Player> winner = this.startGame();
                if (winner.size() < 1) {
                    LOGGER.logMessage("Game " + i + " failed : no winner");
                    return;
                }

                setChanged();
                notifyObservers(winner);
                LOGGER.logMessage("Seed used : " + this.board.getSeed());
                LOGGER.logMessage("The game " + i + " lasted " + turn_count + " turns");
                LOGGER.logMessage(winner + " won the game " + i + " with a score of : " + winner.get(0).getScore() + "\n\t\t\t" + winner + " is very smart");
            } catch (Exception e) {
                LOGGER.logMessage("Game " + i + " failed");
                e.printStackTrace();
                System.exit(-1);
            }

            this.turn_count = 1;
            this.board = new Board();
            Player player, newPlayer;
            for (int j = 0; j < this.players.size(); j++) {
                player = this.players.get(j);
                newPlayer = new Bot(player.toString(), this.board, ((Bot) player).getInitialStrategy().getStrategyType().newStrategyInstance(), player.getHeight(), this.board.getGlobalRandom());
                newPlayer.setStat(player.getStat());
                this.players.set(j, newPlayer);
            }

            if (!LOGGER.isActive()) {
                currentProgress = (i * 50) / nbGame;
                LOGGER.printLoading(currentProgress, 50, i + " /" + nbGame);
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.logMessage("All " + nbGame + " game are over", Level.INFO);
        for (Player player : players) {
            LOGGER.logMessage(player.getStat().toString(), Level.INFO);
        }
        double elapsedTime = (System.nanoTime() - time) * Math.pow(10, -9);
        LOGGER.logMessage("Elapsed time : " +
                        (elapsedTime < 60 ? Math.ceil(elapsedTime) + " second(s)" : (elapsedTime < (60 * 60) ?
                                Math.ceil(elapsedTime / 60) + " minute(s) and " + Math.ceil(elapsedTime % 60) + " second(s)"
                                : Math.ceil(elapsedTime / (60 * 60)) + " hour(s), " + Math.ceil(elapsedTime / 60) + " minute(s) and "
                                + Math.ceil(elapsedTime % 60) + " second(s)"))
                , Level.INFO);
        LOGGER.logMessage("Endless game : " + BUG_GAME, Level.INFO);
    }

    /**
     * Update method when the {@code Observable} notify if the player has end his turn
     *
     * @param o   The {@code Observable} that notify the Game
     * @param arg The argument given
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Turn) {
            if (arg instanceof ActionResult) {
                if (((ActionResult) arg).getAction() != null) {
                    setChanged();
                    notifyObservers(board);
                    if ((((Turn) o).getResult().getAction() instanceof WeatherAction)) {
                        LOGGER.logMessage(((Turn) o).getResult().getAction().toString());
                    } else {
                        LOGGER.logMessage(((Turn) o).getPlayer().toString() + ((ActionResult) arg).getAction().toString());
                    }
                }
            } else if (arg instanceof WeatherDice) {
                setChanged();
                notifyObservers(arg);
            }
        }
    }

    /**
     * Set a output stream to the Logger
     *
     * @param out The Output stream where the Logger will write on
     */
    public void setLoggerStreamOutPut(OutputStream out) {
        LOGGER.setStreamHandler(out);
    }

    /**
     * Getter for the number of turn that game last
     *
     * @return The number of turn
     */
    public int getTurnCount() {
        return turn_count;
    }
}
