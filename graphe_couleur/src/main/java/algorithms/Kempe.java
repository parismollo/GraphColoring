package algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphs.Graph;
import graphs.Vertex;

public class Kempe {

    //private static Graph kempeCGraph = new Graph("Kempe Chain");

    public static Graph kempe(Vertex exception ,Vertex v, int i , Color[] colors){

        Graph graph = new Graph("Kempe graph");
        kempeAux(graph, exception, exception.getVertices().get(0),colors,0);
        if(noMoreColor(exception, colors)){
            if(exception.getVertices().size() == 5){
                Graph kempechain = kempeChain(exception);  
                reverseKempeChain(kempechain);
            }
        }
        else{
            exception.setColor(lastColor(exception, colors));
        }
        return graph;
    }

    public static void kempeAux(Graph graph,Vertex evertex,Vertex current,Color[] colors,int colororder){
        if(current.getColor() != Color.WHITE){
            graph.addVertex(current);
        }
        else{
            if(ifThisColor(colors[colororder], current.getVertices())){
                kempeAux(graph, evertex, current, colors, ++colororder);
            }
            else{
                current.setColor(colors[colororder]);
                colororder++;
                graph.addVertex(current);
                for(Vertex v : current.getVertices()){
                    if(!graph.getVertices().contains(v) && !v.equals(evertex)){
                        kempeAux(graph, evertex, v, colors, colororder);
                    }
                }
            }
        }

    }

    public static boolean ifThisColor(Color c, ArrayList<Vertex> list){
        for(Vertex v : list){
            if(c == v.getColor())
                return true;
        }
        return false;
    }

    public static boolean allColored(ArrayList<Vertex> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getColor().equals(Color.WHITE))
                return false;
        }
        return true;
    }

    public static boolean noMoreColor(Vertex v , Color[] colors){
        int numOfRemainsColor = colors.length;
        for(int i = 0 ; i < v.getVertices().size() ; i++){
            for(int j = 0 ; j < colors.length ;  j++){
                if(v.getVertices().get(i).getColor().equals(colors[j])){
                    colors[j] = null;
                    numOfRemainsColor -= 1;
                }
            }
        }
        return numOfRemainsColor == 0;
    }

    public static Color lastColor(Vertex v, Color[] colors){
        List<Color> temp = Arrays.asList(colors);
        for(Vertex tmp : v.getVertices()){
            for(Color c : temp){
                if(tmp.getColor() == c ){
                    temp.remove(c);
                }
            }
        }
        return temp.get(0);
    }

    public static Graph kempeChain(Vertex current) {
        Graph graph = new Graph("Kempe chain");
        kempeChainAux(graph, current, null);
        return graph;
    }

    public static void kempeChainAux(Graph graph, Vertex current, Color c2) {
        graph.addVertex(current);
        ArrayList<Vertex> vertices = current.getVertices();
        if(c2 == null) {
            if(vertices.isEmpty())
                return;
            c2 = vertices.get(0).getColor();
        }
            
        for(int i = 0 ; i < vertices.size() ; i++) {
            Vertex next = vertices.get(i);
            if(next.getColor() == c2 && 
               !graph.getVertices().contains(next)) {
                kempeChainAux(graph, next, current.getColor());
               }
        }
    }

    public static void reverseKempeChain(Graph graph) {
        ArrayList<Vertex> vertices = graph.getVertices();
        if(vertices.isEmpty())
            return;
        Color c1 = vertices.get(0).getColor();
        vertices = vertices.get(0).getVertices();
        if(vertices.isEmpty())
            return;
        Color c2 = vertices.get(0).getColor();

        for(Vertex v : graph.getVertices())
            v.setColor(v.getColor() == c1 ? c2 : c1);

    }
    
}
