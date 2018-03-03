package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.goals.Goal;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class ValidateGoalParams implements ActionParams {

    private Goal goal;

    /**
     * Method to get the goal to validate
     *
     * @return the goal to validate
     */
    public Goal getGoal() {
        return goal;
    }

    /**
     * Method to set the goal to be validate
     *
     * @param goal The goal that has to be validate
     */
    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type VALIDATE_GOAL
     */
    @Override
    public ActionType getActionType() {
        return ActionType.VALIDATE_GOAL;
    }

    /**
     * Method to check that the params of the <code>Action</code>
     * aren't null
     *
     * @return true if the params aren't null, false otherwise
     */
    @Override
    public boolean paramsNotNull() {
        return goal != null;
    }

    /**
     * Clone method for {@code ValidateGoalParams}
     *
     * @return a copy of the {@code ValidateGoalParams}
     */
    @Override
    public Object clone() {
        ValidateGoalParams copy = new ValidateGoalParams();
        copy.setGoal((Goal) this.goal.clone());
        return copy;
    }
}
