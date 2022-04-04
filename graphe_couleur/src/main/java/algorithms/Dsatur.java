package algorithms;

import java.awt.Color;
import java.util.ArrayList;

import graphs.Graph;
import graphs.Vertex;
import utils.Complexity;

public class Dsatur {
    static int operations = 0;

    public static ArrayList<Vertex> selectionSort(ArrayList<Vertex> list) {
        for(int i = 0 ; i < list.size() -1 ; i ++) {
            int index = i;
            for(int j = i + 1 ; j < list.size() ; j++) {
                if(list.get(j).getVertices().size() > list.get(index).getVertices().size() ) {
                    index = j; 
                    Dsatur.operations++;
                }
            }
            Vertex tmp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, tmp);
        }
        return list;
    }

    public static void dsatur(Graph graph, Color[] colors) {
        long start = System.nanoTime();
        
        ArrayList<Vertex> list = graph.getVertices();
        ArrayList<Vertex> temp = new ArrayList<Vertex>();
        temp = selectionSort(list);
        //Color tab [] = {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW};
        for (Vertex v : temp) {
            for (int j = 0; j < colors.length; j++) {
                if(v.getColor().equals(Color.WHITE) && containsColor(v.getVertices(), colors[j]) == false){
                    v.setColor(colors[j]);
                    Dsatur.operations++;                  
                }
            }
            //System.out.println(temp.get(i).getTitle() + " " + temp.get(i).getColor() + " " + temp.get(i).getVertices());//Pour tester
        }
        graph.setVerticesList(temp);
        long end = System.nanoTime();
        int elapsedTime = (int) (end - start);

        Complexity.runTime = elapsedTime;
        Complexity.timeCommplexity = Dsatur.operations;

    }

    public static boolean containsColor(ArrayList<Vertex> vertices , Color c){
        for(Vertex v : vertices){
            if(v.getColor().equals(c)){
                Dsatur.operations++;
                return true;
            }
        }
        return false;
    }

}
