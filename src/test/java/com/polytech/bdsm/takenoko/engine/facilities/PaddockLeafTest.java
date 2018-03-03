package com.polytech.bdsm.takenoko.engine.facilities;

import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.parcels.YellowParcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class PaddockLeafTest {

    private Board board;

    @BeforeEach
    void setUp() throws Exception{

        //Create new board with few parcels
        board = new Board();
        board.getPond().addNode(0, new PinkParcel());
        board.getPond().addNode(1, new PaddockLeaf(new PinkParcel()));
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
    void popBamboo() throws Exception {

        //Pre-check
        assertEquals(Facility.PADDOCK_FACILITY, ((Parcel)board.getPond().getNeighbours()[1]).getFacility());
        assertEquals(3, board.getPond().getNeighbours()[1].getNeighboursCount());
        assertEquals(4, board.getPond().getNeighbours()[0].getNeighboursCount());
        assertEquals(3, board.getPond().getNeighbours()[2].getNeighboursCount());

        //Change reference of a YellowParcel()
        assertEquals(4, board.getPond().getNeighbours()[5].getNeighbours()[0].getNeighboursCount());
        assertEquals(11, board.size());

        Parcel paddock = new PaddockLeaf((Parcel) board.getPond().getNeighbours()[5].getNeighbours()[0]);
        board.getPond().getNeighbours()[5].getNeighbours()[0] = paddock;

        Parcel p = (Parcel) board.getPond().getNeighbours()[5].getNeighbours()[0];
        assertEquals(4, board.getPond().getNeighbours()[5].getNeighbours()[0].getNeighboursCount());

        assertEquals(paddock, p);
        assertEquals(11, board.size());
        assertEquals(Facility.PADDOCK_FACILITY, ((Parcel)board.getPond().getNeighbours()[5].getNeighbours()[0]).getFacility());

        //Check links with neighbours
        assertSame(paddock, board.getPond().getNeighbours()[0].getNeighbours()[5]);
        assertSame(paddock, board.getPond().getNeighbours()[5].getNeighbours()[0]);
        assertSame(paddock, board.getPond().getNeighbours()[5].getNeighbours()[5].getNeighbours()[1]);
        assertSame(paddock, board.getPond().getNeighbours()[5].getNeighbours()[5].getNeighbours()[0].getNeighbours()[2]);

        // Test of the Panda moving on a paddock
        paddock.pushBamboo();
        paddock.pushBamboo();
        paddock.pushBamboo();
        assertEquals(3, paddock.getBambooStack().size());

        board.getPanda().move(paddock);

        assertEquals(3, paddock.getBambooStack().size());

        // Try to get parcel in a clone board
        Board cloned = (Board) board.clone();
        assertEquals(paddock, cloned.getParcel(paddock));
        assertEquals(board.getPond().getNeighbours()[1], cloned.getParcel((Parcel) board.getPond().getNeighbours()[1]));
    }

}