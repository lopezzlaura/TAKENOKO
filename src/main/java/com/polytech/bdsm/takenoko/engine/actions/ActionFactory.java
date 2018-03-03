package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.actions.params.*;
import com.polytech.bdsm.takenoko.engine.actions.weather.WeatherDice;
import com.polytech.bdsm.takenoko.engine.board.Irrigation;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.ParcelFactory;
import com.polytech.bdsm.takenoko.engine.player.Player;
import com.polytech.bdsm.takenoko.engine.pnj.PNJ;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 * @see Action
 */
public class ActionFactory {

    private final AtomicBoolean parcelsPicked = new AtomicBoolean(false);
    private List<Parcel> parcelsToPlace = new ArrayList<>();

    /**
     * Get the action params corresponding to the
     * given action type
     *
     * @param actionType The ActionType given
     * @return the action params
     */
    public ActionParams getAction(ActionType actionType) {
        ActionParams actionParams;

        switch (actionType) {
            case MOVE_PANDA:
                actionParams = new MovePandaParams();
                break;
            case MOVE_GARDENER:
                actionParams = new MoveGardenerParams();
                break;
            case PLACE_PARCEL:
                actionParams = new PlaceParcelParams();
                break;
            case DRAW_IRRIGATION:
                actionParams = new DrawIrrigationParams();
                break;
            case PLACE_IRRIGATION:
                actionParams = new PlaceIrrigationParams();
                break;
            case VALIDATE_GOAL:
                actionParams = new ValidateGoalParams();
                break;
            case DRAW_GOAL:
                actionParams = new DrawGoalParams();
                break;
            case NONE:
            default:
                actionParams = new NoneParams();
                break;
        }

        return actionParams;
    }

