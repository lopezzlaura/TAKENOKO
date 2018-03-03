package com.polytech.bdsm.takenoko.ai;

import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.goals.PandaGoal;
import com.polytech.bdsm.takenoko.engine.parcels.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Bureau de Sébastien Mosser
 */
class BotTest {

    private Board board;
    private Bot player;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        board.getPond().addNode(0, new PinkParcel());
        board.getPond().addNode(1, new GreenParcel());
        board.getPond().addNode(2, new YellowParcel());
        board.getPond().addNode(3, new PinkParcel());
        board.getPond().addNode(4, new GreenParcel());
        board.getPond().addNode(5, new YellowParcel());

        player = new Bot("Sherlock", board, new PandaStrategy(), new Random());
        player.getGoals().clear();
        List<Bamboo> toCollect = new ArrayList<>();
        toCollect.add(new Bamboo(Color.GREEN));
        toCollect.add(new Bamboo(Color.PINK));
        toCollect.add(new Bamboo(Color.YELLOW));
        player.getGoals().add(new PandaGoal(2, toCollect));
    }

    @Test
    void checkGoalTest() {
        assertEquals(0, player.getInventory().getAccomplishedGoal());
        player.getInventory().addBamboo(new Bamboo(Color.PINK));
        player.getInventory().addBamboo(new Bamboo(Color.YELLOW));
        player.getInventory().addBamboo(new Bamboo(Color.GREEN));
        System.out.println(player.getGoals().get(0));
        System.out.println(player.checkGoal());
        assertEquals(player.getGoals().get(0),player.checkGoal());
    }

}