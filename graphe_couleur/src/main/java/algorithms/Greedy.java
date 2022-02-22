package algorithms;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.Color;

import graphs.Graph;
import graphs.Vertex;

public class Greedy {
    public static boolean boolGraphColoringGreedy(int id, Color[] nb, Color[] color_vertex, Graph graph){
        if(id == graph.getVertices().size()){
            return true;
        }
        for(int c=0;c<nb.length;c++){
            if(!containsColor(graph.getVertices().get(id).getVertices(), nb[c], color_vertex)){
                color_vertex[graph.getVertices().get(id).getId()] = nb[c];                        //dans le cas où les vertex ne serait pas numérotés dans l'ordre d'id dans la list
                if(boolGraphColoringGreedy(id+1, nb, color_vertex, graph)){
                    return true;
                }
                color_vertex[graph.getVertices().get(id).getId()] = Color.white;
            }
        }
        return false;
    }
    public static boolean containsColor(ArrayList<Vertex> vertices , Color c, Color[] color_vertex){
        for(int i = 0 ; i < vertices.size();i++){
            if(color_vertex[vertices.get(i).getId()].equals(c)){
                return true;
            }
        }
        return false;
    }
    public static Graph greedy(Graph graph, Color[] colors){
        Color[] color_vertex = new Color[graph.getVertices().size()+1];
        for(int i=0;i<color_vertex.length;i++){
            color_vertex[i] = Color.white;
        }
        if(boolGraphColoringGreedy(0, colors, color_vertex, graph)){
            for(Vertex v : graph.getVertices()){
                v.setColor(color_vertex[v.getId()]);
            }
        }
        return graph;
    }
    //Mettre un graph en param uniquement et initialisé tab color dedans et return void. 
    //
    //
    //Le tableau de couleurs memo est normalement un tableau avec les couleurs que l'on veut sans importance 
    // mais il ne doit pas contenir des couleurs présentent dans nb (une solution est de mettre toutes les cases
    // en blanc au debut de la fonction) de taille au moins le nombre de sommets dans le graph et on va lui 
    //associer les couleurs des vertex du graph aux id correspondant

    //fonction pour retourner le nombres de façons de colorier le graph donné avec les couleurs données
    public static void bestGraphColoringGreedy(int id,Color[] nb, Color[] color_vertex, Graph graph, Map<Color[],Integer> best_option){
        if(id == graph.getVertices().size()){
            int a =0;
            for(Color v : nb){
                if(containsColor(graph.getVertices(), v, color_vertex)){
                    a++;
                }
            }
            for(Entry<Color[], Integer> old_best : best_option.entrySet()){
                if(old_best.getValue()>a && !containsColor(graph.getVertices(), Color.white, color_vertex)){
                    System.out.println(containsColor(graph.getVertices(), Color.white, color_vertex));
                    best_option.remove(old_best.getKey());
                    Color[] new_best = new Color[color_vertex.length];
                    new_best = color_vertex.clone();
                    best_option.put(new_best, a);
                    break;
                }
            }
            return;
        }
        for(int c=0;c<nb.length;c++){
            if(!containsColor(graph.getVertices().get(id).getVertices(), nb[c], color_vertex)){
                color_vertex[graph.getVertices().get(id).getId()] = nb[c];
                bestGraphColoringGreedy(id+1, nb, color_vertex, graph, best_option);
                color_vertex[graph.getVertices().get(id).getId()] = Color.white;
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
