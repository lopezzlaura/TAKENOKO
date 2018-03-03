package com.polytech.bdsm.takenoko.engine.goals;

import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Bureau de Sébastien Mosser
 */
class GoalFactoryTest {

    GoalFactory factory1, factory2;

    @BeforeEach
    void setUp() {
        HashMap<GoalType, List<Goal>> deck1 = new HashMap<>();
        deck1.put(GoalType.PANDA, new ArrayList<>());
        deck1.put(GoalType.GARDENER, new ArrayList<>());
        deck1.put(GoalType.PARCEL, new ArrayList<>());
        List<Bamboo> b = new ArrayList<>();
        b.add(new Bamboo(Color.GREEN));
        b.add(new Bamboo(Color.GREEN));
        b.add(new Bamboo(Color.GREEN));
        deck1.get(GoalType.PANDA).add(new PandaGoal(0, b));
        deck1.get(GoalType.GARDENER).add(new GardenerGoal(0, 0, Color.GREEN, 0, Facility.NO_FACILITY));

        factory1 = new GoalFactory(PossibleGoals.getPossibleGoals(), new Random());
        factory2 = new GoalFactory(deck1, new Random());
    }

    @Test
    void drawGoalTest() {
        // Check if the goal is in the good GoalType when drawn
        Goal f1g1 = factory1.drawGoal(GoalType.PANDA);
        Goal f1g2 = factory1.drawGoal(GoalType.GARDENER);
        Goal f2g1 = factory2.drawGoal(GoalType.PANDA);
        Goal f2g2 = factory2.drawGoal(GoalType.GARDENER);

        assertEquals(GoalType.PANDA, f1g1.getGoalType());
        assertEquals(GoalType.GARDENER, f1g2.getGoalType());
        assertEquals(GoalType.PANDA, f2g1.getGoalType());
        assertEquals(GoalType.GARDENER, f2g2.getGoalType());

        // Check the drawing in an empty deck
        assertThrows(NullPointerException.class, () -> factory2.drawGoal(GoalType.PARCEL)
        , "This deck is empty");
    }

}