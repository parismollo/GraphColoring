package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.Color;

import graphs.Graph;
import graphs.Vertex;
import utils.Complexity;

public class Greedy {
    public static int operations = 0;

    public static ArrayList<Vertex> selectionSort(ArrayList<Vertex> list) {
        for(int i = 0 ; i < list.size() -1 ; i ++) {
            Greedy.operations++;
            int index = i;
            for(int j = i + 1 ; j < list.size() ; j++) {
                Greedy.operations++;
                if(list.get(j).getId() < list.get(index).getId()) {
                    index = j;
                }
            }
            Vertex tmp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, tmp);
        }
        return list;
    }

    public static boolean boolGraphColoringGreedy(int id, Color[] nb, Color[] color_vertex, ArrayList<Vertex> list ){
        if(id == list.size()){
            return true;
        }
        Vertex v = list.get(id);
        for(int c=0;c<nb.length;c++){
            Greedy.operations++;
            if(!containsColor(v.getVertices(), nb[c], color_vertex)){
                color_vertex[v.getId()] = nb[c];//dans le cas où les vertex ne serait pas numérotés dans l'ordre d'id dans la list
                if(boolGraphColoringGreedy(id+1, nb, color_vertex, list)){
                    return true;
                }
                color_vertex[v.getId()] = Color.white;            
            }
        }
        return false;
    }
    public static boolean containsColor(ArrayList<Vertex> vertices , Color c, Color[] color_vertex){
        for(Vertex v : vertices){
            Greedy.operations++;
            if(color_vertex[v.getId()].equals(c)){
                return true;
            }
        }
        return false;
    }

    public static void greedy(Graph graph, Color[] colors){
        long start = System.nanoTime();

        graph.setVerticesList(selectionSort(graph.getVertices()));
        ArrayList<Vertex> list = graph.getVertices();
        Color[] color_vertex = new Color[list.size()+1];
        for(Vertex v : list){
            Greedy.operations++;
            color_vertex[v.getId()] = v.getColor();
        }
        color_vertex[list.size()] = Color.white;
        /*Dans le cas où le graph donné est partiellement coloré on ne le reset plus a blanc au début de greedy 
        //Rm : il sera neamoins reset a blanc si greedy echoue il me semble
        //Rm : Greedy va modifier les couleurs du graph partiellement coloré si nécessaire ça peut être un problème
        for(int i=0;i<color_vertex.length;i++){
            Greedy.operations++;
            color_vertex[i] = Color.white;
        }*/
        if(boolGraphColoringGreedy(0, colors, color_vertex, list)){
            for(Vertex v : list){
                Greedy.operations++;
                v.setColor(color_vertex[v.getId()]);
            }
        }
        long end = System.nanoTime();
        int elapsedTime = (int) (end - start);

        Complexity.timeCommplexity = Greedy.operations;
        Complexity.runTime = elapsedTime;
    }

    public static void bestGreedy(Graph graph, Color[] colors){
        ArrayList<Vertex> vertices = graph.getVertices();
		Color[] memo = new Color[vertices.size()+1];
        for(Vertex v : vertices){
            memo[v.getId()] = v.getColor();
        }
        memo[vertices.size()] = Color.white;
		/*for(int i=0;i<memo.length;i++){
			memo[i] = Color.white;
		}*/
		Map<Color[],Integer> test = new HashMap<>();
		test.put(colors,colors.length);
        bestGraphColoringGreedy(0, colors, memo, vertices, test);
		Color[] fnl = new Color[memo.length];
		fnl = test.entrySet().iterator().next().getKey();
		for(int i=1;i<=vertices.size();i++){
			graph.getVertex(i).setColor(fnl[i]);
		}
    }

    //fonction pour retourner la meilleur coloration du graph (celle avec le moins de couleur avec greedy)
    public static void bestGraphColoringGreedy(int id,Color[] nb, Color[] color_vertex, ArrayList<Vertex> list, Map<Color[],Integer> best_option){
        if(id == list.size()){
            int a =0;
            for(Color v : nb){
                if(containsColor(list, v, color_vertex)){
                    a++;
                }
            }
            for(Entry<Color[], Integer> old_best : best_option.entrySet()){
                if(old_best.getValue()>a && !containsColor(list, Color.white, color_vertex)){
                    //System.out.println(containsColor(graph.getVertices(), Color.white, color_vertex));
                    best_option.remove(old_best.getKey());
                    Color[] new_best = color_vertex.clone();
                    best_option.put(new_best, a);
                    break;
                }
            }
            return;
        }
        Vertex v = list.get(id);
        for(int c=0;c<nb.length;c++){
            if(!containsColor(v.getVertices(), nb[c], color_vertex)){
                color_vertex[v.getId()] = nb[c];
                bestGraphColoringGreedy(id+1, nb, color_vertex, list, best_option);
                color_vertex[v.getId()] = Color.white;
            }
        }
    }
}
