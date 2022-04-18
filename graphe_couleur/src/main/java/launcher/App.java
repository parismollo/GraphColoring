package launcher;

//import graphs.Vertex;
import gui.GUI;

public class App {
    public static void main( String[] args ){
        //testRoutine1();
        GUI gui = new GUI(1000, 660);
        //gui.setCreatorPage();
        gui.setHomeView();
        // Pour tester la coloration d'une carte :
        // Vous pouvez mettre map en argument :
        // java -cp target/appli.jar map
        if(args != null && args.length == 1) {
            if(args[0].toUpperCase().equals("MAP")) {
                gui.setMapPage("USA", false);
            }
        }

        // POUR TESTER LE MAPCHOOSER :
        // true -> developper mode,
        // false -> on dessine juste pour tester
        // gui.setMapChooser(false);
        //////////////////////////////
        
    }

    /*private static void testRoutine1() {
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
    }*/
}
