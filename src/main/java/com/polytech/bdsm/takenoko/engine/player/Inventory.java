package com.polytech.bdsm.takenoko.engine.player;

import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 * @see Bamboo
 * @see Goal
 */
public class Inventory {

    // List of the bamboo collected through the Panda
    private List<Bamboo> bamboos = new ArrayList<>();

    // The goal that are validated by the Player
    private List<Goal> goals = new ArrayList<>();

    private int irrigations = 0;

    private List<Facility> facilities = new ArrayList<>();

    /**
     * Add a bamboo to the inventory
     *
     * @param bamboo The Bamboo to be add
     */
    public void addBamboo(Bamboo bamboo) {
        this.bamboos.add(bamboo);
    }

    /**
     * Getter for the list of eaten bamboos in the Inventory
     *
     * @return The list of eaten bamboos
     */
    public List<Bamboo> getBamboos() {
        return this.bamboos;
    }

    /**
     * Remove one bamboo of the inventory
     *
     * @param bamboo The Bamboo to be remove
     */
    public void removeBamboo(Bamboo bamboo) {
        this.bamboos.remove(bamboo);
    }

    /**
     * Remove multiple bamboos in the inventory
     *
     * @param nb_bamboos The number of bamboos to remove
     */
    public void removeBamboos(int nb_bamboos) {
        for (int i = 0; i < nb_bamboos; i++) {
            if (i < this.bamboos.size()) {
                this.bamboos.remove(i);
            }
        }
    }

    /**
     * Getter of the irrigations in this Inventory
     *
     * @return The number of irrigations in this Inventory
     */
    public int getIrrigations() {
        return this.irrigations;
    }

    /**
     * Add one irrigation in this inventory
     */
    public void addIrrigation() {
        this.irrigations++;
    }

    /**
     * Remove one irrigation if it's possible
     */
    public boolean removeIrrigation() {
        if (this.irrigations > 0) {
            this.irrigations--;
            return true;
        }

        return false;
    }

    /**
     * Add an accomplished goal to the inventory
     *
     * @param goal The Goal to be add
     */
    public void addGoal(Goal goal) {
        this.goals.add(goal);
    }

    /**
     * Remove an accomplished Goal from the inventory
     *
     * @param goal The Goal to be remove
     */
    public void removeGoal(Goal goal) {
        this.goals.remove(goal);
    }

    /**
     * Add a Facility to the Inventory
     *
     * @param facility The Facility to add
     */
    public void addFacility(Facility facility) {
        this.facilities.add(facility);
    }

    /**
     * Remove the Facility of the Inventory
     *
     * @param facility The Facility to be remove
     */
    public void removeFacility(Facility facility) {
        this.facilities.remove(facility);
    }

    /**
     * Getter of the number of bamboo collected
     *
     * @return The number of bamboo collected
     */
    public int getBamboosCount() {
        return this.bamboos.size();
    }


    /**
     * Get the number of bamboos of the given color from the inventory
     *
     * @param color the color of the bamboos the number is wanted
     * @return the number of bamboos
     */
    public int getBamboosCount(Color color) {
        int count = 0;
        for (Bamboo b : this.bamboos) {
            if (b.getColor() == color) {
                count++;
            }
        }
        return count;
    }

    /**
     * Method to check if one {@code Bamboo} is contain in the {@code Inventory}
     *
     * @param bamboo The {@code Bamboo} that might be contain in the {@code Inventory}
     * @return If the {@code Bamboo} is in the Inventory
     */
    public boolean containBamboo(Bamboo bamboo) {
        return this.bamboos.contains(bamboo);
    }

    /**
     * Getter of the number of accomplished Goals
     *
     * @return The number of accomplished Goals
     */
    public int getAccomplishedGoal() {
        return this.goals.size();
    }

    /**
     * Clear the inventory
     */
    public void clear() {
        this.goals.clear();
        this.bamboos.clear();
        this.irrigations = 0;
    }

    /**
     * Clone method
     *
     * @return a new Instance of the Inventory
     */
    @Override
    public Object clone() {
        Inventory cpy = new Inventory();
        cpy.bamboos = new ArrayList<>(this.bamboos);
        cpy.irrigations = this.irrigations;
        cpy.goals = new ArrayList<>(this.goals);

        return cpy;
    }

    /**
     * Getter for the number of point of accomplished Goal of a certain GoalType
     *
     * @param goalType The GoalType to total score of accomplished goal from
     * @return The score for the total of accomplished goal of the GoalType
     */
    public int getAccomplishedGoalPoint(GoalType goalType) {
        int points = 0;
        for (Goal goal : goals) {
            if (goal.getGoalType() == goalType) points += goal.getPoints();
        }
        return points;
    }

    /**
     * Getter for the number of accomplished Goal of a certain GoalType
     *
     * @param goalType The GoalType to count of accomplished goal from
     * @return The number of accomplished goal of the GoalType
     */
    public int getAccomplishedGoalCount(GoalType goalType) {
        int count = 0;
        for (Goal goal : this.goals) {
            if (goal.getGoalType() == goalType) count++;
        }
        return count;
    }

    /**
     * Check if this inventory contains a Facility of a given type
     *
     * @param facility The Facility type
     * @return True if this facility is in this inventory, false otherwise
     */
    public boolean containFacility(Facility facility) {
        return this.facilities.contains(facility);
    }

    /**
     * Getter for the number of facility
     *
     * @param facility The type of facility
     * @return The number of the facility type in the inventory
     */
    public int getFacility(Facility facility) {
        return (int) this.facilities.stream().filter(facility1 -> facility == facility1).count();
    }

    /**
     * Getter for all the facility in the inventory
     *
     * @return The facility in the inventory
     */
    public List<Facility> getFacilities() {
        return facilities;
    }

}