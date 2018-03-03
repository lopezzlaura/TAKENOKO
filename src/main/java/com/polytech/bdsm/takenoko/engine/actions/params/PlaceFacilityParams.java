package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class PlaceFacilityParams implements ActionParams {

    private Parcel parcel;
    private Facility facility;

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type WHATEVER_FACILITY
     */
    @Override
    public ActionType getActionType() {
        return ActionType.PLACE_FACILITY;
    }

    /**
     * Check if the params of the {@code PlaceFacilityParams} are not null
     * meaning there is a facility and a parcel on which the facility will be placed
     *
     * @return true if they are not null, false either
     */
    @Override
    public boolean paramsNotNull() {
        return parcel != null && facility != null;
    }


    /**
     * Getter for the parcel on which a facility is to be placed
     *
     * @return the parcel on which a facility is to be placed
     */
    public Parcel getParcel() {
        return parcel;
    }

    /**
     * Setter for the parcel on which a facility is to be placed
     *
     * @param parcel the parcel on which a facility is to placed
     */
    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    /**
     * Getter for the facility to be placed
     *
     * @return the {@code Facility} to be placed
     */
    public Facility getFacility() {
        return facility;
    }

    /**
     * Setter for the facility the place
     *
     * @param facility the {@code Facility} to place
     */
    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    /**
     * Clone method for {@code PlaceFacilityParams}
     *
     * @return a copy of the {@code PlaceFacilityParams}
     */
    @Override
    public Object clone() {
        PlaceFacilityParams copy = new PlaceFacilityParams();
        copy.setFacility(this.facility);
        copy.setParcel(this.parcel);
        return copy;
    }
}
