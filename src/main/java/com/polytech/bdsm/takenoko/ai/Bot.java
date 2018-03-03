package com.polytech.bdsm.takenoko.ai;

import com.polytech.bdsm.takenoko.ai.strategies.RandomStrategy;
import com.polytech.bdsm.takenoko.ai.strategies.Strategy;
import com.polytech.bdsm.takenoko.ai.strategies.StrategyHelpers;
import com.polytech.bdsm.takenoko.ai.strategies.StrategyNotApplicableAnymore;
import com.polytech.bdsm.takenoko.engine.actions.ActionFactory;
import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.*;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.CloudsActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.RainActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.StormActionParams;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.board.Irrigation;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.GardenerGoal;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.goals.PandaGoal;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Inventory;
import com.polytech.bdsm.takenoko.engine.player.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 * @see Player
 * @see Board
 * @see Strategy
 */
public class Bot extends Player {

    private Strategy strategy, initialStrategy;

    /**
     * Normal constructor for the bot
     *
     * @param name  Name of the Player
     * @param board The Game Board
     * @see Player#Player(String, Board, Random)
     */
    public Bot(String name, Board board, Strategy strategy, Random randomizer) {
        super(name, board, randomizer);
        this.strategy = strategy;
        this.initialStrategy = strategy;
    }

    /**
     * Normal constructor for the bot
     *
     * @param name   Name of the Player
     * @param board  The Game Board
     * @param height The height of the Player
     * @see Player#Player(String, Board, int, Random)
     */
    public Bot(String name, Board board, Strategy strategy, int height, Random randomizer) {
        super(name, board, height, randomizer);
        this.strategy = strategy;
        this.initialStrategy = strategy;
    }

    /**
     * Constructor for the bot
     *
     * @param name          Name of the Player
     * @param clone         The Game Board
     * @param strategy      The Strategy the bot will adopt
     * @param height        The height of the Player
     * @param noInitialDraw Not draw card as it will do at the start of the game
     */
    public Bot(String name, Board clone, Strategy strategy, int height, boolean noInitialDraw, Random randomizer) {
        super(name, clone, height, noInitialDraw, randomizer);
        this.strategy = strategy;
        this.initialStrategy = strategy;
    }

    /**
     * Getter for the name of the Bot with his strategy type
     *
     * @return The name of the Bot
     */
    public String getName() {
        return super.getName() + " with " + this.initialStrategy.getStrategyType().toString().toLowerCase() + " strategy";
    }

