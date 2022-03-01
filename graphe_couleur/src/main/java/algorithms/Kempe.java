package algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphs.Graph;
import graphs.Vertex;

public class Kempe {

    private List<Color> remainsColor;
    //private static Graph kempeCGraph = new Graph("Kempe Chain");

    private void initColor(){
        Color tab [] = {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW, Color.PINK};
        remainsColor = Arrays.asList(tab); 
    }

    public boolean moreThanFiveVertices(Vertex v) {
        return v.getVertices().size() >= 5;
    }

    public boolean allColored(ArrayList<Vertex> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getColor().equals(Color.WHITE))
                return false;
        }
        return true;
    }

    public boolean noMoreColor(Vertex v){
        this.initColor();
        int numOfRemainsColor = remainsColor.size();
        for(int i = 0 ; i < v.getVertices().size() ; i++){
            for(int j = 0 ; j < numOfRemainsColor ;  j++){
                if(v.getVertices().get(i).getColor().equals(remainsColor.get(j))){
                    remainsColor.remove(j);
                    numOfRemainsColor -= 1;
                }
            }
        }
        return numOfRemainsColor == 0;
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
