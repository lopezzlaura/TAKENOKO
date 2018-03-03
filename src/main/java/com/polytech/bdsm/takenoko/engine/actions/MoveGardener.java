package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.graph.GraphNode;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import com.polytech.bdsm.takenoko.engine.pnj.PNJ;

import java.util.List;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

class MoveGardener extends Move {

    /**
     * Normal override constructor
     *
     * @param player       Player that do the action
     * @param parcelToMove The Parcel where the PNJ will go
     * @param gardener     The <code>Gardener</code>
     */
    public MoveGardener(Player player, Parcel parcelToMove, PNJ gardener) {
        super(player, parcelToMove, gardener);
    }

    /**
     * Method to display the action name
     *
     * @return String of the name of the action
     */
    @Override
    public String getName() {
        if(this.parcelToMove.getColor() == null) {
            return " moved the Gardener to the Pond Parcel.";
        }
        return " moved the Gardener to " + this.parcelToMove.getCoordinate() + " and that parcel has now " + parcelToMove.getBambooStack().size() + " " + parcelToMove.getColor() + " bamboo(s).";
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
     * Meaning move the <code>Gardener</code>, checking if the move is possible
     * and if the <code>Parcel</code> is irrigated so that a <code>Bamboo</code> can grow
     *
     * @return true if the move could be done, false otherwise
     */
    @Override
    public boolean execute() {
        List<Parcel> parcels = BoardUtils.getPossibleMoves(this.pnj.getPosition()).getAllParcels();
        parcels.remove(this.pnj.getPosition());

        if (parcels.contains(parcelToMove)) {
            this.pnj.move(this.parcelToMove);
            if (BoardUtils.isParcelIrrigated(player.getBoard(), parcelToMove)) {
                parcelToMove.pushBamboo();
            }
            GraphNode[] neighbours = parcelToMove.getNeighbours();
            for (GraphNode gn : neighbours) {
                Parcel p = (Parcel) gn;
                if (p != null && BoardUtils.isParcelIrrigated(player.getBoard(), p) && (p.getColor() == parcelToMove.getColor())) {
                    p.pushBamboo();
                }
            }

            return true;
        }
        return false;
    }

    /**
     * Equals method for the {@code MoveGardener} action
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MoveGardener)) return false;
        MoveGardener mg = (MoveGardener) obj;
        return this.parcelToMove.equals(mg.parcelToMove) && this.pnj.equals(mg.pnj);
    }

}
