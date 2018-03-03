package com.polytech.bdsm.takenoko.engine.graph;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class Coordinate {

    private final int x;
    private final int y;

    /**
     * Normal Constructor
     *
     * @param x The x value
     * @param y The y value
     */
    public Coordinate(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for the x value
     *
     * @return The x value
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for the y value
     *
     * @return The y value
     */
    public int getY() {
        return y;
    }

    /**
     * Equals method to check the x and y value
     *
     * @param obj The {@code Object} to be compare with
     * @return If the {@code Object} is a {@code Coordinate} and have the same x and y value
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Coordinate && this.x == ((Coordinate) obj).x && this.y == ((Coordinate) obj).y;
    }

    /**
     * Clone method
     *
     * @return A new instance and the {@code Coordinate} with the same x and y as the current object
     */
    @Override
    public Object clone() {
        return new Coordinate(this.x, this.y);
    }

    /**
     * ToString method
     *
     * @return Representation of the Object in a String format
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
