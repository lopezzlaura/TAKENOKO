package com.polytech.bdsm.takenoko.engine.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Bureau de Sébastien Mosser
 */
class GraphNodeTest {

    private GraphNode root, a, b, c;
    private Graph graph;

    @BeforeEach
    void setUp() {
        root = new GraphNode(6, new Coordinate(0,0));
        a = new GraphNode(6, new Coordinate(0,0));
        b = new GraphNode(6, new Coordinate(0,0));
        c = new GraphNode(6, new Coordinate(0,0));

        graph = new Graph(root);
    }

    @Test
    void getNeighboursTest() {
        assertTrue(true);
    }

    @Test
    void addNodeTest() throws Exception {
        // Vérification sans autre nodes que la racine
        assertEquals(1, graph.size());

        // Ajout d'une node en haut à gauche et en haut à droite de la racine
        graph.getRoot().addNode(5, a);
        graph.getRoot().addNode(1, b);
        assertEquals(3, graph.size());
        assertEquals(3, graph.size());

        // Ajout d'une node en haut de la racine
        graph.getRoot().addNode(0, c);
        assertEquals(4, graph.size());
        assertEquals(b, c.getNeighbours()[2]);
        assertEquals(a, c.getNeighbours()[4]);
        assertEquals(root, c.getNeighbours()[3]);

        // Ajout d'une node entre a et c
        GraphNode d = new GraphNode(6, new Coordinate(0,0));
        c.addNode(5, d);
        assertEquals(5, graph.size());
        assertEquals(a, d.getNeighbours()[3]);
        assertEquals(c, d.getNeighbours()[2]);
        assertEquals(d, c.getNeighbours()[5]);
        assertEquals(d, a.getNeighbours()[0]);

        // Construction et vérification d'un graphe plus complexe
        GraphNode cRoot = new GraphNode(6, new Coordinate(0, 0));
        cRoot.addNode(0, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.addNode(1, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.addNode(2, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.addNode(3, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.addNode(4, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.addNode(5, new GraphNode(6, new Coordinate(0, 0)));

        cRoot.getNeighbours()[0].addNode(5, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.getNeighbours()[0].addNode(0, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.getNeighbours()[0].addNode(1, new GraphNode(6, new Coordinate(0, 0)));

        cRoot.getNeighbours()[1].addNode(1, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.getNeighbours()[1].addNode(2, new GraphNode(6, new Coordinate(0, 0)));

        cRoot.getNeighbours()[2].addNode(2, new GraphNode(6, new Coordinate(0, 0)));

        cRoot.getNeighbours()[3].addNode(2, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.getNeighbours()[3].addNode(4, new GraphNode(6, new Coordinate(0, 0)));

        cRoot.getNeighbours()[4].addNode(4, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.getNeighbours()[4].addNode(5, new GraphNode(6, new Coordinate(0, 0)));

        cRoot.getNeighbours()[5].addNode(5, new GraphNode(6, new Coordinate(0, 0)));

        cRoot.getNeighbours()[4].getNeighbours()[4].addNode(5, new GraphNode(6, new Coordinate(0, 0)));

        cRoot.getNeighbours()[5].getNeighbours()[5].addNode(5, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.getNeighbours()[5].getNeighbours()[5].addNode(0, new GraphNode(6, new Coordinate(0, 0)));

        cRoot.getNeighbours()[0].getNeighbours()[0].addNode(5, new GraphNode(6, new Coordinate(0, 0)));

        cRoot.getNeighbours()[1].getNeighbours()[1].addNode(0, new GraphNode(6, new Coordinate(0, 0)));
        cRoot.getNeighbours()[1].getNeighbours()[1].addNode(2, new GraphNode(6, new Coordinate(0, 0)));

        Graph complex = new Graph(cRoot);
        assertEquals(24, complex.size());
    }

    @Test
    void getNeighboursCountTest() throws Exception {
        graph.getRoot().addNode(5, a);
        graph.getRoot().addNode(1, b);
        graph.getRoot().addNode(0, c);
        GraphNode d = new GraphNode(6, new Coordinate(0,0));
        c.addNode(5, d);

        assertEquals(4,c.getNeighboursCount());
        assertEquals(3,a.getNeighboursCount());
    }

}