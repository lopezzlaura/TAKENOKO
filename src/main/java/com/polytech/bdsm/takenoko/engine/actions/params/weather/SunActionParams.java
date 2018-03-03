package com.polytech.bdsm.takenoko.engine.actions.params.weather;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class SunActionParams implements ActionParams {

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type SUN_ACTION
     */
    @Override
    public ActionType getActionType() {
        return ActionType.SUN_ACTION;
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
     * Clone method for SunActionParams
     *
     * @return a copy of the {@code SunActionParams} to copy
     */
    @Override
    public Object clone() {
        return new SunActionParams();
    }
}
