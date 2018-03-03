package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
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

/**
 * @author Bureau De Sébastien Mosser
 */
class PlaceFacilityTest {

    private Board board;
    private Parcel p1, p2, p3, p4, p5, p6, p7, p8, p9, p10;
    private PlaceFacility addPaddock, addPond, addFertilazer, failedPaddock;
    private Player sherlock;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();

        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1,  new PinkParcel());
        board.getPond().addNode(2, new YellowParcel());
        board.getPond().addNode(3, new GreenParcel());
        board.getPond().addNode(4,  new PinkParcel());
        board.getPond().addNode(5, new YellowParcel());

        sherlock = new Bot("Sherlock", board, new PandaStrategy(), new Random());

        addPaddock = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[1], Facility.PADDOCK_FACILITY);
        failedPaddock = new PlaceFacility(sherlock, p10, Facility.PADDOCK_FACILITY);
    }

    @Test
    void execute() throws Exception {

        //Essayer de placer les facilities sur le PondParcel
        addPaddock = new PlaceFacility(sherlock, board.getPond(), Facility.PADDOCK_FACILITY);
        assertFalse(addPaddock.execute());
        addPond = new PlaceFacility(sherlock, board.getPond(), Facility.POND_FACILITY);
        assertFalse(addPond.execute());
        addFertilazer = new PlaceFacility(sherlock, board.getPond(), Facility.FERTILIZER_FACILITY);
        assertFalse(addFertilazer.execute());

        //PADDOCK Facility
            //Sur parcelle sans bambou
            assertTrue(((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size()==0);
            assertTrue(((Parcel)board.getPond().getNeighbours()[0]).getFacility() == Facility.NO_FACILITY);

            addPaddock = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[0], Facility.PADDOCK_FACILITY);
            assertTrue(addPaddock.execute());

            assertTrue(((Parcel)board.getPond().getNeighbours()[0]).getFacility() == Facility.PADDOCK_FACILITY);
            assertEquals(0, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());

            //Puis ajouter ensuite du bambou
            ((Parcel)board.getPond().getNeighbours()[0]).pushBamboo();
            assertTrue(((Parcel)board.getPond().getNeighbours()[0]).getFacility() == Facility.PADDOCK_FACILITY);
            assertEquals(1, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());

            //Sur parcelle avec bambou
            ((Parcel)board.getPond().getNeighbours()[1]).pushBamboo();
            assertTrue(((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().size()==1);
            assertTrue(((Parcel)board.getPond().getNeighbours()[1]).getFacility() == Facility.NO_FACILITY);

            addPaddock = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[1], Facility.PADDOCK_FACILITY);
            assertFalse(addPaddock.execute());

            assertTrue(((Parcel)board.getPond().getNeighbours()[1]).getFacility() == Facility.NO_FACILITY);
            assertEquals(1, ((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().size());

            //Sur parcelle avec une autre facilities
            ((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().clear();
            addPond = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[1], Facility.POND_FACILITY);
            assertTrue(addPond.execute());

            addPaddock = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[1], Facility.PADDOCK_FACILITY);
            assertFalse(addPaddock.execute());
            assertTrue(((Parcel)board.getPond().getNeighbours()[1]).getFacility() == Facility.POND_FACILITY);

            //Sur parcelle qui contient deja un paddock
            assertTrue(((Parcel)board.getPond().getNeighbours()[0]).getFacility() == Facility.PADDOCK_FACILITY);
            addPaddock = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[0], Facility.PADDOCK_FACILITY);
            assertFalse(addPaddock.execute());
            assertTrue(((Parcel)board.getPond().getNeighbours()[0]).getFacility() == Facility.PADDOCK_FACILITY);

        //POND Facility
            //Sur parcelle sans bambou
            assertTrue(((Parcel)board.getPond().getNeighbours()[2]).getBambooStack().size()==0);
            assertTrue(((Parcel)board.getPond().getNeighbours()[2]).getFacility() == Facility.NO_FACILITY);

            addPond = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[2], Facility.POND_FACILITY);
            assertTrue(addPond.execute());

            assertTrue(((Parcel)board.getPond().getNeighbours()[2]).getFacility() == Facility.POND_FACILITY);
            assertEquals(0, ((Parcel)board.getPond().getNeighbours()[2]).getBambooStack().size());

            //Puis ajouter ensuite du bambou
            ((Parcel)board.getPond().getNeighbours()[2]).pushBamboo();
            assertTrue(((Parcel)board.getPond().getNeighbours()[2]).getFacility() == Facility.POND_FACILITY);
            assertEquals(1, ((Parcel)board.getPond().getNeighbours()[2]).getBambooStack().size());

            //Sur parcelle avec bambou
            ((Parcel)board.getPond().getNeighbours()[3]).pushBamboo();

            assertEquals(1, ((Parcel)board.getPond().getNeighbours()[3]).getBambooStack().size());
            assertTrue(((Parcel)board.getPond().getNeighbours()[3]).getFacility() == Facility.NO_FACILITY);

            addPond = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[3], Facility.POND_FACILITY);
            assertFalse(addPond.execute());

            assertTrue(((Parcel)board.getPond().getNeighbours()[3]).getFacility() == Facility.NO_FACILITY);
            assertEquals(1, ((Parcel)board.getPond().getNeighbours()[3]).getBambooStack().size());

            //Sur parcelle avec autre facilities
            assertTrue(((Parcel)board.getPond().getNeighbours()[0]).getFacility() == Facility.PADDOCK_FACILITY);

            addPond = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[0], Facility.POND_FACILITY);
            assertFalse(addPond.execute());

            assertTrue(((Parcel)board.getPond().getNeighbours()[0]).getFacility() == Facility.PADDOCK_FACILITY);

            //Sur parcelle qui contient déjà un Pond
            assertTrue(((Parcel)board.getPond().getNeighbours()[2]).getFacility() == Facility.POND_FACILITY);

            addPond = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[2], Facility.POND_FACILITY);
            assertFalse(addPond.execute());

            assertTrue(((Parcel)board.getPond().getNeighbours()[2]).getFacility() == Facility.POND_FACILITY);

        //FERTILIZER Facility
            //Sur parcelle sans bambou
            assertTrue(((Parcel)board.getPond().getNeighbours()[4]).getBambooStack().size()==0);
            assertTrue(((Parcel)board.getPond().getNeighbours()[4]).getFacility() == Facility.NO_FACILITY);

            addFertilazer = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[4], Facility.FERTILIZER_FACILITY);
            assertTrue(addFertilazer.execute());

            assertTrue(((Parcel)board.getPond().getNeighbours()[4]).getFacility() == Facility.FERTILIZER_FACILITY);
            assertEquals(0, ((Parcel)board.getPond().getNeighbours()[4]).getBambooStack().size());

            //Puis ajouter ensuite du bambou
            ((Parcel)board.getPond().getNeighbours()[4]).pushBamboo();
            assertTrue(((Parcel)board.getPond().getNeighbours()[4]).getFacility() == Facility.FERTILIZER_FACILITY);
            assertEquals(2, ((Parcel)board.getPond().getNeighbours()[4]).getBambooStack().size());

            //Sur parcelle avec bambou
            ((Parcel)board.getPond().getNeighbours()[5]).pushBamboo();
            assertTrue(((Parcel)board.getPond().getNeighbours()[5]).getBambooStack().size()== 1);
            assertTrue(((Parcel)board.getPond().getNeighbours()[5]).getFacility() == Facility.NO_FACILITY);

            addFertilazer = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[5], Facility.FERTILIZER_FACILITY);
            assertFalse(addFertilazer.execute());

            assertTrue(((Parcel)board.getPond().getNeighbours()[5]).getFacility() == Facility.NO_FACILITY);
            assertEquals(1, ((Parcel)board.getPond().getNeighbours()[5]).getBambooStack().size());

            //Sur parcelle avec autre facilities
            assertTrue(((Parcel)board.getPond().getNeighbours()[2]).getFacility() == Facility.POND_FACILITY);

            addFertilazer = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[2], Facility.FERTILIZER_FACILITY);
            assertFalse(addFertilazer.execute());

            assertTrue(((Parcel)board.getPond().getNeighbours()[2]).getFacility() == Facility.POND_FACILITY);

            //Sur parcelle qui contient deja un fertilizer
            assertTrue(((Parcel)board.getPond().getNeighbours()[4]).getFacility() == Facility.FERTILIZER_FACILITY);

            addFertilazer = new PlaceFacility(sherlock, (Parcel)board.getPond().getNeighbours()[4], Facility.FERTILIZER_FACILITY);
            assertFalse(addFertilazer.execute());

            assertTrue(((Parcel)board.getPond().getNeighbours()[4]).getFacility() == Facility.FERTILIZER_FACILITY);

    }

}