    /**
     * Method to determine which action the player will do depending on the weather
     *
     * @param weatherParams The weather for this turn
     * @return Params for an action
     */
    @Override
    public ActionParams getWeatherMove(ActionParams weatherParams) {

        List<Parcel> allParcels = BoardUtils.getAllParcels(this.board);
        Parcel chosenParcel;

        switch (weatherParams.getActionType()) {
            case STORM_ACTION:

                if (!allParcels.isEmpty()) {

                    // Retirer la position actuelle du Panda
                    allParcels.remove(this.board.getPanda().getPosition());

                    // Si le joueur possède un objectif panda
                    if (StrategyHelpers.handContainsGoalType(this.goals, GoalType.PANDA)) {

                        // Déplacer le panda sur une parcelle de la couleur de l'objectif
                        Color chosenColor = ((PandaGoal) this.goals.stream().filter(goal -> goal.getGoalType() == GoalType.PANDA).collect(Collectors.toList()).get(0)).getBamboosToCollect().get(0).getColor();
                        List<Parcel> parcelsOfGoodColor = allParcels.stream().filter(parcel -> parcel.getColor() == chosenColor).collect(Collectors.toList());

                        // S'il y a des parcelles de la bonne couleur
                        if (!parcelsOfGoodColor.isEmpty()) {
                            parcelsOfGoodColor.sort(Comparator.comparingInt(o -> o.getBambooStack().size()));
                            Collections.reverse(parcelsOfGoodColor);
                            chosenParcel = parcelsOfGoodColor.get(0);

                            ((StormActionParams) weatherParams).setParcel(chosenParcel);
                            return weatherParams;
                        }
                    } else if (StrategyHelpers.handContainsGoalType(this.goals, GoalType.GARDENER)) {

                        // Déplacer le panda sur une parcelle de la couleur de l'objectif et dont le nombre de bambous est inférieur à celui souhaité pour l'objectif
                        Color chosenColor = ((GardenerGoal) this.goals.stream().filter(goal -> goal.getGoalType() == GoalType.GARDENER).collect(Collectors.toList()).get(0)).getColor();
                        int bambooSize = ((GardenerGoal) this.goals.stream().filter(goal -> goal.getGoalType() == GoalType.GARDENER).collect(Collectors.toList()).get(0)).getSize();
                        List<Parcel> parcelsOfGoodColor = allParcels.stream().filter(parcel -> parcel.getColor() == chosenColor).filter(parcel -> parcel.getBambooStack().size() < bambooSize).collect(Collectors.toList());

                        // S'il y a des parcelles correspondantes
                        if (!parcelsOfGoodColor.isEmpty()) {
                            parcelsOfGoodColor.sort(Comparator.comparingInt(o -> o.getBambooStack().size()));
                            Collections.reverse(parcelsOfGoodColor);
                            chosenParcel = parcelsOfGoodColor.get(0);

                            ((StormActionParams) weatherParams).setParcel(chosenParcel);
                            return weatherParams;
                        }
                    }
                    allParcels.sort(Comparator.comparingInt(o -> o.getBambooStack().size()));
                    Collections.reverse(allParcels);
                    chosenParcel = allParcels.get(0);

                    ((StormActionParams) weatherParams).setParcel(chosenParcel);
                    return weatherParams;
                }

                return new NoneParams();
            case RAIN_ACTION:

                //Sélectionner toutes les parcelles irriguées sur lesquelles on peut faire pousser du bambou
                List<Parcel> parcelsCopy, allIrrigatedParcels = new ArrayList<>(allParcels).stream()
                        .filter(parcel -> BoardUtils.isParcelIrrigated(board, parcel))
                        .filter(parcel -> parcel.getBambooStack().size() < 4).collect(Collectors.toList());

                // On enlève la pondParcel
                allIrrigatedParcels.remove(board.getPond());

                if (!allIrrigatedParcels.isEmpty()) {

                    // Si le joueur possède un objectif Gardener
                    if (StrategyHelpers.handContainsGoalType(this.goals, GoalType.GARDENER)) {

                        //Si le joueur possède un ou plusieurs objectifs Gardener
                        List<Goal> possibleGoals = this.getGoals().stream().filter(goal -> goal.getGoalType().equals(GoalType.GARDENER)).collect(Collectors.toList());

                        while (!possibleGoals.isEmpty()) {
                            GardenerGoal goalTested = (GardenerGoal) possibleGoals.get(0);

                            //Si il y a sur le plateau une parcelle de la bonne couleur
                            List<Parcel> parcelsOfGoodColor = allIrrigatedParcels.stream().filter(parcel -> parcel.getColor() == goalTested.getColor()).collect(Collectors.toList());

                            if (!parcelsOfGoodColor.isEmpty()) {
                                //Si elle possède un bamboo de taille - 1
                                List<Parcel> testedParcels = parcelsOfGoodColor.stream().filter(parcel -> parcel.getBambooStack().size() < goalTested.getSize()).collect(Collectors.toList());

                                if (!testedParcels.isEmpty()) {
                                    chosenParcel = testedParcels.get(0);
                                    ((RainActionParams) weatherParams).setParcel(chosenParcel);
                                    return weatherParams;
                                } else {
                                    testedParcels = parcelsOfGoodColor.stream().filter(parcel -> parcel.getBambooStack().size() < goalTested.getSize()).collect(Collectors.toList());

                                    //Sinon si elle possède un bamboo de taille inférieure
                                    if (!testedParcels.isEmpty()) {
                                        testedParcels.sort(Comparator.comparingInt(o -> o.getBambooStack().size()));
                                        Collections.reverse(testedParcels);
                                        chosenParcel = testedParcels.get(0);

                                        ((RainActionParams) weatherParams).setParcel(chosenParcel);
                                        return weatherParams;
                                    }
                                }
                            }
                            possibleGoals.remove(0);
                        }

                        //Sinon si possède objectif panda
                    } else if (StrategyHelpers.handContainsGoalType(this.goals, GoalType.PANDA)) {

                        //Fait pousser le bamboo sur une parcelle de la bonne couleur
                        Color chosenColor = ((PandaGoal) this.goals.stream().filter(goal -> goal.getGoalType() == GoalType.PANDA).collect(Collectors.toList()).get(0)).getBamboosToCollect().get(0).getColor();
                        List<Parcel> parcelsOfGoodColor = allIrrigatedParcels.stream().filter(parcel -> parcel.getColor() == chosenColor).collect(Collectors.toList());

                        // S'il y a des parcelles de la bonne couleur
                        if (!parcelsOfGoodColor.isEmpty()) {
                            parcelsOfGoodColor.sort(Comparator.comparingInt(o -> o.getBambooStack().size()));
                            Collections.reverse(parcelsOfGoodColor);
                            chosenParcel = parcelsOfGoodColor.get(0);

                            ((RainActionParams) weatherParams).setParcel(chosenParcel);
                            return weatherParams;
                        }
                    }

                    allIrrigatedParcels.sort(Comparator.comparingInt(o -> o.getBambooStack().size()));
                    chosenParcel = allIrrigatedParcels.get(0);

                    ((RainActionParams) weatherParams).setParcel(chosenParcel);
                    return weatherParams;
                }

                return new NoneParams();
            case CLOUDS_ACTION:
                Facility facility;
                if ((facility = this.strategy.chooseBestFacilityToDraw(board)) != null) {
                    ((CloudsActionParams) weatherParams).setFacility(facility);
                } else {
                    return StrategyHelpers.chooseWeather(this);
                }
                break;
            case WIND_ACTION:
                break;
            case UNKNOWN_ACTION:
                return StrategyHelpers.chooseWeather(this);
        }
        return weatherParams;
    }


