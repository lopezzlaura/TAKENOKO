package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class PlaceParcelParams implements ActionParams {

    private Parcel toPlace, from;
    private int indexToPlace = -1;

    /**
     * Method to get the parcel from where the placement is going
     * to be made
     *
     * @return the parcel from where the placement is made
     */
    public Parcel getFrom() {
        return from;
    }

    /**
     * Method to get the parcel the player will place on the board
     *
     * @return the parcel to place
     */
    public Parcel getToPlace() {
        return toPlace;
    }

    /**
     * Method to get the index, of the from parcel, where the new parcel will
     * be placed
     *
     * @return the index where the new parcel is placed
     */
    public int getIndexToPlace() {
        return indexToPlace;
    }

    /**
     * Method to set the parcel that is going to be placed
     *
     * @param parcel The parcel placed
     */
    public void setToPlace(Parcel parcel) {
        this.toPlace = parcel;
    }

    /**
     * Method to set the parcel from where the new parcel is going to be placed
     *
     * @param parcel The parcel from where the new parcel is going to be placed
     *
     */
    public void setFrom(Parcel parcel) {
        this.from = parcel;
    }

    /**
     * Method to set the index to place the parcel
     *
     * @param index The index
     */
    public void setIndexToPlace(int index) {
        this.indexToPlace = index;
    }

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type PLACE_PARCEL
     */
    @Override
    public ActionType getActionType() {
        return ActionType.PLACE_PARCEL;
    }

    /**
     * Method to check that the params of the <code>Action</code>
     * aren't null
     *
     * @return true if the params aren't null, false otherwise
     */
    @Override
    public boolean paramsNotNull() {
        return toPlace != null && from != null && (indexToPlace >= 0 && indexToPlace <6);
    }

    /**
     * Clone method for {@code PlaceParcelParams}
     *
     * @return a copy of the {@code PlaceParcelParams}
     */
    @Override
    public Object clone() {
        PlaceParcelParams copy = new PlaceParcelParams();
        copy.setIndexToPlace(this.indexToPlace);
        copy.setFrom(this.from);
        copy.setToPlace(this.toPlace);
        return copy;
    }
}
