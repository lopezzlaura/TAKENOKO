package com.polytech.bdsm.takenoko.engine.actions.params.weather;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class RainActionParams implements ActionParams {

    private Parcel parcel;

    /**
     * Method to get the parcel where the player wants to grow
     * a section of bamboo
     *
     * @return the parcel picked
     */
    public Parcel getParcel() {
        return parcel;
    }

    /**
     * Method to set the parcel where the bamboo will grow
     * thanks to the rain
     *
     * @param parcel   The parcel picked by the player
     */
    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type RAIN_ACTION
     */
    @Override
    public ActionType getActionType() {
        return ActionType.RAIN_ACTION;
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
     * Clone method for RainActionParams
     *
     * @return a copy of the {@code RainActionParams} to copy
     */
    @Override
    public Object clone() {
        RainActionParams rainActionParams = new RainActionParams();
        rainActionParams.setParcel(this.parcel);
        return rainActionParams;
    }
}
