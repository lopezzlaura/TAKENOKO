package com.polytech.bdsm.takenoko.engine.facilities;

import com.polytech.bdsm.takenoko.engine.graph.Coordinate;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class PondLeaf extends FacilityDecorator {

    /**
     * Normal constructor of the decorator
     *
     * @param parcel The parcel to decorate
     */
    public PondLeaf(Parcel parcel) {
        super(parcel);
    }

    /**
     * Parcel is irrigated because of the Facility
     *
     * @return Always true
     */
    @Override
    public boolean isIrrigated() {
        return true;
    }

    /**
     * Clone method for {@code PaddockLeaf}
     *
     * @return a copy of the {@code PaddockLeaf}
     */
    @Override
    public Object clone() {
        PondLeaf copy = new PondLeaf((Parcel) this.parcel.clone());
        copy.degree = this.degree;
        copy.coordinate = (Coordinate) this.coordinate.clone();
        return copy;
    }

    /**
     * Getter for the Facility on the Parcel
     *
     * @return the facility on the parcel
     */
    @Override
    public Facility getFacility() {
        return Facility.POND_FACILITY;
    }

    /**
     * Equals method for the {@code PondLeaf}
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
     * toString method
     *
     * @return a representation of the Object in a String format
     */
    @Override
    public String toString() {
        return "POND" + this.coordinate;
    }

}
