package com.polytech.bdsm.takenoko.engine.graph;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    private static GraphNode root, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r;
    private static Graph graph;
    private static List<GraphNode> nodesTest = new ArrayList<>();

    @BeforeAll
    static void setUp() throws Exception {
        root = new GraphNode(6, new Coordinate(0,0));

        graph = new Graph(root);

        //Initiate all graph nodes
        a =  new GraphNode(6, new Coordinate(0,2));
        b = new GraphNode(6, new Coordinate(1,1));
        c = new GraphNode(6, new Coordinate(1,-1));
        d = new GraphNode(6, new Coordinate(0,-2));
        e = new GraphNode(6, new Coordinate(-1,-1));
        f = new GraphNode(6, new Coordinate(-1,1));
        g = new GraphNode(6, new Coordinate(0,4));
        h = new GraphNode(6, new Coordinate(1,3));
        i = new GraphNode(6, new Coordinate(-1,3));
        j = new GraphNode(6, new Coordinate(3,2));
        k = new GraphNode(6, new Coordinate(3,0));
        l = new GraphNode(6, new Coordinate(3,-2));
        m = new GraphNode(6, new Coordinate(1,-3));
        n = new GraphNode(6, new Coordinate(0,-4));
        o = new GraphNode(6, new Coordinate(-1,-3));
        p = new GraphNode(6, new Coordinate(-3,-2));
        q = new GraphNode(6, new Coordinate(-3,0));
        r = new GraphNode(6, new Coordinate(-3,2));

        //6 graph nodes around the root
        graph.getRoot().addNode(0,a);
        graph.getRoot().addNode(1, b);
        graph.getRoot().addNode(2, c);
        graph.getRoot().addNode(3, d);
        graph.getRoot().addNode(4, e);
        graph.getRoot().addNode(5, f);

        //12 graph nodes around the 6 around the root
        graph.getRoot().getNeighbours()[0].addNode(0, g);
        graph.getRoot().getNeighbours()[0].addNode(1, h);
        graph.getRoot().getNeighbours()[0].addNode(5, i);
        graph.getRoot().getNeighbours()[1].addNode(1, j);
        graph.getRoot().getNeighbours()[1].addNode(2, k);
        graph.getRoot().getNeighbours()[2].addNode(2, l);
        graph.getRoot().getNeighbours()[2].addNode(3, m);
        graph.getRoot().getNeighbours()[3].addNode(3, n);
        graph.getRoot().getNeighbours()[3].addNode(4, o);
        graph.getRoot().getNeighbours()[4].addNode(4, p);
        graph.getRoot().getNeighbours()[4].addNode(5, q);
        graph.getRoot().getNeighbours()[5].addNode(5, r);

        //put all the graph nodes in the testing list
        nodesTest.add(root);
        nodesTest.add(a);
        nodesTest.add(b);
        nodesTest.add(c);
        nodesTest.add(d);
        nodesTest.add(e);
        nodesTest.add(f);
        nodesTest.add(g);
        nodesTest.add(h);
        nodesTest.add(i);
        nodesTest.add(j);
        nodesTest.add(k);
        nodesTest.add(l);
        nodesTest.add(m);
        nodesTest.add(n);
        nodesTest.add(o);
        nodesTest.add(p);
        nodesTest.add(q);
        nodesTest.add(r);
    }

    @Test
    void BFS() {
        //Create the list of nodes
        List<GraphNode> nodes = new ArrayList<>();

        //Pre-check
        assertEquals(19, nodesTest.size());

        //Action execution
        //Add each node visited in the right order of visit into the nodes list
        graph.BFS(node -> nodes.add(node));

        //Post-check
        assertEquals(19, nodes.size());
        assertEquals(nodesTest, nodes);
        assertEquals(root, nodes.get(0));
        assertEquals(a, nodes.get(1));
        assertEquals(b, nodes.get(2));
        assertEquals(c, nodes.get(3));
        assertEquals(d, nodes.get(4));
        assertEquals(e, nodes.get(5));
        assertEquals(f, nodes.get(6));
        assertEquals(g, nodes.get(7));
        assertEquals(h, nodes.get(8));
        assertEquals(i, nodes.get(9));
        assertEquals(j, nodes.get(10));
        assertEquals(k, nodes.get(11));
        assertEquals(l, nodes.get(12));
        assertEquals(m, nodes.get(13));
        assertEquals(n, nodes.get(14));
        assertEquals(o, nodes.get(15));
        assertEquals(p, nodes.get(16));
        assertEquals(q, nodes.get(17));
        assertEquals(r, nodes.get(18));

        assertNotEquals(h, nodes.get(0));
        assertNotEquals(i, nodes.get(12));
        assertNotEquals(j, nodes.get(7));
    }

    @Test
    void asList() {
        List<GraphNode> graphNodes = graph.asList();

        assertEquals(nodesTest, graphNodes);
        assertEquals(19, graphNodes.size());
        assertEquals(root, graphNodes.get(0));
        assertEquals(a, graphNodes.get(1));
        assertEquals(b, graphNodes.get(2));
        assertEquals(c, graphNodes.get(3));
        assertEquals(d, graphNodes.get(4));
        assertEquals(e, graphNodes.get(5));
        assertEquals(f, graphNodes.get(6));
        assertEquals(g, graphNodes.get(7));
        assertEquals(h, graphNodes.get(8));
        assertEquals(i, graphNodes.get(9));
        assertEquals(j, graphNodes.get(10));
        assertEquals(k, graphNodes.get(11));
        assertEquals(l, graphNodes.get(12));
        assertEquals(m, graphNodes.get(13));
        assertEquals(n, graphNodes.get(14));
        assertEquals(o, graphNodes.get(15));
        assertEquals(p, graphNodes.get(16));
        assertEquals(q, graphNodes.get(17));
        assertEquals(r, graphNodes.get(18));
    }

    @Test
    void size() throws Exception{
        assertEquals(19, graph.size());

        graph.getRoot().getNeighbours()[0].getNeighbours()[0].addNode(1, new GraphNode(6, new Coordinate(1,5)));

        assertEquals(20, graph.size());
    }

    @Test
    void contains() {
        //Graph contains graph node
        assertTrue(graph.contains(root));
        assertTrue(graph.contains(a));
        assertTrue(graph.contains(new GraphNode(6, new Coordinate(1,-1))));

        //Graph does not contain graph node
        assertFalse(graph.contains(new GraphNode(6, new Coordinate(-5,-1))));
        assertFalse(graph.contains(new GraphNode(6, new Coordinate(-5,1))));
        assertFalse(graph.contains(new GraphNode(6, new Coordinate(-3,-4))));
    }

    @Test
    void cloneGraph() {
        Graph clonedGraph = new Graph(graph.cloneGraph(root));
        assertEquals(root, clonedGraph.getRoot());
        assertEquals(graph.size(), clonedGraph.size());

        List<GraphNode> nodes = graph.asList(), clonedNodes = clonedGraph.asList();
        for (int i = 0; i < nodes.size(); i++) {
            assertEquals(nodes.get(i), clonedNodes.get(i));
        }
    }

}