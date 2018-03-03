package com.polytech.bdsm.takenoko.engine.parcels;

import com.polytech.bdsm.takenoko.engine.facilities.Facility;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class GreenParcel extends Parcel {

    public Color color = Color.GREEN;

    public GreenParcel() {
        super();
    }

    /**
     * Getter for the {@code Color} of the Parcel
     *
     * @return the Color of the Parcel
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Getter for the Facility on the Parcel
     *
     * @return the facility on the parcel
     */
    @Override
    public Facility getFacility() {
        return Facility.NO_FACILITY;
    }

    /**
     * Equals method for the {@code GreenParcel}
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Clone method for {@code GreenParcel}
     *
     * @return a copy of the {@code GreenParcel}
     */
    @Override
    public Object clone() {
        return super.clone();
    }
}
