package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.GardenerGoal;
import com.polytech.bdsm.takenoko.engine.goals.Goal;
import com.polytech.bdsm.takenoko.engine.goals.PandaGoal;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Bureau de Sébastien Mosser
 */
class ValidateGoalTest {

    private Player player1, player2, player3, player4;
    private Goal goal1, goal2, goal3;
    private Action action, action2, action3, action4,action5 ;
    private Board board;
    private static Bamboo b1, b2, b3;
    private Parcel parcel;

    @BeforeAll
    static void setUpBamboos() throws Exception {
        b1 = new Bamboo(Color.GREEN);
        b2 = new Bamboo(Color.GREEN);
        b3 = new Bamboo(Color.GREEN);
    }

    @BeforeEach
    void setUp() throws Exception {

        //Board creation
        board = new Board();

        //Player1 creation, with 1 PandaGoal achieved
        player1 = new Bot("Jean", board, new PandaStrategy(), new Random());
        player1.getGoals().clear();

        List<Bamboo> b = new ArrayList<>();
        b.add(new Bamboo(Color.GREEN));
        b.add(new Bamboo(Color.GREEN));
        b.add(new Bamboo(Color.GREEN));
        goal1 = new PandaGoal(2, b);
        goal2 = new GardenerGoal(1, 4,Color.GREEN,1, Facility.NO_FACILITY);

        //Add 3 bamboos to the player's inventory
        player1.getInventory().addBamboo(b1);
        player1.getInventory().addBamboo(b2);
        player1.getInventory().addBamboo(b3);

        //Do the validation
        action = new ValidateGoal(player1, goal1);

        //Player2 creation with 1 GardenerGoal achieved
        player2 = new Bot("Marie", board, new PandaStrategy(), new Random());
        player2.getGoals().clear();

        //Create a Parcel with 4 bamboos
        parcel = new GreenParcel();
        parcel.pushBamboo();
        parcel.pushBamboo();
        parcel.pushBamboo();
        parcel.pushBamboo();
        board.getRoot().addNode(1, parcel);

        //Do the validation
        action2 = new ValidateGoal(player2, goal2);

        //Player3 creation, with 2 goals achieved
        player3 = new Bot("Anne", board, new PandaStrategy(), new Random());
        player3.getGoals().clear();

        //Add 3 bamboos to the player's inventory
        player3.getInventory().addBamboo(b1);
        player3.getInventory().addBamboo(b2);
        player3.getInventory().addBamboo(b3);

        //Do the validations
        action3 = new ValidateGoal(player3, goal2);
        action4 = new ValidateGoal(player3, goal1);

        //Player4 creation, with no accomplished goal
        player4 = new Bot("Paul", board, new PandaStrategy(), new Random());
        player4.getGoals().clear();

        //player4.getInventory().addBamboo(b1);
        //player4.getInventory().addBamboo(b2);

        //Do the validation
        action5 = new ValidateGoal(player4, goal1);

        player1.getGoals().add(goal1);
        player1.getGoals().add(goal2);
        player2.getGoals().add(goal1);
        player2.getGoals().add(goal2);
        player3.getGoals().add(goal1);
        player3.getGoals().add(goal2);
        player4.getGoals().add(goal1);
    }

    @Test
    void executeTest() throws Exception {

        //For PandaGoal
            // Pre-check
            assertEquals(2, player1.getGoals().size());
            assertEquals(0, player1.getInventory().getAccomplishedGoal());

            // Action execution
            action.execute();

            // Post-check : has the goal in inventory, delete the goal in player hand
            assertEquals(1, player1.getInventory().getAccomplishedGoal());
            assertEquals(1, player1.getGoals().size());
            assertEquals(2, player1.getScore());

        //For GardenerGoal
            // Pre-check
            assertEquals(2, player2.getGoals().size());
            assertEquals(0, player2.getInventory().getAccomplishedGoal());

            // Action execution
            action2.execute();

            // Post-check
            assertEquals(1, player2.getInventory().getAccomplishedGoal());
            assertEquals(1, player2.getGoals().size());
            assertEquals(1, player2.getScore());

        //For the two accomplished
            // Pre-check
            assertEquals(2, player3.getGoals().size());
            assertEquals(0, player3.getInventory().getAccomplishedGoal());

            // Action execution
            action3.execute();
            action4.execute();

            // Post-check
            assertEquals(2, player3.getInventory().getAccomplishedGoal());
            assertEquals(0, player3.getGoals().size());
            assertEquals(3, player3.getScore());

        //For a ParcelGoal

        //For the three accomplished


        //For no accomplished goal
            // Pre-check
            assertEquals(1, player4.getGoals().size());
            assertEquals(0, player4.getInventory().getAccomplishedGoal());

            // Action execution
            action5.execute();

            // Post-check
            assertEquals(0, player4.getInventory().getAccomplishedGoal());
            assertEquals(1, player4.getGoals().size());
            assertEquals(0, player4.getScore());
    }

}