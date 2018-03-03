package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PandaGoalTest {
    private static Goal goal1, goal2, goal3, goal4;
    private static Player player;
    private static Board board;
    private static List<Bamboo> b, b1, b2, b3;

    @BeforeAll
    public static void SetUp() {
        board = new Board();
        player = new Bot("", board, new PandaStrategy(), new Random());
        b = new ArrayList<>();
        b1 = new ArrayList<>();
        b2 = new ArrayList<>();
        b3 = new ArrayList<>();
        b.add(new Bamboo(Color.GREEN));
        b.add(new Bamboo(Color.GREEN));
        b.add(new Bamboo(Color.GREEN));
        b1.add(new Bamboo(Color.PINK));
        b1.add(new Bamboo(Color.PINK));
        b1.add(new Bamboo(Color.PINK));
        b2.add(new Bamboo(Color.YELLOW));
        b2.add(new Bamboo(Color.YELLOW));
        b2.add(new Bamboo(Color.YELLOW));
        b3.add(new Bamboo(Color.PINK));
        b3.add(new Bamboo(Color.GREEN));
        b3.add(new Bamboo(Color.YELLOW));

        goal1 = new PandaGoal(1, b); //GGG
        goal2 = new PandaGoal(1, b1); //PPP
        goal3 = new PandaGoal(1, b2); //YYY
        goal4 = new PandaGoal(1,b3); //PGY

    }

    @Test
    void testIsValid() {
        player.getInventory().getBamboos().clear();
        //Pre-check
        assertFalse(goal1.isValid(player)); //GGG
        assertFalse(goal2.isValid(player)); //PPP
        assertFalse(goal3.isValid(player)); //YYY
        assertFalse(goal4.isValid(player)); //PGY

        //Adding bamboos to the inventory
        player.getInventory().getBamboos().add(new Bamboo(Color.PINK));
        player.getInventory().getBamboos().add(new Bamboo(Color.GREEN));
        player.getInventory().getBamboos().add(new Bamboo(Color.YELLOW));

        //Post-check
        assertFalse(goal1.isValid(player)); //GGG
        assertFalse(goal2.isValid(player)); //PPP
        assertFalse(goal3.isValid(player)); //YYY
        assertTrue(goal4.isValid(player)); //PGY

        //Adding bamboos to the inventory
        player.getInventory().getBamboos().add(new Bamboo(Color.GREEN));
        player.getInventory().getBamboos().add(new Bamboo(Color.GREEN));
        player.getInventory().getBamboos().add(new Bamboo(Color.GREEN));

        //Post-check
        assertTrue(goal1.isValid(player)); //GGG
        assertFalse(goal2.isValid(player)); //PPP
        assertFalse(goal3.isValid(player)); //YYY
        assertTrue(goal4.isValid(player)); //PGY

        //Adding bamboos to the inventory
        player.getInventory().getBamboos().add(new Bamboo(Color.PINK));
        player.getInventory().getBamboos().add(new Bamboo(Color.PINK));
        player.getInventory().getBamboos().add(new Bamboo(Color.PINK));

        //Post-check
        assertTrue(goal1.isValid(player)); //GGG
        assertTrue(goal2.isValid(player)); //PPP
        assertFalse(goal3.isValid(player)); //YYY
        assertTrue(goal4.isValid(player)); //PGY

        //Adding bamboos to the inventory
        player.getInventory().getBamboos().add(new Bamboo(Color.YELLOW));
        player.getInventory().getBamboos().add(new Bamboo(Color.YELLOW));
        player.getInventory().getBamboos().add(new Bamboo(Color.YELLOW));

        //Post-check
        assertTrue(goal1.isValid(player)); //GGG
        assertTrue(goal2.isValid(player)); //PPP
        assertTrue(goal3.isValid(player)); //YYY
        assertTrue(goal4.isValid(player)); //PGY
    }

    @Test
    void testPostValidation() {
        player.getInventory().getBamboos().clear();

        //Adding bamboos to check GGG and PGY goals
        player.getInventory().getBamboos().addAll(b); //GGG
        player.getInventory().getBamboos().addAll(b3); //PGY

        assertTrue(goal1.isValid(player));
        assertTrue(goal4.isValid(player));

        //Remove the bamboos of GGG
        goal1.postValidation(player);

        assertFalse(goal1.isValid(player));
        assertTrue(goal4.isValid(player));
        player.getInventory().getBamboos().clear();

        //Adding bamboos to check GGG and PGY goals
        player.getInventory().getBamboos().addAll(b); //GGG
        player.getInventory().getBamboos().addAll(b3); //PGY

        assertTrue(goal1.isValid(player));
        assertTrue(goal4.isValid(player));

        //Remove the bamboos of GGG
        goal4.postValidation(player);

        assertTrue(goal1.isValid(player));
        assertFalse(goal4.isValid(player));



    }

}