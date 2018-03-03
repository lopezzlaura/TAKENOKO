package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.*;
import com.polytech.bdsm.takenoko.engine.board.Irrigation;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.goals.PandaGoal;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.GreenParcel;
import com.polytech.bdsm.takenoko.engine.parcels.PinkParcel;
import com.polytech.bdsm.takenoko.engine.parcels.YellowParcel;
import com.polytech.bdsm.takenoko.engine.pnj.Gardener;
import com.polytech.bdsm.takenoko.engine.pnj.Panda;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bureau de Sébastien Mosser
 */
public class

ParamsTest {

    //Actions
    private static PlaceParcelParams placeParcelParams;
    private static PlaceIrrigationParams placeIrrigationParams;
    private static PlaceFacilityParams placeFacilityParams;
    private static MovePandaParams movePandaParams;
    private static MoveGardenerParams moveGardenerParams;
    private static NoneParams noneParams;
    private static ValidateGoalParams validateGoalParams;
    private static DrawGoalParams drawGoalParams;

    //Weather actions
    private static CloudsActionParams cloudsActionParams;
    private static RainActionParams rainActionParams;
    private static StormActionParams stormActionParams;
    private static SunActionParams sunActionParams;
    private static UnknownActionParams unknownActionParams;
    private static WindActionParams windActionParams;

    @BeforeAll
    static void setUpClasses(){

        //Basic actions
        placeParcelParams = new PlaceParcelParams();
        placeIrrigationParams = new PlaceIrrigationParams();
        drawGoalParams = new DrawGoalParams();
        movePandaParams = new MovePandaParams();
        moveGardenerParams = new MoveGardenerParams();
        noneParams = new NoneParams();
        validateGoalParams = new ValidateGoalParams();

        //Facility action
        placeFacilityParams = new PlaceFacilityParams();

        //Weather actions
        cloudsActionParams = new CloudsActionParams();
        rainActionParams = new RainActionParams();
        stormActionParams = new StormActionParams();
        sunActionParams = new SunActionParams();
        unknownActionParams = new UnknownActionParams();
        windActionParams = new WindActionParams();
    }

    @Test
    void getActionType() {

        //Basic tests
            //basic actions
            assertEquals(ActionType.PLACE_PARCEL, placeParcelParams.getActionType());
            assertEquals(ActionType.PLACE_IRRIGATION, placeIrrigationParams.getActionType());
            assertEquals(ActionType.MOVE_PANDA, movePandaParams.getActionType());
            assertEquals(ActionType.MOVE_GARDENER, moveGardenerParams.getActionType());
            assertEquals(ActionType.NONE, noneParams.getActionType());
            assertEquals(ActionType.VALIDATE_GOAL, validateGoalParams.getActionType());
            assertEquals(ActionType.DRAW_GOAL, drawGoalParams.getActionType());

            //facility action
            assertEquals(ActionType.PLACE_FACILITY, placeFacilityParams.getActionType());

            //weather actions
            assertEquals(ActionType.CLOUDS_ACTION, cloudsActionParams.getActionType());
            assertEquals(ActionType.RAIN_ACTION, rainActionParams.getActionType());
            assertEquals(ActionType.STORM_ACTION, stormActionParams.getActionType());
            assertEquals(ActionType.SUN_ACTION, sunActionParams.getActionType());
            assertEquals(ActionType.UNKNOWN_ACTION, unknownActionParams.getActionType());
            assertEquals(ActionType.WIND_ACTION, windActionParams.getActionType());

        //Error cases
            //basic actions
            assertFalse(placeParcelParams.getActionType() == null);
            assertFalse(placeParcelParams.getActionType() == ActionType.MOVE_PANDA);
            assertFalse(placeParcelParams.getActionType() == ActionType.NONE);
            assertFalse(placeIrrigationParams.getActionType() == null);
            assertFalse(placeIrrigationParams.getActionType() == ActionType.PLACE_PARCEL);
            assertFalse(placeIrrigationParams.getActionType() == ActionType.NONE);
            assertFalse(noneParams.getActionType() == null);
            assertFalse(moveGardenerParams.getActionType() == null);
            assertFalse(validateGoalParams.getActionType() == null);
            assertFalse(drawGoalParams.getActionType() == null);
            assertFalse(movePandaParams.getActionType() == null);

            //facility action
            assertFalse(placeFacilityParams.getActionType() == null);
            assertFalse(placeFacilityParams.getActionType() == ActionType.PLACE_PARCEL);

            //weather actions
            assertFalse(cloudsActionParams.getActionType() == null);
            assertFalse(rainActionParams.getActionType() == null);
            assertFalse(stormActionParams.getActionType() == null);
            assertFalse(sunActionParams.getActionType() == null);
            assertFalse(unknownActionParams.getActionType() == null);
            assertFalse(windActionParams.getActionType() == null);

    }

    @Test
    void paramsNotNull() {

        //Error tests

            //PlaceFacilityParams
                //False because no params set
                assertFalse(placeFacilityParams.paramsNotNull());

                //False because only parcel is set
                placeFacilityParams.setParcel(new GreenParcel());
                assertFalse(placeFacilityParams.paramsNotNull());

                //False because the parcel set is null
                placeFacilityParams.setFacility(Facility.POND_FACILITY);
                placeFacilityParams.setParcel(null);
                assertFalse(placeFacilityParams.paramsNotNull());

                placeFacilityParams.setFacility(null);

                //False because only facility is set
                placeFacilityParams.setFacility(Facility.POND_FACILITY);
                assertFalse(placeFacilityParams.paramsNotNull());

            //PlaceParcelParams
                //False because no params set
                assertFalse(placeParcelParams.paramsNotNull());

                //False because params : toPlace(Parcel) and indexToPlace(int) are null
                placeParcelParams.setFrom(new GreenParcel());
                assertFalse(placeParcelParams.paramsNotNull());

                //False because params : from(Parcel) and indexToPlace(int) are null
                placeParcelParams.setFrom(null);
                placeParcelParams.setToPlace(new PinkParcel());
                assertFalse(placeParcelParams.paramsNotNull());

                //False because params : from(Parcel) and toPlace(Parcel)are null
                placeParcelParams.setToPlace(null);
                placeParcelParams.setIndexToPlace(1);
                assertFalse(placeParcelParams.paramsNotNull());

                //False because indexToPlace is a wrong value
                placeParcelParams.setIndexToPlace(7);
                assertFalse(placeParcelParams.paramsNotNull());

            //PlaceIrrigationParams
                //False because no params set
                assertFalse(placeIrrigationParams.paramsNotNull());

                //False because params : Irrigation is null
                Irrigation irrigation = new Irrigation(null, null);
                placeIrrigationParams.setIrrigation(irrigation);
                assertFalse(placeParcelParams.paramsNotNull());

                //False because params : Irrigation has one null param
                irrigation = new Irrigation(new GreenParcel(), null);
                placeIrrigationParams.setIrrigation(irrigation);
                assertFalse(placeParcelParams.paramsNotNull());

            //MovePandaParams
                //False because params not set
                assertFalse(movePandaParams.paramsNotNull());

                //False because pnj is not set
                movePandaParams.setParcel(new GreenParcel());
                assertFalse(movePandaParams.paramsNotNull());

                //False because wrong pnj is set
                movePandaParams.setPNJ(new Gardener(new PinkParcel()));
                assertFalse(movePandaParams.paramsNotNull());

                //False because parcel is not set
                movePandaParams.setParcel(null);
                movePandaParams.setPNJ(new Panda(new GreenParcel()));
                assertFalse(movePandaParams.paramsNotNull());

            //MoveGardenerParams
                //False because params not set
                assertFalse(moveGardenerParams.paramsNotNull());

                //False because parcel is not set
                moveGardenerParams.setPNJ(new Gardener(new PinkParcel()));
                assertFalse(moveGardenerParams.paramsNotNull());

                //False because pnj is not set
                moveGardenerParams.setPNJ(null);
                moveGardenerParams.setParcel(new YellowParcel());
                assertFalse(moveGardenerParams.paramsNotNull());

                //False because wrong pnj is set
                moveGardenerParams.setPNJ(new Panda(new PinkParcel()));
                assertFalse(moveGardenerParams.paramsNotNull());

            //ValidateGoalParams
                //False because params not set
                assertFalse(validateGoalParams.paramsNotNull());

                //False because goal is null
                validateGoalParams.setGoal(null);
                assertFalse(validateGoalParams.paramsNotNull());

            //DrawGoalParams
                //False because params not set
                assertFalse(drawGoalParams.paramsNotNull());

            //RainActionParams
                //False because params not set
                assertFalse(rainActionParams.paramsNotNull());

                //False because parcel is null
                rainActionParams.setParcel(null);
                assertFalse(rainActionParams.paramsNotNull());

            //StormActionParams
                //False because params not set
                assertFalse(stormActionParams.paramsNotNull());

                //False because parcel is null
                stormActionParams.setParcel(null);
                assertFalse(stormActionParams.paramsNotNull());

            //CloudActionParams
                //False because no facility set
                assertFalse(cloudsActionParams.paramsNotNull());

                //False because facility is null
                cloudsActionParams.setFacility(null);
                assertFalse(cloudsActionParams.paramsNotNull());

            //UnknownActionParams
                //False because params are null
                unknownActionParams.setActionParams(null);
                assertFalse(unknownActionParams.paramsNotNull());

            //WindActionParams
                //True because there are no params for Wind Action
                assertTrue(windActionParams.paramsNotNull());

        //Basic tests

            //PlaceParcelParams
            placeParcelParams.setToPlace(new GreenParcel());
            placeParcelParams.setFrom(new GreenParcel());
            placeParcelParams.setIndexToPlace(2);
            assertTrue(placeParcelParams.paramsNotNull());

            //PlaceFacilityParams
            placeFacilityParams.setFacility(Facility.POND_FACILITY);
            placeFacilityParams.setParcel(new GreenParcel());
            assertTrue(placeFacilityParams.paramsNotNull());

            //PlaceIrrigationParams
            placeIrrigationParams.setIrrigation(new Irrigation(new GreenParcel(), new PinkParcel()));
            assertTrue(placeIrrigationParams.paramsNotNull());

            //MovePandaParams
            movePandaParams.setPNJ(new Panda(new GreenParcel()));
            movePandaParams.setParcel(new YellowParcel());
            assertTrue(movePandaParams.paramsNotNull());

            //ValidateGoalParams
            List<Bamboo> bamboos = new ArrayList<>();
            validateGoalParams.setGoal(new PandaGoal(3, bamboos));
            assertTrue(validateGoalParams.paramsNotNull());

            //DrawGoalParams
            drawGoalParams.setGoalType(GoalType.PARCEL);
            assertTrue(drawGoalParams.paramsNotNull());

            //NoneParams
            assertTrue(noneParams.paramsNotNull());

            //CloudsActionParams
            cloudsActionParams.setFacility(Facility.PADDOCK_FACILITY);
            assertTrue(cloudsActionParams.paramsNotNull());

            //RainActionParams
            rainActionParams.setParcel(new GreenParcel());
            assertTrue(rainActionParams.paramsNotNull());

            //StormActionParams
            stormActionParams.setParcel(new GreenParcel());
            assertTrue(stormActionParams.paramsNotNull());

            //SunActionParams
            assertTrue(sunActionParams.paramsNotNull());

            //UnknownActionParams
            unknownActionParams.setActionParams(new NoneParams());
            assertTrue(unknownActionParams.paramsNotNull());

            //WindActionParams
            assertTrue(windActionParams.paramsNotNull());
    }
}
