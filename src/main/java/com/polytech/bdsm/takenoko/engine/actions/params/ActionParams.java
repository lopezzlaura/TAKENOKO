package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public interface ActionParams {

    /**
     * Getter for the {@code ActionType} of the ActionParams
     *
     * @return the {@code ActionType} of the {@code ActionParams}
     */
    ActionType getActionType();

    /**
     * Check if the params of the {@code ActionParams} are not null
     *
     * @return true if they are not null, false either
     */
    boolean paramsNotNull();

    /**
     * Equals method for {@code ActionParams}
     *
     * @param obj the object to be compared to
     *
     * @return true if obj is equals to the ActionParams on which the comparaison is made
     */
    boolean equals(Object obj);

    /**
     * Clone method for {@code ActionParams}
     *
     * @return a copy of the {@code ActionParams}
     */
    Object clone();
}
