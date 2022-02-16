package algorithms;

import java.awt.Color;
import java.util.ArrayList;

import graphs.Vertex;

public class Dsatur {
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

    public static ArrayList<Vertex> dsatur(ArrayList<Vertex> list) {
        ArrayList<Vertex> temp = new ArrayList<Vertex>();
        temp = selectionSort(list);
        Color tab [] = {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW};
        for (int i = 0; i < temp.size(); i++) {
            for (int j = 0; j < tab.length; j++) {
                if(temp.get(i).getColor().equals(Color.WHITE) && containsColor(temp.get(i).getVertices(), tab[j]) == false){
                    temp.get(i).setColor(tab[j]);                    
                }
            }
            //System.out.println(temp.get(i).getTitle() + " " + temp.get(i).getColor() + " " + temp.get(i).getVertices());//Pour tester
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