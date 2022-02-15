package algorithms;

import java.awt.Color;
import java.util.ArrayList;

import graphs.Vertex;

public class Kempe {

    private ArrayList<Color> remainsColor;

    private void initColor(){
        remainsColor = new ArrayList<Color>();
        remainsColor.add(Color.BLUE);
        remainsColor.add(Color.RED);
        remainsColor.add(Color.GREEN);
        remainsColor.add(Color.YELLOW);
        remainsColor.add(Color.PINK);
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

    /*public ArrayList<Vertex> kempeChains(Vertex v1, Vertex v2){ 
        boolean swapColor = false;
        ArrayList<Vertex> res = new ArrayList<Vertex>();
        res.add(v1);
        Vertex current = v1;
        while(current != null){
            for(int i = 0 ; i < current.getVertices().size() ; i++){
                if(swapColor == false){
                    if(current.getVertices().get(i).getColor().equals(v2.getColor())){
                        res.add(current.getVertices().get(i));
                        current = current.getVertices().get(i);
                        swapColor = true;
                    }
                    else{
                        current = null;
                    }
                }
                else{
                    if(current.getVertices().get(i).getColor().equals(v1.getColor())){
                        res.add(current.getVertices().get(i));
                        current = current.getVertices().get(i);
                        swapColor = false;
                    }
                    else{
                        current = null;
                    }
                }
            }
        }
        return res;
    }*/
}
