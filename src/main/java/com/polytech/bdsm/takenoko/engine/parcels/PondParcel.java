package com.polytech.bdsm.takenoko.engine.parcels;


import com.polytech.bdsm.takenoko.engine.facilities.Facility;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 * @see Parcel
 */
public class PondParcel extends Parcel {

    /**
     * Default constructor that call the {@link Parcel} default constructor
     *
     * @see Parcel#Parcel()
     */
    public PondParcel() {
        super();
    }

    /**
     * Method to push a bamboo on a Parcel, except that no bamboo can grow on the Pond Parcel
     * @return false
     */
    @Override
    public boolean pushBamboo() {
        return false; // Don't push a bamboo on the PondParcel
    }

    /**
     * Method to get if the parcel is irrigated, except that the Pond Parcel is always irrigated
     * @return
     */
    @Override
    public boolean isIrrigated() {
        return true; // Pond Parcel is always irrigated
    }


    /**
     * Equals method for the {@code PondParcel}
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof PondParcel && super.equals(obj);
    }

    /**
     * Clone method
     *
     * @return A new instance of the {@code GraphNode}
     */
    @Override
    public Object clone() {
        return super.clone();
    }


    /**
     * Getter for the {@code Color} of the Parcel
     *
     * @return the Color of the Parcel
     */
    @Override
    public Color getColor() {
        return null;
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
     * toString method
     * @return a representation of the object in a String format
     */
    @Override
    public String toString() {
        return "PondParcel at " + this.coordinate;
    }
}
