package com.polytech.bdsm.takenoko.engine.actions.weather;

import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.*;

import java.util.Random;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public enum WeatherDice {

    SUN(0, false) {
        @Override
        public ActionParams getWeatherParams() {
            return new SunActionParams();
        }
    },
    STORM(1, true) {
        @Override
        public ActionParams getWeatherParams() {
            return new StormActionParams();
        }
    },
    WIND(2, false) {
        @Override
        public ActionParams getWeatherParams() {
            return new WindActionParams();
        }
    },
    RAIN(3, true) {
        @Override
        public ActionParams getWeatherParams() {
            return new RainActionParams();
        }
    },
    CLOUDS(4, true) {
        @Override
        public ActionParams getWeatherParams() {
            return new CloudsActionParams();
        }
    },
    UNKNOWN(5, true) {
        @Override
        public ActionParams getWeatherParams() {
            return new UnknownActionParams();
        }
    };

    private int side;
    private boolean playerRequested;

    /**
     * Constructor of the weather dice
     *
     * @param side The side obtained by rolling the dice
     */
    WeatherDice(int side, boolean playerRequested) {
        this.side = side;
        this.playerRequested = playerRequested;
    }

    /**
     * Method to get the side of the dice
     *
     * @return the number of the side
     */
    public int getSide() {
        return this.side;
    }

    /**
     * Getter to know if the player needs to do something with this face
     *
     * @return True if the player has to do something with the ActionParams, False otherwise
     */
    public boolean isPlayerRequested() {
        return playerRequested;
    }

    /**
     * Get the ActionParams associated with this Weather dice side
     *
     * @return The ActionParams to use with this side
     */
    public abstract ActionParams getWeatherParams();

    /**
     * Method to roll the dice and fill in the current <code>Player</code>
     * list of possible actions, depending on the weather randomly
     * picked
     *
     * @return the side of the dice that was obtained
     */
    public static WeatherDice rollTheDice(Random random) {
        int rand = random.nextInt(6);
        return getWeather(rand);
    }

    /**
     * Getter for the weather from the dice
     *
     * @param side the index of the side
     *
     * @return a side of the dice
     */
    public static WeatherDice getWeather(int side) {
        for (WeatherDice md : WeatherDice.values()) {
            if (md.getSide() == side) {
                return md;
            }
        }
        return UNKNOWN;
    }
}

