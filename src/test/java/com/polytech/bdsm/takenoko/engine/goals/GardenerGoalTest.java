package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.GardenerStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.facilities.FertilizerLeaf;
import com.polytech.bdsm.takenoko.engine.facilities.PaddockLeaf;
import com.polytech.bdsm.takenoko.engine.facilities.PondLeaf;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GardenerGoalTest {
    private static Goal goal1, goal6, goal2, goal3, goal4, goal5;
    private static Player player;
    private static Board board;
    private static Parcel p1,p2,p3,p4;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        player = new Bot("",board, new GardenerStrategy(), new Random());

        goal1 = new GardenerGoal(1,4, Color.GREEN,1, Facility.WHATEVER_FACILITY); //With whatever
        goal2 = new GardenerGoal(1,4,Color.GREEN,1,Facility.POND_FACILITY); //With Pond Facility
        goal3 = new GardenerGoal(1,4,Color.GREEN,1,Facility.FERTILIZER_FACILITY); //With Fertilizer Facility
        goal4 = new GardenerGoal(1,4,Color.GREEN,1,Facility.PADDOCK_FACILITY); //With Paddock Facility
        goal5 = new GardenerGoal(1,4,Color.GREEN,1,Facility.NO_FACILITY); //Without Facilities
        goal6 = new GardenerGoal(1,3,Color.PINK,3, Facility.WHATEVER_FACILITY); //With whatever

        p1 = new GreenParcel();
        p2 = new PinkParcel();
        p3 = new PinkParcel();
        p4 = new PinkParcel();

        board.getPond().addNode(0,p1);
        board.getPond().addNode(1,p2);
        board.getPond().addNode(2,p3);
        board.getPond().addNode(3,p4);
    }

    @Test
    void testIsValid() {
        //Pre-growth check
        assertFalse(goal1.isValid(player)); //4-Green-1-Whatever
        assertFalse(goal2.isValid(player)); //4-Green-1 Pond
        assertFalse(goal3.isValid(player)); //4-Green-1 Fertilizer
        assertFalse(goal4.isValid(player)); //4-Green-1 Paddock
        assertFalse(goal5.isValid(player)); //4-Green-1 No facility
        assertFalse(goal6.isValid(player)); //3-Pink-3-Whatever

        //Growing 4 bamboos on a Green Parcel with no facilities
        p1.pushBamboo();
        p1.pushBamboo();
        p1.pushBamboo();
        p1.pushBamboo();

        //Post-growth check
        assertTrue(goal1.isValid(player)); //4-Green-1-Whatever
        assertFalse(goal2.isValid(player)); //4-Green-1 Pond
        assertFalse(goal3.isValid(player)); //4-Green-1 Fertilizer
        assertFalse(goal4.isValid(player)); //4-Green-1 Paddock
        assertTrue(goal5.isValid(player)); //4-Green-1 No facility
        assertFalse(goal6.isValid(player)); //3-Pink-3-Whatever

        //Removing Bamboos in order to place facilities
        p1.getBambooStack().clear();
        p1 = new PondLeaf(p1);

        //Growing 4 bamboos on a Green Parcel with a Pond Facility
        p1.pushBamboo();
        p1.pushBamboo();
        p1.pushBamboo();
        p1.pushBamboo();

        //Post-growth check
        assertTrue(goal1.isValid(player)); //4-Green-1-Whatever
        assertTrue(goal2.isValid(player)); //4-Green-1 Pond
        assertFalse(goal3.isValid(player)); //4-Green-1 Fertilizer
        assertFalse(goal4.isValid(player)); //4-Green-1 Paddock
        assertFalse(goal5.isValid(player)); //4-Green-1 No facility
        assertFalse(goal6.isValid(player)); //3-Pink-3-Whatever

        //Removing Bamboos in order to place facilities
        p1.getBambooStack().clear();
        p1 = new PaddockLeaf(p1);

        //Growing 4 bamboos on a Green Parcel with a Paddock Facility
        p1.pushBamboo();
        p1.pushBamboo();
        p1.pushBamboo();
        p1.pushBamboo();

        //Post-growth check
        assertTrue(goal1.isValid(player)); //4-Green-1-Whatever
        assertFalse(goal2.isValid(player)); //4-Green-1 Pond
        assertFalse(goal3.isValid(player)); //4-Green-1 Fertilizer
        assertTrue(goal4.isValid(player)); //4-Green-1 Paddock
        assertFalse(goal5.isValid(player)); //4-Green-1 No facility
        assertFalse(goal6.isValid(player)); //3-Pink-3-Whatever

        //Removing Bamboos in order to place facilities
        p1.getBambooStack().clear();

        //Growing 4 bamboos on a Green Parcel with a Fertilizer Facility
        p1 = new FertilizerLeaf(p1);
        p1.pushBamboo();
        p1.pushBamboo();
        p1.pushBamboo();
        p1.pushBamboo();

        //Post-growth check
        assertTrue(goal1.isValid(player)); //4-Green-1-Whatever
        assertFalse(goal2.isValid(player)); //4-Green-1 Pond
        assertTrue(goal3.isValid(player)); //4-Green-1 Fertilizer
        assertFalse(goal4.isValid(player)); //4-Green-1 Paddock
        assertFalse(goal5.isValid(player)); //4-Green-1 No facility
        assertFalse(goal6.isValid(player)); //3-Pink-3-Whatever

        p1.getBambooStack().clear();

        //Growing 3 bamboos on 3 Pink Parcels with no facility

        p2.pushBamboo();
        p2.pushBamboo();
        p2.pushBamboo();
        p3.pushBamboo();
        p3.pushBamboo();
        p3.pushBamboo();
        p4.pushBamboo();
        p4.pushBamboo();
        p4.pushBamboo();

        //Post-growth check
        assertFalse(goal1.isValid(player)); //4-Green-1-Whatever
        assertFalse(goal2.isValid(player)); //4-Green-1 Pond
        assertFalse(goal3.isValid(player)); //4-Green-1 Fertilizer
        assertFalse(goal4.isValid(player)); //4-Green-1 Paddock
        assertFalse(goal5.isValid(player)); //4-Green-1 No facility
        assertTrue(goal6.isValid(player)); //3-Pink-3-Whatever

        //Checking if adding a bamboo on one of the pink parcel makes the 3-Pink-3-Whatever not valid
        //Also check if a bamboo of size 4 on a pink parcel does not valid 4-Green-1-Whatever
        p2.pushBamboo();

        //Post-growth check
        assertFalse(goal1.isValid(player)); //4-Green-1-Whatever
        assertFalse(goal2.isValid(player)); //4-Green-1 Pond
        assertFalse(goal3.isValid(player)); //4-Green-1 Fertilizer
        assertFalse(goal4.isValid(player)); //4-Green-1 Paddock
        assertFalse(goal5.isValid(player)); //4-Green-1 No facility
        assertFalse(goal6.isValid(player)); //3-Pink-3-Whatever
    }

}