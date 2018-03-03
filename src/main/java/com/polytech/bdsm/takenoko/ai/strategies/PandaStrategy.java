package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.engine.actions.ActionFactory;
import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.*;
import com.polytech.bdsm.takenoko.engine.board.*;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.goals.PandaGoal;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import com.polytech.bdsm.takenoko.engine.pnj.PNJ;
import com.polytech.bdsm.takenoko.engine.pnj.PNJName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */
public class PandaStrategy implements Strategy {

    /**
     * Getter for the Strategy Type
     *
     * @return This StrategyType
     */
    @Override
    public StrategyType getStrategyType() {
        return StrategyType.PANDA;
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

        // Si elle contient un objectif Panda
        if (StrategyHelpers.handContainsGoalType(player.getGoals(), GoalType.PANDA)) {

            // Comparer la liste des bambous à avoir et l'inventaire,
            // en déduire les bambous qui restent à avoir pour chaque objectif,
            // garder celui qui en a le moins.
            PandaGoal prioritisedGoal;
            List<PandaGoal> possiblePandaGoals = new ArrayList<>();
            player.getGoals().stream().filter(goal -> goal.getGoalType() == GoalType.PANDA).forEach(goal -> possiblePandaGoals.add((PandaGoal) goal));

            while ((prioritisedGoal = PandaStrategyHelpers.getEasiestGoalToComplete(possiblePandaGoals, player.getInventory())) != null) {

                // Récupérer la liste des parcelles sur laquelle peut se déplacer le Panda
                PossibleMovesResult possibleMovesResultPanda = BoardUtils.getPossibleMoves(player.getBoard().getPanda().getPosition());
                List<Parcel> possibleParcelsToMovePanda = possibleMovesResultPanda.getAllParcels().stream().filter(parcel -> parcel.getFacility() != Facility.PADDOCK_FACILITY).collect(Collectors.toList());

                // Y a-t-il au moins un bambou d'une couleur qui nous intéresse
                // Sans paddock
                List<Bamboo> bambooList = PandaStrategyHelpers.getDifferentBamboos(prioritisedGoal.getBamboosToCollect());
                List<Bamboo> bamboosInInventory = PandaStrategyHelpers.getDifferentBamboos(player.getInventory().getBamboos());
                List<Bamboo> checkBamboos = bambooList.stream().filter(bamboo -> !bamboosInInventory.contains(bamboo)).collect(Collectors.toList());
                List<Bamboo> bamboosNotAlreadyCollected = checkBamboos.size() > 0 ? checkBamboos : bambooList;

                // Tentative de mouvements des PNJ
                ActionParams moveParams;
                if ((moveParams = this.tryMovingPNJs(player, bamboosNotAlreadyCollected, possibleParcelsToMovePanda)) != null) {
                    return moveParams;
                }

                // Y a-t-il au moins une parcelle de la couleur du Bambou
                // qui nous intéresse ?
                // Sans paddock
                List<Parcel> allParcelsOfTheBoard = BoardUtils.getAllParcels(player.getBoard())
                        .stream().filter(parcel -> parcel.getFacility() != Facility.PADDOCK_FACILITY).collect(Collectors.toList());

                List<Parcel> possibleExpectedParcels, foundParcels;
                List<Bamboo> bamboosCopy = new ArrayList<>(bambooList);
                while (!bamboosCopy.isEmpty()) {
                    possibleExpectedParcels = StrategyHelpers.getExpectedParcelsOfColor(false, allParcelsOfTheBoard, bamboosCopy.get(0).getColor(), false, player.getBoard(), null);

                    // Peut-on poser un Pond sur une de ces parcelles ?
                    // A t-on un PondFacility dans l'inventaire
                    if (player.canExecuteActionType(ActionType.PLACE_FACILITY)
                            && player.getInventory().containFacility(Facility.POND_FACILITY)
                            && !(foundParcels = BoardUtils.getPossibleFacilityParcels(player.getBoard())
                                                .stream()
                                                .filter(parcel -> parcel.getFacility() == Facility.POND_FACILITY)
                                                .filter(possibleExpectedParcels::contains)
                                                .collect(Collectors.toList())).isEmpty()) {

                        // Poser un Pond sur une des foundParcels
                        PlaceFacilityParams facilityParams = new PlaceFacilityParams();
                        facilityParams.setFacility(Facility.POND_FACILITY);
                        facilityParams.setParcel(foundParcels.get(0));
                        return facilityParams;
                    }

                    // Sinon :

                    // Est-ce qu'une parcelle trouvée est irriguable
                    PossibleIrrigationResult irrigationResult = BoardUtils.getPossibleIrrigations(player.getBoard());

                    // Y a-t-il des irrigations dans l'inventaire
                    if (player.getInventory().getIrrigations() > 0) {
                        if (player.canExecuteActionType(ActionType.PLACE_IRRIGATION)
                                && !(possibleExpectedParcels.isEmpty())
                                && !(foundParcels = possibleExpectedParcels.stream().filter(irrigationResult::containsParcel).collect(Collectors.toList())).isEmpty()) {

                            // Irriguer cette parcelle
                            Irrigation irrigation = irrigationResult.getParcelContainedInResult(foundParcels.get(0));
                            PlaceIrrigationParams irrigationParams = (PlaceIrrigationParams) factory.getAction(ActionType.PLACE_IRRIGATION);
                            irrigationParams.setIrrigation(irrigation);
                            return irrigationParams;
                        }
                    }

                    // Sinon piocher une irrigation
                    else {
                        if (player.canExecuteActionType(ActionType.DRAW_IRRIGATION)) {
                            return new DrawIrrigationParams();
                        }
                    }

                    bamboosCopy.remove(0);
                }


                // Only PlaceParcel Params are returned from here
                PlaceParcelParams placeParcelParams;
                if (player.canExecuteActionType(ActionType.PLACE_PARCEL) && (placeParcelParams = this.tryPlacingParcel(factory, bambooList, player, possibleMovesResultPanda)) != null) {
                    return placeParcelParams;
                }

                // Y a-t-il d'autres objectifs Panda à accomplir
                possiblePandaGoals.remove(prioritisedGoal);
                if (possiblePandaGoals.isEmpty()) {
                    break;
                }
            }

            throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.RANDOM);
        }

