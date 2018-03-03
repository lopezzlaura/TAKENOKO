package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.engine.actions.ActionFactory;
import com.polytech.bdsm.takenoko.engine.actions.ActionRefusedException;
import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.*;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.board.PossibleIrrigationResult;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.goals.ParcelGoal;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class ParcelStrategy implements Strategy {

    /**
     * Getter for the Strategy Type
     *
     * @return This StrategyType
     */
    @Override
    public StrategyType getStrategyType() {
        return StrategyType.PARCEL;
    }

    /**
     * Think about the best next action to do for a player with this Strategy
     *
     * @param player The player who uses that strategy
     * @return The ActionParams already set that the player should execute
     * @throws StrategyNotApplicableAnymore When this strategy can't return a good action to do, the Player will has to change his strategy
     */
    @Override
    public ActionParams chooseBestMove(Player player) throws StrategyNotApplicableAnymore {
        ActionFactory factory = player.getActionFactory();

        /* Regarder la liste d'objectifs */

        // Si elle contient au moins un objectif Parcel
        if (StrategyHelpers.handContainsGoalType(player.getGoals(), GoalType.PARCEL)) {

            //Récupérer le(s) objectif(s) Parcel
            List<Goal> parcelGoalsAlmostDone = player.getGoals().stream().filter(goal -> goal.getGoalType().equals(GoalType.PARCEL)).collect(Collectors.toList());

            //S'il reste des parcelles dans la pioche
            if (!player.getBoard().getParcelsDeck().isEmpty()) {

                //Est-il possible de placer une parcelle sur le board ?
                if (player.canExecuteActionType(ActionType.PLACE_PARCEL)) {
                    List<Parcel> possibleParcelToDraw;
                    PlaceParcelParams placeParcelParams;

                    try {
                        //Si oui piocher
                        possibleParcelToDraw = factory.pickParcel(player.getBoard().getParcelsDeck());

                        //Si l'une des trois parcelles proposées peut accomplir un objectif parcelle
                        //Placer la parcelle
                        placeParcelParams = StrategyHelpers.getParcelThatCanCompleteParcelGoal(parcelGoalsAlmostDone, possibleParcelToDraw, player.getBoard());
                        if (placeParcelParams.paramsNotNull()) {
                            return placeParcelParams;
                        }

                        //Si aucun objectif n'a pu être complété
                        //Piocher la parcelle la plus favorable à la réalisation d'un des objectifs
                        placeParcelParams = StrategyHelpers.placeRandomParcel(player.getBoard(), possibleParcelToDraw, player.getRandomizer());
                        return placeParcelParams;

                    } catch (ActionRefusedException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                //Poser un POND
                ParcelGoal parcelGoal = (ParcelGoal) parcelGoalsAlmostDone.get(0);
                List<Parcel> finalParcelsExpected1 = StrategyHelpers.getExpectedParcelsOfColor(false, BoardUtils.getAllParcels(player.getBoard()), parcelGoal.getColor()[0], false, player.getBoard(), null);
                List<Parcel> foundParcels;
                if (player.canExecuteActionType(ActionType.PLACE_FACILITY)
                        && player.getInventory().containFacility(Facility.POND_FACILITY)
                        && !(foundParcels = BoardUtils.getPossibleFacilityParcels(player.getBoard()).stream()
                        .filter(parcel -> parcel.getFacility() == Facility.POND_FACILITY)
                        .filter(finalParcelsExpected1::contains)
                        .collect(Collectors.toList())).isEmpty()) {

                    // Poser un Pond sur une des foundParcels
                    PlaceFacilityParams facilityParams = new PlaceFacilityParams();
                    facilityParams.setFacility(Facility.POND_FACILITY);
                    facilityParams.setParcel(foundParcels.get(0));
                    return facilityParams;
                } else {


                    //S'il n'y a plus de parcelles à piocher
                    if (player.canExecuteActionType(ActionType.PLACE_IRRIGATION) && player.getInventory().getIrrigations() > 0) {

                        //Si le joueur peut poser une irrigation
                        PossibleIrrigationResult possibleIrrigationResult = BoardUtils.getPossibleIrrigations(player.getBoard());
                        if (possibleIrrigationResult.getIrrigationCount() > 0) {
                            PlaceIrrigationParams placeIrrigationParams = (PlaceIrrigationParams) factory.getAction(ActionType.PLACE_IRRIGATION);
                            placeIrrigationParams.setIrrigation(possibleIrrigationResult.getHeadIrrigation());
                            return placeIrrigationParams;
                        } else if (player.canExecuteActionType(ActionType.DRAW_IRRIGATION)) {
                            //Si le joueur peut piocher une irrigation
                            return factory.getAction(ActionType.DRAW_IRRIGATION);
                        }
                    }

                }
            }
            // Si elle ne contient pas d'objectif Parcel
        } else {
            // Y a-t-il des objectifs Parcel à piocher
            if (StrategyHelpers.goalsDeckContainsGoalType(player.getBoard().getGoalsDeck(), GoalType.PARCEL)) {

                // Y a-t-il moins de 5 objectifs dans la main du joueur
                if (player.canExecuteActionType(ActionType.DRAW_GOAL) && player.getGoals().size() < 5) {
                    //Piocher un objectif Parcel
                    DrawGoalParams drawGoalParams = (DrawGoalParams) factory.getAction(ActionType.DRAW_GOAL);
                    drawGoalParams.setGoalType(GoalType.PARCEL);
                    return drawGoalParams;
                } else {
                    // La main du joueur est pleine (5 objectifs)
                    int panda = 0;
                    int gardener = 0;
                    for (Goal g : player.getGoals()) {
                        switch (g.getGoalType()) {
                            case PANDA:
                                panda++;
                                break;
                            case GARDENER:
                                gardener++;
                                break;
                        }
                    }
                    if (panda >= gardener) {
                        throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.PANDA);
                    } else {
                        throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.GARDENER);
                    }
                }
            } else {
                // Il n'y a pas d'objectifs Parcel à piocher
                if (!player.getGoals().stream().filter(goal -> goal.getGoalType().equals(GoalType.PANDA)).collect(Collectors.toList()).isEmpty()) {
                    throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.PANDA);
                } else if (!player.getGoals().stream().filter(goal -> goal.getGoalType().equals(GoalType.GARDENER)).collect(Collectors.toList()).isEmpty()) {
                    throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.GARDENER);
                }
            }
        }
        throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.RANDOM);
    }

    /**
     * TODO doc
     *
     * @param board The board to analyse
     * @return
     */
    @Override
    public Facility chooseBestFacilityToDraw(Board board) {
        if (board.getFacilityDeck().canDrawFacility(Facility.POND_FACILITY)) {
            return Facility.POND_FACILITY;
        } else {
            if (board.getFacilityDeck().canDrawFacility(Facility.PADDOCK_FACILITY)) {
                return Facility.PADDOCK_FACILITY;
            } else {
                if (board.getFacilityDeck().canDrawFacility(Facility.FERTILIZER_FACILITY)) {
                    return Facility.FERTILIZER_FACILITY;
                } else {
                    return null;
                }
            }
        }
    }
}
