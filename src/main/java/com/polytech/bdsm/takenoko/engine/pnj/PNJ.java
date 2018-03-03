package com.polytech.bdsm.takenoko.engine.pnj;

import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public abstract class PNJ {

    protected Parcel position;

    /**
     * Method to move the Gardener from the current parcel to another
     *
     * @param parcel The Parcel to move to
     * @return A <code>Bamboo</code> if the PNJ is a Panda and if there is a bamboo
     */
    public abstract Bamboo move(Parcel parcel);

    /**
     * Getter of the name of the {@code PNJ}
     *
     * @return The name of the {@code PNJ}
     */
    public abstract PNJName getName();

    /**
     * Clone method for {@code PNJ}
     *
     * @return a copy of the {@code PNJ}
     */
    @Override
    public abstract Object clone();

    /**
     * Getter of the position of this PNJ
     *
     * @return Parcel position instance
     */
    public Parcel getPosition() {
        return position;
    }

}
