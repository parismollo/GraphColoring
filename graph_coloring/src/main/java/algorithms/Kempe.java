package algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

import graphs.Graph;
import graphs.Vertex;
import utils.Complexity;

public class Kempe {
    public static int operations = 0;

    public static void reverseKempeChain(Graph graph) {
        ArrayList<Vertex> vertices = graph.getVertices();
        if(vertices.isEmpty())
            return;
        Color c1 = vertices.get(0).getColor();
        ArrayList<Vertex> vertices2 = vertices.get(0).getVertices();
        Color c2 = Color.white;
        if(vertices2.isEmpty())
            return;
        for(Vertex v : vertices2){
            Kempe.operations++;
            if(vertices.contains(v))
                c2 = v.getColor();
        }
        if(c2.equals(Color.white))
            return;
        for(Vertex v : graph.getVertices()) {
            Kempe.operations++;
            v.setColor(v.getColor() == c1 ? c2 : c1);
        }
          
    }

    public static Vertex minVertices(ArrayList<Vertex> vertices){
        int minVertx = vertices.get(0).getVertices().size();
        Vertex min = vertices.get(0);
        for(Vertex v : vertices){
            Kempe.operations++;
            int nbVertx = v.getVertices().size();
            if( nbVertx < minVertx){
                minVertx = nbVertx;
                min = v;
            }
        }
        return min;
    }

    public static Graph kempeChain(Vertex current, int id){
        ArrayList<Vertex> vertices = current.getVertices();
        Graph kempe_graph = new Graph("Kempe chain");
        Vertex v1 = null;
        Vertex v2 = null;
        if(id == 0){
            v1 = vertices.get(0);
            for(int i=1;i<vertices.size();i++){
                Kempe.operations++;
                v2 = vertices.get(i);
                if(!v1.getVertices().contains(v2)){
                    break;
                }
            }
        } else {
            for(Vertex v : vertices){
                Kempe.operations++;
                if(!(v.getId() == 0 || v.getId() == id)){
                    v1 = v;
                }
            }
            for(Vertex v : vertices){
                Kempe.operations++;
                if(!(v.getId() == 0 || v.getId() == id || v == v1)){
                    if(!v1.getVertices().contains(v)){
                        v2 = v;
                    }
                }
            }
        }
        kempe_graph.addVertex(v1);
        Color c1 = v1.getColor();
        Color c2 = v2.getColor();
         
        kempe_graph(kempe_graph,v1,c1,c2);
        if(kempe_graph.getVertices().contains(v2))
            kempeChain(current, v2.getId());
        // kempe_graph.info();
        return kempe_graph;
    }
    
    public static void kempe_graph(Graph kempe_graph, Vertex current, Color c1, Color c2){
        ArrayList<Vertex> vertices = current.getVertices();
        for(Vertex v : vertices){
            Kempe.operations++;
            if((v.getColor().equals(c1) || v.getColor().equals(c2)) && !kempe_graph.getVertices().contains(v) ){
                kempe_graph.addVertex(v);
                kempe_graph(kempe_graph,v,c1,c2);
            }
        }
    }
    

    @SuppressWarnings("unchecked")
    public static void kempe(Graph graph, Color[] colors){
        long start = System.nanoTime();
        if(graph.getVertices().size() == 0){
            return;
        }
        ArrayList<Vertex> verticesClone = (ArrayList<Vertex>)graph.getVertices().clone();
        Stack<Integer> stack = new Stack<Integer>();
        kempeAux(verticesClone, colors, stack);
        kempeColoring(graph, colors, stack, 0);

        long end = System.nanoTime();
        int elapsedTime = (int) (end - start);

        Complexity.runTime = elapsedTime;
        Complexity.timeCommplexity = Kempe.operations;
    }
    
