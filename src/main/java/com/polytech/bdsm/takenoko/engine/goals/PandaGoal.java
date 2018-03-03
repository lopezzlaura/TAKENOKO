package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bureau De Sebastien Mosser
 * @version 8.0
 */
public class PandaGoal extends Goal {

    private List<Bamboo> bamboosToCollect;

    public List<Bamboo> getBamboosToCollect() {
        return bamboosToCollect;
    }

    /**
     * Normal constructor
     *
     * @param points           The number of points given when cleared
     * @param bamboosToCollect the bamboos needed to clear the goal
     */
    public PandaGoal(int points, List<Bamboo> bamboosToCollect) {
        super(points);
        this.bamboosToCollect = bamboosToCollect;
    }

    /**
     * Getter of the Goal type
     *
     * @return The Goal type
     */
    @Override
    public GoalType getGoalType() {
        return GoalType.PANDA;
    }

    /**
     * Overridden clone method
     *
     * @return the cloned object
     */
    @Override
    public Object clone() {
        return new PandaGoal(this.points, this.bamboosToCollect);
    }

    /**
     * Check if this goal is reached by a player
     *
     * @return The player used to check the goal
     */
    @Override
    public boolean isValid(Player player) {
        List<Bamboo> bambooList = new ArrayList<>(player.getInventory().getBamboos());
        for (Bamboo bamboo : this.bamboosToCollect) {
            if (!bambooList.contains(bamboo)) return false;
            bambooList.remove(bamboo);
        }
        return true;
    }

    /**
     * Delete the bamboo from this goal from the player's inventory
     *
     * @param player The player doing those actions
     */
    @Override
    public void postValidation(Player player) {
        for (Bamboo bamboo : bamboosToCollect) {
            player.getInventory().getBamboos().remove(bamboo);
        }
    }

    /**
     * ToString method
     *
     * @return Representation of the Object in a String format
     */
    @Override
    public String toString() {
        int green = 0;
        int yellow = 0;
        int pink = 0;
        for (Bamboo b : bamboosToCollect) {
            switch (b.getColor()) {
                case GREEN:
                    green++;
                    break;
                case YELLOW:
                    yellow++;
                    break;
                case PINK:
                    pink++;
                    break;
            }
        }
        return super.toString() + " : " + green + " green bamboo(s), " + pink + " pink bamboo(s), " + yellow + " yellow bamboo(s)";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PandaGoal)) return false;
        PandaGoal g = (PandaGoal) obj;
        return super.equals(obj) && g.bamboosToCollect == this.bamboosToCollect;
    }
}