    /**
     * Player decide to do an action based on its action params
     *
     * @param actionParams The ActionParams class referring to the action
     * @param player       The player to do the action
     * @param result       The result of the executed action
     * @return true if the action is correctly executed, false otherwise
     */
    public boolean doAction(ActionParams actionParams, Player player, ActionResult result) {
        if (actionParams.paramsNotNull()) {
            for (ActionType actionType : player.getActionList()) {
                if (actionParams.getActionType() == actionType) {
                    if (actionType.execute(player, actionParams, result)) {

                        // Decrement the number of actions of the player
                        if (result.getAction().isActionDecrementedFromPlayer()) {
                            player.removeActionType(actionType);
                            player.removeNbActions(1);

                            // End the turn if nbActions is 0
                            if (player.getNbActions() == 0) {
                                player.endTurn();
                            }
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Method to execute the Weather Action
     *
     * @param actionParams The ActionParams class referring to the action
     * @param player       The player to do the action
     * @param result       The result of the executed action
     * @return True if the action is correctly executed, false otherwise
     */
    public boolean doWeatherAction(WeatherDice weatherDice, ActionParams actionParams, Player player, ActionResult result) {
        if (actionParams.paramsNotNull()
                && weatherDice.getWeatherParams().getActionType() == actionParams.getActionType()
                && actionParams.getActionType().execute(player, actionParams, result)) {
            return true;
        } else if (actionParams.getActionType() == ActionType.NONE
                && actionParams.getActionType().execute(player, actionParams, result)) {
            player.removeActionType(actionParams.getActionType());
            return true;
        }

        return false;
    }

    /**
     * Get a new Move Action for a player
     * Either to move the panda or the gardener
     *
     * @param player       The player to create the Action
     * @param pnj          The pnj that should move
     * @param parcelToMove The parcel where the pnj move
     * @return The {@code Move} Action created
     * @throws ActionRefusedException When a player picked a parcel without placed it
     */
    Action newMoveAction(Player player, PNJ pnj, Parcel parcelToMove) throws ActionRefusedException {
        if (parcelsPicked.get()) throw new ActionRefusedException("Player has to place a parcel");

        switch (pnj.getName()) {
            case PANDA:
                return new MovePanda(player, parcelToMove, pnj);
            case GARDENER:
                return new MoveGardener(player, parcelToMove, pnj);
            default:
                throw new IllegalArgumentException("PNJ name doesn't exists : " + pnj.getName());
        }
    }

    /**
     * Draw an irrigation from the deck
     *
     * @param player The player that draw an irrigation
     * @return The {@code DrawIrrigation} Action created
     */
    Action newDrawIrrigation(Player player) throws ActionRefusedException {
        if (parcelsPicked.get()) throw new ActionRefusedException("Player has to place a parcel");

        return new DrawIrrigation(player);
    }

    /**
     * Get a new Place Irrigation Action for a player
     *
     * @param player     The player to create the Action
     * @param irrigation The irrigation to place
     * @return The {@code PlaceIrrigation} Action created
     * @throws ActionRefusedException When a player picked a parcel without placed it
     */
    Action newPlaceIrrigation(Player player, Irrigation irrigation) throws ActionRefusedException {
        if (parcelsPicked.get()) throw new ActionRefusedException("Player has to place a parcel");

        return new PlaceIrrigation(player, irrigation);
    }

    /**
     * Get a new Validate Goal Action for a player
     *
     * @param player The player to create the Action
     * @param goal   The goal to validate
     * @return The {@code ValidateGoal} Action created
     * @throws ActionRefusedException When a player picked a parcel without placed it
     */
    Action newValidateGoal(Player player, Goal goal) throws ActionRefusedException {
        if (parcelsPicked.get()) throw new ActionRefusedException("Player has to place a parcel");

        return new ValidateGoal(player, goal);
    }

    /**
     * Pick three parcels in the deck of parcels given in parameter
     * The player has to place a parcel after picked it
     *
     * @param parcelFactory The parcels deck to pick
     * @return Three parcels of the deck to chose one by the player
     * @throws ActionRefusedException When an Exception occurred with the deck
     */
    public List<Parcel> pickParcel(ParcelFactory parcelFactory) throws ActionRefusedException {
        this.parcelsPicked.set(true);

        try {
            this.parcelsToPlace = parcelFactory.getParcelsWithoutDrawing(3);
            return this.parcelsToPlace;
        } catch (Exception e) {
            this.parcelsPicked.set(false);
            throw new ActionRefusedException(e.getMessage());
        }
    }

    /**
     * Get a new Place Parcel Action for a player
     * The player has to pick a list of parcel in the deck before using this
     *
     * @param player  The player to create the Action
     * @param toPlace One of the parcel chosen to place on the board
     * @param from    The parcel chosen to place the new parcel next to it
     * @param index   The position to place the new parcel on the from parcel
     * @return The {@code PlaceParcel} created
     * @throws Exception Whenever the player hasn't pick parcels, or if an Exception is raised by the deck
     */
    Action newPlaceParcel(Player player, Parcel toPlace, Parcel from, int index) throws Exception {
        if (!parcelsPicked.get()) throw new ActionRefusedException("Player has to pick parcels");
        if (!parcelsToPlace.contains(toPlace))
            throw new IllegalArgumentException("The parcel to place has to be in the possible parcels picked");

        try {
            Parcel finalParcelToPlace = player.getBoard().getParcelsDeck().drawParcel(toPlace);
            return new PlaceParcel(player, finalParcelToPlace, from, index);
        } catch (Exception e) {
            throw e;
        } finally {
            this.parcelsPicked.set(false);
        }
    }

    /**
     * Get a new Draw Goal Action for a player
     *
     * @param player   The player doing the action
     * @param goalType The type of the goal the player want to draw
     * @return The {@code DrawGoal} created
     * @throws ActionRefusedException When a player picked a parcel without placed it
     */
    Action newDrawGoal(Player player, GoalType goalType) throws ActionRefusedException {
        if (parcelsPicked.get()) throw new ActionRefusedException("Player has to place a parcel");
        return new DrawGoal(player, goalType);
    }

    /**
     * Get a new Place Facility for a Player
     *
     * @param player   The Player doing the Action
     * @param parcel   The parcel to add a Facility on
     * @param facility The Facility to add on the Parcel
     * @return The {@code PlaceFacility} Action created
     * @throws ActionRefusedException When a player picked a parcel without placed it
     */
    Action newPlaceFacility(Player player, Parcel parcel, Facility facility) throws ActionRefusedException {
        if (parcelsPicked.get()) throw new ActionRefusedException("Player has to place a parcel");
        return new PlaceFacility(player, parcel, facility);
    }

    /**
     * Get a new None Action for the {@code Player}
     *
     * @param player The Player doing the action
     * @return The {@code None} Action created
     */
    Action newNone(Player player) {
        return new None(player);
    }

    /**
     * Clone method for the {@code ActionFactory}
     *
     * @return a copy of the {@code ActionFactory}
     */
    @Override
    public Object clone() {
        ActionFactory cpy = new ActionFactory();
        cpy.parcelsToPlace = new ArrayList<>(this.parcelsToPlace);
        cpy.parcelsPicked.set(this.parcelsPicked.get());
        return cpy;
    }
}