        // Si elle ne contient pas d'objectif Panda
        else {

            // Y a-t-il des objectifs Panda à piocher
            if (StrategyHelpers.goalsDeckContainsGoalType(player.getBoard().getGoalsDeck(), GoalType.PANDA)) {

                // Y a-t-il moins de 5 objectifs dans la main du joueur
                if (player.canExecuteActionType(ActionType.DRAW_GOAL) && player.getGoals().size() < 5) {

                    // /!\ Piocher un objectif Panda
                    DrawGoalParams drawGoalParams = (DrawGoalParams) factory.getAction(ActionType.DRAW_GOAL);
                    drawGoalParams.setGoalType(GoalType.PANDA);
                    return drawGoalParams;
                }

                /*
                // La main du joueur est pleine (5 objectifs)
                else {
                    int gardener = 0;
                    int parcel = 0;
                    for (Goal g : player.getGoals()) {
                        switch (g.getGoalType()) {
                            case PARCEL:
                                parcel++;
                                break;
                            case GARDENER:
                                gardener++;
                                break;
                        }
                    }
                    if (gardener >= parcel) {
                        throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.GARDENER);
                    } else {
                        throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.PARCEL);
                    }
                }*/
            }
            /*
            // Il n'y a pas d'objectifs Panda à piocher
            else {
                if (!player.getGoals().stream().filter(goal -> goal.getGoalType().equals(GoalType.PARCEL)).collect(Collectors.toList()).isEmpty()) {
                    throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.PARCEL);
                } else if (!player.getGoals().stream().filter(goal -> goal.getGoalType().equals(GoalType.GARDENER)).collect(Collectors.toList()).isEmpty()) {
                    throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.GARDENER);
                }
            }*/
        }

        throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.RANDOM);
    }

    /**
     * TODO doc
     * @param board The board to analyse
     * @return
     */
    @Override
    public Facility chooseBestFacilityToDraw(Board board) {

        if (board.getFacilityDeck().canDrawFacility(Facility.POND_FACILITY)) {
            return Facility.POND_FACILITY;
        } else {
            if (board.getFacilityDeck().canDrawFacility(Facility.FERTILIZER_FACILITY)) {
                return Facility.FERTILIZER_FACILITY;
            } else {
                if (board.getFacilityDeck().canDrawFacility(Facility.PADDOCK_FACILITY)) {
                    return Facility.PADDOCK_FACILITY;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Method to get the params to move a PNJ to a Parcel
     *
     * @param typeOfMove {@code MoveGardener} if the {@code PNJ} is the Gardener, {@code MovePanda} etiher
     * @param withBamboos if true, will return parcels with bamboo of the specified color, if false and with irrigatedOnly false ,will return parcel of the specified color
     * @param pnj The {@code PNJ} to move
     * @param bamboos The current panda goal list of bamboos that must be eaten
     * @param possibleParcels the list of parcels on which one PNJ can move on
     * @param irrigatedOnly if true and withBamboos false, will return only the irrigated parcels of the specified color, if false and withBamboos false will return parcel of the specified color
     * @param player the {@code Player} doing the move
     * @return the params for the move
     */
    private ActionParams tryMovingPNJToParcel(ActionType typeOfMove, boolean withBamboos, PNJ pnj, List<Bamboo> bamboos, List<Parcel> possibleParcels, boolean irrigatedOnly, Player player) {
        List<Parcel> possibleExpectedParcels;
        List<Bamboo> bambooList = new ArrayList<>(bamboos);
        while (!bambooList.isEmpty()) {
            if (!(possibleExpectedParcels = StrategyHelpers.getExpectedParcelsOfColor(withBamboos, possibleParcels, bambooList.get(0).getColor(), irrigatedOnly, player.getBoard(), null)).isEmpty()) {

                // Déplacer le PNJ sur une des parcelles
                ActionParams moveParams = player.getActionFactory().getAction(typeOfMove);
                if (pnj.getName() == PNJName.PANDA) {
                    ((MovePandaParams) moveParams).setPNJ(pnj);
                    ((MovePandaParams) moveParams).setParcel(possibleExpectedParcels.get(0));
                } else {
                    ((MoveGardenerParams) moveParams).setPNJ(pnj);
                    ((MoveGardenerParams) moveParams).setParcel(possibleExpectedParcels.get(0));
                }

                return moveParams;
            }

            bambooList.remove(bambooList.get(0));
        }

        return null;
    }

    /**
     * Method that will try to move the panda to a optimal position,
     * if it's not in the advantage of the player,
     * it will check if the Gardener can be move to a irrigated parcel
     * with the color corresponding to the current panda goal
     *
     * @param player                        The current Player
     * @param bambooList                    The bamboo list needed to accomplished the current panda goal of the strategy
     * @param possibleParcelsToMovePanda    The possible parcel where the panda can be moved
     * @return The optimal
     */
    private ActionParams tryMovingPNJs(Player player, List<Bamboo> bambooList, List<Parcel> possibleParcelsToMovePanda) {
        ActionParams tryToMove;
        if (player.canExecuteActionType(ActionType.MOVE_PANDA) && (tryToMove = this.tryMovingPNJToParcel(ActionType.MOVE_PANDA, true, player.getBoard().getPanda(), bambooList, possibleParcelsToMovePanda, false, player)) != null) {
            return tryToMove;
        }

        // Il n'y a pas de parcelles avec des bamboos de la couleur cherchée
        // Y a-t-il une parcelle IRRIGUEE dans les parcelles accessibles par le jardinier
        // de la couleur du Bambou qui nous intéresse ?
        // Sans paddock
        List<Parcel> possibleParcelsToMoveGardener = BoardUtils.getPossibleMoves(player.getBoard().getGardener().getPosition())
                .getAllParcels().stream().filter(parcel -> parcel.getFacility() != Facility.PADDOCK_FACILITY).collect(Collectors.toList());

        if (player.canExecuteActionType(ActionType.MOVE_GARDENER) && (tryToMove = this.tryMovingPNJToParcel(ActionType.MOVE_GARDENER, false, player.getBoard().getGardener(), bambooList, possibleParcelsToMoveGardener, true, player)) != null) {
            return tryToMove;
        }

        return null;
    }

    /**
     * Method that look for the optimal position where to place a parcel.
     *
     * Verify lots of conditions :
     *  - Check the bamboo list given
     *  - Check if there is parcel to be picked
     *  - Look for a parcel of the color of the current goal
     *  - Does the parcel have a pond facility
     *  - Look for the optimal parcel to place depends on the color of the panda goal and the possible parcel to pick
     *
     * @param factory                   The factory to get the possible parcel to pick
     * @param bambooList                The bamboo list that must be eaten to complete the current panda goal
     * @param player                    The current player
     * @param possibleMovesResultPanda  The list of the parcel where the Panda can move
     * @return The optimal Place Parcel Param that will place a parcel that will help the bot
     * to accomplish the panda goal as fast as possible
     */
    private PlaceParcelParams tryPlacingParcel(ActionFactory factory, List<Bamboo> bambooList, Player player, PossibleMovesResult possibleMovesResultPanda) {
        PlaceParcelParams parcelParams;
        PossiblePlacingResult possibleParcels = BoardUtils.getPossibleParcelsToPlace(player.getBoard());

        List<Bamboo> bamboosCopyAgain = new ArrayList<>(bambooList);
        List<Parcel> possibleParcelsToMovePanda = possibleMovesResultPanda.getAllParcels();
        List<Parcel> parcelsAccesibleByPanda, goodColorParcelsToDraw, goodColorWithPondParcels;

        while (!bamboosCopyAgain.isEmpty()) {

            // Reste-t-il des parcelles dans la pioche
            if (!player.getBoard().getParcelsDeck().isEmpty()) {

                // Piocher une parcelle
                try {
                    List<Parcel> possibleParcelToDraw = factory.pickParcel(player.getBoard().getParcelsDeck());

                    // Y a-t-il une parcelle d'une couleur qui nous intéresse
                    if (!(goodColorParcelsToDraw = possibleParcelToDraw.stream().filter(parcel -> parcel.getColor() == bamboosCopyAgain.get(0).getColor()).collect(Collectors.toList())).isEmpty()) {

                        //Si cette parcelle possède un pond
                        if (!(goodColorWithPondParcels = goodColorParcelsToDraw.stream().filter(parcel -> parcel.getFacility() == Facility.POND_FACILITY).collect(Collectors.toList())).isEmpty()) {

                            // la placer à un endroit accessible par le panda
                            if ((parcelParams = StrategyHelpers.tryPlacingParcelAccessibleByPNJ(factory, possibleMovesResultPanda, possibleParcelsToMovePanda, possibleParcels, goodColorWithPondParcels.get(0))) != null) {
                                return parcelParams;
                            }
                        }

                        //Sinon :

                        // Cette parcelle est-elle plaçable près de l'étang
                        for (int i = 0; i < player.getBoard().getPond().getDegree(); i++) {
                            if (player.getBoard().getPond().getNeighbours()[i] == null) {
                                // La placer là
                                parcelParams = (PlaceParcelParams) factory.getAction(ActionType.PLACE_PARCEL);
                                parcelParams.setFrom(player.getBoard().getPond());
                                parcelParams.setToPlace(goodColorParcelsToDraw.get(0));
                                parcelParams.setIndexToPlace(i);

                                return parcelParams;
                            }
                        }

                        // Elle n'est pas plaçable près de l'étang
                        // Est-il possible de la placer près d'une parcelle irriguée et accessible par le panda ?
                        if (!(parcelsAccesibleByPanda = possibleParcelsToMovePanda.stream().filter(parcel -> BoardUtils.isParcelIrrigated(player.getBoard(), parcel)).collect(Collectors.toList())).isEmpty()) {

                            if ((parcelParams = StrategyHelpers.tryPlacingParcelAccessibleByPNJ(factory, possibleMovesResultPanda, parcelsAccesibleByPanda, possibleParcels, goodColorParcelsToDraw.get(0))) != null) {
                                return parcelParams;
                            }
                        }

                        return StrategyHelpers.placeRandomParcel(player.getBoard(), goodColorParcelsToDraw, player.getRandomizer());
                    }

                    // Il n'y a pas de parcelle avec la couleur qui nous intéresse
                    else {

                        // Y a-t-il des objectifs parcelles à compléter
                        if (StrategyHelpers.handContainsGoalType(player.getGoals(), GoalType.PARCEL)) {

                            // Y a-t-il une parcelle pouvant compléter un objectif parcelle
                            List<Goal> parcelGoals = new ArrayList<>();
                            player.getGoals().stream().filter(goal -> goal.getGoalType() == GoalType.PARCEL).forEach(parcelGoals::add);
                            PlaceParcelParams toPlaceParams = StrategyHelpers.getParcelThatCanCompleteParcelGoal(parcelGoals, possibleParcelToDraw, player.getBoard());
                            if (toPlaceParams.paramsNotNull()) {
                                // Placer les parcelles
                                return toPlaceParams;
                            }

                            // Sinon, on place une parcelle aléatoirement
                            else {
                                return StrategyHelpers.placeRandomParcel(player.getBoard(), possibleParcelToDraw, player.getRandomizer());
                            }
                        }

                        // Il n'y a pas d'objectifs parcelles à faire
                        else {

                            // Une partie de l'arbre de décision n'est pas gérée,
                            // Possibilité de rendre cette partie plus intelligente
                            return StrategyHelpers.placeRandomParcel(player.getBoard(), possibleParcelToDraw, player.getRandomizer());
                        }
                    }
                } catch (Exception ignored) {}
            }

            bamboosCopyAgain.remove(0);
        }

        return null;
    }
}
