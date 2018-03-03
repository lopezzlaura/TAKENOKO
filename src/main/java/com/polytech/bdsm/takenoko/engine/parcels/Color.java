package com.polytech.bdsm.takenoko.engine.parcels;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public enum Color {
    PINK("Pink"),
    YELLOW("Yellow"),
    GREEN("Green");

    private final String name;

    Color(String name) {
        this.name = name;
    }

    /**
     * Getter for the color's name
     * @return the color's name
     */
    public String getName() {
        return name;
    }

    /**
     * toString method
     *
     * @return a representation of the object in a String format
     */
    @Override
    public String toString() {
        return this.name;
    }
}
