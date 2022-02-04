package algorithms;

import java.awt.Color;
import java.util.ArrayList;
import graphs.Vertex;

public class WelshPowell {
    
    public ArrayList<Vertex> selectionSort(ArrayList<Vertex> list) {
        for(int i = 0 ; i < list.size() -1 ; i ++) {
            int index = i;
            for(int j = i + 1 ; j < list.size() ; j++) {
                if(list.get(j).getVertices().size() < list.get(index).getVertices().size() ) {
                    index = j; 
                }

            }
            Vertex tmp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, tmp);
        }
        return list;
    }

    public ArrayList<Vertex> welshPowell(ArrayList<Vertex> list){
        ArrayList<Vertex> temp;
        temp = selectionSort(list);
        for(int i = 0 ; i < list.size() ; i++){
            if(i % 7 == 0){
                list.get(i).setColor(Color.BLUE);
            }
            else if(i % 7 == 2 || i % 7 == 6){
                list.get(i).setColor(Color.RED);
            }
            else if(i % 7 == 3 || i % 7 == 5){
                list.get(i).setColor(Color.GREEN);
            }
            else if(i % 7 == 4){
                list.get(i).setColor(Color.YELLOW);
            }
            else{
                System.out.println("Erreur dans le code de changement de couleur");
            }
        }
        return temp;
    }
}
