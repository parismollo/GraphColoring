package utils;

import algorithms.Dsatur;
import algorithms.Greedy;
import algorithms.Kempe;
import algorithms.WelshPowell;
import graphs.Graph;
import gui.GUI;

import java.awt.Color;
public class Complexity {

        /**
     * (1) This program allows you to test the complexity (time and space) of each coloring algorithm.
     * (2) Details: size of n, time complexity (number of elementary operations), space complexity (auxiliary memory in bytes), runtime (in seconds) and algorithm perfomance (number of colors).
     * (3) Generate instance of random planar graph using  Voronoi and run complexity analysis on it. 
     */

    public static int timeCommplexity=0; 
    public static int spaceComplexity=0;
    public static int runTime=0;
    public static Graph g;
    public static int inputSize;
    public static Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA};

    public static int a = 0;
    public static int b = 0;


    public static void runComplexity(int iterations, int VoronoiDensity, boolean eucliedian) {
        GUI gui = new GUI(500, 500);
        /**
         * For each Voronoi iteration run each algorithm and ouput perfomance.
         */

         for(int i=0; i<iterations; i++) {
            Complexity.g = Voronoi.runVoronoi(VoronoiDensity, eucliedian, false, 500, true);
            Complexity.inputSize = VoronoiDensity;
            gui.setGraphViewPage(g);

            Dsatur.dsatur(g, colors);
            Complexity.info("Dsatur");
            

            Greedy.greedy(g, colors);
            Complexity.info("Greedy");

            WelshPowell.welshPowell(g, colors);
            Complexity.info("WelshPowell");

            Kempe.kempe(g, colors);
            Complexity.info("Kempe");

         }
    }

    public static void info(String title) {
        System.out.println("\n--------------------------------------------------------");
        System.out.println("Complexity Analysis of ["+title+"]\n");
        System.out.println("Size of input (n° of vertices): "+Complexity.inputSize+" vertices\n");
        System.out.println("Time Complexity: "+Complexity.timeCommplexity+" operations\n");
        System.out.println("Space Complexity: "+Complexity.spaceComplexity+" bytes\n");
        System.out.println("Runtime: "+Complexity.runTime+" nanoseconds \n");
        // System.out.println("\n--------------------------------------------------------");

    }

    public static void main(String[] args) {
        Complexity.runComplexity(10, 10, false);
    }
}
