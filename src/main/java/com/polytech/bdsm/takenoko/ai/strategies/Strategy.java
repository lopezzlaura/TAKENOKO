package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */
public interface Strategy {

    /**
     * Getter of the strategy type
     *
     * @return The StrategyType value
     */
    StrategyType getStrategyType();

    /**
     * Choose the best next move for a given player at a state of the game,
     * accorded to this strategy
     *
     * @param player    The player using this strategy
     * @return          The ActionParams already filled for the action to do
     * @throws StrategyNotApplicableAnymore When this strategy decide that it's worth it to do an other strategy
     */
    ActionParams chooseBestMove(Player player) throws StrategyNotApplicableAnymore;

    /**
     * Choose the best facility to draw accorded to this strategy and a given board
     *
     * @param board The board to analyse
     * @return      The facility type to choose
     */
    Facility chooseBestFacilityToDraw(Board board);
}
