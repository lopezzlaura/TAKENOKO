package com.polytech.bdsm.takenoko.engine.facilities;

import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.parcels.YellowParcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Bureau De Sébastien Mosser
 */
class PondLeafTest {

    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        //Create new board with few parcels
        board = new Board();
        board.getPond().addNode(0, new PinkParcel());
        board.getPond().addNode(1, new PondLeaf(new PinkParcel()));
        board.getPond().addNode(2, new YellowParcel());
        board.getPond().addNode(3, new YellowParcel());
        board.getPond().addNode(4, new PinkParcel());
        board.getPond().addNode(5, new GreenParcel());
        board.getPond().getNeighbours()[5].addNode(0, new YellowParcel());
        board.getPond().getNeighbours()[5].addNode(5, new GreenParcel());
        board.getPond().getNeighbours()[5].getNeighbours()[5].addNode(0, new PinkParcel());
        board.getPond().getNeighbours()[5].addNode(4, new YellowParcel());
    }

    @Test
    void isIrrigated() {
        for (int i = 0; i < 6; i++) {
            assertTrue(BoardUtils.isParcelIrrigated(board, (Parcel) board.getPond().getNeighbours()[i]));
        }

        assertTrue(((Parcel) board.getPond().getNeighbours()[0]).isIrrigated());
        assertTrue(((Parcel) board.getPond().getNeighbours()[1]).isIrrigated());

        assertFalse(((Parcel)board.getPond().getNeighbours()[5].getNeighbours()[5]).isIrrigated());

        assertFalse(BoardUtils.isParcelIrrigated(board, (Parcel) board.getPond().getNeighbours()[5].getNeighbours()[5]));

        PondLeaf pond = new PondLeaf((Parcel) board.getPond().getNeighbours()[5].getNeighbours()[5]);

        assertTrue(BoardUtils.isParcelIrrigated(board, (Parcel) board.getPond().getNeighbours()[5].getNeighbours()[5]));

        assertFalse(BoardUtils.isParcelIrrigated(board, (Parcel) board.getPond().getNeighbours()[5].getNeighbours()[5].getNeighbours()[0]));
    }

}