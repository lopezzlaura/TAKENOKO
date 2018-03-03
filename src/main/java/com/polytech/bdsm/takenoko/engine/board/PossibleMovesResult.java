package com.polytech.bdsm.takenoko.engine.board;

import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class PossibleMovesResult {

    private HashMap<Integer, List<Parcel>> result;

    /**
     * Normal constructor
     *
     * @param result an HashMap containing direction indexes associated to a list of the parcels in that direction
     */
    public PossibleMovesResult(HashMap<Integer, List<Parcel>> result) {
        this.result = result;
    }

    /**
     * Getter for the number of direction on which a PNJ can move
     *
     * @return the number of direction
     */
    public int getDirectionsCount() {
        return result.size();
    }

    /**
     * Get the possible directions of this result
     *
     * @return The possible directions of this result
     */
    public List<Integer> getDirections() {
        return new ArrayList<>(result.keySet());
    }

    /**
     * Get all the parcels in this result
     *
     * @return A list of all the parcels of this result
     */
    public List<Parcel> getAllParcels() {
        List<Parcel> totalParcels = new ArrayList<>();

        // Add all parcels of the hashMap in the result
        for (List<Parcel> parcelList : this.result.values()) {
            totalParcels.addAll(parcelList);
        }

        return totalParcels;
    }

    /**
     * Get all parcels of a direction
     *
     * @param direction The direction index to search
     *
     * @return A list of the parcels in the specified direction
     */
    public List<Parcel> getParcelsInDirection(int direction) {
        if (result.containsKey(direction)) {
            return result.get(direction);
        }

        throw new IndexOutOfBoundsException("Direction " + direction + " is not allowed in this PossibleMovesResult");
    }
}
