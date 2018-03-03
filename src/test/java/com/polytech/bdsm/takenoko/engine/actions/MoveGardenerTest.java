package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bureau de Sébastien Mosser
 */
class MoveGardenerTest {

    private Player player;
    private Action action, pond;
    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        player = new Bot("Bot", board, new PandaStrategy(), new Random());
        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1,new GreenParcel());
        board.getPond().addNode(2,new PinkParcel());
        board.getPond().getNeighbours()[0].addNode(1, new GreenParcel());
        board.getPond().getNeighbours()[0].addNode(0, new GreenParcel());
        action = new MoveGardener(player, (Parcel)board.getPond().getNeighbours()[0], board.getGardener());
        pond = new MoveGardener(player,board.getPond(),board.getGardener());
    }

    @Test
    void executeTest() throws Exception {

        //Moving the Gardener on a Parcel that does not exists
        assertEquals(board.getPond(),board.getGardener().getPosition());
        assertFalse(new MoveGardener(player, (Parcel)board.getPond().getNeighbours()[3], board.getGardener()).execute());
        assertEquals(board.getPond(),board.getGardener().getPosition());

        //Move the gardener to a Parcel not accessible (not aligned)
        assertTrue(new MoveGardener(player, (Parcel)board.getPond().getNeighbours()[2], board.getGardener()).execute());
        assertEquals(board.getPond().getNeighbours()[2],board.getGardener().getPosition());
        assertFalse(new MoveGardener(player, (Parcel)board.getPond().getNeighbours()[0], board.getGardener()).execute());
        assertEquals(board.getPond().getNeighbours()[2],board.getGardener().getPosition());

        pond.execute();

        //Moving the Gardener to a Parcel where there is no bamboo
        assertEquals(board.getPond(),board.getGardener().getPosition());
        assertEquals(0, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());
        action.execute();
        assertEquals(board.getPond().getNeighbours()[0], board.getGardener().getPosition());
        assertEquals(1, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());

        ((Parcel)board.getPond().getNeighbours()[0]).popBamboo();
        ((Parcel)board.getPond().getNeighbours()[0]).pushBamboo();
        pond.execute();

        //Moving the Gardener to a Parcel where there is 1 bamboo
        assertEquals(1, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());
        assertEquals(board.getPond(),board.getGardener().getPosition());
        action.execute();
        assertEquals(board.getPond().getNeighbours()[0], board.getGardener().getPosition());
        assertEquals(2, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());

        ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().clear();
        ((Parcel)board.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[0]).pushBamboo();
        pond.execute();

        //Moving the Gardener to a Parcel where there are already 4 bamboos
        assertEquals(4, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());
        assertEquals(board.getPond(),board.getGardener().getPosition());
        action.execute();
        assertEquals(board.getPond().getNeighbours()[0], board.getGardener().getPosition());
        assertEquals(4, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());

        //Moving the Gardener to a Parcel with parcels of the same color next to it
        ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().clear();
        ((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().clear();
        pond.execute();
        action.execute();
        assertEquals(board.getPond().getNeighbours()[0], board.getGardener().getPosition());
        assertEquals(1,((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());
        assertEquals(1,((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().size());
        assertEquals(0,((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]).getBambooStack().size());

        //Moving the Gardener to a Parcel with parcels of the same color next to it with 4 bamboos
        ((Parcel)board.getPond().getNeighbours()[1]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[1]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[1]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().clear();
        pond.execute();
        action.execute();
        assertEquals(4,((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().size());

        ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]).getBambooStack().clear();

        //Add a PondFacility on parcel
        player.getInventory().addFacility(Facility.POND_FACILITY);
        assertTrue(new PlaceFacility(player, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[0]), Facility.POND_FACILITY).execute());
        pond.execute();

        //Moving the gardener on a parcel that contains a PondFacility
        assertEquals(board.getPond(),board.getGardener().getPosition());
        assertEquals(0, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[0]).getBambooStack().size());
        new MoveGardener(player, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[0]), board.getGardener()).execute();
        assertEquals(board.getPond().getNeighbours()[0].getNeighbours()[0], board.getGardener().getPosition());
        assertEquals(1, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[0]).getBambooStack().size());
    }

}