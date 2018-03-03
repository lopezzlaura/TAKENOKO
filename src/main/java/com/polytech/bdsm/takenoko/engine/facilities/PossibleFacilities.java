package com.polytech.bdsm.takenoko.engine.facilities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public final class PossibleFacilities {

    /**
     * Getter for all the possible facilities to draw
     *
     * @return a List of all the possible facilities
     */
    public static List<Facility> getPossibleFacilities() {
        List<Facility> facilities = new ArrayList<>();

        facilities.add(Facility.PADDOCK_FACILITY);
        facilities.add(Facility.PADDOCK_FACILITY);
        facilities.add(Facility.PADDOCK_FACILITY);

        facilities.add(Facility.POND_FACILITY);
        facilities.add(Facility.POND_FACILITY);
        facilities.add(Facility.POND_FACILITY);

        facilities.add(Facility.FERTILIZER_FACILITY);
        facilities.add(Facility.FERTILIZER_FACILITY);
        facilities.add(Facility.FERTILIZER_FACILITY);

        return facilities;
    }
}
