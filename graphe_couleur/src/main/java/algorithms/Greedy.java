package algorithms;

import java.util.ArrayList;
import java.awt.Color;

import graphs.Vertex;

public class Greedy {
    public static boolean boolGraphColoringGreedy(int id, Color[] nb, Color[] color, ArrayList<Vertex> list){
        if(id == list.size()){
            return true;
        }
        for(int c=0;c<nb.length;c++){
            if(!containsColor(list.get(id).getVertices(), nb[c], color)){
                color[list.get(id).getId()] = nb[c];                        //dans le cas où les vertex ne serait pas numérotés dans l'ordre d'id dans la list
                if(boolGraphColoringGreedy(id+1, nb, color, list)){
                    return true;
                }
                color[list.get(id).getId()] = Color.white;
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
        if(boolGraphColoringGreedy(0, colors, memo, list)){
            for(Vertex v : list){
                v.setColor(memo[v.getId()]);
            }
        }
        return list;
    }
    //Mettre un graph en param uniquement et initialisé tab color dedans et return void. 
    //
    //
    //Le tableau de couleurs memo est normalement un tableau avec les couleurs que l'on veut sans importance 
    // mais il ne doit pas contenir des couleurs présentent dans nb (une solution est de mettre toutes les cases
    // en blanc au debut de la fonction) de taille au moins le nombre de sommets dans le graph et on va lui 
    //associer les couleurs des vertex du graph aux id correspondant

    //fonction pour retourner le nombres de façons de colorier le graph donné avec les couleurs données
    public static void nbGraphColoringGreedy(int id,Color[] nb, Color[] color, ArrayList<Vertex> list){
        if(id == list.size()){
            System.out.println("color array solution");
            return;
        }
        for(int c=0;c<nb.length;c++){
            if(!containsColor(list.get(id).getVertices(), nb[c], color)){
                color[list.get(id).getId()] = nb[c];
                nbGraphColoringGreedy(id+1, nb, color, list);
                color[id] = Color.white;
            }
        }
    }



















    /*
    public static Color[] colorGraphColoringGreedy(int id,Color[] nb, Color[] color, ArrayList<Vertex> list){
        if(id == list.size()){
            return color;
        }
        for(int c=0;c<nb.length;c++){
            if(!containsColor(list.get(id).getVertices(), nb[c], color)){
                color[list.get(id).getId()] = nb[c];
                if(boolGraphColoringGreedy(id+1, nb, color, list)){
                    //color = colorGraphColoringGreedy(id+1, nb, color, list);
                    return color;
                }
                color[id] = Color.white;
            }
        }
        return color;
    }
    */
}
