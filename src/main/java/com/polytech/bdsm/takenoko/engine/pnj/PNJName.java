package com.polytech.bdsm.takenoko.engine.pnj;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public enum PNJName {

    PANDA("Panda"),
    GARDENER("Gardener");

    private final String name;

    /**
     * Constructor of the enum
     *
     * @param name The name of the {@code PNJ}
     */
    PNJName(String name) {
        this.name = name;
    }

    /**
     * Getter for the name of the {@code PNJ}
     *
     * @return The name of the {@code PNJ}
     */
    public String getName() {
        return name;
    }
}
