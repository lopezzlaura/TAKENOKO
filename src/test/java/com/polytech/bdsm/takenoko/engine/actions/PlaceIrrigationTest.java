package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.board.Irrigation;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.parcels.YellowParcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlaceIrrigationTest {

    private Player player;
    private Parcel parcel1, parcel2, parcel3;
    private Action action;
    private Board board;

    @BeforeEach
    void setUp() throws Exception{
        board = new Board();
        player = new Bot("Tom", board, new PandaStrategy(), new Random());

        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1, new PinkParcel());
        board.getPond().addNode(2, new PinkParcel());
        board.getPond().getNeighbours()[0].addNode(1, new PinkParcel());
        board.getPond().getNeighbours()[1].addNode(1, new YellowParcel());
        board.getPond().getNeighbours()[1].addNode(2, new YellowParcel());

        player.getInventory().addIrrigation();
        player.getInventory().addIrrigation();
        player.getInventory().addIrrigation();
        player.getInventory().addIrrigation();
    }

    @Test
    void execute() throws Exception{
        //Basic case
            //Pre-check
            assertTrue(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[0]));
            assertTrue(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[1]));
            assertFalse(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[0]));
            assertFalse(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[1]));
            assertFalse(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[2].getNeighbours()[1]));
            assertEquals(4, player.getInventory().getIrrigations());

            assertEquals(0, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]).getBambooStack().size());

            //Execution
            action = new PlaceIrrigation(player, new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[1]));
            assertTrue(action.execute());
            action = new PlaceIrrigation(player, new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]));
            assertTrue(action.execute());

            //Post-check
            assertTrue(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[0]));
            assertTrue(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[1]));
            assertTrue(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[0]));
            assertFalse(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[1]));
            assertFalse(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[2]));

            assertEquals(1, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]).getBambooStack().size());
            assertEquals(2, player.getInventory().getIrrigations());

        //Placer irrigation là où y en a déjà
        action = new PlaceIrrigation(player, new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]));
        assertFalse(action.execute());

        assertTrue(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[0]));

        //Placer irrigation à la suite de non irrigation
        assertTrue(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[2]));
        action = new PlaceIrrigation(player, new Irrigation((Parcel)board.getPond().getNeighbours()[1].getNeighbours()[1], (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[2]));
        assertFalse(action.execute());

        assertFalse(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[1]));
        assertFalse(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[2]));

        //Placer irrigation la où il y a un fertilizer facility
        assertEquals(0, ((Parcel)board.getPond().getNeighbours()[1].getNeighbours()[2]).getBambooStack().size());
        assertTrue(new PlaceFacility(player, (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[2], Facility.FERTILIZER_FACILITY).execute());

        action = new PlaceIrrigation(player, new Irrigation((Parcel)board.getPond().getNeighbours()[1], (Parcel)board.getPond().getNeighbours()[2]));
        assertTrue(action.execute());
        action = new PlaceIrrigation(player, new Irrigation((Parcel)board.getPond().getNeighbours()[1], (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[2]));
        assertTrue(action.execute());

        assertEquals(2, ((Parcel)board.getPond().getNeighbours()[1].getNeighbours()[2]).getBambooStack().size());

        //Placer irrigation alors que pas d'irrigation dans l'inventaire
        player.getInventory().removeIrrigation();

        assertEquals(0, player.getInventory().getIrrigations());

        Irrigation i = new Irrigation((Parcel)board.getPond().getNeighbours()[2], (Parcel)board.getPond().getNeighbours()[2].getNeighbours()[1]);
        action = new PlaceIrrigation(player, i);
        assertFalse(action.execute());

        assertFalse(board.getIrrigations().contains(i));
    }
}