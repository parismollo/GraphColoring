package algorithms;

import java.awt.Color;
import java.util.ArrayList;

import graphs.Graph;
import graphs.Vertex;
import utils.Complexity;

public class WelshPowell {
    public static int operations = 0;

    
    public static ArrayList<Vertex> selectionSort(ArrayList<Vertex> list) {
        for(int i = 0 ; i < list.size() -1 ; i ++) {
            WelshPowell.operations++;
            int index = i;
            for(int j = i + 1 ; j < list.size() ; j++) {
                WelshPowell.operations++;
                if(list.get(j).getVertices().size() > list.get(index).getVertices().size() ) {
                    index = j;
                }
            }
            Vertex tmp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, tmp);
        }
        return list;
    }

    public static void welshPowell(Graph graph, Color[] colors){
        long start = System.nanoTime();

        ArrayList<Vertex> list = graph.getVertices();
        ArrayList<Vertex> temp = new ArrayList<Vertex>();
        temp = selectionSort(list);
        //Color tab [] = {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW, Color.PINK};
        int iterator = 0;
        while(iterator != colors.length) {
            WelshPowell.operations++;
            Color current = colors[iterator];
            for(Vertex v : temp){
                WelshPowell.operations++;
                if(v.getColor().equals(Color.WHITE) && containsColor(v.getVertices(), current) == false){
                    v.setColor(current);
                }
            }
            iterator++;
        }
        graph.setVerticesList(temp);
        long end = System.nanoTime();
        int elapsedTime = (int) (end - start);

        Complexity.runTime = elapsedTime;
        Complexity.timeCommplexity = WelshPowell.operations;
    }

    public static boolean containsColor(ArrayList<Vertex> vertices , Color c){
        for(Vertex v : vertices){
            WelshPowell.operations++;
            if(v.getColor().equals(c)){
                return true;
            }
        }
        return false;
    }
}
