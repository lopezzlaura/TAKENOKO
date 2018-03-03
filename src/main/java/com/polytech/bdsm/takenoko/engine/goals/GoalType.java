package com.polytech.bdsm.takenoko.engine.goals;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public enum GoalType {

    PANDA("Panda"),
    GARDENER("Gardener"),
    PARCEL("Parcel"),
    EMPEROR("Emperor");

    private final String name;

    /**
     * Normal constructor
     * @param name
     */
    GoalType(String name) {
        this.name = name;
    }

    /**
     * Get the name of the GoalType
     * @return the name of the GoalType
     */
    public String getName() {
        return name;
    }
}

