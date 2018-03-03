package com.polytech.bdsm.takenoko.engine.pnj;

import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.parcels.PondParcel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PandaTest {

    private static Panda panda, panda2;
    private static Parcel p, p2;
    @BeforeAll
    static void setUp() throws Exception {

        p = new PinkParcel();
        p2 = new PondParcel();
        p.pushBamboo();
        p2.addNode(2, p);
        panda2 = new Panda(p2);
        panda = new Panda(new PondParcel());
    }

    @Test
    void getInstance() throws Exception {
        //Basic case
        assertEquals( panda.toString(), panda.toString());

    }

    @Test
    void move() {
        //Basic case
            //Pre-check
            Bamboo b = (Bamboo) p.getBambooStack().peek();
            assertEquals(1, p.getBambooStack().size());
            //Execute
            assertEquals(b, panda2.move(p));
            //Post-check
            assertEquals(0, p.getBambooStack().size());

        //No bamboo to eat
            //Pre-check
            assertEquals(0, p2.getBambooStack().size());
            //Execute
            assertEquals(null, panda2.move(p2));
            //Post-check
            assertEquals(0, p2.getBambooStack().size());
    }

}