    public static void kempeAux(ArrayList<Vertex> verticesClone, Color[] colors, Stack<Integer> stack){
        if(verticesClone.size() == 0){
            return;
        }
        Vertex min = minVertices(verticesClone);
        stack.push(min.getId());
        verticesClone.remove(min);
        kempeAux(verticesClone, colors, stack);

    }
    //Faire en sorte qu'il puisse prendre en paramétre des graph partiellement coloriés pour imposer qu'il utilise les chaînes de Kempe.
    public static void kempeColoring(Graph graph, Color[] colors, Stack<Integer> stack, int colorPos){
        if(stack.empty())
            return;
        boolean colored = false;
        Vertex current = graph.getVertex(stack.pop());
        if(current.getColor().equals(Color.WHITE)){
            for(Color c : colors){
                Kempe.operations++;
                if(!containsColor(current.getVertices(), c)){
                    current.setColor(c);
                    colored = true;
                    break;
                }
                
            }
        } else 
            colored = true;
        if(!colored){
            reverseKempeChain(kempeChain(current, 0));
            // System.out.print("On utilise les chaines de kempe");
            for(Color c : colors){
                Kempe.operations++;
                if(!containsColor(current.getVertices(), c)){
                    current.setColor(c);
                    break;
                }
            }
        }
        kempeColoring(graph, colors, stack, colorPos);
    }

    public static boolean containsColor(ArrayList<Vertex> vertices , Color c){
        for(Vertex v : vertices){
            Kempe.operations++;
            if(v.getColor().equals(c)){
                return true;
            }
        }
        return false;
    }













    //Inutile et à supprimer je pense mais je les laisse la pour l'instant jusqu'a ce que je sois sûr qu'on peut les enlever



    //private static Graph kempeCGraph = new Graph("Kempe Chain");

    /*public static Graph kempe(Vertex exception ,Vertex v, int i , Color[] colors){

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
                current.setColor(colors[colororder%colors.length]);
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
    
    public static ArrayList<Vertex> coloringGraph(Graph g, Color[] color){
        ArrayList<Vertex> list = new ArrayList<Vertex>();
        list = coloringGraphAux(g, list);
        int colorPos = 0;
        for(Vertex v : list){
            for(Vertex tmp : v.getVertices()){
                if(tmp.getColor() == color[colorPos%color.length]){
                    colorPos++;
                }
            }
            v.setColor(color[colorPos%color.length]);
            colorPos++;
        }
        return list;
    }

    public static ArrayList<Vertex> coloringGraphAux(Graph g,ArrayList<Vertex> list){
        Graph current = g;
        for(Vertex vertex : current.getVertices()){
            if(vertex.getVertices().size() < 5){
                list.add(vertex);
                current.removeVertex(vertex);
                return coloringGraphAux(current, list);
            }
        }
        if(g.getVertices().size() > 0){ // On prend un vertex choisit arbitrairement (ici le premier)
            list.add(g.getVertices().get(0));
            current.removeVertex(g.getVertices().get(0));
            coloringGraphAux(current, list);
        }
        return list;
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
    
    //Si je me trompe pas kempeChain renvoie un graph qui est la chaine de kempe pour un vertex donné 
    //et son 1er voisin en gros. Mais c'est pas ce qu'on veut je crois vu qu'on veut partir d'un vertex avec 
    //5 voisins, tous de col differentes et en prendre 2 non voisins entre eux et créer la chaine à partir de 
    //ces 2 la. ?? 
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
            c2 = vertices.get(0).getColor();//y a tjrs un voisin avec get(0) en id ? Donc les vertex ils ont un id pour le graph et un id pour chaque voisin ?
        }
            
        for(int i = 0 ; i < vertices.size() ; i++) {
            Vertex next = vertices.get(i);
            if(next.getColor() == c2 && 
               !graph.getVertices().contains(next)) {
                kempeChainAux(graph, next, current.getColor());
               }
        }
    }
    public static Graph beginKempe(Graph graph){
        int idmin = isKempe(graph);
        if(idmin == -1)
            return graph;//on return le même graph mais en soit faudrait le colorier avec 5 couleurs ducoup c'est tt
        Vertex v = graph.getVertex(idmin);
        return kempeChain2(v, 0);
    }

    //retourne l'id du vertex avec le moins de voisins dans un graph si il a 5 voisins
    //Rm : il faudrait faire fct qui verifie qu'un graph est planaire avant d'appeler cette fct.
    public static int isKempe(Graph graph){
        int minVertx = 0;
        int  idmin = 0;
        for(Vertex v : graph.getVertices()){
            int nbVertx = v.getVertices().size();
            int idV = v.getId();
            if( nbVertx < minVertx){
                minVertx = nbVertx;
                idmin = idV;
            }
        }
        if(minVertx < 5)
            return -1;
        return idmin;
    }
    */
    
  }
