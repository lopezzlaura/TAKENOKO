package com.polytech.bdsm.takenoko.engine.graph;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */
@FunctionalInterface
public interface ISearch {
    /**
     * Action to execute for each node to be searched
     *
     * @param gn The node where the action will be apply
     */
    void action(GraphNode gn);
}
