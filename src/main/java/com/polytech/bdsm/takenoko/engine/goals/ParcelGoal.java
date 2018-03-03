package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class ParcelGoal extends Goal {

    private ParcelGoalPattern pattern;
    private Color[] colors;

    /**
     * Normal constructor for ParcelGoal
     *
     * @param points  The number of points the goal gives when cleared
     * @param pattern The pattern of the ParcelGoal
     * @param colors  the color (or colors for diamond pattern) of the ParcelGoal
     */
    public ParcelGoal(int points, ParcelGoalPattern pattern, Color... colors) {
        super(points);
        this.pattern = pattern;
        this.colors = colors;
    }

    /**
     * Check if this goal is reached by a player
     *
     * @param player the player from whom the goal is check
     * @return true if the goal is valid
     */
    @Override
    public boolean isValid(Player player) {
        return isValid(player.getBoard());
    }

    /**
     * Check if this goal is reached by a player
     *
     * @param board the board from which the goal is check
     * @return true if the goal is valid
     */
    public boolean isValid(Board board) {
        return pattern.isValid(board, colors);
    }

    /**
     * Check is this pattern is almost present on {@code Board}
     * @param board the board on which the pattern is to be checked
     * @return true if the pattern is present, false either
     */
    public boolean isAlmostValid(Board board) {
        return pattern.isAlmostValid(board, colors);
    }

    /**
     * @param player The player doing those actions
     */
    @Override
    public void postValidation(Player player) {

    }

    /**
     * ToString method
     *
     * @return Representation of the Object in a String format
     */
    @Override
    public String toString() {
        String colorString = "";

        for (int i = 0; i < colors.length; i++) {
            colorString += colors[i];
            colorString += " ";
        }

        return super.toString() + " : Place a " + colorString + pattern.getName();
    }

    /** Get the GoalType
     * @return the type of the goal
     */
    @Override
    public GoalType getGoalType() {
        return GoalType.PARCEL;
    }

    /**
     * Overridden clone method
     *
     * @return the cloned object
     */
    @Override
    public Object clone() {
        return new ParcelGoal(this.points, this.pattern, this.colors);
    }

    public Color[] getColor() {
        return colors;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ParcelGoal)) return false;
        ParcelGoal g = (ParcelGoal) obj;
        return super.equals(obj) && g.points == this.points && g.colors == this.colors && g.pattern == this.pattern;
    }
}