    /**
     * Getter for the strategy decided when the bot was instantiated
     *
     * @return The initial strategy
     */

    public Strategy getInitialStrategy() {
        return initialStrategy;
    }

    /**
     * Get the next move decided by the Bot
     *
     * @return The move that the Bot decided was best to play
     */
    @Override
    public ActionParams getNextMove() {
        ActionParams action;

        if ((action = this.goalValidation()) != null) {
            return action;
        }

        while (action == null) {
            try {
                action = this.strategy.chooseBestMove(this);
            } catch (StrategyNotApplicableAnymore strategyNotApplicableAnymore) {
                this.strategy = strategyNotApplicableAnymore.getNewStrategyProposed().newStrategyInstance();
            }
        }

        return action;
    }

    /**
     * Method to get a new action
     *
     * @param wrongAction the action that can't be done anymore
     * @return params for an action that is applicable
     */
    @Override
    public ActionParams requestOtherMove(ActionParams wrongAction) {
        ActionParams res = null;
        try {
            res = new RandomStrategy(true).chooseBestMove(this);
        } catch (StrategyNotApplicableAnymore ignored) {
        }

        return res;
    }

    /**
     * Method to determine the params for validate a goal
     *
     * @return params for validate a goal
     */
    private ActionParams goalValidation() {
        Goal g = checkGoal();

        if (g != null) {
            ActionParams action = actionFactory.getAction(ActionType.VALIDATE_GOAL);
            ((ValidateGoalParams) action).setGoal(g);
            return action;
        }

        return null;
    }

