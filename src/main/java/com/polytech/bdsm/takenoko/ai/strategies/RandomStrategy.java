package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.*;
import com.polytech.bdsm.takenoko.engine.board.*;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import com.polytech.bdsm.takenoko.engine.pnj.PNJ;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 * @see Strategy
 * @see StrategyHelpers
 * @see StrategyType
 */

public class RandomStrategy implements Strategy {

    private int nbRandomTry = 0;
    private boolean forceRandom;

    /**
     * Constructor of the {@code RandomStrategy}
     *
     * @param forceRandom To specify if the player must keep this strategy or change to another one
     */
    public RandomStrategy(boolean forceRandom) {
        this.forceRandom = forceRandom;
    }

    /**
     * Getter for the {@code StrategyType}
     *
     * @return The {@code StrategyType} of the {@code Strategy}
     */
    @Override
    public StrategyType getStrategyType() {
        return StrategyType.RANDOM;
    }

    /**
     * Choose the best move for the current {@code Strategy}
     *
     * @param player The player to get the best move from
     * @return The {@code ActionParam} of the player chosen move
     * @throws StrategyNotApplicableAnymore If the strategy can't be applied anymore
     */
    @Override
    public ActionParams chooseBestMove(Player player) throws StrategyNotApplicableAnymore {
        Random randomizer = player.getRandomizer();

        if (!forceRandom && nbRandomTry++ % 2 != 0) {
            int luck = randomizer.nextInt(100);
            if (luck > 90) {
                List<StrategyType> strat = new ArrayList<>();
                strat.add(StrategyType.PARCEL);
                strat.add(StrategyType.GARDENER);
                strat.add(StrategyType.PANDA);
                throw new StrategyNotApplicableAnymore(StrategyType.RANDOM, strat.get(randomizer.nextInt(strat.size())));
            } else
                throw new StrategyNotApplicableAnymore(StrategyType.RANDOM, ((Bot) player).getInitialStrategy().getStrategyType());
        }

        ActionParams params = null;
        List<ActionType> notTested = new ArrayList<>(player.getActionList());
        int random;
        boolean canExecute = false;

        while (!canExecute && !notTested.isEmpty()) {
            random = randomizer.nextInt(notTested.size());
            switch (notTested.get(random)) {

                case PLACE_PARCEL:
                    try {
                        params = StrategyHelpers.placeRandomParcel(player.getBoard(), player.getActionFactory().pickParcel(player.getBoard().getParcelsDeck()), randomizer);
                        canExecute = ((Bot) player).canExecuteAction(params);
                    } catch (Exception e) {
                        canExecute = false;
                    }

                    break;

                case PLACE_FACILITY:
                    // Choose the first facility found in the inventory
                    if (player.getInventory().getFacilities().isEmpty()) break;

                    // Choose the first parcel which can receive a facility
                    List<Parcel> possibleFacilityParcels = BoardUtils.getPossibleFacilityParcels(player.getBoard());
                    if (possibleFacilityParcels.isEmpty()) break;

                    params = new PlaceFacilityParams();
                    ((PlaceFacilityParams) params).setFacility(player.getInventory().getFacilities().get(0));
                    ((PlaceFacilityParams) params).setParcel(possibleFacilityParcels.get(0));

                    canExecute = ((Bot) player).canExecuteAction(params);
                    break;

                case DRAW_IRRIGATION:
                    if (player.getBoard().getIrrigationsDeck() <= 0) break;
                    params = new DrawIrrigationParams();
                    canExecute = ((Bot) player).canExecuteAction(params);

                    break;

                case PLACE_IRRIGATION:
                    if (player.getInventory().getIrrigations() <= 0) break;
                    PossibleIrrigationResult irrigationResult = BoardUtils.getPossibleIrrigations(player.getBoard());
                    params = new PlaceIrrigationParams();
                    Irrigation irrigation = irrigationResult.getHeadIrrigation();

                    if (irrigation != null) {
                        ((PlaceIrrigationParams) params).setIrrigation(irrigation);
                        canExecute = ((Bot) player).canExecuteAction(params);
                    } else {
                        canExecute = false;
                    }

                    break;

                case DRAW_GOAL:
                    if (player.getGoals().size() > 5) break;
                    params = new DrawGoalParams();
                    GoalType goalType = GoalType.PANDA;
                    ((DrawGoalParams) params).setGoalType(goalType);
                    if (canExecute = ((Bot) player).canExecuteAction(params)) break;
                    goalType = GoalType.GARDENER;
                    ((DrawGoalParams) params).setGoalType(goalType);
                    if (canExecute = ((Bot) player).canExecuteAction(params)) break;
                    goalType = GoalType.PARCEL;
                    ((DrawGoalParams) params).setGoalType(goalType);
                    canExecute = ((Bot) player).canExecuteAction(params);

                    break;

                case MOVE_PANDA:
                    params = new MovePandaParams();
                    PNJ pnjToMove1 = player.getBoard().getPanda();
                    PossibleMovesResult movesResult1 = BoardUtils.getPossibleMoves(pnjToMove1.getPosition());
                    if (movesResult1.getAllParcels().size() <= 0) {
                        //canExecute = false;
                        break;
                    }
                    ((MovePandaParams) params).setPNJ(pnjToMove1);
                    ((MovePandaParams) params).setParcel(movesResult1.getAllParcels().get(randomizer.nextInt(movesResult1.getAllParcels().size())));
                    canExecute = ((Bot) player).canExecuteAction(params);

                    break;

                case MOVE_GARDENER:
                    params = new MoveGardenerParams();
                    PNJ pnjToMove2 = player.getBoard().getGardener();
                    PossibleMovesResult movesResult2 = BoardUtils.getPossibleMoves(pnjToMove2.getPosition());
                    if (movesResult2.getAllParcels().size() <= 0) {
                        //canExecute = false;
                        break;
                    }
                    ((MoveGardenerParams) params).setPNJ(pnjToMove2);
                    ((MoveGardenerParams) params).setParcel(movesResult2.getAllParcels().get(randomizer.nextInt(movesResult2.getAllParcels().size())));
                    canExecute = ((Bot) player).canExecuteAction(params);

                    break;

                case NONE:
                    params = new NoneParams();
                    canExecute = ((Bot) player).canExecuteAction(params);

                    break;
                default:
                    break;
            }

            notTested.remove(notTested.get(random));
            if (!canExecute) params = null;
        }

        if (params == null) {
            throw new StrategyNotApplicableAnymore(StrategyType.RANDOM, ((Bot) player).getInitialStrategy().getStrategyType());
        }

        return params;
    }

    /**
     * TODO doc
     *
     * @param board The board to analyse
     * @return
     */
    @Override
    public Facility chooseBestFacilityToDraw(Board board) {
        if (board.getFacilityDeck().canDrawFacility(Facility.PADDOCK_FACILITY)) {
            return Facility.PADDOCK_FACILITY;
        } else {
            if (board.getFacilityDeck().canDrawFacility(Facility.POND_FACILITY)) {
                return Facility.POND_FACILITY;
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
