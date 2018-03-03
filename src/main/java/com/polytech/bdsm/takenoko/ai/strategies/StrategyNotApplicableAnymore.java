package com.polytech.bdsm.takenoko.ai.strategies;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class StrategyNotApplicableAnymore extends Exception {

    private StrategyType triedStrategy;
    private StrategyType newStrategyProposed;

    /**
     * Normal constructor
     *
     * @param triedStrategy       The strategy type that was tested and that not work anymore
     * @param newStrategyProposed The new strategy proposed to switch on
     */
    public StrategyNotApplicableAnymore(StrategyType triedStrategy, StrategyType newStrategyProposed) {
        this.triedStrategy = triedStrategy;
        this.newStrategyProposed = newStrategyProposed;
    }

    /**
     * Getter of the new strategy proposed
     *
     * @return The new StrategyType proposed
     */
    public StrategyType getNewStrategyProposed() {
        return newStrategyProposed;
    }

    /**
     * Getter of the already tried strategy
     *
     * @return the not applicable anymore StrategyType
     */
    public StrategyType getTriedStrategy() {
        return triedStrategy;
    }
}