    /**
     * Method to check if the player have goal card that can be validated
     *
     * @return The goal that is completed
     */
    public Goal checkGoal() {
        for (Goal goal : getGoals()) {
            if (goal.isValid(this)) {
                return goal;
            }
        }

        return null;
    }

    /**
     * Check if an ActionParams can be executed without problems
     *
     * @param actionParams the ActionParams to check
     * @return true if it can be executed, false either
     */
    public boolean canExecuteAction(ActionParams actionParams) {
        Bot copy = (Bot) this.clone();

        ActionResult resultTest = new ActionResult();
        ActionParams paramsCopy;

        // Special cases
        if (actionParams.getActionType() == ActionType.MOVE_PANDA) {
            paramsCopy = new MovePandaParams();
            ((MovePandaParams) paramsCopy).setPNJ(copy.getBoard().getPanda());
            try {
                ((MovePandaParams) paramsCopy).setParcel(copy.getBoard().getParcel(((MovePandaParams) actionParams).getParcel()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (actionParams.getActionType() == ActionType.MOVE_GARDENER) {
            paramsCopy = new MoveGardenerParams();
            ((MoveGardenerParams) paramsCopy).setPNJ(copy.getBoard().getGardener());
            try {
                ((MoveGardenerParams) paramsCopy).setParcel(copy.getBoard().getParcel(((MoveGardenerParams) actionParams).getParcel()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (actionParams.getActionType() == ActionType.PLACE_IRRIGATION) {
            paramsCopy = new PlaceIrrigationParams();
            try {
                ((PlaceIrrigationParams) paramsCopy).setIrrigation(
                        new Irrigation(
                                copy.getBoard().getParcel(((PlaceIrrigationParams) actionParams).getIrrigation().getParcelToTheLeft()),
                                copy.getBoard().getParcel(((PlaceIrrigationParams) actionParams).getIrrigation().getParcelToTheRight())
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (actionParams.getActionType() == ActionType.PLACE_PARCEL) {
            paramsCopy = new PlaceParcelParams();
            try {
                ((PlaceParcelParams) paramsCopy).setFrom(copy.getBoard().getParcel(((PlaceParcelParams) actionParams).getFrom()));
                ((PlaceParcelParams) paramsCopy).setToPlace((Parcel) ((PlaceParcelParams) actionParams).getToPlace().clone());
                ((PlaceParcelParams) paramsCopy).setIndexToPlace(((PlaceParcelParams) actionParams).getIndexToPlace());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (actionParams.getActionType() == ActionType.PLACE_FACILITY) {
            paramsCopy = new PlaceFacilityParams();
            try {
                ((PlaceFacilityParams) paramsCopy).setParcel(copy.getBoard().getParcel(((PlaceFacilityParams) actionParams).getParcel()));
                ((PlaceFacilityParams) paramsCopy).setFacility(((PlaceFacilityParams) actionParams).getFacility());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            paramsCopy = (ActionParams) actionParams.clone();
        }

        boolean res = actionParams.getActionType().execute(copy, paramsCopy, resultTest);

        return this.actionList.contains(actionParams.getActionType())
                && res
                && resultTest.exists();
    }

    /**
     * Clone method
     *
     * @return the cloned object
     */
    @Override
    public Object clone() {
        Bot cpy = new Bot(this.name, (Board) this.board.clone(), this.strategy, this.height, true, this.randomizer);
        cpy.score = this.score;
        cpy.inventory = (Inventory) this.inventory.clone();
        cpy.goals = new ArrayList<>();
        for (Goal goal : this.goals) {
            cpy.goals.add((Goal) goal.clone());
        }
        cpy.actionList = new ArrayList<>(this.actionList);
        cpy.actionFactory = (ActionFactory) this.actionFactory.clone();
        return cpy;
    }

}
