package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

class ValidateGoal extends Action {

    private Goal goal;

    /**
     * Normal override constructor
     *
     * @param player Player that do the action
     * @param goal   Goal to validate
     */
    public ValidateGoal(Player player, Goal goal) {
        super(player);
        this.goal = goal;
    }

    /**
     * Getter for the Validate goal action name
     *
     * @return Validate Goal action name
     */
    @Override
    public String getName() {
        return " validated : " + this.goal.toString() + " and has now a score of " + this.player.getScore() + ".\n \t\t\t  This player validated " + this.player.getInventory().getAccomplishedGoal() + " goal(s) until now.";
    }

    /**
     * Check if the Action is recorded as an action for the player
     *
     * @return True if the Action counts in the possible actions of the player for a turn,
     *         false otherwise.
     */
    @Override
    public boolean isActionDecrementedFromPlayer() {
        return false;
    }

    /**
     * Getter for the goal to validate
     *
     * @return Goal instance
     */
    public Goal getGoal() {
        return goal;
    }

    /**
     * Execute this action
     * Meaning check if the goal is valid, adding to the player points of the goal, adding the goal to the player inventory
     * and then do the post validation action
     *
     * @return true if the execution went good, false otherwise
     */
    @Override
    public boolean execute() {
        if (this.goal.isValid(this.player)) {

            this.player.addPoints(this.goal.getPoints());

            this.player.getInventory().addGoal(goal);

            this.player.getGoals().remove(goal);
            this.goal.postValidation(this.player);

            return true;
        }

        return false;
    }

    /**
     * Equals method for the {@code ValidateGoal} action
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ValidateGoal && ((ValidateGoal)obj).goal.equals(this.goal);
    }
}
