package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.DrawGoalParams;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class MultipleStrategy implements Strategy {

    private Strategy current;

    /**
     * Getter of the Strategy Type
     *
     * @return This StrategyType
     */
    @Override
    public StrategyType getStrategyType() {
        return StrategyType.MULTIPLE;
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

        if (player.getGoals().isEmpty()
                && player.canExecuteActionType(ActionType.DRAW_GOAL)
                && StrategyHelpers.goalsDeckContainsGoalType(player.getBoard().getGoalsDeck(), GoalType.PANDA)) {
            DrawGoalParams drawGoalParams = new DrawGoalParams();
            drawGoalParams.setGoalType(GoalType.PANDA);
            return drawGoalParams;
        }

        GoalType prioritizedGoalType = MultipleStrategyHelper.chooseBestGoalType(player);

        switch (prioritizedGoalType) {
            case PANDA:
                try {
                    current = new PandaStrategy();
                    return current.chooseBestMove(player);
                } catch (StrategyNotApplicableAnymore ignored) {}
            case GARDENER:
                try {
                    current = new GardenerStrategy();
                    return current.chooseBestMove(player);
                } catch (StrategyNotApplicableAnymore ignored) {}
            case PARCEL:
                try {
                    current = new ParcelStrategy();
                    return current.chooseBestMove(player);
                } catch (StrategyNotApplicableAnymore ignored) {}
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
        if (current != null) {
            return current.chooseBestFacilityToDraw(board);
        }

        return new PandaStrategy().chooseBestFacilityToDraw(board);
    }
}
