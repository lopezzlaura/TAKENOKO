package com.polytech.bdsm.takenoko.engine.player;

import com.polytech.bdsm.takenoko.engine.actions.ActionFactory;
import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 * @see Inventory
 * @see Board
 */
public abstract class Player extends Observable implements Comparable<Player> {

    protected List<ActionType> actionList;
    protected int score;
    protected Inventory inventory;
    protected String name;
    protected int height;
    protected Board board;
    protected List<Goal> goals = new ArrayList<>();
    protected int nbActions = 0;
    protected ActionFactory actionFactory = new ActionFactory();
    protected Statistic stat;
    protected final Random randomizer;

    /**
     * Constructor for the player
     *
     * @param name  Name of the player
     * @param board The Game Board
     */
    public Player(String name, Board board, Random random) {
        this(name, board, random.nextInt(200 - 150) + 150, false, random);
    }


    /**
     * Constructor for the Player
     *
     * @param name   Name of the player
     * @param board  The Game Board
     * @param height The height of the Player
     */
    public Player(String name, Board board, int height, Random random) {
        this(name, board, random);
        this.height = height;
    }

    /**
     * Constructor for the player
     *
     * @param name  Name of the player
     * @param board The Game Board
     * @param height The height of the player (used to determine who will start)
     * @param noInitialDraw Used to prevent (or allow) the player from drawing new goals
     */
    public Player(String name, Board board, int height, boolean noInitialDraw, Random random) {
        this.randomizer = random;
        this.actionList = new ArrayList<>();
        this.score = 0;
        this.name = name;
        this.inventory = new Inventory();
        this.board = board;
        this.height = height;
        this.stat = new Statistic(name);

        if (!noInitialDraw) {
            // Pick three goals in the deck
            this.drawGoal(GoalType.PANDA);
            this.drawGoal(GoalType.GARDENER);
            this.drawGoal(GoalType.PARCEL);
        }
    }

    /**
     * Getter of the randomizer
     *
     * @return The random used by the player
     */
    public Random getRandomizer() {
        return randomizer;
    }

    /**
     * Getter for the score of the {@code Player}
     *
     * @return The score of the {@code Player}
     */
    public int getScore() {
        return score;
    }

    /**
     * Method to increment the player's score
     *
     * @param points the points to be added
     */
    public void addPoints(int points) {
        this.score += points;
    }

    /**
     * Getter for the different types of action the player is allowed to make during a turn
     *
     * @return The List of {@code ActionType} the player can do
     */
    public List<ActionType> getActionList() {
        return this.actionList;
    }

    /**
     * Setter for the different types of action the player is allowed to make during a turn
     *
     * @param actions The new List of possible action
     */
    public void setActionList(List<ActionType> actions) {
        this.actionList = actions;
    }

    /**
     * Method to remove an specific {@code ActionType} from the ActionList of the {@code Player}
     *
     * @param actionTypeToRemove the {@code ActionType} to be removed
     * @return true if it was removed, false either
     */
    public boolean removeActionType(ActionType actionTypeToRemove) {
        return this.actionList.remove(actionTypeToRemove);
    }

    /**
     * Check if the player have this action type in his list of possible action
     *
     * @param type The action type to check
     * @return If the Player does have
     */
    public boolean canExecuteActionType(ActionType type) {
        return this.actionList.contains(type);
    }

    /**
     * Getter for the next Action
     *
     * @return The next Action of the Bot
     */
    public abstract ActionParams getNextMove();

    /**
     * Method to get another action param when the previous action failed
     *
     * @param wrongAction The previous action that failed
     * @return The action param for the next move
     */
    public abstract ActionParams requestOtherMove(ActionParams wrongAction);

    /**
     * Getter for the action param to execute the weather action
     *
     * @param weatherParams The empty action params to fill
     * @return The fill action params to be execute
     */
    public abstract ActionParams getWeatherMove(ActionParams weatherParams);

    /**
     * Getter of the height of the Player
     *
     * @return The height of the Player
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Getter of the current private goal
     *
     * @return The current Goal
     */
    public List<Goal> getGoals() {
        return this.goals;
    }

    /**
     * Method to notify {@code Observer} that the player end his turn
     */
    public void endTurn() {
        setChanged();
        notifyObservers();
    }

    /**
     * Getter of Board
     *
     * @return The game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Getter of the {@code Inventory} of the player
     *
     * @return The {@code Inventory} of the player
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Getter for the Action Factory
     *
     * @return The Action Factory
     */
    public ActionFactory getActionFactory() {
        return actionFactory;
    }

    /**
     * Getter for the number of actions the player can execute this turn
     *
     * @return The remaining number of action left for the player this turn
     */
    public int getNbActions() {
        return nbActions;
    }

    /**
     * Add action for the player that he can do for this turn
     *
     * @param nbActions The number of actions he can execute
     */
    public void addNbActions(int nbActions) {
        this.nbActions += nbActions;
    }

    /**
     * Remove the number of action this player can do for this turn
     *
     * @param nbActions The number of action the player execute
     */
    public void removeNbActions(int nbActions) {
        int i = this.nbActions - nbActions;
        if (i < 0) this.nbActions = 0;
        else this.nbActions = i;
    }

    /**
     * Getter of the name
     *
     * @return The name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Draw a goal from the deck
     *
     * @param type The category of goal to draw
     * @return If the goal was drawn or not
     */
    public boolean drawGoal(GoalType type) {
        return this.goals.add(this.board.getGoalsDeck().drawGoal(type));
    }

    /**
     * Method to update the statistic
     *
     * @param hasPlayerWon      Boolean to specify if the player has won or not
     * @param otherPlayerWon    Boolean to specify if someone else is tied with him
     */
    public void endGame(boolean hasPlayerWon, boolean otherPlayerWon) {
        stat.update(hasPlayerWon, otherPlayerWon, score);
    }

    /**
     * Getter for the statistics of the {@code Player}
     *
     * @return The Player's Statistic
     */
    public Statistic getStat() {
        return this.stat;
    }

    /**
     * Setter for the statistics of the {@code Player}
     *
     * @param stat The new Player's Statistic
     */
    public void setStat(Statistic stat) {
        this.stat = stat;
    }

    /**
     * Clear the player of his goal, inventory and reset his score to 0
     *
     * @param newBoard The new board for the game
     */
    public void clear(Board newBoard) {
        this.board = newBoard;
        this.goals.clear();
        this.actionList.clear();
        this.inventory.clear();
        this.actionFactory = new ActionFactory();
        this.score = 0;
    }

    /**
     * Compare the {@code Player} based on his score
     * If both of score are equals, it will compare the number on {@code PandaGoal} in the {@code Inventory}
     *
     * @param p the {@code Player} to be compare with
     * @return 0 if both have the same score and number of Panda Goal in the Inventory, 1 if
     */
    @Override
    public int compareTo(Player p) {
        int c = -Integer.compare(this.score, p.getScore());
        if (c == 0) {
            c = -Integer.compare(this.inventory.getAccomplishedGoalPoint(GoalType.PANDA), p.inventory.getAccomplishedGoalPoint(GoalType.PANDA));
        }
        return c;
    }

    /**
     * ToString method
     *
     * @return Representation of the Object in a String format
     */
    @Override
    public String toString() {
        return this.name;
    }


}
