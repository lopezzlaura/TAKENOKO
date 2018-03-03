package com.polytech.bdsm.takenoko.engine.graph;

import java.util.*;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class Graph {

    protected final GraphNode root;

    /**
     * Normal constructor of a graph
     *
     * @param root Root node of this graph
     */
    public Graph(GraphNode root) {
        this.root = root;
    }

    /**
     * Getter for the root node of this graph
     *
     * @return root GraphNode
     */
    public GraphNode getRoot() {
        return root;
    }

    /**
     * Breath-first search algorithm in this graph
     *
     * @param lambda Function to execute for each node of this graph
     */
    public void BFS(ISearch lambda) {

        Queue<GraphNode> queue = new LinkedList<>();
        List<GraphNode> visitedNodes = new ArrayList<>();
        lambda.action(root);
        root.visited = true;
        visitedNodes.add(root);
        queue.add(root);

        while (!queue.isEmpty()) {
            GraphNode v = queue.poll();
            for (GraphNode w : v.neighbours) {
                if (w != null && !w.visited) {
                    lambda.action(w);
                    w.visited = true;
                    visitedNodes.add(w);
                    queue.add(w);
                }
            }
        }
        for (GraphNode gn : visitedNodes) {
            gn.visited = false;
        }
    }

    /**
     * Method to return the nodes of the Graph in a List form
     *
     * @return A List that contain all the GraphNodes of the Graph
     */
    public List<GraphNode> asList() {
        List<GraphNode> nodes = new ArrayList<>();
        BFS(nodes::add);
        return nodes;
    }

    /**
     * Count the nodes of this graph
     *
     * @return Number of nodes in this graph
     */
    public int size() {
        return this.asList().size();
    }

    /**
     * Method to check if a GraphNode is contained in a Graph
     *
     * @param graphN The GraphNode to check if it is contained in the Graph
     * @return True if the GraphNode is indeed contain in the Graph, false otherwise
     */
    public boolean contains(GraphNode graphN) {
        return this.asList().contains(graphN);
    }

    /**
     * toString method using BFS
     *
     * @return String representation of this graph
     */
    @Override
    public String toString() {
        List<String> res = new ArrayList<>();
        BFS(gn -> {
            if (gn != null) res.add(gn.toString() + "\n");
        });

        StringBuilder finalRes = new StringBuilder();
        for (String s : res) {
            finalRes.append(s);
        }
        return finalRes.toString();
    }

    /**
     * A method which clones the graph and
     * returns the reference of new cloned source node
     *
     * @param source The root of the Graph to clone
     * @return The reference of the new root cloned
     */
    public GraphNode cloneGraph(GraphNode source) {
        Queue<GraphNode> q = new LinkedList<>();
        q.add(source);

        // An HashMap to keep track of all the
        // nodes which have already been created
        HashMap<GraphNode, GraphNode> hm = new HashMap<>();

        //Put the node into the HashMap
        hm.put(source, (GraphNode) source.clone());

        int i;
        while (!q.isEmpty()) {
            // Get the front node from the queue
            // and then visit all its neighbours
            GraphNode u = q.poll();

            // Get corresponding Cloned Graph Node
            GraphNode cloneNodeU = hm.get(u);
            if (u.neighbours != null) {
                i = 0;
                GraphNode[] v = u.neighbours;
                for (GraphNode graphNode : v) {
                    if (graphNode != null) {

                        // Get the corresponding cloned node
                        // If the node is not cloned then we will
                        // simply get a null
                        GraphNode cloneNodeG = hm.get(graphNode);

                        // Check if this node has already been created
                        if (cloneNodeG == null) {
                            q.add(graphNode);

                            // If not then create a new Node and
                            // put into the HashMap
                            cloneNodeG = (GraphNode) graphNode.clone();

                            hm.put(graphNode, cloneNodeG);
                        }

                        // add the 'cloneNodeG' to neighbour
                        // vector of the cloneNodeG
                        cloneNodeU.neighbours[i] = cloneNodeG;
                    }
                    i++;
                }
            }
        }

        // Return the reference of cloned source Node
        return hm.get(source);
    }

    /**
     * Clone method
     *
     * @return A new instance and the {@code Graph} with the same value as the current object
     */
    @Override
    public Object clone() {
        Graph cloned = new Graph(this.cloneGraph(this.root));
        return cloned;
    }
}
