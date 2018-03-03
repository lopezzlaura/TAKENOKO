package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.parcels.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParcelGoalPatternTest {
    private Board board, board1, board2;
    private Parcel pV, pP, pY, pV1, pP1, pY1;
    private ParcelGoal g, g1, g2, g3;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        board1 = new Board();
        board2 = new Board();
        g = new ParcelGoal(2, ParcelGoalPattern.DIAMOND, Color.YELLOW, Color.PINK);
        g1 = new ParcelGoal(2, ParcelGoalPattern.DIAMOND, Color.YELLOW);
        g2 = new ParcelGoal(2, ParcelGoalPattern.DIAMOND, Color.PINK, Color.GREEN);
        g3 = new ParcelGoal(2, ParcelGoalPattern.DIAMOND, Color.GREEN);
        pY = new YellowParcel();
        pY1 = new YellowParcel();
        pP = new PinkParcel();
        board.getPond().addNode(2, pY);
        board.getPond().addNode(3, pY1);
        board.getPond().getNeighbours()[2].addNode(3, pP);

        board1.getPond().addNode(2, new YellowParcel());
        board1.getPond().addNode(3, new GreenParcel());
        board1.getPond().addNode(4, new GreenParcel());
        board1.getPond().getNeighbours()[2].addNode(3, new PinkParcel());
        board1.getPond().getNeighbours()[3].addNode(3, new PinkParcel());

        board2.getPond().addNode(2, new YellowParcel());
        board2.getPond().addNode(3, new GreenParcel());
        board2.getPond().addNode(4, new GreenParcel());
        board2.getPond().getNeighbours()[2].addNode(3, new PinkParcel());
        board2.getPond().getNeighbours()[3].addNode(3, new GreenParcel());


    }

    @Test
    void isAlmostValidDiamondTest() throws Exception {
        //Test avec un cas triangle et 2 couleurs
        assertTrue(g.isAlmostValid(board));
        assertFalse(g1.isAlmostValid(board));

        board.getPond().getNeighbours()[2].getNeighbours()[3].addNode(4, new PinkParcel());
        board.getPond().getNeighbours()[2].getNeighbours()[3].addNode(3, new PinkParcel());
        board.getPond().getNeighbours()[2].addNode(2, new YellowParcel());

        //Test avec un cas triangle et 1 couleur
        assertTrue(g1.isAlmostValid(board));

        //Test avec une ligne brisée et 2 couleurs
        assertTrue(g2.isAlmostValid(board1));

        //Test avec une ligne brisée et 1 couleur
        assertTrue(g3.isAlmostValid(board2));
    }

}
