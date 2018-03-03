package com.polytech.bdsm.takenoko.engine.actions.weather;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.ai.strategies.PandaStrategy;
import com.polytech.bdsm.takenoko.engine.actions.DrawIrrigation;
import com.polytech.bdsm.takenoko.engine.actions.PlaceFacility;
import com.polytech.bdsm.takenoko.engine.actions.params.PlaceParcelParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.*;
import com.polytech.bdsm.takenoko.engine.board.Board;
import com.polytech.bdsm.takenoko.engine.board.Irrigation;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.facilities.FertilizerLeaf;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.parcels.YellowParcel;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WeatherActionTest {

    private Player player, player2, player3;
    private Board board, board2;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        board2 = new Board();
        board2.getPond().addNode(0,new GreenParcel());
        board2.getPond().addNode(1,new GreenParcel());
        board2.getPond().addNode(2,new GreenParcel());
        board2.getPond().addNode(3,new GreenParcel());
        board2.getPond().addNode(4,new GreenParcel());
        board2.getPond().addNode(5,new GreenParcel());
        player = new Bot("Sherlock", board, new PandaStrategy(), new Random());
        player2 = new Bot("Watson", board2, new PandaStrategy(), new Random());
        player3 = new Bot("Moriarty", board, new PandaStrategy(), new Random());
    }

    @Test
    void sunActionTest() throws Exception {

        SunAction sunAction = new SunAction(player);
        sunAction.execute();
        assertEquals(1, player.getNbActions());

        sunAction.execute();
        assertEquals(2, player.getNbActions());
    }

    @Test
    void cloudActionTest() throws Exception {
        CloudsAction cloudAction = new CloudsAction(player2, Facility.POND_FACILITY, null);
        cloudAction.execute();
        assertTrue(player2.getInventory().containFacility(Facility.POND_FACILITY));
        for (int i = 1; i < 3; i++) {
            cloudAction.execute();
        }
        assertTrue(player2.getInventory().getFacilities().size() == 3);
        cloudAction = new CloudsAction(player2,Facility.PADDOCK_FACILITY, null);
        for(int i = 0; i < 3; i++) {
            cloudAction.execute();
        }
        assertTrue(player2.getInventory().getFacilities().size() == 6);
        cloudAction = new CloudsAction(player2,Facility.FERTILIZER_FACILITY, null);
        for(int i = 0; i < 3; i++) {
            cloudAction.execute();
        }
        assertTrue(player2.getInventory().getFacilities().size() == 9);

    }

    @Test
    void rainActionTest() throws Exception {
        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1, new PinkParcel());
        board.getPond().addNode(2, new YellowParcel());

        //Do a RainAction on a parcel next to the pond, with no bamboo
        assertEquals(0, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());

        RainAction rainAction = new RainAction(player, (Parcel) board.getPond().getNeighbours()[0]);
        rainAction.execute();

        assertEquals(1, ((Parcel)board.getPond().getNeighbours()[0]).getBambooStack().size());

        //Do a RainAction on a NOT irrigated parcel, with no bamboo
        board.getPond().getNeighbours()[0].addNode(1, new PinkParcel());

        rainAction = new RainAction(player, (Parcel) board.getPond().getNeighbours()[0].getNeighbours()[1]);
        assertFalse(rainAction.execute());

        assertEquals(0, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]).getBambooStack().size());

        //Do a RainAction on a parcel that already has 4 bamboos
        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[1]));
        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[0], (Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]));
        ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]).pushBamboo();

        rainAction = new RainAction(player, (Parcel) board.getPond().getNeighbours()[0].getNeighbours()[1]);
        assertFalse(rainAction.execute());

        assertEquals(4, ((Parcel)board.getPond().getNeighbours()[0].getNeighbours()[1]).getBambooStack().size());

        //Do a RainAction on a parcel that has a paddock facility
        ((Parcel)board.getPond().getNeighbours()[1]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[1]).pushBamboo();
        new PlaceFacility(player, (Parcel)board.getPond().getNeighbours()[1], Facility.PADDOCK_FACILITY).execute();

        rainAction = new RainAction(player, (Parcel) board.getPond().getNeighbours()[1]);
        assertTrue(rainAction.execute());

        assertEquals(3, ((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().size());

        //Do a RainAction on a parcel that has a pond facility
        board.getPond().getNeighbours()[1].addNode(1, new PinkParcel());

        new PlaceFacility(player, (Parcel)board.getPond().getNeighbours()[1].getNeighbours()[1], Facility.POND_FACILITY).execute();
        rainAction = new RainAction(player, (Parcel) board.getPond().getNeighbours()[1].getNeighbours()[1]);
        assertTrue(rainAction.execute());

        assertEquals(1, ((Parcel)board.getPond().getNeighbours()[1].getNeighbours()[1]).getBambooStack().size());

        //Do a RainAction on a parcel of 1 bamboo that has a fertilizer facility
        board.getPond().getNeighbours()[2].addNode(1, new YellowParcel());
        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[1], (Parcel)board.getPond().getNeighbours()[2]));
        board.addIrrigation(new Irrigation((Parcel)board.getPond().getNeighbours()[2], (Parcel)board.getPond().getNeighbours()[2].getNeighbours()[1]));

        new PlaceFacility(player, (Parcel)board.getPond().getNeighbours()[2].getNeighbours()[1], Facility.FERTILIZER_FACILITY).execute();
        assertTrue(board.getPond().getNeighbours()[2].getNeighbours()[1] instanceof FertilizerLeaf);
        rainAction = new RainAction(player, (Parcel) board.getPond().getNeighbours()[2].getNeighbours()[1]);
        assertTrue(rainAction.execute());

        assertEquals(2, ((Parcel)board.getPond().getNeighbours()[2].getNeighbours()[1]).getBambooStack().size());

        //Do a RainAction on a parcel of 3 bamboos that has a fertilizer facility
        ((Parcel)board.getPond().getNeighbours()[2].getNeighbours()[1]).pushBamboo();

        assertFalse(rainAction.execute());

        assertEquals(4, ((Parcel)board.getPond().getNeighbours()[2].getNeighbours()[1]).getBambooStack().size());
    }

    @Test
    void stormActionTest() throws Exception {
        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1, new PinkParcel());
        board.getPond().addNode(2, new YellowParcel());

        StormAction stormAction;

        //Déplacer le panda sur une parcel sans bambou
        stormAction = new StormAction(player, (Parcel) board.getPond().getNeighbours()[1]);
        stormAction.execute();

        assertEquals(board.getPond().getNeighbours()[1], board.getPanda().getPosition());
        assertEquals(0, player.getInventory().getBamboosCount());

        //Déplacer le panda sur une parcel avec 1 bambou
        ((Parcel)board.getPond().getNeighbours()[1]).pushBamboo();
        stormAction = new StormAction(player, (Parcel) board.getPond().getNeighbours()[1]);
        stormAction.execute();

        assertEquals(board.getPond().getNeighbours()[1], board.getPanda().getPosition());
        assertEquals(1, player.getInventory().getBamboosCount());

        //Déplacer le panda sur une parcel avec 2 bambous
        ((Parcel)board.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[0]).pushBamboo();

        stormAction = new StormAction(player, (Parcel) board.getPond().getNeighbours()[0]);
        stormAction.execute();

        assertEquals(board.getPond().getNeighbours()[0], board.getPanda().getPosition());
        assertEquals(2, player.getInventory().getBamboosCount());

        //Déplacer le panda sur la parcelle où il était précédemment
        stormAction = new StormAction(player, board.getPanda().getPosition());
        stormAction.execute();

        assertEquals(board.getPond().getNeighbours()[0], board.getPanda().getPosition());
        assertEquals(3, player.getInventory().getBamboosCount());

        //Déplacer le panda sur le pond parcel
        stormAction = new StormAction(player, board.getPond());
        stormAction.execute();

        assertEquals(board.getPond(), board.getPanda().getPosition());
        assertEquals(3, player.getInventory().getBamboosCount());

        // Déplacer le panda sur une parcelle avec du bambou, où il y a un paddock
        new PlaceFacility(player, (Parcel)board.getPond().getNeighbours()[2], Facility.PADDOCK_FACILITY).execute();
        ((Parcel)board.getPond().getNeighbours()[2]).pushBamboo();
        ((Parcel)board.getPond().getNeighbours()[2]).pushBamboo();

        stormAction = new StormAction(player, (Parcel)board.getPond().getNeighbours()[2]);
        stormAction.execute();

        assertEquals(board.getPond().getNeighbours()[2], board.getPanda().getPosition());
        assertEquals(3, player.getInventory().getBamboosCount());
        assertEquals(2, ((Parcel)board.getPond().getNeighbours()[2]).getBambooStack().size());
    }

    @Test
    void windActionTest() throws Exception {

        //Basic case
        int size = player.getActionList().size();

        WindAction windAction = new WindAction(player);
        windAction.execute();
        assertEquals(size * 2, player.getActionList().size());

        assertTrue(new DrawIrrigation(player).execute());
        assertTrue(new DrawIrrigation(player).execute());

        //Doing three action, one different between two same
        size = player2.getActionList().size();

        windAction = new WindAction(player2);
        windAction.execute();
        assertEquals(size * 2, player2.getActionList().size());

        assertTrue(new DrawIrrigation(player2).execute());
        assertTrue(new PlaceFacility(player2, (Parcel) board2.getPond().getNeighbours()[2], Facility.PADDOCK_FACILITY).execute());
        assertTrue(new DrawIrrigation(player2).execute());
    }

    @Test
    void unknownActionTest() throws Exception {
        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1, new PinkParcel());
        board.getPond().addNode(2, new YellowParcel());
        board.getPond().addNode(3, new YellowParcel());

        ((Parcel)board.getPond().getNeighbours()[1]).pushBamboo();

        UnknownActionParams uap = new UnknownActionParams();

        //Le player choisi une StormAction
        StormActionParams stormActionParams = new StormActionParams();

            //Celle-ci ne contient pas les bons paramètres
            assertFalse(stormActionParams.paramsNotNull());
            UnknownAction unknownAction = new UnknownAction(player, stormActionParams);
            assertFalse(unknownAction.execute());

            //Celle-ci est correctement paramétrée
            stormActionParams.setParcel((Parcel)board.getPond().getNeighbours()[1]);

            //Pre-checks
            assertEquals(1, ((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().size());
            assertTrue(stormActionParams.paramsNotNull());

            //Action
            unknownAction = new UnknownAction(player, stormActionParams);
            assertTrue(unknownAction.execute());

            //Post-checks
            assertEquals(board.getPond().getNeighbours()[1], board.getPanda().getPosition());
            assertEquals(1,player.getInventory().getBamboosCount());
            assertEquals(0, ((Parcel)board.getPond().getNeighbours()[1]).getBambooStack().size());

        //Le player choisi une sun action
        SunActionParams sunActionParams = new SunActionParams();

        assertTrue(sunActionParams.paramsNotNull());
        unknownAction = new UnknownAction(player, sunActionParams);
        assertTrue(unknownAction.execute());
        assertEquals(1, player.getNbActions());

        //Le player choisi une cloud action
        CloudsActionParams cloudsActionParams = new CloudsActionParams();

            //Celle-ci ne contient pas les bons paramètres
            assertFalse(cloudsActionParams.paramsNotNull());
            unknownAction = new UnknownAction(player, cloudsActionParams);
            assertFalse(unknownAction.execute());

            //Celle-ci est correctement paramétrée
            cloudsActionParams.setFacility(Facility.POND_FACILITY);
            assertTrue(unknownAction.execute());

            assertEquals(Facility.POND_FACILITY, player.getInventory().getFacilities().get(0));
            assertEquals(1, player.getInventory().getFacilities().size());

        //Le player choisi une rain action
        RainActionParams rainActionParams = new RainActionParams();

            //Celle-ci ne contient pas les bons paramètres
            assertFalse(rainActionParams.paramsNotNull());
            unknownAction = new UnknownAction(player, rainActionParams);
            assertFalse(unknownAction.execute());

            //Celle-ci est correctement paramétrée
            rainActionParams.setParcel((Parcel)board.getPond().getNeighbours()[3]);
            assertTrue(unknownAction.execute());

            assertEquals(1, ((Parcel)board.getPond().getNeighbours()[3]).getBambooStack().size());

        //Le player choisi de faire une WindAction
            //

        //Problème : le player choisi une action autre que la météo
        PlaceParcelParams ppp = new PlaceParcelParams();
        unknownAction = new UnknownAction(player, ppp);
        assertFalse(unknownAction.execute());
    }


    @Test
    void rollTheDice() {
        int nbActionList = player.getActionList().size();

        WeatherDice diceSide = WeatherDice.rollTheDice(player.getBoard().getGlobalRandom());

        // Test if the dice value is in the possible weathers
        assertTrue(Arrays.asList(WeatherDice.values()).contains(diceSide));
    }

}