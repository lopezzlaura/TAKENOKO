package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import com.polytech.bdsm.takenoko.engine.pnj.PNJ;


/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
abstract class Move extends Action {

    // Parameters of a Move Action
    protected Parcel parcelToMove;
    protected PNJ pnj;

    /**
     * Normal override constructor
     *
     * @param player       <code>Player</code> that do the action
     * @param parcelToMove The <code>Parcel</code> where the <code>PNJ</code> will go
     * @param pnj          the <code>PNJ</code> to be moved
     */
    public Move(Player player, Parcel parcelToMove, PNJ pnj) {
        super(player);
        this.parcelToMove = parcelToMove;
        this.pnj = pnj;
    }

    /**
     * Getter for the {@code PNJ} to move
     *
     * @return The {@code PNJ} to move
     */
    public PNJ getPNJ() {
        return this.pnj;
    }
}
