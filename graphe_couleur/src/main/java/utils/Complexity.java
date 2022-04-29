package utils;

import algorithms.Dsatur;
import algorithms.Greedy;
import algorithms.Kempe;
import algorithms.WelshPowell;
import graphs.Graph;

import java.awt.Color;
import java.util.ArrayList;
public class Complexity {

        /**
     * (1) This program allows you to test the complexity (time and space) of each coloring algorithm.
     * (2) Details: size of n, time complexity (number of loops), space complexity (auxiliary memory in bytes), runtime (in seconds) and algorithm perfomance (number of colors).
     * (3) Generate instance of random planar graph using  Voronoi and run complexity analysis on it. 
     */

    public static int timeCommplexity = 0; 
    public static int runTime = 0;
    public static int numberOfColors = -1;

    public static Graph g;
    public static int inputSize;
    public static Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA};

    public static ArrayList<Integer> timeComplexityAvg = new ArrayList<Integer>();
    public static ArrayList<Integer> runTimeAvg = new ArrayList<Integer>();
    public static ArrayList<Integer> numberOfColorsAvg = new ArrayList<Integer>();


    // public static int a = 0;
    // public static int b = 0;


    public static void resetParams() {
        Complexity.timeCommplexity = 0;
        Complexity.runTime = 0;
        Complexity.numberOfColors = 0;
        Complexity.timeComplexityAvg.clear();
        Complexity.runTimeAvg.clear();
        Complexity.numberOfColorsAvg.clear();
    }


    public static ArrayList<Graph> generateRandomGraphs(int number, int density, boolean euclidian) {
        ArrayList<Graph> graphs = new ArrayList<Graph>(); // Create an ArrayList object
        // Complexity.inputSize = density;

        for(int i=0; i<number; i++) {
            graphs.add(Voronoi.runVoronoi(density, euclidian, false, 500, true));
        }
        return graphs;
    }

    public static void computePerfomance(Graph graph, String algo) {
        ArrayList<Graph> g = new ArrayList<Graph>();
        g.add(graph);
        computePerfomance(g, algo);
    }

    public static String[] computePerfomance(ArrayList<Graph> graphs, String algo) {
        algo = algo.toUpperCase();
        switch (algo) {
            case "DSATUR":
                for (Graph graph : graphs) {
                    Dsatur.dsatur(graph, colors);
                    updateStats(graph);
                    // reset the stats at dsatur
                    Dsatur.operations = 0;
                }
                break;

            case "KEMPE":
                for (Graph graph : graphs) {
                    Kempe.kempe(graph, colors);
                    updateStats(graph);
                    // reset the stats at kempe
                    Kempe.operations = 0;
                }
                break;

            case "GREEDY":
                for (Graph graph : graphs) {
                    Greedy.greedy(graph, colors);
                    updateStats(graph);
                    // reset the stats at greedy
                    Greedy.operations = 0;
                }
                break;

            case "WELSH":
                for (Graph graph : graphs) {
                    WelshPowell.welshPowell(graph, colors);
                    updateStats(graph);
                    WelshPowell.operations = 0;
                }
                break;

            default:
                System.out.println("[LOG]: Please select a valid algorithm: [Welsh, Dsatur, Greedy, Kempe");
                break;
        }
        return infoAvg(algo);   
    }

    private static void updateStats(Graph graph) {
        numberOfColors = graph.numberOfColors();
        timeComplexityAvg.add(timeCommplexity);
        runTimeAvg.add(runTime);
        numberOfColorsAvg.add(numberOfColors);
        Complexity.inputSize = graph.getVertices().size();
    }

    public static String[] infoAvg(String title) {
        int sumTime = 0;
        for(int time : timeComplexityAvg) {
            sumTime+=time;
        }

        int sumRunTime = 0;
        for(int run : runTimeAvg) {
            sumRunTime+=run;
        }

        int numberOfColors = 0;
        for(int color : numberOfColorsAvg) {
            numberOfColors+=color;
        }

        int operations = (sumTime/timeComplexityAvg.size());
        int runtime = (sumRunTime/runTimeAvg.size());
        int colors = (numberOfColors/numberOfColorsAvg.size());

        System.out.println("\n--------------------------------------------------------");
        System.out.println("Complexity Analysis of ["+title+"]\n");
        System.out.println("Size of input (n° of vertices): "+Complexity.inputSize+" vertices\n");
        System.out.println("Time Complexity: "+operations+" operations\n");
        System.out.println("Runtime: "+runtime+" nanoseconds \n");
        System.out.println("Number of Colors: "+colors);

        // Algo, input size, operations, runtime, colors


        String[] res = {title, Integer.toString(Complexity.inputSize), Integer.toString(operations), Integer.toString(runtime), Integer.toString(colors)};
        return res;
    }

    public static void main(String[] args) {
        coloringSimulation(10, 20, false);
    }

    private static void coloringSimulation(int iterations, int density, boolean euclidian) {
        ArrayList<Graph> graphs = Complexity.generateRandomGraphs(iterations, density, euclidian);
        Complexity.computePerfomance(graphs, "dsatur");
        try { Complexity.computePerfomance(graphs, "kempe");} catch(Exception e) {System.out.println("\n[LOG]: Kempe Failed.");}
        Complexity.computePerfomance(graphs, "greedy");
        Complexity.computePerfomance(graphs, "welsh");
    }
/*
    public static void runComplexity(int iterations, int VoronoiDensity, boolean eucliedian) {
        
        GUI gui = new GUI(500, 500);
        
        //  For each Voronoi iteration run each algorithm and ouput perfomance.
         

         for(int i=0; i<iterations; i++) {
            Complexity.g = Voronoi.runVoronoi(VoronoiDensity, eucliedian, false, 500, false);
            Complexity.inputSize = VoronoiDensity;
            gui.setGraphViewPage(g);
      
            Dsatur.dsatur(g, colors); 
            numberOfColors = g.numberOfColors();
            Complexity.info("Dsatur", g.getTitle());
         

            Greedy.greedy(g, colors);
            numberOfColors = g.numberOfColors();
            Complexity.info("Greedy", g.getTitle());

            WelshPowell.welshPowell(g, colors);
            numberOfColors = g.numberOfColors();
            Complexity.info("WelshPowell", g.getTitle());

            Kempe.kempe(g, colors);
            numberOfColors = g.numberOfColors();
            Complexity.info("Kempe", g.getTitle());

         }
    }

    public static void info(String title, String graphTitle) {
        System.out.println("\n--------------------------------------------------------");
        System.out.println("Complexity Analysis of ["+title+"] for "+graphTitle+"\n");
        System.out.println("Size of input (n° of vertices): "+Complexity.inputSize+" vertices\n");
        System.out.println("Time Complexity: "+Complexity.timeCommplexity+" operations\n");
        System.out.println("Runtime: "+Complexity.runTime+" nanoseconds \n");
        System.out.println("Number of Colors: "+Complexity.numberOfColors);
    }
*/
}
