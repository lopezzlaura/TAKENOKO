package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.ai.Bot;
import com.polytech.bdsm.takenoko.engine.actions.ActionFactory;
import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionResult;
import com.polytech.bdsm.takenoko.engine.actions.params.PlaceParcelParams;
import com.polytech.bdsm.takenoko.engine.board.*;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.goals.*;
import com.polytech.bdsm.takenoko.engine.parcels.*;
import com.polytech.bdsm.takenoko.engine.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StrategyHelpersTest {
    private Board board;
    private Bot s;
    private Goal g, g1, g2;
    private GoalFactory deck;
    private Parcel greenParcel, greenParcel1, greenParcel2, yellowParcel, yellowParcel1, yellowParcel2, pinkParcel, pinkParcel1, pinkParcel2, pinkParcel3;

    @BeforeEach
    void setUp() throws Exception {
        deck = new GoalFactory(PossibleGoals.getPossibleGoals(), new Random());

        //Creating Board
        board = new Board();
        greenParcel = new GreenParcel();
        yellowParcel = new YellowParcel();
        pinkParcel = new PinkParcel();
        greenParcel1 = new GreenParcel();
        pinkParcel1 = new PinkParcel();
        yellowParcel1 = new YellowParcel();
        greenParcel2 = new GreenParcel();
        yellowParcel2 = new YellowParcel();
        pinkParcel2 = new PinkParcel();
        pinkParcel3 = new PinkParcel();

        //add Parcel on Board
        board.getPond().addNode(0, greenParcel);
        board.getPond().addNode(1, yellowParcel);
        board.getPond().addNode(2, pinkParcel);
        board.getPond().addNode(3, greenParcel1);
        board.getPond().addNode(4, yellowParcel1);
        board.getPond().addNode(5, pinkParcel1);
        board.getPond().getNeighbours()[0].addNode(1, pinkParcel2);
        board.getPond().getNeighbours()[1].addNode(2, greenParcel2);
        board.getPond().getNeighbours()[2].addNode(3, yellowParcel2);
        board.getPond().getNeighbours()[4].addNode(5, pinkParcel3);
        //add irrigation on Board
        board.addIrrigation(new Irrigation(pinkParcel, greenParcel1));
        board.addIrrigation(new Irrigation(pinkParcel, yellowParcel2));
        board.addIrrigation(new Irrigation(greenParcel, yellowParcel));
        board.addIrrigation(new Irrigation(pinkParcel2, yellowParcel));

        s = new Bot("DoubleH", board, new PandaStrategy(), 184, new Random());
    }

    @Test
    void handContainsGoalTypeTest() {
        assertTrue(StrategyHelpers.handContainsGoalType(s.getGoals(), GoalType.PANDA));
        assertTrue(StrategyHelpers.handContainsGoalType(s.getGoals(), GoalType.GARDENER));
        assertTrue(StrategyHelpers.handContainsGoalType(s.getGoals(), GoalType.PARCEL));
        s.getGoals().remove(0);
        assertEquals(2, s.getGoals().size());
        assertFalse(StrategyHelpers.handContainsGoalType(s.getGoals(), GoalType.PANDA));
        assertFalse(StrategyHelpers.handContainsGoalType(s.getGoals(), GoalType.EMPEROR));

    }

    @Test
    void goalsDeckContainsGoalTypeTest() {
        assertTrue(StrategyHelpers.goalsDeckContainsGoalType(deck, GoalType.PANDA));
        assertTrue(StrategyHelpers.goalsDeckContainsGoalType(deck, GoalType.GARDENER));
        assertTrue(StrategyHelpers.goalsDeckContainsGoalType(deck, GoalType.EMPEROR));
        assertTrue(StrategyHelpers.goalsDeckContainsGoalType(deck, GoalType.PARCEL));

        deck.drawGoal(GoalType.EMPEROR);
        assertFalse(StrategyHelpers.goalsDeckContainsGoalType(deck, GoalType.EMPEROR));

        List<Goal> goalList = new ArrayList<>(deck.getGoals(GoalType.PANDA));
        for (Goal g : goalList) {
            deck.drawGoal(GoalType.PANDA);
        }
        assertFalse(StrategyHelpers.goalsDeckContainsGoalType(deck, GoalType.PANDA));
    }

    @Test
    void getExpectedParcelOfColorTest() {
        //Test to get all the pink parcels in the board
        List<Parcel> expected1 = new ArrayList<>();
        expected1.add(pinkParcel);
        expected1.add(pinkParcel1);
        expected1.add(pinkParcel2);
        expected1.add(pinkParcel3);
        List<Parcel> obtained1 = StrategyHelpers.getExpectedParcelsOfColor(false, BoardUtils.getAllParcels(board), Color.PINK, false, board, null);
        assertTrue(expected1.containsAll(obtained1) && (obtained1.containsAll(expected1)));

        //Test to get all the irrigated pink parcels
        List<Parcel> expected2 = new ArrayList<>();
        expected2.add(pinkParcel2);
        expected2.add(pinkParcel);
        expected2.add(pinkParcel1);
        List<Parcel> obtained2 = StrategyHelpers.getExpectedParcelsOfColor(false, BoardUtils.getAllParcels(board), Color.PINK, true, board, null);
        assertTrue(expected2.containsAll(obtained2) && obtained2.containsAll(expected2));

        //adding bamboos on some parcels
        yellowParcel.pushBamboo();
        for (int i = 0; i < 4; i++) {
            yellowParcel1.pushBamboo();
        }
        pinkParcel.pushBamboo();
        //Test to get all the yellow parcels with bamboos on it
        List<Parcel> expected3 = new ArrayList<>();
        expected3.add(yellowParcel);
        expected3.add(yellowParcel1);
        List<Parcel> obtained3 = StrategyHelpers.getExpectedParcelsOfColor(true, BoardUtils.getAllParcels(board), Color.YELLOW, false, board, null);
        assertTrue(expected3.containsAll(obtained3) && obtained3.containsAll(expected3));
    }

    @Test
    void getParcelThatCanCompleteParcelGoalTest() throws Exception {
        // Basic Test with semi-line
        List<Goal> curved1 = new ArrayList<>(Collections.singletonList(new ParcelGoal(666, ParcelGoalPattern.SEMILINE, Color.GREEN)));
        List<Goal> curved2 = new ArrayList<>(Collections.singletonList(new ParcelGoal(666, ParcelGoalPattern.SEMILINE, Color.PINK)));

        Board basic = new Board();
        basic.getPond().addNode(1, new PinkParcel());
        basic.getPond().addNode(2, new GreenParcel());
        basic.getPond().addNode(3, new GreenParcel());

        List<Parcel> possibleParcels = new ArrayList<>(Arrays.asList(new PinkParcel(), new GreenParcel(), new YellowParcel()));

        PlaceParcelParams params1 = StrategyHelpers.getParcelThatCanCompleteParcelGoal(curved1, possibleParcels, basic);
        assertTrue(params1.paramsNotNull());
        assertFalse(StrategyHelpers.getParcelThatCanCompleteParcelGoal(curved2, possibleParcels, basic).paramsNotNull());

        // More precise test, check if the collected parcelParams is the expected (either the PondParcel or the Green under it)
        assertSame(possibleParcels.get(1), params1.getToPlace());
        assertTrue(basic.getPond().getNeighbours()[3] == params1.getFrom() || basic.getPond() == params1.getFrom());
        assertTrue(params1.getIndexToPlace() == 5 || params1.getIndexToPlace() == 4);

        // Test of the Diamond pattern
        Board diamond = new Board();
        diamond.getPond().addNode(2, new GreenParcel());
        diamond.getPond().addNode(3, new GreenParcel());
        diamond.getPond().getNeighbours()[3].addNode(2, new PinkParcel());
        diamond.getPond().getNeighbours()[2].addNode(2, new YellowParcel());

        List<Goal> validDiamondGoal = new ArrayList<>(Collections.singletonList(new ParcelGoal(666, ParcelGoalPattern.DIAMOND, Color.PINK, Color.GREEN)));
        List<Goal> wrongDiamondGoal = new ArrayList<>(Collections.singletonList(new ParcelGoal(666, ParcelGoalPattern.DIAMOND, Color.GREEN, Color.YELLOW)));

        PlaceParcelParams params2 = StrategyHelpers.getParcelThatCanCompleteParcelGoal(validDiamondGoal, possibleParcels, diamond);
        assertTrue(params2.paramsNotNull());
        assertFalse(StrategyHelpers.getParcelThatCanCompleteParcelGoal(wrongDiamondGoal, possibleParcels, diamond).paramsNotNull());
    }

    @Test
    void placeRandomParcelTest() throws Exception {
        Board board = new Board();
        ParcelFactory factory = new ParcelFactory(PossibleParcels.getPossibleParcels(), new Random());
        Player sherlock = new Bot("Sherlock", board, new PandaStrategy(), new Random());
        sherlock.getActionFactory().pickParcel(factory);
        board.getPond().addNode(0, new GreenParcel());
        board.getPond().addNode(1, new GreenParcel());
        board.getPond().addNode(2, new GreenParcel());

        int nbParcelOnBoard = board.size();

        PossiblePlacingResult placingResult = BoardUtils.getPossibleParcelsToPlace(board);

        List<Parcel> parcelToPicks = sherlock.getActionFactory().pickParcel(new ParcelFactory(PossibleParcels.getPossibleParcels(), new Random()));

        PlaceParcelParams params = StrategyHelpers.placeRandomParcel(board, parcelToPicks, new Random());

        assertTrue(placingResult.getAllParcels().contains(params.getFrom()));

        assertTrue(placingResult.getParcelPossibleIndexes(params.getFrom()).contains(params.getIndexToPlace()));

        int ngh = 0;
        for (int i = 0; i < 6; i++) {
            if (placingResult.getAllParcels().get(placingResult.getAllParcels().indexOf(params.getFrom())).getNeighbours()[i] != null)
                ngh++;
        }

        assertTrue(ngh >= 2);

        assertTrue(params.paramsNotNull());

        ActionResult result = new ActionResult();
        sherlock.getActionList().add(ActionType.PLACE_PARCEL);

        assertTrue(sherlock.getActionFactory().doAction(params, sherlock, result));

        assertTrue(result.exists());

        assertEquals(nbParcelOnBoard + 1, board.size());
    }

    @Test
    void irrigateAllBoardTest() {
        boolean allIrrigated = true;
        Board board1 = (Board) board.clone();
        for (Parcel p : BoardUtils.getAllParcels(board1)) {
            if (!(BoardUtils.isParcelIrrigated(board1, p))) {
                allIrrigated = false;
                break;
            }
        }
        assertFalse(allIrrigated);

        StrategyHelpers.irrigateAllBoard(board1);

        allIrrigated = true;
        for (Parcel p : BoardUtils.getAllParcels(board1)) {
            if (!(BoardUtils.isParcelIrrigated(board1, p))) {
                allIrrigated = false;
                break;
            }
        }
        assertTrue(allIrrigated);
    }

    @Test
    void tryPlacingParcelAccessibleByPNJTest() throws Exception {

        Board b = new Board();
        b.getPond().addNode(0, new GreenParcel());
        b.getPond().addNode(1, new YellowParcel());
        b.getPond().getNeighbours()[0].addNode(1, new PinkParcel());
        PlaceParcelParams ppp = new PlaceParcelParams();
        PlaceParcelParams ppp1 = new PlaceParcelParams();
        PlaceParcelParams ppp2 = new PlaceParcelParams();
        ppp.setIndexToPlace(0);
        ppp.setFrom((Parcel) b.getPond().getNeighbours()[0]);
        ppp.setToPlace(new GreenParcel());
        ppp1.setFrom((Parcel) b.getPond().getNeighbours()[1].getNeighbours()[0]);
        ppp1.setToPlace(new GreenParcel());
        ppp1.setIndexToPlace(5);
        ppp2.setFrom((Parcel) b.getPond().getNeighbours()[1]);
        ppp2.setToPlace(new GreenParcel());
        ppp2.setIndexToPlace(1);


        PossibleMovesResult pmr = BoardUtils.getPossibleMoves(b.getPond());
        List<Parcel> parcelList = new ArrayList<>();
        parcelList.add(b.getPond());
        parcelList.add((Parcel) b.getPond().getNeighbours()[0]);
        parcelList.add((Parcel) b.getPond().getNeighbours()[0].getNeighbours()[1]);
        parcelList.add((Parcel) b.getPond().getNeighbours()[1]);
        PossiblePlacingResult ppr = BoardUtils.getPossibleParcelsToPlace(b);
        PlaceParcelParams pppToCheck = StrategyHelpers.tryPlacingParcelAccessibleByPNJ(new ActionFactory(), pmr, parcelList, ppr, new GreenParcel());

        assertTrue(ppp.getFrom().equals(pppToCheck.getFrom()) || ppp1.getFrom().equals(pppToCheck.getFrom()) || ppp2.getFrom().equals(pppToCheck.getFrom()));
        assertTrue(ppp.getToPlace().equals(pppToCheck.getToPlace()) || ppp1.getToPlace().equals(pppToCheck.getToPlace()) || ppp2.getToPlace().equals(pppToCheck.getToPlace()));
        assertTrue(ppp.getIndexToPlace() == pppToCheck.getIndexToPlace() || ppp1.getIndexToPlace() == pppToCheck.getIndexToPlace() || ppp2.getIndexToPlace() == pppToCheck.getIndexToPlace());
    }

    @Test
    void chooseBestFacilityToDrawTest() {
        //Test with a bot with a Panda Strategy
        assertTrue(s.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.POND_FACILITY);
        //Test with a bot with a Gardener Strategy
        Bot g = new Bot("g", board, new GardenerStrategy(), 123, board.getGlobalRandom());
        assertTrue(g.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.FERTILIZER_FACILITY);
        //Test with a bot with a Parcel Strategy
        Bot p = new Bot("p", board, new ParcelStrategy(), 124, board.getGlobalRandom());
        assertTrue(p.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.POND_FACILITY);
        //Test with a bot with a Random Strategy
        Bot r = new Bot("r", board, new RandomStrategy(true), 125, board.getGlobalRandom());
        assertTrue(r.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.PADDOCK_FACILITY);

        board.getFacilityDeck().drawFacility(Facility.POND_FACILITY);
        board.getFacilityDeck().drawFacility(Facility.POND_FACILITY);
        board.getFacilityDeck().drawFacility(Facility.POND_FACILITY);

        assertTrue(s.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.FERTILIZER_FACILITY);
        assertTrue(g.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.FERTILIZER_FACILITY);
        assertTrue(p.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.PADDOCK_FACILITY);
        assertTrue(r.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.PADDOCK_FACILITY);

        board.getFacilityDeck().drawFacility(Facility.PADDOCK_FACILITY);
        board.getFacilityDeck().drawFacility(Facility.PADDOCK_FACILITY);
        board.getFacilityDeck().drawFacility(Facility.PADDOCK_FACILITY);

        assertTrue(s.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.FERTILIZER_FACILITY);
        assertTrue(g.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.FERTILIZER_FACILITY);
        assertTrue(p.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.FERTILIZER_FACILITY);
        assertTrue(r.getInitialStrategy().chooseBestFacilityToDraw(board) == Facility.FERTILIZER_FACILITY);

        Board b = new Board();

        b.getFacilityDeck().drawFacility(Facility.FERTILIZER_FACILITY);
        b.getFacilityDeck().drawFacility(Facility.FERTILIZER_FACILITY);
        b.getFacilityDeck().drawFacility(Facility.FERTILIZER_FACILITY);

        assertTrue(s.getInitialStrategy().chooseBestFacilityToDraw(b) == Facility.POND_FACILITY);
        assertTrue(g.getInitialStrategy().chooseBestFacilityToDraw(b) == Facility.PADDOCK_FACILITY);
        assertTrue(p.getInitialStrategy().chooseBestFacilityToDraw(b) == Facility.POND_FACILITY);
        assertTrue(r.getInitialStrategy().chooseBestFacilityToDraw(b) == Facility.PADDOCK_FACILITY);

    }

    @Test
    void chooseWeatherTest() {
        Bot b = new Bot("b", board, new PandaStrategy(), new Random());
        List<Bamboo> bamboos = new ArrayList<>();
        bamboos.add(new Bamboo(Color.GREEN));
        bamboos.add(new Bamboo(Color.GREEN));
        bamboos.add(new Bamboo(Color.GREEN));
        b.getGoals().add(new PandaGoal(2, bamboos));

        ((Parcel) board.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel) board.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel) board.getPond().getNeighbours()[0]).pushBamboo();

        assertEquals(ActionType.STORM_ACTION, StrategyHelpers.chooseWeather(b).getActionType());

        for (Parcel p : BoardUtils.getAllParcels(board)) {
            p.getBambooStack().clear();
        }

        assertEquals(ActionType.RAIN_ACTION, StrategyHelpers.chooseWeather(b).getActionType());

        b.getGoals().clear();

        b.getGoals().add(new GardenerGoal(1, 4, Color.GREEN, 1, Facility.WHATEVER_FACILITY));

        assertEquals(ActionType.RAIN_ACTION, StrategyHelpers.chooseWeather(b).getActionType());

        b.getGoals().clear();

        assertEquals(ActionType.SUN_ACTION, StrategyHelpers.chooseWeather(b).getActionType());
    }

    @Test
    void getRightBambooParcelsTest() throws Exception {
        Board b = new Board();
        b.getPond().addNode(0, new GreenParcel());
        b.getPond().addNode(1, new PinkParcel());
        b.getPond().addNode(2, new YellowParcel());
        b.getPond().addNode(3, new GreenParcel());

        //Adding 4 bamboos on a Green Parcel
        ((Parcel) b.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[0]).pushBamboo();

        //Adding 3 bamboos on a Pink Parcel
        ((Parcel) b.getPond().getNeighbours()[1]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[1]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[1]).pushBamboo();

        //Adding 2 bamboos on a Yellow Parcel
        ((Parcel) b.getPond().getNeighbours()[2]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[2]).pushBamboo();

        //Adding 1 bamboo on a Green Parcel
        ((Parcel) b.getPond().getNeighbours()[3]).pushBamboo();

        assertTrue(GardenerStrategyHelpers.getRightBambooParcels(b, (Parcel) b.getPond().getNeighbours()[0], Color.GREEN, Facility.WHATEVER_FACILITY, 4));
        assertFalse(GardenerStrategyHelpers.getRightBambooParcels(b, (Parcel) b.getPond().getNeighbours()[0], Color.GREEN, Facility.WHATEVER_FACILITY, 1));
        assertTrue(GardenerStrategyHelpers.getRightBambooParcels(b, (Parcel) b.getPond().getNeighbours()[1], Color.PINK, Facility.WHATEVER_FACILITY, 3));
        assertTrue(GardenerStrategyHelpers.getRightBambooParcels(b, (Parcel) b.getPond().getNeighbours()[2], Color.YELLOW, Facility.WHATEVER_FACILITY, 2));
        assertTrue(GardenerStrategyHelpers.getRightBambooParcels(b, (Parcel) b.getPond().getNeighbours()[3], Color.GREEN, Facility.WHATEVER_FACILITY, 1));
        assertFalse(GardenerStrategyHelpers.getRightBambooParcels(b, (Parcel) b.getPond().getNeighbours()[3], Color.GREEN, Facility.WHATEVER_FACILITY, 4));
    }

    @Test
    void getMoreOrLessBambooParcels() throws Exception {
        Board b = new Board();
        b.getPond().addNode(0, new GreenParcel());
        b.getPond().addNode(1, new GreenParcel());
        b.getPond().addNode(2, new GreenParcel());
        b.getPond().addNode(3, new GreenParcel());
        b.getPond().addNode(4, new GreenParcel());

        GardenerGoal g4 = new GardenerGoal(1, 4, Color.GREEN, 1);
        GardenerGoal g3 = new GardenerGoal(1, 3, Color.GREEN, 1);
        GardenerGoal g2 = new GardenerGoal(1, 2, Color.GREEN, 1);
        GardenerGoal g1 = new GardenerGoal(1, 1, Color.GREEN, 1);
        GardenerGoal g5 = new GardenerGoal(1, 5, Color.GREEN, 1);

        ((Parcel) b.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[0]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[0]).pushBamboo();

        ((Parcel) b.getPond().getNeighbours()[1]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[1]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[1]).pushBamboo();

        ((Parcel) b.getPond().getNeighbours()[2]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[2]).pushBamboo();

        ((Parcel) b.getPond().getNeighbours()[3]).pushBamboo();

        ((Parcel) b.getPond().getNeighbours()[4]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[4]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[4]).pushBamboo();
        ((Parcel) b.getPond().getNeighbours()[4]).pushBamboo();

        List<Parcel> parcelList = BoardUtils.getAllParcels(b);


        //There is 0 parcel with less than 0 bamboos
        assertEquals(0, GardenerStrategyHelpers.getLessOrMoreBambooParcel(parcelList, g1, false, b).size());
        //There is 1 parcel with less than 1 bamboo
        assertEquals(0, GardenerStrategyHelpers.getLessOrMoreBambooParcel(parcelList, g2, false, b).size());
        //There is 1 parcel with less than 2 bamboos
        assertEquals(1, GardenerStrategyHelpers.getLessOrMoreBambooParcel(parcelList, g3, false, b).size());
        //There are 2 parcels with less than 3 bamboos
        assertEquals(2, GardenerStrategyHelpers.getLessOrMoreBambooParcel(parcelList, g4, false, b).size());
        //There are 3 parcels with less than 4 bamboos
        assertEquals(3, GardenerStrategyHelpers.getLessOrMoreBambooParcel(parcelList, g5, false, b).size());

        //There is 3 parcels with more than 2 bamboos
        assertEquals(3, GardenerStrategyHelpers.getLessOrMoreBambooParcel(parcelList, g1, true, b).size());
        //There is 2 parcels with more than 3 bamboo
        assertEquals(2, GardenerStrategyHelpers.getLessOrMoreBambooParcel(parcelList, g2, true, b).size());
        //There is 0 parcel with more than 4 bamboos
        assertEquals(0, GardenerStrategyHelpers.getLessOrMoreBambooParcel(parcelList, g3, true, b).size());
        //There are 0 parcels with more than 5 bamboos
        assertEquals(0, GardenerStrategyHelpers.getLessOrMoreBambooParcel(parcelList, g4, true, b).size());
        //There are 0 parcels with more than 6 bamboos
        assertEquals(0, GardenerStrategyHelpers.getLessOrMoreBambooParcel(parcelList, g5, true, b).size());
    }

    @Test
    void getEasiestGoalToCompleteTest() {
        List<Bamboo> bamboos = new ArrayList<>();
        bamboos.add(new Bamboo(Color.GREEN));
        bamboos.add(new Bamboo(Color.GREEN));
        bamboos.add(new Bamboo(Color.GREEN));

        g1 = new PandaGoal(1, bamboos);

        List<Bamboo> bamboo = new ArrayList<>();
        bamboo.add(new Bamboo(Color.PINK));
        bamboo.add(new Bamboo(Color.PINK));
        bamboo.add(new Bamboo(Color.YELLOW));
        g2 = new PandaGoal(1, bamboo);

        s.getGoals().clear();
        s.getGoals().add(g1);
        s.getGoals().add(g2);
        s.getInventory().addBamboo(new Bamboo(Color.GREEN));
        s.getInventory().addBamboo(new Bamboo(Color.GREEN));
        s.getInventory().addBamboo(new Bamboo(Color.PINK));

        List<PandaGoal> pandaGoals = new ArrayList<>();
        s.getGoals().stream()
                .filter(goal -> goal.getGoalType() == GoalType.PANDA)
                .forEach(goal -> pandaGoals.add((PandaGoal) goal));

        assertTrue(g1 == PandaStrategyHelpers.getEasiestGoalToComplete(pandaGoals, s.getInventory()));
    }

    @Test
    void getDifferentBamboosTest() {
        List<Bamboo> bambooList = new ArrayList<>();
        bambooList.add(new Bamboo(Color.GREEN));
        bambooList.add(new Bamboo(Color.PINK));
        bambooList.add(new Bamboo(Color.PINK));
        bambooList.add(new Bamboo(Color.PINK));
        bambooList.add(new Bamboo(Color.PINK));
        bambooList.add(new Bamboo(Color.YELLOW));

        assertTrue(PandaStrategyHelpers.getDifferentBamboos(bambooList).size() == 3);
    }


}




