package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.parcels.YellowParcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlaceParcelTest {

    private Board board;
    private Player player;
    private Parcel p1, p2, p3;


    @BeforeEach
    void setUp() throws Exception{
        board = new Board();
        player = new Bot("Luc", board, null, new Random());

        p1 = new GreenParcel();
        p2 = new PinkParcel();
        p3 = new YellowParcel();
    }

    @Test
    void execute() {

        //Place parcel basic tests
            //Pre-check
            assertEquals(6, player.getBoard().getPond().getNeighbours().length);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[0]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[1]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[2]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[3]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[4]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[5]);

            //Action execution
            Action a = new PlaceParcel(player, p1, player.getBoard().getPond(), 0);

            //Post-check
            assertTrue(a.execute());
            assertEquals(p1, player.getBoard().getPond().getNeighbours()[0]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[1]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[2]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[3]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[4]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[5]);

            //Action execution
            a = new PlaceParcel(player, p2, player.getBoard().getPond(), 1);

            //Post-check
            assertTrue(a.execute());
            assertEquals(p2, player.getBoard().getPond().getNeighbours()[1]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[2]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[3]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[4]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[5]);

            //Action execution
            a = new PlaceParcel(player, p3, player.getBoard().getPond(), 2);

            //Post-check
            assertTrue(a.execute());
            assertEquals(p3, player.getBoard().getPond().getNeighbours()[2]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[3]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[4]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[5]);

        //Place parcel where a parcel already exists
            a = new PlaceParcel(player, p3, player.getBoard().getPond(), 1);

            //Post-check
            assertFalse(a.execute());
            assertEquals(p2, player.getBoard().getPond().getNeighbours()[1]);
            assertEquals(p3, player.getBoard().getPond().getNeighbours()[2]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[3]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[4]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[5]);

        //Place a parcel at a wrong index
            //Action execution
            a = new PlaceParcel(player, p3, player.getBoard().getPond(), 6);
            assertFalse(a.execute());

            //Post-check
            assertEquals(p3, player.getBoard().getPond().getNeighbours()[2]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[3]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[4]);
            assertEquals(null, player.getBoard().getPond().getNeighbours()[5]);

        //Placer une parcelle alors que pas 2 parcelles autour
            //Pre-check
            assertNull(player.getBoard().getPond().getNeighbours()[2].getNeighbours()[1]);
            assertNull(player.getBoard().getPond().getNeighbours()[2].getNeighbours()[3]);

            //Action execution
            a = new PlaceParcel(player, p3, (Parcel)player.getBoard().getPond().getNeighbours()[2],2 );

            //Post-check
            assertFalse(a.execute());
            assertNull(player.getBoard().getPond().getNeighbours()[2].getNeighbours()[2]);

    }

}