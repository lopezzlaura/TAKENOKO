package com.polytech.bdsm.takenoko.engine.board;

import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class Irrigation {

    private Parcel parcelToTheLeft, parcelToTheRight;

    /**
     * Normal Constructor
     *
     * @param parcelToTheLeft  The Parcel on the left of the Irrigation
     * @param parcelToTheRight The Parcel on the right of the Irrigation
     */
    public Irrigation(Parcel parcelToTheLeft, Parcel parcelToTheRight) {
        this.parcelToTheLeft = parcelToTheLeft;
        this.parcelToTheRight = parcelToTheRight;
    }

    /**
     * Getter for the parcel to the left of the Irrigation
     *
     * @return The Parcel on the left of the Irrigation
     */
    public Parcel getParcelToTheLeft() {
        return parcelToTheLeft;
    }

    /**
     * Getter for the parcel to the right of the Irrigation
     *
     * @return The Parcel on the right of the Irrigation
     */
    public Parcel getParcelToTheRight() {
        return parcelToTheRight;
    }

    /**
     * Equals method that compare the two {@code Parcel} between the irrigation
     *
     * @param o The object to be compared with
     *
     * @return If the two parcel of the irrigation are the same as the one in the object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Irrigation i = (Irrigation) o;

        return i.parcelToTheLeft.equals(this.parcelToTheLeft) && i.parcelToTheRight.equals(this.parcelToTheRight)
                || i.parcelToTheLeft.equals(this.parcelToTheRight) && i.parcelToTheRight.equals(this.parcelToTheLeft);
    }

    /**
     * toString method
     *
     * @return a representation of the object in a String format
     */
    @Override
    public String toString() {
        return this.parcelToTheLeft.getCoordinate() + " | " + this.parcelToTheRight.getCoordinate();
    }
}
