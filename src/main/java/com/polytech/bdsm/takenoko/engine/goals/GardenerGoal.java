package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;

import java.util.List;

/**
 * @author Bureau De Sebastien Mosser
 * @version 8.0
 */
public class GardenerGoal extends Goal {

    private int size;
    private Color color;
    private int number;
    private Facility facility;

    /**
     * Normal constructor
     *
     * @param points    The number of points the goal gives when cleared
     * @param color     The color of the bamboos needed to clear this goal
     * @param number    The number of bamboos needed to clear this goal
     * @param size      The height of the bamboos needed to clear this goal
     * @param facility  The {@code Facility} for the Bamboo to be grown on
     */
    public GardenerGoal(int points, int size, Color color, int number, Facility facility) {
        super(points);
        this.size = size;
        this.color = color;
        this.number = number;
        this.facility = facility;
    }

    /**
     * Normal constructor
     *
     * @param points    The number of points the goal gives when cleared
     * @param color     The color of the bamboos needed to clear this goal
     * @param number    The number of bamboos needed to clear this goal
     * @param size      The height of the bamboos needed to clear this goal
     */
    public GardenerGoal(int points, int size, Color color, int number) {
        this(points, size, color, number, Facility.WHATEVER_FACILITY);
    }

    /**
     * Getter of the color of the bamboos needed to achieve
     * the goal
     *
     * @return The color needed
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * Getter of the number of bamboo sections needed
     * to achieve the goal
     *
     * @return The number of bamboo sections
     */
    public int getSize(){
        return this.size;
    }

    /**
     * Getter for the type of Facility
     *
     * @return The {@code Facility} for the Bamboo to be grown on
     */
    public Facility getFacility() {
        return facility;
    }
    /**
     * Getter of the Goal type
     *
     * @return The Goal type
     */
    @Override
    public GoalType getGoalType() {
        return GoalType.GARDENER;
    }

    /**
     * Overridden clone method
     *
     * @return the cloned object
     */
    @Override
    public Object clone() {
        return new GardenerGoal(this.points, this.size, this.color, this.number, this.facility);
    }

    /**
     * Check if this goal is reached by a player
     *
     * @return The player used to check the goal
     */
    @Override
    public boolean isValid(Player player) {
        List<Parcel> parcelsToCheck;
        parcelsToCheck = BoardUtils.findParcelWithBamboos(player.getBoard(), this.size);

        parcelsToCheck.removeIf(parcel -> parcel.getColor() != this.color);
        if (parcelsToCheck.size() >= this.number) {
            if (this.facility == Facility.WHATEVER_FACILITY) return true;
            if (parcelsToCheck.stream().filter(parcel -> parcel.getFacility() == facility).count() >= this.number) return true;
        }
        return false;
    }

    /**
     * Nothing happens for Gardener Goal
     *
     * @param player The player doing those actions
     */
    @Override
    public void postValidation(Player player) {
    }

    /**
     * ToString method
     *
     * @return Representation of the Object in a String format
     */
    @Override
    public String toString() {
        return super.toString() + " : Grow " + this.number + " " + this.color + " Bamboo of size " + this.size + " (" + this.facility + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GardenerGoal)) return false;
        GardenerGoal g = (GardenerGoal) obj;
        return super.equals(obj) && g.size == this.size && g.color == this.color && g.number == this.number && g.facility == this.facility;
    }

}
