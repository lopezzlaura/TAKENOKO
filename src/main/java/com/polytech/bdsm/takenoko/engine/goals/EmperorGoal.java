package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class EmperorGoal extends Goal {

    /**
     *
     */
    public EmperorGoal() {
        super(2);
    }

    /**
     * Emperor Goal is always valid
     * @param player
     * @return true
     */
    @Override
    public boolean isValid(Player player) {
        return true;
    }

    /**
     * Nothing happens for Emperor Goal
     * @param player The player doing those actions
     */
    @Override
    public void postValidation(Player player) {
    }

    /**
     * Getter of the Goal type
     *
     * @return The Goal type
     */
    @Override
    public GoalType getGoalType() {
        return GoalType.EMPEROR;
    }

    /**
     * Overridden clone method
     *
     * @return the cloned object
     */
    @Override
    public Object clone() {
        return new EmperorGoal();
    }
}
