package utils;

import algorithms.Dsatur;
import algorithms.Greedy;
import algorithms.Kempe;
import algorithms.WelshPowell;
import graphs.Graph;
import gui.GUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
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


    public static int a = 0;
    public static int b = 0;


    public static ArrayList<Graph> generateRandomGraphs(int number, int density, boolean euclidian) {
        ArrayList<Graph> graphs = new ArrayList<Graph>(); // Create an ArrayList object
        Complexity.inputSize = density;

        for(int i=0; i<number; i++) {
            graphs.add(Voronoi.runVoronoi(density, euclidian, false, 500, false));
        }
        return graphs;
    }

    public static void computePerfomance(ArrayList<Graph> graphs, String algo) {
        algo = algo.toUpperCase();
        switch (algo) {
            case "DSATUR":
                for (Graph graph : graphs) {
                    Dsatur.dsatur(graph, colors);
                    updateStats(graph);
                }
                break;

            case "KEMPE":
                for (Graph graph : graphs) {
                    Kempe.kempe(graph, colors);
                    updateStats(graph);
                }
                break;

            case "GREEDY":
                for (Graph graph : graphs) {
                    Greedy.greedy(graph, colors);
                    updateStats(graph);
                }
                break;

            case "WELSH":
                for (Graph graph : graphs) {
                    WelshPowell.welshPowell(graph, colors);
                    updateStats(graph);
                }
                break;

            default:
                System.out.println("[LOG]: Please select a valid algorithm: [Welsh, Dsatur, Greedy, Kempe");
                break;
        }
        infoAvg(algo);   
    }

    private static void updateStats(Graph graph) {
        numberOfColors = graph.numberOfColors();
        timeComplexityAvg.add(timeCommplexity);
        runTimeAvg.add(runTime);
        numberOfColorsAvg.add(numberOfColors);
    }



    public static void runComplexity(int iterations, int VoronoiDensity, boolean eucliedian) {
        
        GUI gui = new GUI(500, 500);
        /**
         * For each Voronoi iteration run each algorithm and ouput perfomance.
         */

         for(int i=0; i<iterations; i++) {
            Complexity.g = Voronoi.runVoronoi(VoronoiDensity, eucliedian, false, 500, false);
            Complexity.inputSize = VoronoiDensity;
            gui.setGraphViewPage(g);
      
            Dsatur.dsatur(g, colors); 
            numberOfColors = g.numberOfColors();
            Complexity.info("Dsatur");
         

            Greedy.greedy(g, colors);
            numberOfColors = g.numberOfColors();
            Complexity.info("Greedy");

            WelshPowell.welshPowell(g, colors);
            numberOfColors = g.numberOfColors();
            Complexity.info("WelshPowell");

            Kempe.kempe(g, colors);
            numberOfColors = g.numberOfColors();
            Complexity.info("Kempe");

         }
    }

    public static void info(String title) {
        System.out.println("\n--------------------------------------------------------");
        System.out.println("Complexity Analysis of ["+title+"]\n");
        System.out.println("Size of input (n° of vertices): "+Complexity.inputSize+" vertices\n");
        System.out.println("Time Complexity: "+Complexity.timeCommplexity+" operations\n");
        System.out.println("Runtime: "+Complexity.runTime+" nanoseconds \n");
        System.out.println("Number of Colors: "+Complexity.numberOfColors);
    }

    public static void infoAvg(String title) {
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
    
        System.out.println("\n--------------------------------------------------------");
        System.out.println("Complexity Analysis of ["+title+"]\n");
        System.out.println("Size of input (n° of vertices): "+Complexity.inputSize+" vertices\n");
        System.out.println("Time Complexity: "+(sumTime/timeComplexityAvg.size())+" operations\n");
        System.out.println("Runtime: "+(sumRunTime/runTimeAvg.size())+" nanoseconds \n");
        System.out.println("Number of Colors: "+(numberOfColors/numberOfColorsAvg.size()));
    }

    public static void main(String[] args) {
        // Complexity.runComplexity(10, 10, false);
        ArrayList<Graph> graphs = generateRandomGraphs(10, 50, false);
        Complexity.computePerfomance(graphs, "dsatur");
        try { Complexity.computePerfomance(graphs, "kempe");} catch(Exception e) {System.out.println("\n[LOG]: Kempe Failed.");}
        Complexity.computePerfomance(graphs, "greedy");
        Complexity.computePerfomance(graphs, "welsh");
    }
}
