package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.pnj.PNJ;
import com.polytech.bdsm.takenoko.engine.pnj.Panda;


/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class MovePandaParams implements ActionParams {

    private Parcel parcel;
    private PNJ pnj;

    /**
     * Method to get the parcel where the player wants to move
     * his pnj
     *
     * @return the given parcel
     */
    public Parcel getParcel() {
        return parcel;
    }

    /**
     * Method to set the destination parcel of the pnj
     *
     * @param toMove The parcel to move the pnj
     */
    public void setParcel(Parcel toMove) {
        this.parcel = toMove;
    }

    /**
     * Method to get the pnj that is going to be moved by the player
     *
     * @return the pnj
     */
    public PNJ getPNJ() {
        return pnj;
    }

    /**
     * Method to set the pnj to move
     *
     * @param pnj The pnj to move
     */
    public void setPNJ(PNJ pnj) {
        this.pnj = pnj;
    }

    /**
     * Method to get the ActionType of the current object
     *
     * @return the current action type MOVE
     */
    @Override
    public ActionType getActionType() {
        return ActionType.MOVE_PANDA;
    }

    /**
     * Method to check that the params of the <code>Action</code>
     * aren't null
     *
     * @return true if the params aren't null, false otherwise
     */
    @Override
    public boolean paramsNotNull() {
        return parcel != null && pnj != null && pnj instanceof Panda;
    }

    /**
     * Clone method for {@code MovePandaParams}
     *
     * @return a copy of the {@code MovePandaParams}
     */
    @Override
    public Object clone() {
        MovePandaParams copy = new MovePandaParams();
        copy.setParcel(this.parcel);
        copy.setPNJ(this.pnj);
        return copy;
    }
}
