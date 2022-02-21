package algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphs.Graph;
import graphs.Vertex;

public class Kempe {

    private List<Color> remainsColor;
    private Graph kempeCGraph = new Graph("Kempe Chain");

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

    public Graph kempeChains(Vertex current, Color c1, Color c2){
        if(!current.equals(kempeCGraph.getVertices().get(0))){
            kempeCGraph.addVertex(current);
        } 
        for(int i = 0 ; i < current.getVertices().size() ; i++){
            if(current.getColor().equals(c1) && 
            current.getVertices().get(i).getColor().equals(c2) && 
            !current.getVertices().contains(current.getVertices().get(i))){
                return kempeChains(current.getVertices().get(i), c1, c2);
            }
            else if (current.getVertices().get(i).getColor().equals(c2) && 
            !current.getVertices().contains(current.getVertices().get(i))){
                    return kempeChains(current.getVertices().get(i), c1, c2);
            }
        }
        return kempeCGraph;
    }

    public void reverseKempeChain(ArrayList<Vertex> list){
        for(int i = 0 ;i < list.size();i++){
            if(list.get(i).getColor().equals(list.get(0).getColor())){
                list.get(i).setColor(list.get(1).getColor());
            }
            else{
                list.get(i).setColor(list.get(0).getColor());
            }
        }
    }
}
