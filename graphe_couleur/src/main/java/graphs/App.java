package graphs;

import gui.GUI;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        testRoutine1();
        new GUI(1200, 800);
    }

    private static void testRoutine1() {
        System.out.println("Test Routine 1");
        Graph graph = Graph.randomGraph(10);
        System.out.println(graph);
        Vertex v = graph.getVertices().get(0);
        graph.removeVertex(v);
        System.out.println(graph);
        graph.addVertex();
        System.out.println(graph);
        System.out.println(graph.searchVertex(7));
        System.out.println(graph.searchVertex(v));
    }
}
