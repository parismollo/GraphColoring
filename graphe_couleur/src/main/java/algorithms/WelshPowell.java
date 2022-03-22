package algorithms;

import java.awt.Color;
import java.util.ArrayList;

import graphs.Graph;
import graphs.Vertex;
import utils.Complexity;

public class WelshPowell {
    static int operations = 0;

    
    public static ArrayList<Vertex> selectionSort(ArrayList<Vertex> list) {
        for(int i = 0 ; i < list.size() -1 ; i ++) {
            int index = i;
            for(int j = i + 1 ; j < list.size() ; j++) {
                if(list.get(j).getVertices().size() > list.get(index).getVertices().size() ) {
                    index = j;
                    WelshPowell.operations++;
                }
            }
            Vertex tmp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, tmp);
        }
        return list;
    }

    public static void welshPowell(Graph graph, Color[] colors){
        long start = System.currentTimeMillis();

        ArrayList<Vertex> list = graph.getVertices();
        ArrayList<Vertex> temp = new ArrayList<Vertex>();
        temp = selectionSort(list);
        //Color tab [] = {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW, Color.PINK};
        int iterator = 0;
        while(iterator != colors.length) {
            Color current = colors[iterator];
            for(int i = 0 ; i < temp.size();i++){
                if(temp.get(i).getColor().equals(Color.WHITE) && containsColor(temp.get(i).getVertices(), current) == false){
                    temp.get(i).setColor(current);
                    WelshPowell.operations++;
                }
            }
            iterator++;
        }
        graph.setVerticesList(temp);
        long end = System.currentTimeMillis();
        int elapsedTime = (int) (end - start) / 1000;

        Complexity.runTime = elapsedTime;
        Complexity.timeCommplexity = Dsatur.operations;
    }

    public static boolean containsColor(ArrayList<Vertex> vertices , Color c){
        for(int i = 0 ; i < vertices.size();i++){
            if(vertices.get(i).getColor().equals(c)){
                WelshPowell.operations++;
                return true;
            }
        }
        return false;
    }
}
