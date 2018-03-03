package com.polytech.bdsm.takenoko.ai.strategies;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public enum StrategyType {

    MULTIPLE {
        @Override
        public Strategy newStrategyInstance() {
            return new MultipleStrategy();
        }
    },
    PANDA {
        @Override
        public Strategy newStrategyInstance() {
            return new PandaStrategy();
        }
    },
    GARDENER {
        @Override
        public Strategy newStrategyInstance() {
            return new GardenerStrategy();
        }
    },
    PARCEL {
        @Override
        public Strategy newStrategyInstance() {
            return new ParcelStrategy();
        }
    },
    RANDOM {
        @Override
        public Strategy newStrategyInstance() {
            return new RandomStrategy(false);
        }
    };

    StrategyType() {}

    public abstract Strategy newStrategyInstance();

}
