package com.polytech.bdsm.takenoko.engine.actions.params;

import com.polytech.bdsm.takenoko.engine.actions.Action;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class ActionResult {

    private Action action;

    /**
     * Getter of the action
     *
     * @return this action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Setter of this action
     *
     * @param action The new action to set
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * Check if this ActionResult exists
     *
     * @return True if the action exists, false otherwise
     */
    public boolean exists() {
        return action != null;
    }
}
