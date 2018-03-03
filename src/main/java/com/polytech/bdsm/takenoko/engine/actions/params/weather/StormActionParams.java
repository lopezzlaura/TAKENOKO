package com.polytech.bdsm.takenoko.engine.actions.params.weather;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class StormActionParams implements ActionParams {

    private Parcel parcel;

    /**
     * Method to get the parcel where the player want to move
     * the pnj
     *
     * @return the parcel picked
     */
    public Parcel getParcel() {
        return parcel;
    }

    /**
     * Method to set the parcel where the player wants to
     * move the panda
     *
     * @param parcel  The parcel picked by the player
     */
    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type STORM_ACTION
     */
    @Override
    public ActionType getActionType() {
        return ActionType.STORM_ACTION;
    }

    /**
     * Method to check that the params of the <code>Action</code>
     * aren't null
     *
     * @return true if the params aren't null, false otherwise
     */
    @Override
    public boolean paramsNotNull() {
        return parcel != null;
    }

    /**
     * Clone method for StormActionParams
     *
     * @return a copy of the {@code StormActionParams} to copy
     */
    @Override
    public Object clone() {
        StormActionParams stormActionParams = new StormActionParams();
        stormActionParams.setParcel(this.parcel);
        return stormActionParams;
    }
}
