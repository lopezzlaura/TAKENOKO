package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.engine.actions.ActionFactory;
import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.PlaceParcelParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.RainActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.StormActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.SunActionParams;
import com.polytech.bdsm.takenoko.engine.board.*;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalFactory;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.goals.ParcelGoal;
import com.polytech.bdsm.takenoko.engine.graph.GraphNode;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public final class StrategyHelpers {

    /**
     * Private Constructor to block instantiation
     */
    private StrategyHelpers() {}

    /**
     * Check if a list of goals contains a specified GoalType goal in it
     *
     * @param goals The List of the goals given
     * @param type  The expected GoalType searched
     * @return True if the goals contains at least one goal with the specified GoalType, false otherwise
     */
    public static boolean handContainsGoalType(List<Goal> goals, GoalType type) {
        for (Goal g : goals) {
            if (g.getGoalType() == type) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a deck of Goals still contains Goals of the specified GoalType in it
     *
     * @param deck The GoalFactory deck to check
     * @param type The GoalType to check in the deck
     * @return True if the deck contains at least one goal with the specified GoalType, false otherwise
     */
    public static boolean goalsDeckContainsGoalType(GoalFactory deck, GoalType type) {
        try {
            List<Goal> goals = deck.getGoals(type);
            return (!goals.isEmpty());
        } catch (Exception emptyDeck) {
            return false;
        }
    }

    /**
     * Try to find parcels with specific parameters
     *
     * @param withBamboos     if true, will return parcels with bamboo of the specified color, if false and with irrigatedOnly false ,will return parcel of the specified color
     * @param possibleParcels the list of parcels on which one PNJ can move on
     * @param expectedColor   the color of the parcels looked for
     * @param irrigatedOnly   if true and withBamboos false, will return only the irrigated parcels of the specified color, if false and withBamboos false will return parcel of the specified color
     * @return a List of parcel with the specified parameters
     */
    public static List<Parcel> getExpectedParcelsOfColor(boolean withBamboos, List<Parcel> possibleParcels, Color expectedColor, boolean irrigatedOnly, Board board, @Nullable Facility facility) {

        List<Parcel> parcelsSearched = new ArrayList<>(), possibleParcelsCopy = new ArrayList<>(possibleParcels);
        for (Parcel p : possibleParcelsCopy) {
            if (withBamboos) {
                if (p.getColor() == expectedColor && p.getBambooStack().size() > 0) {
                    if (facility != null && p.getFacility() == facility) {
                        parcelsSearched.add(p);
                    } else if (facility == null) {
                        parcelsSearched.add(p);
                    }
                }
            } else {
                if (p.getColor() == expectedColor) {
                    if (irrigatedOnly) {
                        if (BoardUtils.isParcelIrrigated(board, p)) {
                            if (facility != null && p.getFacility() == facility) {
                                parcelsSearched.add(p);
                            } else if (facility == null) {
                                parcelsSearched.add(p);
                            }
                        }
                    } else {
                        if (facility != null && p.getFacility() == facility) {
                            parcelsSearched.add(p);
                        } else if (facility == null) {
                            parcelsSearched.add(p);
                        }
                    }
                }
            }
        }

        return parcelsSearched;
    }

    /**
     * Getter for the Parcels that if they are placed, it will finish a Parcel Goal
     *
     * @param goals   The Parcel Goal
     * @param parcels the possible parcels to place
     * @return a {@code PlaceParcelParams} with the parcel to place
     */
    public static PlaceParcelParams getParcelThatCanCompleteParcelGoal(List<Goal> goals, List<Parcel> parcels, Board board) {
        Board cpy = (Board) board.clone();
        PlaceParcelParams params = new PlaceParcelParams();
        PossiblePlacingResult placingResult = BoardUtils.getPossibleParcelsToPlace(board);

        for (Goal goal : goals) {
            ParcelGoal parcelGoal = (ParcelGoal) goal;
            if (parcelGoal.isAlmostValid(board)) {
                for (Parcel p : placingResult.getAllParcels()) {
                    for (Parcel toTest : parcels) {
                        for (Integer index : placingResult.getParcelPossibleIndexes(p)) {
                            try {
                                GraphNode toTestCopy = (GraphNode) toTest.clone();
                                cpy.getParcel(p).addNode(index, toTestCopy);

                                irrigateAllBoard(cpy);

                                if (parcelGoal.isValid(cpy)) {
                                    params.setFrom(p);
                                    params.setToPlace(toTest);
                                    params.setIndexToPlace(index);
                                    return params;
                                }
                            } catch (Exception ignored) {
                            }

                            cpy = (Board) board.clone();
                        }
                    }
                }
            }
        }
        return params;
    }

    /**
     * Method to irrigate all the parcels in a board
     *
     * @param board the board to irrigate
     */
    static void irrigateAllBoard(Board board) {
        List<Parcel> allParcels = BoardUtils.getAllParcels(board);
        allParcels.remove(board.getPond());
        for (Parcel p : allParcels) {
            for (int i = 0; i < p.getDegree(); i++) {
                if (p.getNeighbours()[i] != null && !p.getNeighbours()[i].equals(board.getPond())) {
                    board.addIrrigation(new Irrigation(p, (Parcel) p.getNeighbours()[i]));
                }
            }
        }
    }

    /**
     * Place a parcel randomly
     *
     * @param board                The board of the player
     * @param possibleParcelToDraw The List of the possible parcels to place
     * @return The PlaceParcelParams correctly set
     */
    public static PlaceParcelParams placeRandomParcel(Board board, List<Parcel> possibleParcelToDraw, Random random) {

        // Récupération aléatoire d'une parcelle à placer
        PossiblePlacingResult possibleParcels = BoardUtils.getPossibleParcelsToPlace(board);
        List<Parcel> allPossibleParcels = possibleParcels.getAllParcels();

        // Choix aléatoire du from et de l'index
        Parcel chosenParcel = allPossibleParcels.get(random.nextInt(allPossibleParcels.size()));
        List<Integer> chosenParcelPossibleIndexes = possibleParcels.getParcelPossibleIndexes(chosenParcel);
        Integer chosenIndex = chosenParcelPossibleIndexes.get(random.nextInt(chosenParcelPossibleIndexes.size()));

        // Création du params à renvoyer
        PlaceParcelParams toPlaceParams = new PlaceParcelParams();
        toPlaceParams.setIndexToPlace(chosenIndex);
        toPlaceParams.setFrom(chosenParcel);
        toPlaceParams.setToPlace(possibleParcelToDraw.get(random.nextInt(possibleParcelToDraw.size())));
        return toPlaceParams;
    }

    /**
     * Method that try to find a parcel that can be placed in a specific manner so that one
     * of the PNJ will be able to move on after the player has place a parcel with the parameter
     * returned from this method
     *
     * @param factory                   The Action Factory to get the possible parcels from
     * @param possibleMovesResultPNJ    The parcel where one PNJ can mve to
     * @param possibleFromParcels       The different parcels from where the PNJ will begin from
     * @param possibleParcels           All the parcel that can be placed on the board
     * @param toPlace                   The parcel to place on the board
     * @return  The Place Parcel param that specify from wich parcel to place another parcel so that one PNJ can move on that one right after this action
     */
    public static PlaceParcelParams tryPlacingParcelAccessibleByPNJ(ActionFactory factory, PossibleMovesResult possibleMovesResultPNJ, List<Parcel> possibleFromParcels, PossiblePlacingResult possibleParcels, Parcel toPlace) {

        // A AMELIORER POSSIBLEMENT : RENDRE PLUS MODULABLE QUE CA
        HashMap<Integer, List<Parcel>> directionnalParcels = new HashMap<>();

        // Ne pas prendre les parcelles non présentes dans les possiblePlaceParcels
        for (Integer direction : possibleMovesResultPNJ.getDirections()) {
            directionnalParcels.put(direction, new ArrayList<>());
            for (Parcel p : possibleMovesResultPNJ.getParcelsInDirection(direction)) {
                if (possibleFromParcels.contains(p) && possibleParcels.getAllParcels().contains(p)) {
                    directionnalParcels.get(direction).add(p);
                }
            }
        }

        // Pour chaque direction, dès qu'un indice est utilisable, le garder
        Integer indexToPlace = -1;
        Parcel parcelFromPlace = null;
        for (Integer direction : directionnalParcels.keySet()) {
            for (Parcel p : directionnalParcels.get(direction)) {
                if (p.getNeighbours()[direction] == null) {
                    indexToPlace = direction;
                    parcelFromPlace = p;
                    break;
                } else if (p.getNeighbours()[(direction + 3) % 6] == null) {
                    indexToPlace = (direction + 3) % 6;
                    parcelFromPlace = p;
                    break;
                }
            }
        }

        if (indexToPlace != -1) {
            // La placer là
            PlaceParcelParams parcelParams = (PlaceParcelParams) factory.getAction(ActionType.PLACE_PARCEL);
            parcelParams.setFrom(parcelFromPlace);
            parcelParams.setToPlace(toPlace);
            parcelParams.setIndexToPlace(indexToPlace);

            return parcelParams;
        }

        return null;
    }


    /**
     * Choose the best weather for a player
     *
     * @param player The player who chooses the weather for his current turn
     * @return The actionParams of the weather chosen
     */
    public static ActionParams chooseWeather(Player player) {
        //Si UnknownAction
        //Si possède un objectif Panda
        //si bamboo à manger choisi StormAction
        //sinon choisi RainAction pour faire pousser le bamboo de son choix
        //si aucune parcel irriguée, choisi sun action
        //Si possède un objectif Gardener
        //choisi RainAction sur parcel de son choix
        //si aucune parcel irriguée, choisi sun action
        //Si possède un objectif Parcel
        //choisi sun action

        ActionParams weatherParams;
        List<Parcel> allParcels = BoardUtils.getAllParcels(player.getBoard());

        if (StrategyHelpers.handContainsGoalType(player.getGoals(), GoalType.PANDA)) {
            if (!BoardUtils.findParcelWithBamboos(player.getBoard(), 1).isEmpty()
                    || !BoardUtils.findParcelWithBamboos(player.getBoard(), 2).isEmpty()
                    || !BoardUtils.findParcelWithBamboos(player.getBoard(), 3).isEmpty()
                    || !BoardUtils.findParcelWithBamboos(player.getBoard(), 4).isEmpty()) {
                weatherParams = player.getWeatherMove(new StormActionParams());
            } else {
                List<Parcel> irrigatedParcels = new ArrayList<>(allParcels).stream()
                        .filter(parcel -> BoardUtils.isParcelIrrigated(player.getBoard(), parcel))
                        .filter(parcel -> parcel.getBambooStack().size() < 4).collect(Collectors.toList());

                // On enlève la pondParcel
                irrigatedParcels.remove(player.getBoard().getPond());
                if (!irrigatedParcels.isEmpty()) {
                    weatherParams = player.getWeatherMove(new RainActionParams());
                } else {
                    weatherParams = player.getWeatherMove(new SunActionParams());
                }
            }
        } else {
            if (StrategyHelpers.handContainsGoalType(player.getGoals(), GoalType.GARDENER)) {
                List<Parcel> irrigatedParcels = new ArrayList<>(allParcels).stream()
                        .filter(parcel -> BoardUtils.isParcelIrrigated(player.getBoard(), parcel))
                        .filter(parcel -> parcel.getBambooStack().size() < 4).collect(Collectors.toList());

                // On enlève la pondParcel
                irrigatedParcels.remove(player.getBoard().getPond());
                if (!irrigatedParcels.isEmpty()) {
                    weatherParams = player.getWeatherMove(new RainActionParams());
                } else {
                    weatherParams = player.getWeatherMove(new SunActionParams());
                }
            } else {
                weatherParams = player.getWeatherMove(new SunActionParams());
            }
        }

        return weatherParams;
    }
}
