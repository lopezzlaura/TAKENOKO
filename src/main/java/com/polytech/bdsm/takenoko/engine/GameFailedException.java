package com.polytech.bdsm.takenoko.engine;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class GameFailedException extends RuntimeException {

    /**
     * Normal Constructor
     * This exception should be raised only if the program cannot correct the state of the game
     * The main reason why this exception is raised is because of the weather
     *
     * @param s The message for why the game failed
     */
    public GameFailedException(String s) {
        super(s);
    }
}
