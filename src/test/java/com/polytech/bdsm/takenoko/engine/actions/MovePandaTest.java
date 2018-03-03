package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.parcels.*;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MovePandaTest {
    private Player player;
    private Parcel parcel;
    private Action action, pond;
    private Board board;


    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        player = new Bot("", board, new PandaStrategy(), new Random());

        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1, new PinkParcel());
        board.getPond().addNode(2, new YellowParcel());

        pond = new MovePanda(player, board.getPond(), board.getPanda());
    }

    @Test
    void executeTest() throws Exception {

        //Moving the Panda to a Parcel with 0 bamboo
        assertEquals(0, player.getInventory().getBamboosCount());
        assertEquals(0, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());
        assertEquals(board.getPond(), board.getPanda().getPosition());

        action = new MovePanda(player, (Parcel)board.getPond().getNeighbours()[0], board.getPanda());
        assertTrue(action.execute());

        assertEquals(board.getPond().getNeighbours()[0], ((MovePanda) action).getPNJ().getPosition());
        assertEquals(0, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());
        assertEquals(0, player.getInventory().getBamboosCount());

        pond.execute();
        ((Parcel)board.getPond().getNeighbours()[1]).pushBamboo();

        //Moving the Panda to a Parcel with 2 bamboos
        assertEquals(0, player.getInventory().getBamboosCount());
        assertEquals(1, ((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().size());
        assertEquals(board.getPond(), board.getPanda().getPosition());

        action = new MovePanda(player, (Parcel)board.getPond().getNeighbours()[1], board.getPanda());
        assertTrue(action.execute());

        assertEquals(board.getPond().getNeighbours()[1], ((MovePanda) action).getPNJ().getPosition());
        assertEquals(0, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());
        assertEquals(1, player.getInventory().getBamboosCount());
        assertEquals(Color.PINK, player.getInventory().getBamboos().get(0).getColor());

        player.getInventory().removeBamboos(1);
        pond.execute();

        //Move the Panda to a Parcel with a PaddockFacility
        assertTrue(new PlaceFacility(player, (Parcel)board.getPond().getNeighbours()[2], Facility.PADDOCK_FACILITY).execute());
        ((Parcel)board.getPond().getNeighbours()[2]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[2]).pushBamboo();

        assertEquals(0, player.getInventory().getBamboosCount());
        assertEquals(2, ((Parcel)board.getPond().getNeighbours()[2]).getBambooStack().size());
        assertEquals(board.getPond(), board.getPanda().getPosition());

        action = new MovePanda(player, (Parcel)board.getPond().getNeighbours()[2], board.getPanda());
        assertTrue(action.execute());

        assertEquals(board.getPond().getNeighbours()[2], ((MovePanda) action).getPNJ().getPosition());
        assertEquals(2, ((Parcel)board.getPond().getNeighbours()[2]).getBambooStack().size());
        assertEquals(0, player.getInventory().getBamboosCount());

        //Moving the panda to a not aligned parcel (should not move here)
        assertEquals(board.getPond().getNeighbours()[2], board.getPanda().getPosition());

        action = new MovePanda(player, (Parcel)board.getPond().getNeighbours()[0], board.getPanda());
        assertFalse(action.execute());

        assertEquals(board.getPond().getNeighbours()[2], ((MovePanda) action).getPNJ().getPosition());
        assertEquals(0, player.getInventory().getBamboosCount() );

        //Moving the panda to a Parcel that does not exists
        assertEquals(board.getPond().getNeighbours()[2], board.getPanda().getPosition());
        assertTrue(board.getPond().getNeighbours()[3] == null);

        action = new MovePanda(player, (Parcel)board.getPond().getNeighbours()[3], board.getPanda());
        assertFalse(action.execute());

        assertEquals(board.getPond().getNeighbours()[2], ((MovePanda) action).getPNJ().getPosition());

    }

}