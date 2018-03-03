package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.actions.params.*;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.*;
import com.polytech.bdsm.takenoko.engine.actions.weather.*;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public enum ActionType {

    MOVE_PANDA {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            MovePandaParams mp = (MovePandaParams) actionParams;
            try {
                Action toDo = player.getActionFactory().newMoveAction(player, mp.getPNJ(), mp.getParcel());
                result.setAction(toDo);
                return toDo.execute();
            } catch (ActionRefusedException e) {
                return false;
            }
        }
    },
    MOVE_GARDENER {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            MoveGardenerParams mp = (MoveGardenerParams) actionParams;
            try {
                Action toDo = player.getActionFactory().newMoveAction(player, mp.getPNJ(), mp.getParcel());
                result.setAction(toDo);
                return toDo.execute();
            } catch (ActionRefusedException e) {
                return false;
            }
        }
    },
    PLACE_PARCEL {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            PlaceParcelParams ppp = (PlaceParcelParams) actionParams;
            try {
                Action toDo = player.getActionFactory().newPlaceParcel(player, ppp.getToPlace(), ppp.getFrom(), ppp.getIndexToPlace());
                result.setAction(toDo);
                return toDo.execute();
            } catch (Exception e) {
                return false;
            }
        }
    },
    DRAW_IRRIGATION {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            try {
                Action toDo = player.getActionFactory().newDrawIrrigation(player);
                result.setAction(toDo);
                return toDo.execute();
            } catch (ActionRefusedException e) {
                return false;
            }
        }
    },
    PLACE_IRRIGATION {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            PlaceIrrigationParams pip = (PlaceIrrigationParams) actionParams;
            try {
                Action toDo = player.getActionFactory().newPlaceIrrigation(player, pip.getIrrigation());
                result.setAction(toDo);
                return toDo.execute();
            } catch (ActionRefusedException e) {
                return false;
            }
        }
    },
    VALIDATE_GOAL {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            ValidateGoalParams vgp = (ValidateGoalParams) actionParams;
            try {
                Action toDo = player.getActionFactory().newValidateGoal(player, vgp.getGoal());
                result.setAction(toDo);
                return toDo.execute();
            } catch (ActionRefusedException e) {
                return false;
            }
        }
    },
    NONE {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            Action toDo = player.getActionFactory().newNone(player);
            result.setAction(toDo);
            return toDo.execute();
        }
    },
    SUN_ACTION {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            SunActionParams sap = (SunActionParams) actionParams;
            Action toDo = new SunAction(player);
            result.setAction(toDo);
            return toDo.execute();
        }
    },
    RAIN_ACTION {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            RainActionParams rap = (RainActionParams) actionParams;
            Action toDo = new RainAction(player, rap.getParcel());
            result.setAction(toDo);
            return toDo.execute();
        }
    },
    STORM_ACTION {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            StormActionParams sap = (StormActionParams) actionParams;
            Action toDo = new StormAction(player, sap.getParcel());
            result.setAction(toDo);
            return toDo.execute();
        }
    },
    WIND_ACTION {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            WindActionParams wap = (WindActionParams) actionParams;
            Action toDo = new WindAction(player);
            result.setAction(toDo);
            return toDo.execute();
        }
    },
    CLOUDS_ACTION {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            CloudsActionParams cap = (CloudsActionParams) actionParams;
            Action toDo = new CloudsAction(player, cap.getFacility(), cap.getOtherParams());
            result.setAction(toDo);
            return toDo.execute();
        }
    },
    UNKNOWN_ACTION {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            UnknownActionParams uap = (UnknownActionParams) actionParams;
            Action toDo = new UnknownAction(player, uap.getActionParams());
            result.setAction(toDo);
            return toDo.execute();
        }
    },
    DRAW_GOAL {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            DrawGoalParams dgp = (DrawGoalParams) actionParams;
            try {
                Action toDo = player.getActionFactory().newDrawGoal(player, dgp.getGoalType());
                result.setAction(toDo);
                return toDo.execute();
            } catch (Exception e) {
                return false;
            }
        }
    },
    PLACE_FACILITY {
        @Override
        public boolean execute(Player player, ActionParams actionParams, ActionResult result) {
            PlaceFacilityParams pfp = (PlaceFacilityParams) actionParams;
            try {
                Action toDo = player.getActionFactory().newPlaceFacility(player, pfp.getParcel(), pfp.getFacility());
                result.setAction(toDo);
                return toDo.execute();
            } catch (ActionRefusedException e) {
                return false;
            }
        }
    };

    /**
     * Method to execute the given action thanks to the action params
     * and the current player
     *
     * @param player The {@code Player} who wants to do the action
     * @param actionParams The action params needed
     *
     * @return true if the action was correctly executed, false otherwise
     */
    public abstract boolean execute(Player player, ActionParams actionParams, ActionResult result);
}
