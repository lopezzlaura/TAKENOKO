package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import com.polytech.bdsm.takenoko.engine.pnj.PNJ;

import java.util.List;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class MovePanda extends Move {

    /**
     * Normal override constructor
     *
     * @param player       Player that do the action
     * @param parcelToMove The Parcel where the PNJ will go
     * @param panda        the <code>Panda</code>
     */
    public MovePanda(Player player, Parcel parcelToMove, PNJ panda) {
        super(player, parcelToMove, panda);
    }

    /**
     * Getter for the name of the {@code MovePanda} action
     *
     * @return The name of the action and what it does
     */
    @Override
    public String getName() {
        if(this.parcelToMove.getColor() == null) {
            return " moved the Panda to the Pond Parcel.";
        }
        return " moved the Panda to " + parcelToMove.getCoordinate() + " and has now " + this.player.getInventory().getBamboosCount(Color.GREEN) + " green bamboo(s), " + this.player.getInventory().getBamboosCount(Color.PINK) + " pink bamboo(s), " + this.player.getInventory().getBamboosCount(Color.YELLOW) + " yellow bamboo(s).";
    }


    /**
     * Check if the Action is recorded as an action for the player
     *
     * @return True if the Action counts in the possible actions of the player for a turn,
     *         false otherwise.
     */
    @Override
    public boolean isActionDecrementedFromPlayer() {
        return true;
    }


    /**
     * Execute the action
     * Meaning move the <code>Panda</code>, checking if the move is possible
     * and adding a <code>Bamboo</code> to the <code>Player</code> inventory
     *
     * @return true if the move could be done, false otherwise
     */
    @Override
    public boolean execute() {
        List<Parcel> parcels = BoardUtils.getPossibleMoves(this.pnj.getPosition()).getAllParcels();
        parcels.remove(this.pnj.getPosition());

        if (parcels.contains(parcelToMove)) {
            Bamboo bamboo = this.pnj.move(this.parcelToMove);
            if (bamboo != null) {
                this.player.getInventory().addBamboo(bamboo);
            }
            return true;
        }
        return false;
    }

    /**
     * Equals method for the {@code MovePanda} action
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MovePanda)) return false;
        MovePanda mp = (MovePanda) obj;
        return this.parcelToMove.equals(mp.parcelToMove) && this.pnj.equals(mp.pnj);
    }
}
