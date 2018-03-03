package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.ai.strategies.ParcelStrategy;
import com.polytech.bdsm.takenoko.engine.actions.Action;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.board.Irrigation;
import com.polytech.bdsm.takenoko.engine.parcels.*;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParcelGoalTest {

    private Color[] colors;
    private Board board, board2, board3, board4, board5;
    private Action action;
    private ParcelGoal parcelGoal, pg2, pg3, pg4, pg5;
    private Player player1, player2, player3, player4, player5;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        player1 = new Bot("Tom", board, new PandaStrategy(), new Random());

        //One ParcelGoal achieved (Pond, 3 GreenParcels, 1 YellowParcel)
        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1, new GreenParcel());
        board.getPond().addNode(2, new GreenParcel());
        board.getPond().getNeighbours()[0].addNode(1, new YellowParcel());

        parcelGoal = new ParcelGoal(3, ParcelGoalPattern.SEMILINE, Color.GREEN);

        //Line
        board2 = new Board();
        player2 = new Bot("Marc", board2, new PandaStrategy(), new Random());
        board2.getPond().addNode(0, new PinkParcel());
        board2.getPond().addNode(1, new GreenParcel());
        board2.getPond().addNode(2, new GreenParcel());
        board2.getPond().getNeighbours()[0].addNode(1, new GreenParcel());
        board2.addIrrigation(new Irrigation((Parcel) board2.getPond().getNeighbours()[0], (Parcel) board2.getPond().getNeighbours()[0].getNeighbours()[1]));

        pg2 = new ParcelGoal(2, ParcelGoalPattern.LINE, Color.GREEN);

        //Triangle
        board3 = new Board();
        player3 = new Bot("Marc", board3, new PandaStrategy(), new Random());
        board3.getPond().addNode(0, new YellowParcel());
        board3.getPond().addNode(1, new PinkParcel());
        board3.getPond().addNode(2, new PinkParcel());
        board3.getPond().getNeighbours()[2].addNode(1, new PinkParcel());
        board3.addIrrigation(new Irrigation((Parcel) board3.getPond().getNeighbours()[2], (Parcel) board3.getPond().getNeighbours()[2].getNeighbours()[1]));

        pg3 = new ParcelGoal(2, ParcelGoalPattern.TRIANGLE, Color.PINK);

        //Diamond
        board4 = new Board();
        player4 = new Bot("Marc", board4, new PandaStrategy(), new Random());
        board4.getPond().addNode(0, new GreenParcel());
        board4.getPond().addNode(1, new YellowParcel());
        board4.getPond().addNode(2, new PinkParcel());
        board4.getPond().getNeighbours()[1].addNode(2, new YellowParcel());
        board4.getPond().getNeighbours()[2].addNode(2, new PinkParcel());
        board4.addIrrigation(new Irrigation((Parcel) board4.getPond().getNeighbours()[1], (Parcel) board4.getPond().getNeighbours()[1].getNeighbours()[2]));
        board4.addIrrigation(new Irrigation((Parcel) board4.getPond().getNeighbours()[2], (Parcel) board4.getPond().getNeighbours()[2].getNeighbours()[2]));

        pg4 = new ParcelGoal(2, ParcelGoalPattern.DIAMOND, Color.YELLOW, Color.PINK);

        //1 line 1 curved line

        //Goal Valid but no irrigation
        board5 = new Board();
        board5.getPond().addNode(0, new PinkParcel());
        board5.getPond().addNode(1,new GreenParcel());
        board5.getPond().getNeighbours()[0].addNode(1,new GreenParcel());
        board5.getPond().getNeighbours()[0].addNode(0, new PinkParcel());
        board5.getPond().getNeighbours()[0].getNeighbours()[0].addNode(1,new YellowParcel());
        board5.getPond().getNeighbours()[0].getNeighbours()[0].addNode(0,new PinkParcel());
        player5 = new Bot("Test",board5,new ParcelStrategy(), new Random());
        pg5 = new ParcelGoal(2,ParcelGoalPattern.LINE,Color.PINK);

    }

    @Test
    void isValid() {

        //Basic curved line case
        assertTrue(parcelGoal.isValid(player1));

        //Basic line case
        assertFalse(pg2.isValid(player1));
        assertTrue(pg2.isValid(player2));

        //Basic triangle cas
        assertTrue(pg3.isValid(player3));

        //Basic diamond case
        assertTrue(pg4.isValid(player4));

        //Test to verify a goal with no irrigation
        assertFalse(pg5.isValid(player5));

        //Adding the irrigations
        board5.addIrrigation(new Irrigation((Parcel) board5.getPond().getNeighbours()[0],(Parcel) board5.getPond().getNeighbours()[0].getNeighbours()[1]));
        board5.addIrrigation(new Irrigation((Parcel) board5.getPond().getNeighbours()[0].getNeighbours()[0],(Parcel) board5.getPond().getNeighbours()[0].getNeighbours()[1]));
        board5.addIrrigation(new Irrigation((Parcel) board5.getPond().getNeighbours()[0].getNeighbours()[0],(Parcel) board5.getPond().getNeighbours()[0].getNeighbours()[1].getNeighbours()[0]));
        board5.addIrrigation(new Irrigation((Parcel) board5.getPond().getNeighbours()[0].getNeighbours()[0].getNeighbours()[0],(Parcel) board5.getPond().getNeighbours()[0].getNeighbours()[0].getNeighbours()[1]));

        //Checking if the goal is valid now
        assertTrue(pg5.isValid(player5));

    }

}






