package com.polytech.bdsm.takenoko.engine.actions.params.weather;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;


/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class UnknownActionParams implements ActionParams {

    private ActionParams actionParams;

    /**
     * Method to get the action params of the weather action
     * picked by the player
     *
     * @return the action params of the weather action picked
     */
    public ActionParams getActionParams(){
        return this.actionParams;
    }

    /**
     * Method to set the action params of the weather action the
     * player has picked
     *
     * @param actionParams   The action params of the action
     *                       picked by the player
     */
    public void setActionParams(ActionParams actionParams) {
        this.actionParams = actionParams;
    }

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type UNKNOWN_ACTION
     */
    @Override
    public ActionType getActionType() {
        return ActionType.UNKNOWN_ACTION;
    }

    /**
     * Method to check that the params of the <code>Action</code>
     * aren't null
     *
     * @return true if the params aren't null, false otherwise
     */
    @Override
    public boolean paramsNotNull() {
        return actionParams != null;
    }

    /**
     * Clone method for UnknownActionParams
     *
     * @return a copy of the {@code UnknownActionParams} to copy
     */
    @Override
    public Object clone() {
        UnknownActionParams unknownActionParams = new UnknownActionParams();
        unknownActionParams.setActionParams(this.actionParams);
        return unknownActionParams;
    }
}
