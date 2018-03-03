package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DrawGoalTest {

    private static Player player, player2;
    private static Board board;

    @BeforeAll
    static void setUp() {

        board = new Board();
        player = new Bot("Jean", board, null, new Random());
        player2 = new Bot("Marc", board, new PandaStrategy(), new Random());

    }

    @Test
    void execute() {

        //Basic tests
            //Pre-check
            assertEquals(3, player.getGoals().size());
            assertEquals(GoalType.PANDA, player.getGoals().get(0).getGoalType());
            assertEquals(GoalType.GARDENER, player.getGoals().get(1).getGoalType());
            assertEquals(GoalType.PARCEL, player.getGoals().get(2).getGoalType());

            //Action execution
            assertTrue(new DrawGoal(player, GoalType.GARDENER).execute());

            //Post-check
            assertEquals(4, player.getGoals().size());
            assertEquals(GoalType.GARDENER, player.getGoals().get(3).getGoalType());

            //Action execution
            assertTrue(new DrawGoal(player, GoalType.EMPEROR).execute());

            //Post-check
            assertEquals(5, player.getGoals().size());
            assertEquals(GoalType.EMPEROR, player.getGoals().get(4).getGoalType());

        //Draw goal but player already has 5 goals in his hand
            //Pre-check
            assertEquals(5, player.getGoals().size());

            //Action execution
            Action a = new DrawGoal(player, GoalType.PARCEL);
            assertFalse(a.execute());

            //Post-check
            assertEquals(5, player.getGoals().size());

        //Draw goal but goal deck is empty
            //empty goal deck
            List<Goal> pandaGoals = board.getGoalsDeck().getGoals(GoalType.PANDA);
            List<Goal> gardenerGoals = board.getGoalsDeck().getGoals(GoalType.GARDENER);
            List<Goal> parcelGoals = board.getGoalsDeck().getGoals(GoalType.PARCEL);
            while(!board.getGoalsDeck().isEmpty()){
                if(!board.getGoalsDeck().isEmpty() && !pandaGoals.isEmpty()) {
                    board.getGoalsDeck().drawGoal(GoalType.PANDA);
                    pandaGoals = board.getGoalsDeck().getGoals(GoalType.PANDA);
                }
                if(!board.getGoalsDeck().isEmpty() && !gardenerGoals.isEmpty()) {
                    board.getGoalsDeck().drawGoal(GoalType.GARDENER);
                    gardenerGoals = board.getGoalsDeck().getGoals(GoalType.GARDENER);
                }
                if(!board.getGoalsDeck().isEmpty() && !parcelGoals.isEmpty()) {
                    board.getGoalsDeck().drawGoal(GoalType.PARCEL);
                    parcelGoals = board.getGoalsDeck().getGoals(GoalType.PARCEL);
                }
            }

            //Pre-check
            assertTrue(board.getGoalsDeck().isEmpty());
            assertEquals(3, player2.getGoals().size());

            //Action execution
            a = new DrawGoal(player, GoalType.PARCEL);
            assertFalse(a.execute());

            //Post-check
            assertTrue(board.getGoalsDeck().isEmpty());
            assertEquals(3, player2.getGoals().size());
    }

}