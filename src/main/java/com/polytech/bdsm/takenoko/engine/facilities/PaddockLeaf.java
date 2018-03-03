package com.polytech.bdsm.takenoko.engine.facilities;

import com.polytech.bdsm.takenoko.engine.graph.Coordinate;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class PaddockLeaf extends FacilityDecorator {

    /**
     * Normal constructor of the decorator
     *
     * @param parcel The parcel to decorate
     */
    public PaddockLeaf(Parcel parcel) {
        super(parcel);
    }


    /**
     * Getter for the Facility on the Parcel
     *
     * @return the facility on the parcel
     */
    @Override
    public Facility getFacility() {
        return Facility.PADDOCK_FACILITY;
    }

    /**
     * No popBamboo on this kind of Facility
     *
     * @return Null
     */
    @Override
    public Bamboo popBamboo() {
        return null;
    }

    /**
     * Clone method for {@code PaddockLeaf}
     *
     * @return a copy of the {@code PaddockLeaf}
     */
    @Override
    public Object clone() {
        PaddockLeaf copy = new PaddockLeaf((Parcel) this.parcel.clone());
        copy.degree = this.degree;
        copy.coordinate = (Coordinate) this.coordinate.clone();
        return copy;
    }

    /**
     * Equals method for the {@code PaddockLeaf}
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
        return "PADDOCK" + this.coordinate;
    }
}
