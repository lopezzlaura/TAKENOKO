package com.polytech.bdsm.takenoko.engine.board;

import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

import java.util.List;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class PossibleIrrigationResult {

    private List<Irrigation> result;

    /**
     * Normal constructor
     *
     * @param result The {@code List} of irrigation that can be placed
     */
    public PossibleIrrigationResult(List<Irrigation> result) {
        this.result = result;
    }

    /**
     * Getter for the number of available irrigation to place
     *
     * @return The number of available irrigation to place
     */
    public int getIrrigationCount() {
        return result.size();
    }

    /**
     * Get always the first irrigation in stock and pop it
     *
     * @return The first irrigation that can be placed
     */
    public Irrigation getHeadIrrigation() {
        return !result.isEmpty() ? result.get(0) : null;
    }

    /**
     * Getter for all the irrigation of the board
     *
     * @return a List of irrigation
     */
    public List<Irrigation> getAllIrrigations() {
        return this.result;
    }

    /**
     * Check if an irrigation contains a given parcel
     *
     * @param parcel The specified Parcel to check in all irrigations of this result
     *
     * @return True if the parcel is in an irrigation of this result, false otherwise
     */
    public boolean containsParcel(Parcel parcel) {
        for (Irrigation irrigation : result) {
            if (irrigation.getParcelToTheLeft().equals(parcel) || irrigation.getParcelToTheRight().equals(parcel)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method to get the irrigation associated to a Parcel
     *
     * @param parcel the parcel from which the irrigation is wanted
     *
     * @return the irrigation wanted
     */
    public Irrigation getParcelContainedInResult(Parcel parcel) {
        for (Irrigation irrigation : result) {
            if (irrigation.getParcelToTheLeft().equals(parcel) || irrigation.getParcelToTheRight().equals(parcel)) {
                return irrigation;
            }
        }

        return null;
    }

    /**
     * Method to check if one Irrigation is equals to one of the possible Irrigation to place
     *
     * @param i The Irrigation to check
     * @return If the Irrigation given can be found in the possible Irrigation to place
     */
    public boolean doesContainIrrigation(Irrigation i) {
        return result.contains(i);
    }
}
