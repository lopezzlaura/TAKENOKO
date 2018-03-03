package com.polytech.bdsm.takenoko.engine.facilities;

import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.parcels.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Bureau De Sébastien Mosser
 */
class FertilizerLeafTest {

    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        //Create new board with few parcels
        board = new Board();
        board.getPond().addNode(0, new PinkParcel());
        board.getPond().addNode(1, new FertilizerLeaf(new PinkParcel()));
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
    void pushBamboo() {
        assertEquals(0, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());
        assertEquals(0, ((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().size());
        assertEquals(Facility.FERTILIZER_FACILITY, ((Parcel)board.getPond().getNeighbours()[1]).getFacility());

        FertilizerLeaf fertilizer = new FertilizerLeaf((Parcel) board.getPond().getNeighbours()[5]);
        assertEquals(0, fertilizer.getBambooStack().size());

        fertilizer.pushBamboo();
        assertEquals(2, fertilizer.getBambooStack().size());

        fertilizer.getBambooStack().push(new Bamboo(fertilizer.getColor()));
        assertEquals(3, fertilizer.getBambooStack().size());

        fertilizer.pushBamboo();
        assertEquals(4, fertilizer.getBambooStack().size());
    }

}