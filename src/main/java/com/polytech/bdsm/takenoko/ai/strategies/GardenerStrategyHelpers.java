package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.GardenerGoal;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public final class GardenerStrategyHelpers {

    /**
     * Private Constructor to block instantiation
     */
    private GardenerStrategyHelpers() {
    }

    /**
     * Getter for the first {@code GardenerGoal} in player's hand
     *
     * @param player the {@code Player} from whom the goal is needed
     * @return a GardenerGoal if there is one, null either
     */
    static GardenerGoal getFirstGardenerGoal(Player player) {
        for (Goal goal : player.getGoals()) {
            if (goal.getGoalType().equals(GoalType.GARDENER)) {
                return (GardenerGoal) goal;
            }
        }
        return null;
    }

    /**
     * Check if a parcel contains the right number of bamboos of the right color
     *
     * @param board      the {@code Board} on which is the {@code Parcel}
     * @param parcel     the {@code Parcel} to check
     * @param color      the {@code Color} to check
     * @param facility   The {@code Facility} to check
     * @param bambooSize the size of the {@code Bamboo} to check
     * @return true if the parcel contains the right number of bamboos, false either
     */
    public static boolean getRightBambooParcels(Board board, Parcel parcel, Color color, Facility facility, int bambooSize) {
        if (BoardUtils.isParcelIrrigated(board, parcel)) {
            if (parcel.getBambooStack().size() == bambooSize && parcel.getColor() == color && (facility == Facility.WHATEVER_FACILITY || parcel.getFacility() == facility)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that get parcel corresponding to the gardener goal depending on the boolean isMore :
     * - If the boolean is true, it will return parcels that have more bamboo than the goal number of bamboos
     * - If the boolean is false, it will return parcels that have less bamboo than the goal number of bamboos
     *
     * @param parcels The possible parcels where the Gardener can move to
     * @param goal    The gardener goal
     * @param isMore  The boolean to check if the method will get parcels that have more or less bamboos than the goal bamboos number
     * @param board   The current board
     * @return The list of parcels that have less or more bamboo
     */
    public static List<Parcel> getLessOrMoreBambooParcel(List<Parcel> parcels, GardenerGoal goal, boolean isMore, Board board) {
        List<Parcel> parcelsFound = new ArrayList<>();

        for (Parcel parcel : parcels) {
            if (BoardUtils.isParcelIrrigated(board, parcel) && parcel.getColor() == goal.getColor() && (goal.getFacility() == Facility.WHATEVER_FACILITY || parcel.getFacility() == goal.getFacility())) {
                if (isMore && parcel.getBambooStack().size() > 0 && parcel.getBambooStack().size() > goal.getSize() + 1) {
                    parcelsFound.add(parcel);
                } else if (!isMore && parcel.getBambooStack().size() > 0 && parcel.getBambooStack().size() < goal.getSize() - 1) {
                    parcelsFound.add(parcel);
                }
            }
        }
        return parcelsFound;
    }

}
