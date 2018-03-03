package com.polytech.bdsm.takenoko.engine.pnj;

import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.parcels.PondParcel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GardenerTest {

    private static Gardener gardener;
    private static Parcel p, p2, p3;
    private static Board board;

    @BeforeAll
    static void setUp() throws Exception {
        board = new Board();
        gardener = new Gardener((Parcel)board.getRoot());
        p = new PinkParcel();
        p2 = new GreenParcel();

        p2.pushBamboo();
        board.getRoot().addNode(1, p);
        board.getRoot().addNode(2, p2);

        p3 = new GreenParcel();
        p3.pushBamboo();
        p3.pushBamboo();
        p3.pushBamboo();
        p3.pushBamboo();
        board.getRoot().addNode(3, p3);

    }

    @Test
    void getInstance() throws Exception {
        //Basic case
        Gardener gardener = new Gardener(new PondParcel());
        assertEquals( gardener.toString(), gardener.toString());

        //Null case
        assertFalse(gardener.toString() == null);
    }

    @Test
    void move() throws Exception {
       assertEquals(board.getPond(), gardener.getPosition());
       gardener.move(p2);
       assertEquals(p2,gardener.getPosition());
       gardener.move(board.getPond());
       assertEquals(board.getPond(),gardener.getPosition());
    }

}