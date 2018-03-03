package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public final class PossibleGoals {

    /**
     * Private Constructor to block instantiation
     */
    private PossibleGoals() {}

    /**
     * Get all the possible goals in a list
     * - 15 Panda goals
     * - 15 Gardener goals
     * - 15 Parcel goals
     * - 1 emperor goal
     *
     * @return A list of all the possible goals in the game, classified by <code>GoalType</code>
     */
    public static HashMap<GoalType, List<Goal>> getPossibleGoals() {
        HashMap<GoalType, List<Goal>> goals = new HashMap<>();

        /* Panda goals */
        goals.put(GoalType.PANDA, new ArrayList<>());
        List<Goal> pandaList = goals.get(GoalType.PANDA);

        List<Bamboo> bambooList = new ArrayList<>();

        // 2 Green - 2 points (x5)
        bambooList.add(new Bamboo(Color.GREEN));
        bambooList.add(new Bamboo(Color.GREEN));
        IntStream.range(0, 5).forEach(i -> pandaList.add(new PandaGoal(2, new ArrayList<>(bambooList))));
        // 2 Yellow - 4 pts (x4)
        bambooList.clear();
        bambooList.add(new Bamboo(Color.YELLOW));
        bambooList.add(new Bamboo(Color.YELLOW));
        IntStream.range(0, 4).forEach(i -> pandaList.add(new PandaGoal(4, new ArrayList<>(bambooList))));
        // 2 Pink - 5 pts (x3)
        bambooList.clear();
        bambooList.add(new Bamboo(Color.PINK));
        bambooList.add(new Bamboo(Color.PINK));
        IntStream.range(0, 3).forEach(i -> pandaList.add(new PandaGoal(5, new ArrayList<>(bambooList))));
        // 1 Green, 1 Yellow, 1 Pink - 2 pts (x3)
        bambooList.clear();
        bambooList.add(new Bamboo(Color.PINK));
        bambooList.add(new Bamboo(Color.GREEN));
        bambooList.add(new Bamboo(Color.YELLOW));
        IntStream.range(0, 3).forEach(i -> pandaList.add(new PandaGoal(2, new ArrayList<>(bambooList))));

        /* Gardener goals */
        goals.put(GoalType.GARDENER, new ArrayList<>());
        List<Goal> gardenerList = goals.get(GoalType.GARDENER);

        // 4 Tall on Green w/ Fertilizer - 3 pts
        gardenerList.add(new GardenerGoal(3, 4, Color.GREEN, 1, Facility.FERTILIZER_FACILITY));
        // 4 Tall on Green w/ Enclosure - 4 pts
        gardenerList.add(new GardenerGoal(4, 4, Color.GREEN, 1, Facility.PADDOCK_FACILITY));
        // 4 Tall on Green w/ Watershed - 4 pts
        gardenerList.add(new GardenerGoal(4, 4, Color.GREEN, 1, Facility.POND_FACILITY));
        // 4 Tall on Green w/ NO Improvements - 5 pts
        gardenerList.add(new GardenerGoal(5, 4, Color.GREEN, 1, Facility.NO_FACILITY));
        // 3 Tall on 4 Greens - 8 pts
        gardenerList.add(new GardenerGoal(8, 3, Color.GREEN, 4, Facility.WHATEVER_FACILITY));
        // 4 Tall on Yellow w/ Fertilizer - 4 pts
        gardenerList.add(new GardenerGoal(4, 4, Color.YELLOW, 1, Facility.FERTILIZER_FACILITY));
        // 4 Tall on Yellow w/ Enclosure - 5 pts
        gardenerList.add(new GardenerGoal(5, 4, Color.YELLOW, 1, Facility.PADDOCK_FACILITY));
        // 4 Tall on Yellow w/ Watershed - 5 pts
        gardenerList.add(new GardenerGoal(5, 4, Color.YELLOW, 1, Facility.POND_FACILITY));
        // 4 Tall on Yellow w/ NO Improvements - 6 pts
        gardenerList.add(new GardenerGoal(6, 4, Color.YELLOW, 1, Facility.NO_FACILITY));
        // 3 Tall on 3 Yellows - 7 pts
        gardenerList.add(new GardenerGoal(7, 3, Color.YELLOW, 3, Facility.WHATEVER_FACILITY));
        // 4 Tall on Pink w/ Fertilizer - 5 pts
        gardenerList.add(new GardenerGoal(5, 4, Color.PINK, 1, Facility.FERTILIZER_FACILITY));
        // 4 Tall on Pink w/ Enclosure - 6 pts
        gardenerList.add(new GardenerGoal(6, 4, Color.PINK, 1, Facility.PADDOCK_FACILITY));
        // 4 Tall on Pink w/ Watershed - 6 pts
        gardenerList.add(new GardenerGoal(6, 4, Color.PINK, 1, Facility.POND_FACILITY));
        // 4 Tall on Pink w/ NO Improvements - 7 pts
        gardenerList.add(new GardenerGoal(7, 4, Color.PINK, 1, Facility.NO_FACILITY));
        // 3 Tall on 2 Pinks - 6 pts
        gardenerList.add(new GardenerGoal(6, 3, Color.PINK, 2, Facility.WHATEVER_FACILITY));

        /* Parcel Goals */
        goals.put(GoalType.PARCEL, new ArrayList<>());
        List<Goal> parcelsList = goals.get(GoalType.PARCEL);

        parcelsList.add(new ParcelGoal(2, ParcelGoalPattern.TRIANGLE, Color.GREEN));
        parcelsList.add(new ParcelGoal(3, ParcelGoalPattern.DIAMOND, Color.GREEN));
        parcelsList.add(new ParcelGoal(5, ParcelGoalPattern.DIAMOND, Color.YELLOW, Color.PINK));
        parcelsList.add(new ParcelGoal(4, ParcelGoalPattern.DIAMOND, Color.GREEN, Color.PINK));
        parcelsList.add(new ParcelGoal(3, ParcelGoalPattern.DIAMOND, Color.GREEN, Color.YELLOW));
        parcelsList.add(new ParcelGoal(3, ParcelGoalPattern.LINE, Color.YELLOW));
        parcelsList.add(new ParcelGoal(4, ParcelGoalPattern.DIAMOND, Color.YELLOW));
        parcelsList.add(new ParcelGoal(3, ParcelGoalPattern.SEMILINE, Color.YELLOW));
        parcelsList.add(new ParcelGoal(2, ParcelGoalPattern.LINE, Color.GREEN));
        parcelsList.add(new ParcelGoal(2, ParcelGoalPattern.SEMILINE, Color.GREEN));
        parcelsList.add(new ParcelGoal(4, ParcelGoalPattern.SEMILINE, Color.PINK));
        parcelsList.add(new ParcelGoal(4, ParcelGoalPattern.TRIANGLE, Color.PINK));
        parcelsList.add(new ParcelGoal(5, ParcelGoalPattern.DIAMOND, Color.PINK));
        parcelsList.add(new ParcelGoal(4, ParcelGoalPattern.LINE, Color.PINK));
        parcelsList.add(new ParcelGoal(3, ParcelGoalPattern.TRIANGLE, Color.YELLOW));

        /*Emperor*/

        goals.put(GoalType.EMPEROR, new ArrayList<>());
        List<Goal> emperor = goals.get(GoalType.EMPEROR);
        emperor.add(new EmperorGoal());

        return goals;
    }
}
