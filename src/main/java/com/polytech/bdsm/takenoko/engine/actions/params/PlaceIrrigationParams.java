package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.board.Irrigation;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class PlaceIrrigationParams implements ActionParams {

    private Irrigation irrigation;

    /**
     * Method to get the parcel where the player wants to move
     * his pnj
     *
     * @return the irrigation placed
     */
    public Irrigation getIrrigation() {
        return irrigation;
    }

    /**
     * Method to set the irrigation
     *
     * @param irrigation The irrigation placed
     */
    public void setIrrigation(Irrigation irrigation) {
        this.irrigation = irrigation;
    }

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type PLACE_IRRIGATION
     */
    @Override
    public ActionType getActionType() {
        return ActionType.PLACE_IRRIGATION;
    }

    /**
     * Method to check that the params of the <code>Action</code>
     * aren't null
     *
     * @return true if the params aren't null, false otherwise
     */
    @Override
    public boolean paramsNotNull() {
        return irrigation != null;
    }

    /**
     * Clone method for {@code PlaceIrrigationParams}
     *
     * @return a copy of the {@code PlaceIrrigationParams}
     */
    @Override
    public Object clone() {
        PlaceIrrigationParams copy = new PlaceIrrigationParams();
        copy.setIrrigation(new Irrigation(this.irrigation.getParcelToTheLeft(), this.irrigation.getParcelToTheRight()));
        return copy;
    }
}
