package com.polytech.bdsm.takenoko.engine.actions.params.weather;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class WindActionParams implements ActionParams {

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type WIND_ACTION
     */
    @Override
    public ActionType getActionType() {
        return ActionType.WIND_ACTION;
    }

    /**
     * Method to check that the params of the <code>Action</code>
     * aren't null
     *
     * @return true if the params aren't null, false otherwise
     */
    @Override
    public boolean paramsNotNull() {
        return true;
    }

    /**
     * Clone method for WindActionParams
     *
     * @return a copy of the {@code WindActionParams} to copy
     */
    @Override
    public Object clone() {
        return new WindActionParams();
    }
}
