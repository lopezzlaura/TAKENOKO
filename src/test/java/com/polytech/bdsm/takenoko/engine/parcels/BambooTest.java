package com.polytech.bdsm.takenoko.engine.parcels;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BambooTest {

    @Test
    void equalsTest() {
        Bamboo b = new Bamboo(Color.YELLOW);
        Bamboo b1 = new Bamboo(Color.YELLOW);
        Bamboo b2 = new Bamboo(Color.GREEN);
        Bamboo b3 = new Bamboo(Color.PINK);
        assertTrue(b1.equals(b));
        assertFalse(b.equals(b2));
        assertFalse(b.equals(b3));
        assertFalse(b2.equals(b3));
    }

}