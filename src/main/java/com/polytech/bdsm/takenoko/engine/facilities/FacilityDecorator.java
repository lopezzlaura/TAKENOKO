package com.polytech.bdsm.takenoko.engine.facilities;

import com.polytech.bdsm.takenoko.engine.graph.Coordinate;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;

import java.util.Stack;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public abstract class FacilityDecorator extends Parcel {

    protected Parcel parcel;

    /**
     * Normal constructor of the decorator
     *
     * @param parcel The parcel to decorate
     */
    public FacilityDecorator(Parcel parcel) {
        this.parcel = parcel;

        this.bambooStack = new Stack<>();
        this.degree = parcel.getDegree();
        this.coordinate = (Coordinate) parcel.getCoordinate().clone();
        this.visited = parcel.isVisited();

        this.neighbours = parcel.getNeighbours();

        //Go through this neighbours references to add this reference into
        //their neighbour's array
        for (int i = 0; i < degree; i++) {
            if (this.neighbours[i] != null) {
                this.neighbours[i].getNeighbours()[(i + (degree / 2)) % degree] = this;
            }
        }


    }

    /**
     * Getter for the color of the Parcel on which is the facility
     * @return the color of the parcel
     */
    @Override
    public Color getColor() {
        return this.parcel.getColor();
    }



    /**
     * Clone method for {@code FacilityDecorator}
     *
     * @return a copy of the {@code FacilityDecorator}
     */
    @Override
    public abstract Object clone();

    /**
     * Equals method for the {@code FacilityDecorator}
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
