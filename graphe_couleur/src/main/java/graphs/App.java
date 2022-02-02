package graphs;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        System.out.println("Test Graph : ");
        Graph graph = Graph.randomGraph(10);

        System.out.println(graph);

        Vertex v = graph.getVertices().get(0);
        graph.removeVertex(v);

        System.out.println(graph);

        graph.addVertex();

        System.out.println(graph);
    }
}
