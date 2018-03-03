package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.engine.actions.ActionFactory;
import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.*;
import com.polytech.bdsm.takenoko.engine.board.*;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.GardenerGoal;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
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


public class GardenerStrategy implements Strategy {

    /**
     * Getter for the Strategy Type
     *
     * @return This StrategyType
     */
    @Override
    public StrategyType getStrategyType() {
        return StrategyType.GARDENER;
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

        //Parcourir les objectifs du joueur
        //Si il contient un objectif Gardener
        if (StrategyHelpers.handContainsGoalType(player.getGoals(), GoalType.GARDENER)) {

            List<GardenerGoal> possibleGoals = new ArrayList<>();
            player.getGoals().stream().filter(goal -> goal.getGoalType().equals(GoalType.GARDENER)).forEach(goal -> possibleGoals.add((GardenerGoal) goal));

            //Récupérer tous les objectifs Gardener trouvés
            //Tant qu'il y en a
            List<Parcel> foundParcels, possibleExpectedParcels;
            while (!possibleGoals.isEmpty()) {

                //Etudier le premier objectif
                GardenerGoal gardenerGoal = possibleGoals.get(0);

                //Récupérer la liste des parcelles où peut se déplacer le jardinier
                PossibleMovesResult possibleMovesResultGardener = BoardUtils.getPossibleMoves(player.getBoard().getGardener().getPosition());
                List<Parcel> possibleMoves = possibleMovesResultGardener.getAllParcels();

                //Y en a-t-il une contenant un bambou de la bonne couleur et de la taille -1, avec le bon amménagement ?
                List<Parcel> parcelsExpected = new ArrayList<>();
                List<Parcel> possibleMovesCpy = new ArrayList<>(possibleMoves);
                parcelsExpected.addAll(possibleMovesCpy.stream().filter(parcel -> GardenerStrategyHelpers.getRightBambooParcels(player.getBoard(), parcel, gardenerGoal.getColor(), gardenerGoal.getFacility(), gardenerGoal.getSize() - 1)).collect(Collectors.toList()));

                //Si oui et accessible par le jardinier
                //Déplacer le jardinier dessus
                ActionParams a;

                // Si l'objectif est WHATEVER_FACILITY ou FERTILIZER
                if (gardenerGoal.getFacility() == Facility.WHATEVER_FACILITY || gardenerGoal.getFacility() == Facility.FERTILIZER_FACILITY) {

                    //Y a-t-il une parcelle irriguée de la bonne couleur avec un fertilizer sans bambou ?
                    parcelsExpected = StrategyHelpers.getExpectedParcelsOfColor(false, possibleMoves, gardenerGoal.getColor(), true, player.getBoard(), Facility.FERTILIZER_FACILITY);

                    //Si oui et accessible par le jardinier
                    //Déplacer le jardinier dessus
                    if (player.canExecuteActionType(ActionType.MOVE_GARDENER) && (a = tryMovingPNJToParcel(ActionType.MOVE_GARDENER, false, true, gardenerGoal, player.getBoard().getGardener(), parcelsExpected, player)) != null) {
                        return a;
                    }
                }

                // L'objectif est il avec WHATEVER_FACILITY ou FERTILIZER ?
                if (gardenerGoal.getFacility() == Facility.WHATEVER_FACILITY || gardenerGoal.getFacility() == Facility.FERTILIZER_FACILITY) {

                    //Y a-t-il une parcelle irriguée de la bonne couleur ne contenant aucun bambou ?
                    parcelsExpected = StrategyHelpers.getExpectedParcelsOfColor(false, possibleMoves, gardenerGoal.getColor(), true, player.getBoard(), null);

                    //Si oui et accessible par le jardinier

                    //Est il possible de placer un fertilizer dessus ?
                    if (player.canExecuteActionType(ActionType.PLACE_FACILITY)
                            && player.getInventory().containFacility(Facility.FERTILIZER_FACILITY)
                            && !(foundParcels = BoardUtils.getPossibleFacilityParcels(player.getBoard()).stream()
                            .filter(parcel -> parcel.getFacility() == Facility.FERTILIZER_FACILITY)
                            .filter(parcelsExpected::contains)
                            .collect(Collectors.toList())).isEmpty()) {

                        // Poser un Fertilizer sur une des foundParcels
                        PlaceFacilityParams facilityParams = new PlaceFacilityParams();
                        facilityParams.setFacility(Facility.FERTILIZER_FACILITY);
                        facilityParams.setParcel(foundParcels.get(0));
                        return facilityParams;
                    }

                    //Si non et accessible par le jardinier
                    //Déplacer le jardinier dessus
                    if (player.canExecuteActionType(ActionType.MOVE_GARDENER) && (a = tryMovingPNJToParcel(ActionType.MOVE_GARDENER, false, true, gardenerGoal, player.getBoard().getGardener(), parcelsExpected, player)) != null) {
                        return a;
                    }
                }


                //Y a-t-il une parcelle non irriguée de la bonne couleur et du bon amménagement ?
                parcelsExpected = StrategyHelpers.getExpectedParcelsOfColor(false, possibleMoves, gardenerGoal.getColor(), false, player.getBoard(), gardenerGoal.getFacility());

                // Si l'objectif est PADDOCK ou WHATEVER_FACILITY, poser un paddock dessus
                if (gardenerGoal.getFacility() == Facility.WHATEVER_FACILITY || gardenerGoal.getFacility() == Facility.PADDOCK_FACILITY) {

                    //Est ce qu'on peut placer un paddock dessus ?
                    // A t-on un PaddockFacility dans l'inventaire
                    if (player.canExecuteActionType(ActionType.PLACE_FACILITY)
                            && player.getInventory().containFacility(Facility.PADDOCK_FACILITY)
                            && !(foundParcels = BoardUtils.getPossibleFacilityParcels(player.getBoard()).stream()
                            .filter(parcel -> parcel.getFacility() == Facility.PADDOCK_FACILITY)
                            .filter(parcelsExpected::contains)
                            .collect(Collectors.toList())).isEmpty()) {

                        // Poser un Paddock sur une des foundParcels
                        PlaceFacilityParams facilityParams = new PlaceFacilityParams();
                        facilityParams.setFacility(Facility.PADDOCK_FACILITY);
                        facilityParams.setParcel(foundParcels.get(0));
                        return facilityParams;
                    }
                }

                // Si l'objectif est POND ou WHATEVER_FACILITY
                if (gardenerGoal.getFacility() == Facility.WHATEVER_FACILITY || gardenerGoal.getFacility() == Facility.POND_FACILITY) {

                    //Est ce qu'on peut placer un pond dessus ?
                    // A t-on un PondFacility dans l'inventaire
                    if (player.canExecuteActionType(ActionType.PLACE_FACILITY)
                            && player.getInventory().containFacility(Facility.POND_FACILITY)
                            && !(foundParcels = BoardUtils.getPossibleFacilityParcels(player.getBoard()).stream()
                            .filter(parcel -> parcel.getFacility() == Facility.POND_FACILITY)
                            .filter(parcelsExpected::contains)
                            .collect(Collectors.toList())).isEmpty()) {

                        // Poser un Pond sur une des foundParcels
                        PlaceFacilityParams facilityParams = new PlaceFacilityParams();
                        facilityParams.setFacility(Facility.POND_FACILITY);
                        facilityParams.setParcel(foundParcels.get(0));
                        return facilityParams;
                    }
                }

                if (player.canExecuteActionType(ActionType.MOVE_GARDENER) && (a = tryMovingPNJToParcel(ActionType.MOVE_GARDENER, true, true, gardenerGoal, player.getBoard().getGardener(), parcelsExpected, player)) != null) {
                    return a;
                }

                // Uniquement si l'objectif contient WHATEVER_FACILITY ou FERTILIZER , on regarde cette condition :
                if (gardenerGoal.getFacility() == Facility.WHATEVER_FACILITY || gardenerGoal.getFacility() == Facility.FERTILIZER_FACILITY) {

                    //Y en a-t-il une contenant un bambou de la bonne couleur de la taille -2 avec un fertilizer sur laquelle peut aller le jardinier ?
                    List<Parcel> fertilizerGoodColorParcels = possibleMoves.stream()
                            .filter(parcel -> parcel.getFacility() == Facility.FERTILIZER_FACILITY)
                            .filter(parcel -> parcel.getColor() == gardenerGoal.getColor())
                            .filter(parcel -> parcel.getBambooStack().size() == gardenerGoal.getSize() - 2)
                            .collect(Collectors.toList());

                    // Si oui, déplacer le jardinier dessus
                    if (player.canExecuteActionType(ActionType.MOVE_GARDENER) && (!fertilizerGoodColorParcels.isEmpty())) {
                        MoveGardenerParams moveParams = (MoveGardenerParams) player.getActionFactory().getAction(ActionType.MOVE_GARDENER);
                        moveParams.setParcel(fertilizerGoodColorParcels.get(0));
                        moveParams.setPNJ(player.getBoard().getGardener());
                        return moveParams;
                    }
                }

                //Y a-t-il un bambou de la bonne couleur et de la taille + 1 ?
                //Sur une parcelle sans paddock ?
                parcelsExpected.clear();
                parcelsExpected.addAll(possibleMoves.stream()
                        .filter(parcel -> GardenerStrategyHelpers.getRightBambooParcels(player.getBoard(), parcel, gardenerGoal.getColor(), gardenerGoal.getFacility(), gardenerGoal.getSize() + 1))
                        .filter(parcel -> parcel.getFacility() != Facility.PADDOCK_FACILITY)
                        .collect(Collectors.toList())
                );

                //Est-ce que le panda peut l'atteindre ?
                //Si oui déplacer le panda dessus
                if (player.canExecuteActionType(ActionType.MOVE_PANDA) && (a = tryMovingPNJToParcel(ActionType.MOVE_PANDA, true, true, gardenerGoal, player.getBoard().getPanda(), parcelsExpected, player)) != null) {
                    return a;
                }

                //Y en a-t-il une contenant un bambou de la bonne couleur et de taille inférieure?
                // avec le bon aménagement
                //Ne prend en compte que les parcelles que peut atteindre le Jardinier
                parcelsExpected = GardenerStrategyHelpers.getLessOrMoreBambooParcel(possibleMoves, gardenerGoal, false, player.getBoard());

                //Si oui et accessible par le jardinier
                //Déplacer le jardinier dessus
                if (player.canExecuteActionType(ActionType.MOVE_GARDENER) && (a = tryMovingPNJToParcel(ActionType.MOVE_GARDENER, true, true, gardenerGoal, player.getBoard().getGardener(), parcelsExpected, player)) != null) {
                    return a;
                }

                //Y en a-t-il une contenant un bambou de la bonne couleur et de taille supérieure ?
                //Sans paddock
                parcelsExpected = GardenerStrategyHelpers.getLessOrMoreBambooParcel(
                        possibleMoves.stream()
                        .filter(parcel -> parcel.getFacility() != Facility.PADDOCK_FACILITY)
                        .collect(Collectors.toList()),
                        gardenerGoal, true, player.getBoard()
                );

                //Est-ce que le panda peut l'atteindre ?
                //Si oui déplacer le panda dessus
                if (player.canExecuteActionType(ActionType.MOVE_PANDA) && (a = tryMovingPNJToParcel(ActionType.MOVE_PANDA, true, true, gardenerGoal, player.getBoard().getPanda(), parcelsExpected, player)) != null) {
                    return a;
                }

                //Si non irriguer la parcelle
                PossibleIrrigationResult irrigationResult = BoardUtils.getPossibleIrrigations(player.getBoard());

                // Y a-t-il des irrigations dans l'inventaire
                if (player.getInventory().getIrrigations() > 0) {
                    if (player.canExecuteActionType(ActionType.PLACE_IRRIGATION)
                            && !(possibleExpectedParcels = StrategyHelpers.getExpectedParcelsOfColor(false, BoardUtils.getAllParcels(player.getBoard()), gardenerGoal.getColor(), false, player.getBoard(), gardenerGoal.getFacility())).isEmpty()
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

                possibleGoals.remove(0);

                // Only PlaceParcelParams are returned from here
                PlaceParcelParams placeParcelParams;
                if (player.canExecuteActionType(ActionType.PLACE_PARCEL) && (placeParcelParams = this.tryPlacingParcel(factory, possibleGoals, player, possibleMovesResultGardener)) != null) {
                    return placeParcelParams;
                }

            }

            throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.RANDOM);

        } else {
            // Si elle ne contient pas d'objectif Gardener
            // Y a-t-il des objectifs Jardinier à piocher
            if (StrategyHelpers.goalsDeckContainsGoalType(player.getBoard().getGoalsDeck(), GoalType.GARDENER)) {

                // Y a-t-il moins de 5 objectifs dans la main du joueur
                if (player.canExecuteActionType(ActionType.DRAW_GOAL) && player.getGoals().size() < 5) {

                    // Piocher un objectif Jardinier
                    DrawGoalParams drawGoalParams = (DrawGoalParams) factory.getAction(ActionType.DRAW_GOAL);
                    drawGoalParams.setGoalType(GoalType.GARDENER);
                    return drawGoalParams;
                }

                // La main du joueur est pleine (5 objectifs)
//                else {
//                    int panda = 0;
//                    int parcel = 0;
//                    for (Goal g : player.getGoals()) {
//                        switch (g.getGoalType()) {
//                            case PANDA:
//                                panda++;
//                                break;
//                            case PARCEL:
//                                parcel++;
//                                break;
//                        }
//                    }
//                    if (panda >= parcel) {
//                        throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.PANDA);
//                    } else {
//                        throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.PARCEL);
//                    }
//                }
            }

            // Il n'y a pas d'objectifs Jardinier à piocher
//            else {
//                if (!player.getGoals().stream().filter(goal -> goal.getGoalType().equals(GoalType.PARCEL)).collect(Collectors.toList()).isEmpty()) {
//                    throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.PARCEL);
//                } else if (!player.getGoals().stream().filter(goal -> goal.getGoalType().equals(GoalType.PANDA)).collect(Collectors.toList()).isEmpty()) {
//                    throw new StrategyNotApplicableAnymore(this.getStrategyType(), StrategyType.PANDA);
//                }
//            }

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
        if (board.getFacilityDeck().canDrawFacility(Facility.FERTILIZER_FACILITY)) {
            return Facility.FERTILIZER_FACILITY;
        } else {
            if (board.getFacilityDeck().canDrawFacility(Facility.PADDOCK_FACILITY)) {
                return Facility.PADDOCK_FACILITY;
            } else {
                if (board.getFacilityDeck().canDrawFacility(Facility.POND_FACILITY)) {
                    return Facility.POND_FACILITY;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * TODO doc
     *
     * @param typeOfMove
     * @param withBamboos
     * @param irrigatedOnly
     * @param goal
     * @param pnj
     * @param possibleParcels
     * @param player
     * @return
     */
    private ActionParams tryMovingPNJToParcel(ActionType typeOfMove, boolean withBamboos, boolean irrigatedOnly, GardenerGoal goal, PNJ pnj, List<Parcel> possibleParcels, Player player) {
        List<Parcel> possibleExpectedParcels;

        if (!(possibleExpectedParcels = StrategyHelpers.getExpectedParcelsOfColor(withBamboos, possibleParcels, goal.getColor(), irrigatedOnly, player.getBoard(), null)).isEmpty()) {

            ActionParams moveParams = player.getActionFactory().getAction(typeOfMove);
            if (pnj.getName() == PNJName.GARDENER) {
                ((MoveGardenerParams) moveParams).setParcel(possibleExpectedParcels.get(0));
                ((MoveGardenerParams) moveParams).setPNJ(pnj);
            } else {
                ((MovePandaParams) moveParams).setParcel(possibleExpectedParcels.get(0));
                ((MovePandaParams) moveParams).setPNJ(pnj);
            }

            return moveParams;
        }
        return null;
    }

    /**
     * TODO doc
     *
     * @param factory
     * @param goals
     * @param player
     * @param possibleMovesResultGardener
     * @return
     */
    private PlaceParcelParams tryPlacingParcel(ActionFactory factory, List<GardenerGoal> goals, Player player, PossibleMovesResult possibleMovesResultGardener) {
        PlaceParcelParams parcelParams;
        PossiblePlacingResult possibleParcels = BoardUtils.getPossibleParcelsToPlace(player.getBoard());

        List<GardenerGoal> goalsCopy = new ArrayList<>(goals);
        List<Parcel> possibleParcelsToMoveGardener = possibleMovesResultGardener.getAllParcels();
        List<Parcel> parcelsAccesibleByGardener, goodColorParcelsToDraw, goodColorWithPondParcels;

        while (!goalsCopy.isEmpty()) {

            // Reste-t-il des parcelles dans la pioche
            if (!player.getBoard().getParcelsDeck().isEmpty()) {

                // Piocher une parcelle
                try {
                    List<Parcel> possibleParcelToDraw = factory.pickParcel(player.getBoard().getParcelsDeck());

                    // Y a-t-il une parcelle d'une couleur qui nous intéresse
                    if (!(goodColorParcelsToDraw = possibleParcelToDraw.stream().filter(parcel -> parcel.getColor() == goalsCopy.get(0).getColor()).collect(Collectors.toList())).isEmpty()) {

                        //Si cette parcelle possède un pond
                        if (!(goodColorWithPondParcels = goodColorParcelsToDraw.stream().filter(parcel -> parcel.getFacility() == Facility.POND_FACILITY).collect(Collectors.toList())).isEmpty()) {

                            // la placer à un endroit accessible par le jardinier
                            if ((parcelParams = StrategyHelpers.tryPlacingParcelAccessibleByPNJ(factory, possibleMovesResultGardener, possibleParcelsToMoveGardener, possibleParcels, goodColorWithPondParcels.get(0))) != null) {
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
                        if (!(parcelsAccesibleByGardener = possibleParcelsToMoveGardener.stream().filter(parcel -> BoardUtils.isParcelIrrigated(player.getBoard(), parcel)).collect(Collectors.toList())).isEmpty()) {

                            if ((parcelParams = StrategyHelpers.tryPlacingParcelAccessibleByPNJ(factory, possibleMovesResultGardener, parcelsAccesibleByGardener, possibleParcels, goodColorParcelsToDraw.get(0))) != null) {
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
                                // Placer les parcelle
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

            goalsCopy.remove(0);
        }

        return null;
    }
}
