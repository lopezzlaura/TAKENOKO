package com.polytech.bdsm.takenoko.engine.parcels;

import com.polytech.bdsm.takenoko.engine.player.Inventory;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 * @see Parcel
 * @see Inventory
 * @see Color
 */
public class Bamboo {

    private Color color;

    /**
     * Normal Constructor
     *
     * @param color The {@code Color} of the Bamboo
     */
    public Bamboo(Color color) {
        this.color = color;
    }

    /**
     * Default Constructor
     *
     * Set the color of the {@code Bamboo} to Green
     */
    public Bamboo() {
        this.color = Color.GREEN;
    }

    /**
     * Getter for the {@code Color} of the Bamboo
     *
     * @return the Color of the Bamboo
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Equals method that compare the color of the {@code Bamboo}
     *
     * @param o The {@code Object} to be compare with
     *
     * @return If the object is a Bamboo and if the two of them have the same {@code Color}
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Bamboo && this.color == ((Bamboo) o).color;
    }

    /**
     * ToString method
     *
     * @return Representation of the Object in a String format
     */
    @Override
    public String toString() {
        return this.color + " Bamboo";
    }
}
