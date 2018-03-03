package com.polytech.bdsm.takenoko.engine.actions.params.weather;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;

import javax.annotation.Nullable;


/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class CloudsActionParams implements ActionParams {

    private Facility facility;

    @Nullable
    private ActionParams otherParams;

    /**
     * Getter for the facility to draw
     *
     * @return The facility to draw
     */
    public Facility getFacility() {
        return facility;
    }

    /**
     * Setter for the facility the player is going to draw
     *
     * @param facility The type of {@code Facility} to draw
     */
    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type CLOUD_ACTION
     */
    @Override
    public ActionType getActionType() {
        return ActionType.CLOUDS_ACTION;
    }

    /**
     * Getter of the other possible params
     *
     * @return The other params if it's defined
     */
    @Nullable
    public ActionParams getOtherParams() {
        return otherParams;
    }

    /**
     * Setter of an other params if there are no more facilities to draw
     *
     * @param otherParams The other params to set
     */
    public void setOtherParams(@Nullable ActionParams otherParams) {
        this.otherParams = otherParams;
    }

    /**
     * Method to check that the params of the {@code Action}
     * aren't null
     *
     * @return true if the params aren't null, false otherwise
     */
    @Override
    public boolean paramsNotNull() {
        return facility != null || otherParams != null;
    }

    /**
     * Clone method for CloudActionParams
     *
     * @return a copy of the {@code CloudActionParams} to copy
     */
    @Override
    public Object clone() {
        CloudsActionParams cpy = new CloudsActionParams();
        cpy.setFacility(this.facility);
        return new CloudsActionParams();
    }

}
