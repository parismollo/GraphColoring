package graphs;

import java.io.FileNotFoundException;

import gui.GUI;
import utils.Converter;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        //testRoutine1();
        GUI gui = new GUI(1200, 800);
        // Pour tester la coloration d'une carte :
        // Vous pouvez mettre map en argument :
        // java -cp target/appli.jar map
        if(args != null && args.length == 1) {
            if(args[0].toUpperCase().equals("MAP")) {
                Graph graph = null;
                try {
                    graph = Converter.mapToGraph("src/resources/USA.csv");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                gui.setMapPage(graph, "src/resources/USA.jpg");
            }
        }
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
        System.out.println(graph.getVertex(7));
        System.out.println(graph.getVertex(v));
    }
}
