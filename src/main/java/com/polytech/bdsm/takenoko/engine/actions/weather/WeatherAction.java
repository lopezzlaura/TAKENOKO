package com.polytech.bdsm.takenoko.engine.actions.weather;

import com.polytech.bdsm.takenoko.engine.actions.Action;
import com.polytech.bdsm.takenoko.engine.player.Player;


/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public abstract class WeatherAction extends Action {

    /**
     * Normal override constructor
     *
     * @param player Player that do the action
     */
    public WeatherAction(Player player) {
        super(player);
    }

    /**
     * Method to get the name of the action
     *
     * @return the name of the action
     */
    @Override
    public String getName() {
        return "Weather action";
    }


}
