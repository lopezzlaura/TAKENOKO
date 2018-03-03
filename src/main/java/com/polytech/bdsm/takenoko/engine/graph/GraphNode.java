package com.polytech.bdsm.takenoko.engine.graph;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public class GraphNode {

    protected int degree;
    protected GraphNode[] neighbours;
    protected boolean visited = false;
    protected Coordinate coordinate;

    /**
     * Default constructor of a GraphNode, takes no params, uses normal constructor
     * with a degree of 6 and 0,0 coordinate
     *
     */
    public GraphNode() {
        this(6, new Coordinate(0, 0));
    }

    /**
     * Normal constructor of a GraphNode
     *
     * @param degree Number of neighbours for this node
     */
    public GraphNode(int degree, Coordinate coordinate) {
        this.neighbours = new GraphNode[degree];
        this.degree = degree;
        this.coordinate = coordinate;
    }

    /**
     * Getter of the coordinate of this node
     *
     * @return The coordinate of the node
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Getter of the degree of this node
     *
     * @return The degree of the node
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Getter of this node's neighbours array
     *
     * @return Neighbours array
     */
    public GraphNode[] getNeighbours() {
        return neighbours;
    }

    /**
     * Setter of the visited attribute
     *
     * @param visited The new visited state
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Get the visited state
     *
     * @return true if is visited, false otherwise
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Getter of the number of set neighbours
     *
     * @return The set neighbours number
     */
    public int getNeighboursCount() {
        int count = 0;
        for (GraphNode gn : this.neighbours) {
            if (gn != null) {
                count++;
            }
        }

        return count;
    }

    /**
     * Method to add a neighbour node to this node
     *
     * @param index Position to place the new node, relative at this node
     * @param node  Node to add
     */
    public void addNode(int index, GraphNode node) throws Exception {
        // Vérification qu'il n'y a pas déjà une node à l'emplacement demandé
        if (this.neighbours[index] != null) {
            throw new IllegalArgumentException("This graph node already contains an element at this index : " + index);
        }

        // Mise de visited à vrai sur cette node
        this.visited = true;

        // Ajout de node à l'index spécifié
        this.neighbours[index] = node;
        Coordinate coordinate;
        switch (index) {
            case 0:
                coordinate = new Coordinate(this.coordinate.getX(), this.coordinate.getY() + 2);
                break;
            case 1:
                coordinate = new Coordinate(this.coordinate.getX() + 1, this.coordinate.getY() + 1);
                break;
            case 2:
                coordinate = new Coordinate(this.coordinate.getX() + 1, this.coordinate.getY() - 1);
                break;
            case 3:
                coordinate = new Coordinate(this.coordinate.getX(), this.coordinate.getY() - 2);
                break;
            case 4:
                coordinate = new Coordinate(this.coordinate.getX() - 1, this.coordinate.getY() - 1);
                break;
            case 5:
                coordinate = new Coordinate(this.coordinate.getX() - 1, this.coordinate.getY() + 1);
                break;
            default:
                throw new IllegalArgumentException("Index must be include between 0 and 5");
        }

        node.coordinate = coordinate;

        // Ajout de this dans node à l'index+3 mod 6
        node.neighbours[(index + 3) % 6] = this;

        // Récupération des nodes placées à gauche et à droite de l'index demandé
        GraphNode leftNode = this.neighbours[(index + 5) % 6];
        GraphNode rightNode = this.neighbours[(index + 1) % 6];

        // Si this.neighbours[index+5 mod 6] existe et n'est pas visité
        //  on appelle addNode dans cet élément avec index+1 mod 6
        if (leftNode != null && !leftNode.visited) {
            leftNode.addNode((index + 1) % 6, node);
        }

        // Si this.neighbours[index+1 mod 6] existe et n'est pas visité
        //  on appelle addNode dans cet élément avec index+5 mod 6
        if (rightNode != null && !rightNode.visited) {
            rightNode.addNode((index + 5) % 6, node);
        }

        // Remise de visited à faux sur cette node
        this.visited = false;
    }

    /**
     * Check if this node is equals to an other Object
     *
     * @param obj An Object
     * @return If the current GraphNode is the same as the Object
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof GraphNode && this.coordinate.equals(((GraphNode) obj).coordinate);
    }

    /**
     * Clone method
     *
     * @return A new instance of the {@code GraphNode}
     */
    @Override
    public Object clone() {
        GraphNode copy;
        try {
            copy = this.getClass().newInstance();
            copy.degree = this.degree;
            copy.coordinate = (Coordinate) this.coordinate.clone();

            return copy;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ToString method for this GraphNode
     *
     * @return String representing coordinate of this node
     */
    @Override
    public String toString() {
        return this.coordinate.toString();
    }


}
