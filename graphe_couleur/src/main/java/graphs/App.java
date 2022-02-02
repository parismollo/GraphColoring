package graphs;

import gui.GUI;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        System.out.println("Test Graph : ");
        Graph graph = Graph.randomGraph(10);
        System.out.println(graph);

        new GUI(1200, 800);
    }
}
