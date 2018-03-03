package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class DrawGoalParams implements ActionParams {

    private GoalType goalType;

    /**
     * Getter of the GoalType
     *
     * @return GoalType
     */
    public GoalType getGoalType() {
        return goalType;
    }

    /**
     * Setter of the GoalType
     *
     * @param goalType The new GoalType
     */
    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type DRAW_GOAL
     */
    @Override
    public ActionType getActionType() {
        return ActionType.DRAW_GOAL;
    }

    /**
     * Method to check that the params of the <code>Action</code>
     * aren't null
     *
     * @return true if the params aren't null, false otherwise
     */
    @Override
    public boolean paramsNotNull() {
        return goalType != null;
    }

    /**
     * Clone method for {@code DrawGoalParams}
     *
     * @return a copy of the {@code DrawGoalParams}
     */
    @Override
    public Object clone() {
        DrawGoalParams copy = new DrawGoalParams();
        copy.setGoalType(this.goalType);
        return copy;
    }
}
