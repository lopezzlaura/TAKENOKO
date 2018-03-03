package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.parcels.YellowParcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DrawIrrigationTest {

    private Board board;
    private Player player;

    @BeforeEach
    void setUp() throws Exception{
        board = new Board();
        player = new Bot("",board, new PandaStrategy(), new Random() );

        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1, new PinkParcel());
        board.getPond().addNode(2, new YellowParcel());
    }

    @Test
    void execute() {

        //Piocher une irrigation
            //Pre-check
            assertEquals(0, player.getInventory().getIrrigations());

            //Action execution
            assertTrue(new DrawIrrigation(player).execute());

            //Post-check
            assertEquals(1, player.getInventory().getIrrigations());

            //Action execution
            assertTrue(new DrawIrrigation(player).execute());
            assertTrue(new DrawIrrigation(player).execute());

            //Post-check
            assertEquals(3, player.getInventory().getIrrigations());

        //Piocher une irrigation alors que y en a plus dans la pioche
            //Pre-check
            assertEquals(3, player.getInventory().getIrrigations());

            //empty irrigation deck
            while(board.getIrrigationsDeck() > 0){
                board.drawIrrigation();
            }
            assertTrue(board.getIrrigationsDeck() == 0);

            //Action execution
            assertFalse(new DrawIrrigation(player).execute());

            //Post-check
            assertEquals(3, player.getInventory().getIrrigations());
            assertTrue(board.getIrrigationsDeck() == 0);
    }

}