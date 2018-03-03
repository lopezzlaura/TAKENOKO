package com.polytech.bdsm.takenoko.engine.parcels;

import com.polytech.bdsm.takenoko.engine.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bureau De Sébastien Mosser
 */
class ParcelTest {

    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        Parcel p1 = new GreenParcel();
        Parcel p2 = new PinkParcel();
        Parcel p3 = new YellowParcel();
        Parcel p4 = new GreenParcel();
        Parcel p5 = new PinkParcel();
        Parcel p6 = new YellowParcel();
        Parcel p7 = new GreenParcel();
        Parcel p8 = new GreenParcel();

        board.getPond().addNode(0, p1);
        board.getPond().addNode(1, p2);
        board.getPond().addNode(2, p3);
        board.getPond().addNode(3, p4);
        board.getPond().addNode(4, p5);
        board.getPond().addNode(5, p6);
        board.getPond().getNeighbours()[0].addNode(1, p7);
        board.getPond().getNeighbours()[0].addNode(5, p8);
    }

    @Test
    void isIrrigatedTest() {
        for (int i = 0; i < 6; i++) {
            assertTrue(((Parcel) board.getPond().getNeighbours()[i]).isIrrigated());
        }

        assertFalse(((Parcel) board.getPond().getNeighbours()[0].getNeighbours()[1]).isIrrigated());
        assertFalse(((Parcel) board.getPond().getNeighbours()[0].getNeighbours()[5]).isIrrigated());
    }

    @Test
    void pushBambooTest() {
        assertFalse(board.getPond().pushBamboo());
        Parcel p = (Parcel) board.getPond().getNeighbours()[0];
        assertTrue(p.pushBamboo());
        Bamboo b = (Bamboo) p.getBambooStack().peek();
        assertEquals(p.getColor(),b.getColor());
    }

}