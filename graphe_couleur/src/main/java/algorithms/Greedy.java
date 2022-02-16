package algorithms;

import java.util.ArrayList;
import java.awt.Color;

import graphs.Vertex;

public class Greedy {
    public static boolean graphColoring(int id,Color[] nb, Color[] color, ArrayList<Vertex> list){
        if(id == list.size()){
            return true;
        }
        for(int c=0;c<nb.length;c++){
            if(!containsColor(list.get(id).getVertices(), nb[c], color)){
                color[list.get(id).getId()] = nb[c];
                if(graphColoring(id+1, nb, color, list)){
                    return true;
                }
                color[id] = Color.white;
            }
        }
        return false;
    }
    public static boolean containsColor(ArrayList<Vertex> vertices , Color c, Color[] tabCol){
        for(int i = 0 ; i < vertices.size();i++){
            if(tabCol[vertices.get(i).getId()].equals(c)){
                return true;
            }
        }
        return false;
    }
    public static ArrayList<Vertex> greedy(ArrayList<Vertex> list, Color[] colors, Color[] memo){
        if(graphColoring(0, colors, memo, list)){
            for(Vertex v : list){
                v.setColor(memo[v.getId()]);
            }
        }
        return list;
    }
}// Le tableau de couleurs memo est normalement un tableau avec les couleurs que l'on veut sans importance 
// de taille au moins le nombre de sommets dans le graph et on va lui associer les couleurs des vertex 
//du graph aux id correspondant
