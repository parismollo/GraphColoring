package algorithms;

import java.awt.Color;
import java.util.ArrayList;

import graphs.Vertex;

public class WelshPowell {
    
    public static ArrayList<Vertex> selectionSort(ArrayList<Vertex> list) {
        for(int i = 0 ; i < list.size() -1 ; i ++) {
            int index = i;
            for(int j = i + 1 ; j < list.size() ; j++) {
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

    public static ArrayList<Vertex> welshPowell(ArrayList<Vertex>list){
        ArrayList<Vertex> temp = new ArrayList<Vertex>();
        temp = selectionSort(list);
        /*
        for(int i = 0 ; i < temp.size();i++){
            System.out.println(temp.get(i).getVertices().size());
        }
        */
        Color tab [] = {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW, Color.PINK};
        int iterator = 0;
        while(iterator != tab.length) {
            Color current = tab[iterator];
            for(int i = 0 ; i < temp.size();i++){
                if(temp.get(i).getColor().equals(Color.WHITE) && containsColor(temp.get(i).getVertices(), current) == false){
                    temp.get(i).setColor(current);
                }
            }
            iterator++;
        }
        return temp;
    }

    public static boolean containsColor(ArrayList<Vertex> vertices , Color c){
        for(int i = 0 ; i < vertices.size();i++){
            if(vertices.get(i).getColor().equals(c)){
                return true;
            }
        }
        return false;
    }
}
