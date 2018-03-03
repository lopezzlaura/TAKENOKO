package com.polytech.bdsm.takenoko.engine.pnj;

import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class Panda extends PNJ {

    /**
     * Normal constructor
     *
     * @param parcel The current parcel of the Panda (should by the PondParcel by default)
     */
    public Panda(Parcel parcel) {
        this.position = parcel;
    }

    /**
     * Getter for the name of the {@code Panda}
     *
     * @return The name of the {@code Panda}
     */
    @Override
    public PNJName getName() {
        return PNJName.PANDA;
    }

    /**
     * Method to move the Panda from the current parcel to another
     *
     * @param parcel The Parcel to move to, it will also remove a Bamboo is there is one in that Parcel
     * @return Will return a Bamboo if there is one, null otherwise
     */
    @Override
    public Bamboo move(Parcel parcel) {
        this.position = parcel;
        return parcel.popBamboo();
    }

    /**
     * Clone method for {@code Panda}
     *
     * @return a copy of the {@code Panda}
     */
    @Override
    public Object clone() {
        return new Panda((Parcel) this.position.clone());
    }

}
