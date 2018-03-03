package com.polytech.bdsm.takenoko.engine.actions;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class ActionRefusedException extends Exception {

    /**
     * Normal Constructor
     * This exception if use only in case of wrong input from the Player
     * If the ActionParam are not correct or null, the ActionFactory will raised this exception
     *
     * @param message   The message for why the action failed
     */
    public ActionRefusedException(String message) {
        super(message);
    }
}
