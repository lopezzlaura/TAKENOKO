package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class DrawIrrigationParams implements ActionParams {

    @Override
    public ActionType getActionType() {
        return ActionType.DRAW_IRRIGATION;
    }

    /**
     * Check if the params of the {@code DrawIrrigationParams} are not null
     *
     * @return always true because there are no params
     */
    @Override
    public boolean paramsNotNull() {
        return true;
    }

    /**
     * Clone method for {@code DrawIrrigationParams}
     *
     * @return a copy of the {@code DrawIrrigationParams}
     */
    @Override
    public Object clone() {
        return new DrawIrrigationParams();
    }
}
