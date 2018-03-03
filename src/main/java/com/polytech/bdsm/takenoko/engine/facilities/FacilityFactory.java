package com.polytech.bdsm.takenoko.engine.facilities;

import java.util.List;
import java.util.Optional;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class FacilityFactory {

    private List<Facility> possibleFacilities;

    /**
     * Normal Constructor
     *
     * @param possibleFacilities The possible Facility to draw from the facility deck
     */
    public FacilityFactory(List<Facility> possibleFacilities) {
        this.possibleFacilities = possibleFacilities;
    }

    /**
     * Draw a Facility from the possible Facilities
     *
     * @param facility The desired Facility
     *
     * @return A empty Optional and one with the desired Facility inside
     */
    public Optional<Facility> drawFacility(Facility facility) {
        if (!(canDrawFacility(facility))) return Optional.empty();
        possibleFacilities.remove(facility);
        return Optional.of(facility);
    }

    /**
     * Method to check if there is a Facility of a given type that can draw
     *
     * @param facility The Facility type
     *
     * @return true if a Facility can draw from the possible Facility, false either
     */
    public boolean canDrawFacility(Facility facility) {
        return this.possibleFacilities.contains(facility);
    }

    /**
     * Check if there is no more Facilities
     *
     * @return true if there is no more Facilities, false either
     */
    public boolean isEmpty() {
        return this.possibleFacilities.isEmpty();
    }
}