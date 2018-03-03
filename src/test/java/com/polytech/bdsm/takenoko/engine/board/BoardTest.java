package com.polytech.bdsm.takenoko.engine.board;

import com.polytech.bdsm.takenoko.engine.facilities.FertilizerLeaf;
import com.polytech.bdsm.takenoko.engine.facilities.PaddockLeaf;
import com.polytech.bdsm.takenoko.engine.facilities.PondLeaf;
import com.polytech.bdsm.takenoko.engine.graph.GraphNode;
import com.polytech.bdsm.takenoko.engine.parcels.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bureau de Sébastien Mosser
 */
class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        Parcel p1 = new YellowParcel();
        Parcel p2 = new YellowParcel();
        Parcel p3 = new YellowParcel();
        Parcel p4 = new YellowParcel();
        Parcel p5 = new YellowParcel();
        Parcel p6 = new YellowParcel();

        p1.pushBamboo();
        p2.pushBamboo();
        p3.pushBamboo();
        p4.pushBamboo();
        p5.pushBamboo();
        p6.pushBamboo();

        board.getPond().addNode(0, p1);
        board.getPond().addNode(1, p2);
        board.getPond().addNode(2, p3);
        board.getPond().addNode(3, p4);
        board.getPond().addNode(4, p5);
        board.getPond().addNode(5, p6);
    }

    @Test
    void getPossibleMovesTest() throws Exception {
        PossibleMovesResult res = BoardUtils.getPossibleMoves(board.getPond());
        assertEquals(3, res.getDirectionsCount());
        assertEquals(2, res.getParcelsInDirection(0).size());
        assertEquals(2, res.getParcelsInDirection(1).size());
        assertEquals(2, res.getParcelsInDirection(2).size());

        Parcel p1 = new YellowParcel();
        Parcel p2 = new YellowParcel();
        Parcel p3 = new YellowParcel();

        board.getPond().getNeighbours()[0].addNode(5, p1);
        board.getPond().getNeighbours()[0].addNode(0, p2);
        board.getPond().getNeighbours()[0].addNode(1, p3);

        res = BoardUtils.getPossibleMoves(board.getPond());
        assertEquals(3, res.getDirectionsCount());
        assertEquals(3, res.getParcelsInDirection(0).size());
        assertEquals(2, res.getParcelsInDirection(1).size());
        assertEquals(2, res.getParcelsInDirection(2).size());

        // Construction et vérification avec un plateau plus complexe
        Board complex = new Board();

        PondParcel cRoot = complex.getPond();
        cRoot.addNode(0, new GreenParcel());
        cRoot.addNode(1, new GreenParcel());
        cRoot.addNode(2, new PinkParcel());
        cRoot.addNode(3, new YellowParcel());
        cRoot.addNode(4, new PinkParcel());
        cRoot.addNode(5, new YellowParcel());

        cRoot.getNeighbours()[0].addNode(5, new YellowParcel());
        cRoot.getNeighbours()[0].addNode(0, new GreenParcel());
        cRoot.getNeighbours()[0].addNode(1, new PinkParcel());

        cRoot.getNeighbours()[1].addNode(1, new GreenParcel());
        cRoot.getNeighbours()[1].addNode(2, new YellowParcel());

        cRoot.getNeighbours()[2].addNode(2, new PinkParcel());

        cRoot.getNeighbours()[3].addNode(2, new GreenParcel());
        cRoot.getNeighbours()[3].addNode(4, new GreenParcel());

        cRoot.getNeighbours()[4].addNode(4, new YellowParcel());
        cRoot.getNeighbours()[4].addNode(5, new GreenParcel());

        cRoot.getNeighbours()[5].addNode(5, new YellowParcel());

        cRoot.getNeighbours()[4].getNeighbours()[4].addNode(5, new PinkParcel());

        cRoot.getNeighbours()[5].getNeighbours()[5].addNode(0, new PinkParcel());
        cRoot.getNeighbours()[5].getNeighbours()[5].addNode(5, new GreenParcel());

        cRoot.getNeighbours()[0].getNeighbours()[0].addNode(5, new YellowParcel());

        cRoot.getNeighbours()[1].getNeighbours()[1].addNode(0, new PinkParcel());
        cRoot.getNeighbours()[1].getNeighbours()[1].addNode(2, new GreenParcel());

        // Check the board
        assertEquals(24, complex.size());

        // Move the panda to a leaf
        complex.getPanda().move((Parcel) cRoot.getNeighbours()[5].getNeighbours()[5].getNeighbours()[5]);
        assertSame(cRoot.getNeighbours()[5].getNeighbours()[5].getNeighbours()[5], complex.getPanda().getPosition());

        // Move the gardener to a leaf
        complex.getGardener().move((Parcel) cRoot.getNeighbours()[2]);
        complex.getGardener().move((Parcel) cRoot.getNeighbours()[2].getNeighbours()[1]);
        assertSame(cRoot.getNeighbours()[2].getNeighbours()[1], complex.getGardener().getPosition());

        // Get the possible moves of the Panda
        PossibleMovesResult pandaMoves = BoardUtils.getPossibleMoves(complex.getPanda().getPosition());

        // Check the possible moves
        List<Parcel> pandaPossibleMoves = new ArrayList<>(Arrays.asList(
                (Parcel) cRoot.getNeighbours()[5].getNeighbours()[5],
                (Parcel) cRoot.getNeighbours()[5],
                         cRoot,
                (Parcel) cRoot.getNeighbours()[2],
                (Parcel) cRoot.getNeighbours()[2].getNeighbours()[2],
                (Parcel) cRoot.getNeighbours()[5].getNeighbours()[5].getNeighbours()[0],
                (Parcel) cRoot.getNeighbours()[5].getNeighbours()[5].getNeighbours()[0].getNeighbours()[1]
        ));

        boolean found = false;
        for (Parcel p : pandaMoves.getAllParcels()) {
            for (Parcel move : pandaPossibleMoves) {
                if (p == move) {
                    found = true;
                    assertSame(p, move);
                }
            }

            if (!found) {
                assertTrue(false);
            }

            found = false;
        }
    }

    @Test
    void getPossibleParcelsToPlaceTest() throws Exception {
        PossiblePlacingResult test = BoardUtils.getPossibleParcelsToPlace(board);

        assertEquals(12, test.getIndexesCount());

        for (Parcel parcel : test.getAllParcels()) {
            for (int i : test.getParcelPossibleIndexes(parcel)) {
                parcel.addNode(i, new YellowParcel());
                break;
            }
            break;
        }
        test = BoardUtils.getPossibleParcelsToPlace(board);

        assertEquals(14, test.getIndexesCount());

        //Create a new board
        Board b = new Board();
        test = BoardUtils.getPossibleParcelsToPlace(b);

        //Return the 6 positions around the pond
        assertEquals(6, test.getIndexesCount());

        //Add one parcel at index 0 of the pond
        Parcel p = new YellowParcel();
        b.getPond().addNode(0, p);
        test = BoardUtils.getPossibleParcelsToPlace(b);

        //return 7 possible positions to place
        assertEquals(7, test.getIndexesCount());

        //Add a facility to the parcel at index 0
        b.getPond().getNeighbours()[0] = new PaddockLeaf((Parcel)b.getPond().getNeighbours()[0]);

        //Return the same amount of possible positions to place
        assertEquals(7, test.getIndexesCount());

        //Add few more parcels
        b.getPond().addNode(1, new GreenParcel());
        b.getPond().addNode(2, new YellowParcel());
        b.getPond().getNeighbours()[0].addNode(1, new PinkParcel());

        //Check number of possible position to place
        test = BoardUtils.getPossibleParcelsToPlace(b);
        assertEquals(11, test.getIndexesCount());

        //Add again
        b.getPond().getNeighbours()[0].getNeighbours()[1].addNode(2, new GreenParcel());
        b.getPond().getNeighbours()[0].getNeighbours()[1].addNode(1, new PinkParcel());


        //Check number of possible position to place
        test = BoardUtils.getPossibleParcelsToPlace(b);
        assertEquals(14, test.getIndexesCount());
    }

    @Test
    void getPossibleIrrigation() throws Exception {
        Parcel p7 = new YellowParcel();
        Parcel p8 = new YellowParcel();
        Parcel p9 = new YellowParcel();

        board.getPond().getNeighbours()[0].addNode(1, p7);
        board.getPond().getNeighbours()[0].addNode(5, p8);
        board.getPond().getNeighbours()[0].addNode(0, p9);

        int size = BoardUtils.getPossibleIrrigations(board).getIrrigationCount();

        assertEquals(6, size);

        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[1]));

        size = BoardUtils.getPossibleIrrigations(board).getIrrigationCount();

        assertEquals(7, size);

        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[5]));
        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]));
        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[0].getNeighbours()[5]));

        size = BoardUtils.getPossibleIrrigations(board).getIrrigationCount();

        assertEquals(9, size);

        Parcel current;
        List<Irrigation> aroundPond = new ArrayList<>();
        for (GraphNode gn : board.getPond().getNeighbours()) {
            current = (Parcel) gn;
            if (current != null) {
                aroundPond.add(new Irrigation(board.getPond(), current));
            }
        }

        // Test if no irrigations are around pond
        List<Irrigation> possibleIrrigations = BoardUtils.getPossibleIrrigations(board).getAllIrrigations();
        for (Irrigation i : aroundPond) {
            assertFalse(possibleIrrigations.contains(i));
        }
    }

    @Test
    void isParcelIrrigated() throws Exception {
        Parcel p7 = new YellowParcel();
        Parcel p8 = new YellowParcel();
        Parcel p9 = new YellowParcel();

        board.getPond().getNeighbours()[0].addNode(1, p7);
        board.getPond().getNeighbours()[0].addNode(5, p8);
        board.getPond().getNeighbours()[0].addNode(0, p9);

        // Check Parcel next to the pond
        for (int i = 0; i < 6; i++) {
            assertTrue(((Parcel)board.getPond().getNeighbours()[i]).isIrrigated());
        }

        // Check the parcel not attach to the pond
        assertFalse(BoardUtils.isParcelIrrigated(board, (Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]));
        assertFalse(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[5])));
        assertFalse(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[0])));

        // Add two irrigation to irrigate one parcel that is not attach to the pond
        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[1]));
        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]));

        // Check if one parcel is irrigate with the irrigation and check the other to make sure there not
        assertTrue(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1])));
        assertFalse(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[5])));
        assertFalse(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[0])));

        // Add another irrigation to supply another parcel
        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[0].getNeighbours()[0]));

        // Check if two parcel are now irrigate and check the last one to make sure it is not
        assertTrue(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1])));
        assertFalse(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[5])));
        assertTrue(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[0])));

        // Add the last irrigation to irrigate the last parcel
        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[0].getNeighbours()[5]));

        // Check that every parcels are irrigated
        assertTrue(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1])));
        assertTrue(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[5])));
        assertTrue(BoardUtils.isParcelIrrigated(board, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[0])));

    }

    @Test
    void getPossibleFacilityParcels() throws Exception {
        Board board = new Board();

        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1, new YellowParcel());
        board.getPond().addNode(2, new PinkParcel());
        board.getPond().addNode(3, new GreenParcel());
        board.getPond().addNode(4, new YellowParcel());
        board.getPond().addNode(5, new PinkParcel());
        board.getPond().getNeighbours()[0].addNode(1, new PaddockLeaf(new GreenParcel()));
        board.getPond().getNeighbours()[2].addNode(3, new FertilizerLeaf(new YellowParcel()));
        board.getPond().getNeighbours()[4].addNode(5, new PondLeaf(new PinkParcel()));

        List<Parcel> res = BoardUtils.getPossibleFacilityParcels(board);

        assertEquals(6, res.size());
    }

}