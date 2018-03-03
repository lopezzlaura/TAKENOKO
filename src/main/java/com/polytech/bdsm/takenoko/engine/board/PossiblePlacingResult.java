package com.polytech.bdsm.takenoko.engine.board;

import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class PossiblePlacingResult {

    private HashMap<Parcel, List<Integer>> result;

    /**
     * Normal constructor
     *
     * @param result an HashMap containing the parcels that can be used for placing other parcels, associated with the possible indexes
     */
    public PossiblePlacingResult(HashMap<Parcel, List<Integer>> result) {
        this.result = result;
    }

    /**
     * Get all the possible parcels to place from
     *
     * @return A list of all the parcels
     */
    public List<Parcel> getAllParcels() {
        return new ArrayList<>(result.keySet());
    }

    /**
     * Get the number of indexes calculated in this result
     *
     * @return The number of indexes of this results
     */
    public int getIndexesCount() {
        int count = 0;
        for (List<Integer> list : result.values()) {
            count += list.size();
        }

        return count;
    }

    /**
     * Get the possible indexes to place a parcel, of a from parcel passed in parameter
     *
     * @param parcel A parcel present in the result
     *
     * @return A list of the possible indexes to place a new Parcel
     */
    public List<Integer> getParcelPossibleIndexes(Parcel parcel) {
        if (result.containsKey(parcel)) {
            return result.get(parcel);
        }

        throw new IndexOutOfBoundsException("The parcel: " + parcel + " is not present in this PossiblePlacingResult");
    }

}
