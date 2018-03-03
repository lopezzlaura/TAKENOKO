package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public abstract class Goal {

    protected int points;

    /**
     * Normal constructor
     * @param points The number of points the goal gives when cleared
     */
    public Goal(int points) {
        this.points = points;
    }

    /**
     * Getter of the number of point that this Goal give when accomplished
     *
     * @return The number of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Check if this goal is reached by a player
     *
     * @return The player used to check the goal
     */
    public abstract boolean isValid(Player player);

    /**
     * Actions to do after a validation of this goal
     *
     * @param player The player doing those actions
     */
    public abstract void postValidation(Player player);

    /**
     * Getter of the Goal type
     *
     * @return The Goal type
     */
    public abstract GoalType getGoalType();

    /**
     * Equals method
     *
     * @param obj Object to be compared to
     * @return If the Object have the same value
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Goal)) return false;
        Goal g = (Goal) obj;
        return g.getGoalType() == this.getGoalType() && g.points == this.points;
    }

    /**
     * Force to override the clone method in inherited classes
     */
    @Override
    public abstract Object clone();

    /**
     * ToString method
     *
     * @return Representation of the Object in a String format
     */
    @Override
    public String toString() {
        return this.getGoalType().getName() + " goal";
    }

}
