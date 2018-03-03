package com.polytech.bdsm.takenoko.engine.goals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * GoalFactory deck
 *
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public final class GoalFactory {

    private HashMap<GoalType, List<Goal>> deck;
    private final Random randomizer;

    /**
     * Normal constructor for a GoalFactory
     * Initialize only one GoalFactory for a game
     *
     * @param possibleGoals A HashMap of possible goals to add in the deck, classified by <code>GoalType</code>
     */
    public GoalFactory(HashMap<GoalType, List<Goal>> possibleGoals, Random randomizer) {
        this.deck = possibleGoals;
        this.randomizer = randomizer;
    }

    /**
     * Draw a goal in the deck randomly
     *
     * @return A goal drawn in the deck
     */
    public Goal drawGoal(GoalType type) {
        if (this.deck.get(type).isEmpty()) {
            throw new NullPointerException("This deck is empty");
        }

        // Pick a goal at a index between 0 (inclusive) and the number of Goals in the deck (exclusive)
        int indexToDraw = randomizer.nextInt(this.deck.get(type).size());
        Goal goalDrawn = (Goal) this.deck.get(type).get(indexToDraw).clone();

        // Remove this goal from the deck
        this.deck.get(type).remove(indexToDraw);

        return goalDrawn;
    }

    /**
     * Get the goal of a given GoalType
     * @param goalType the GoalType looked for
     * @return an list of goals of the given GoalType present in the deck
     */
    public List<Goal> getGoals(GoalType goalType) {
        if (!this.deck.containsKey(goalType))
            throw new IllegalArgumentException("GoalType doesn't exist in this deck : " + goalType);

        return new ArrayList<>(this.deck.get(goalType));
    }

    public boolean isEmpty(){
        int empty = 0;

        for (GoalType goalType : this.deck.keySet()) {
            if (this.deck.get(goalType).isEmpty()){
                empty++;
            }
        }
        return empty == 4;
    }

    @Override
    public Object clone() {
        HashMap<GoalType, List<Goal>> clonedDeck = new HashMap<>();
        List<Goal> currentClonedGoals;

        for (GoalType gt : this.deck.keySet()) {
            currentClonedGoals = new ArrayList<>();
            for (Goal g : this.deck.get(gt)) {
                currentClonedGoals.add((Goal) g.clone());
            }
            clonedDeck.put(gt, currentClonedGoals);
        }

        GoalFactory cloned = new GoalFactory(clonedDeck, this.randomizer);
        return cloned;
    }
}
