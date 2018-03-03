package com.polytech.bdsm.takenoko.ai.neuralnet.adapter;

import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class PlayerAdapter {

    /**
     *
     * @param player
     */
    public PlayerAdapter(Player player) {

    }

    /**
     * Getter of the inputs needed by the NeuralNetwork
     *
     * @return the tab of double for the inputs
     */
    public double[] getInputs() {
        return new double[0];
    }
}
