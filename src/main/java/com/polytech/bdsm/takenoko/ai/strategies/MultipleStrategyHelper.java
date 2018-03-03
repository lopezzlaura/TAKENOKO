package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.engine.actions.params.PlaceParcelParams;
import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.board.PossibleMovesResult;
import com.polytech.bdsm.takenoko.engine.goals.GardenerGoal;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.goals.PandaGoal;
import com.polytech.bdsm.takenoko.engine.parcels.*;
import com.polytech.bdsm.takenoko.engine.player.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public final class MultipleStrategyHelper {

    /**
     * Private Constructor to block instantiation
     */
    private MultipleStrategyHelper() {}

    /**
     * TODO doc
     *
     * @param player
     * @return
     */
    public static GoalType chooseBestGoalType(Player player) {
        List<Goal> playerGoals = player.getGoals();

        // For each goal, calculate a priority value, and sort the goals by this value
        TreeMap<Integer, List<Goal>> prioritizedGoals = new TreeMap<>(Integer::compare);

        int value;
        for (Goal goal : playerGoals) {
            value = getGoalPriority(goal, player);
            if (!prioritizedGoals.containsKey(value)) {
                prioritizedGoals.put(value, new ArrayList<>());
            }
            prioritizedGoals.get(value).add(goal);
        }

        // Return the first element of the map
        return playerGoals.isEmpty() ? GoalType.PANDA : prioritizedGoals.get(prioritizedGoals.lastKey()).get(0).getGoalType();
    }

    static int getGoalPriority(Goal goal, Player player) {
        int difficulty = 10000;

        switch (goal.getGoalType()) {
            case PANDA:

                // Les pandas c'est plus facile !
                difficulty /= 20;

                // Comparaison bambous dans l'inventaire avec les bambous à avoir
                List<Bamboo> playerBamboos = new ArrayList<>(player.getInventory().getBamboos());
                List<Bamboo> goalBamboos = ((PandaGoal) goal).getBamboosToCollect();
                int delta = goalBamboos.size();

                for (int i = 0; i < goalBamboos.size() && delta > 0; i++) {
                    if (playerBamboos.contains(goalBamboos.get(i))) {
                        delta--;
                    }
                }

                switch (delta) {
                    case 1:
                        difficulty = 1;
                        break;
                    case 2:
                        break;
                    default:
                        difficulty *= 2;
                }

                break;
            case GARDENER:

                // Les jardiniers c'est un peu moins facile !
                difficulty /= 10;

                // Chercher les parcelles accessibles par le jardinier
                PossibleMovesResult possibleMoves = BoardUtils.getPossibleMoves(player.getBoard().getGardener().getPosition());
                List<Parcel> parcelsAccessibleByGardener = possibleMoves.getAllParcels().stream()

                        // Filtrer les parcelles avec la couleur et la facility de l'objectif
                        .filter(parcel -> ((GardenerGoal) goal).getColor() == parcel.getColor())
                        .filter(parcel -> ((GardenerGoal) goal).getFacility() == parcel.getFacility())

                        // Chercher les parcelles ayant un bambou de plus ou de moins que le nombre de bambous de l'objectifs
                        .filter(parcel -> Math.abs(((GardenerGoal) goal).getSize() - parcel.getBambooStack().size()) <= 1)
                        .collect(Collectors.toList());

                if (!parcelsAccessibleByGardener.isEmpty()) {
                    difficulty = 1;
                }

                break;
            case PARCEL:
                difficulty /= 5;

                PlaceParcelParams toPlaceParams = StrategyHelpers.getParcelThatCanCompleteParcelGoal(new ArrayList<>(Collections.singletonList(goal)), new ArrayList<>(Arrays.asList(new GreenParcel(), new PinkParcel(), new YellowParcel())), player.getBoard());
                if (toPlaceParams.paramsNotNull()) {
                    difficulty = 1;
                }

                break;
            default:
                break;
        }

        return (int) (((double) goal.getPoints() / (double) difficulty) * 10000.);
    }

}
