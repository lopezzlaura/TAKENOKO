package com.polytech.bdsm.takenoko.engine.actions.weather;

import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class StormAction extends WeatherAction {

    private Parcel parcel;

    /**
     * Normal override constructor
     *
     * @param player Player that do the action
     */
    public StormAction(Player player, Parcel parcel) {
        super(player);
        this.parcel = parcel;
    }

    /**
     * Getter for the name of Storm Weather
     *
     * @return a String explaining the weather and the action the player did
     */
    @Override
    public String getName() {
        return "Storm weather : " + player + " can move the Panda to a parcel, on which the Panda will eat a bamboo." +
                "\n\t\t\t" + player + " moved the Panda to " + parcel.getCoordinate().toString() + " and has " + player.getInventory().getBamboosCount(Color.GREEN) + " green bamboo(s), " +
                player.getInventory().getBamboosCount(Color.PINK) + " pink bamboo(s), " + player.getInventory().getBamboosCount(Color.YELLOW) + " yellow bamboo(s).";
    }

    /**
     * Check if the Action is recorded as an action for the player
     *
     * @return True if the Action counts in the possible actions of the player for a turn,
     *         false otherwise.
     */
    @Override
    public boolean isActionDecrementedFromPlayer() {
        return false;
    }

    /**
     * Method to move the <code>Panda</code>, checking if the move is possible
     * and possibly adding a <code>Bamboo</code> to the <code>Player</code>
     * inventory
     *
     * @return true if the move could be done, false otherwise
     */
    public boolean execute() {
        Bamboo bamboo = player.getBoard().getPanda().move(parcel);

        if (bamboo != null) {
            player.getInventory().addBamboo(bamboo);
        }

        return true;
    }

    /**
     * Equals method for the {@code StormAction}
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof StormAction;
    }
}
