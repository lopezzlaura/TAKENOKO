package com.polytech.bdsm.takenoko.engine.facilities;

import com.polytech.bdsm.takenoko.engine.graph.Coordinate;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class FertilizerLeaf extends FacilityDecorator {

    /**
     * Normal constructor of the decorator
     *
     * @param parcel The parcel to decorate
     */
    public FertilizerLeaf(Parcel parcel) {
        super(parcel);
    }

    /**
     * Method to add bamboo on a Parcel
     * Check that the size does not exceed MAX_BAMBOO (4)
     * In this particular case, 2 bamboos are grown because there is a fertilizer
     *
     * @return true if the bamboo was grown, false either
     */
    @Override
    public boolean pushBamboo() {
        if (this.bambooStack.size() < MAX_BAMBOO) {
            this.bambooStack.push(new Bamboo(this.getColor()));
            if (this.bambooStack.size() < MAX_BAMBOO) this.bambooStack.push(new Bamboo(this.getColor()));
            return true;
        }

        return false;
    }

    /**
     * Clone method for {@code FertilizerLeaf}
     *
     * @return a copy of the {@code FertilizerLeaf}
     */
    @Override
    public Object clone() {
        FertilizerLeaf copy = new FertilizerLeaf((Parcel) this.parcel.clone());
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
        return Facility.FERTILIZER_FACILITY;
    }

    /**
     * Equals method for the {@code FertilizerLeaf}
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
        return "FERTILIZER" + this.coordinate;
    }


}
