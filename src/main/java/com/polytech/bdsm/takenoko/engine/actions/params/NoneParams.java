package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;


/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class NoneParams implements ActionParams{

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type WHATEVER_FACILITY
     */
    @Override
    public ActionType getActionType() {
        return ActionType.NONE;
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
     * Clone method for {@code NoneParams}
     *
     * @return a copy of the {@code NoneParams}
     */
    @Override
    public Object clone() {
        return new NoneParams();
    }
}
