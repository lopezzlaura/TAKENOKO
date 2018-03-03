package com.polytech.bdsm.takenoko.engine.parcels;

import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.graph.Coordinate;
import com.polytech.bdsm.takenoko.engine.graph.GraphNode;

import java.util.Stack;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */
public abstract class Parcel extends GraphNode {

    protected static final int MAX_BAMBOO = 4;
    protected Stack<Bamboo> bambooStack = new Stack<>();

    /**
     * Default Constructor
     */
    public Parcel() {
        super(6, new Coordinate(0, 0));
    }

    /**
     * Getter for {@code Color} of the {@code Parcel}
     *
     * @return The {@code Color} of the {@code Parcel}
     */
    public abstract Color getColor();

    public abstract Facility getFacility();

    /**
     * Check if a parcel is placeable at this index,
     * in other words next to two parcels
     *
     * @param index Location of the future parcel to place
     *
     * @return if you can place a parcel next to this parcel
     */
    public boolean isPlaceable(int index) {
        return this.neighbours[index] == null && (this instanceof PondParcel || ((this.neighbours[(index + 1) % 6] != null || this.neighbours[(index + 5) % 6] != null)));
    }

    /**
     * Method to add parcel as a neighbour
     *
     * @param index Position relative at this node
     * @param node  Node to add
     */
    @Override
    public void addNode(int index, GraphNode node) throws Exception {
        // Check if just placed node is valid : next to two parcels
        if (this.isPlaceable(index)) super.addNode(index, node);
        else throw new Exception("Cannot add node: " + node + " at the index: " + index);
    }

    /**
     * Getter of the Bamboo Stack
     *
     * @return The Bamboo Stack
     */
    public Stack<Bamboo> getBambooStack() {
        return this.bambooStack;
    }

    /**
     * Push a Bamboo into the Bamboo Stack
     *
     * @return indicates if the process went fine or wrong
     */
    public boolean pushBamboo() {
        if (this.bambooStack.size() < MAX_BAMBOO) {
            this.bambooStack.push(new Bamboo(this.getColor()));
            return true;
        }

        return false;
    }

    /**
     * Pop a Bamboo in the Bamboo Stack
     *
     * @return Will return a Bamboo if there is one, {@code null} otherwise
     */
    public Bamboo popBamboo() {
        return this.bambooStack.size() > 0 ? this.bambooStack.pop() : null;
    }

    /**
     * Method to check if the parcel is irrigated by being next to the PondParcel
     *
     * @return If the Parcel is Irrigated by the PondParcel
     */
    public boolean isIrrigated() {
        for (int i = 0; i < 6; i++) {
            if (this.getNeighbours()[i] instanceof PondParcel) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method to check if the current Parcel is equals to an Object
     *
     * @param obj The reference object with which to compare.
     *
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Parcel && this.coordinate.equals(((Parcel) obj).coordinate) && this.getColor() == ((Parcel) obj).getColor();
    }

    /**
     * ToString method
     *
     * @return Representation of the Object in a String format
     */
    public String toString() {
        return this.getColor().toString() + " at " + this.coordinate;
    }
}
