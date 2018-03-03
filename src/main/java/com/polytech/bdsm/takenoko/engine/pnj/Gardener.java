package com.polytech.bdsm.takenoko.engine.pnj;

import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class Gardener extends PNJ {

    /**
     * Normal constructor
     *
     * @param parcel The current parcel of the Gardener (should by the PondParcel by default)
     */
    public Gardener(Parcel parcel) {
        this.position = parcel;
    }

    /**
     * Getter for the name of the {@code Gardener}
     *
     * @return The name of the {@code Gardener}
     */
    @Override
    public PNJName getName() {
        return PNJName.GARDENER;
    }

    /**
     * Clone method for {@code Gardener}
     *
     * @return a copy of the {@code Gardener}
     */
    @Override
    public Object clone() {
        return new Gardener((Parcel) this.position.clone());
    }

    /**
     * Method to move the Gardener from the current parcel to another
     *
     * @param parcel The Gardener to move to
     * @return Will return null every time
     */
    @Override
    public Bamboo move(Parcel parcel) {
        this.position = parcel;
        return null;
    }

}
