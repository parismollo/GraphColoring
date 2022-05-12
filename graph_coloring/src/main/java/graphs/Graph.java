package graphs;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import algorithms.Dsatur;
import algorithms.Greedy;
import algorithms.Kempe;
import algorithms.WelshPowell;

public class Graph implements Serializable {

    private static final long serialVersionUID = 1077578369820L;   
    
    private String title;
    private ArrayList<Vertex> vertices;

    private int id = 1;

    public Graph(String title, ArrayList<Vertex> vertices) {
        this.setTitle(title);
        this.vertices = vertices;
    }

    public Graph(String title) {
        this(title, new ArrayList<Vertex>());
    }
    
    public String getTitle() {
        return title;
    }

    public ArrayList<Vertex> getVertices() {
        return this.vertices;
    }

    public boolean addVertex() {
        Vertex v = new Vertex(id++);
        return this.vertices.add(v);
    }


    public Vertex addVertex(String title) {
        Vertex v = new Vertex(id++, title);
        this.vertices.add(v);
        return v;
         
    }

    public boolean addVertex(Vertex v) {
        return this.vertices.add(v);
    }

    @SuppressWarnings("unchecked")
    public boolean removeVertex(Vertex vertex) {
        boolean allEdgesRemoved = true;
        ArrayList<Vertex> nextVertices = (ArrayList<Vertex>) vertex.getVertices().clone();
        for (Vertex  otherVertex: nextVertices) {
            allEdgesRemoved = (allEdgesRemoved && this.removeEdge(vertex, otherVertex));
        }
        return this.vertices.remove(vertex) && allEdgesRemoved;
    }

    public boolean addEdge(Vertex v1, Vertex v2) {
        if(this.vertices.contains(v1) && this.vertices.contains(v2)){
            return v1.addVertex(v2) && v2.addVertex(v1);
        }
        return false;
    }
    
    public boolean removeEdge(Vertex v1, Vertex v2) {
        if(this.vertices.contains(v1) && this.vertices.contains(v2)){
            return v1.removeVertex(v2) && v2.removeVertex(v1);
        }
        return false;
    }

    public Vertex getVertex(Vertex v) {
        for(Vertex vertex : this.vertices) {
            if(vertex.equals(v)) {
                return v;
            }
        }
        return null;
    }

    public Vertex getVertex(int id) {
        for(Vertex vertex : this.vertices) {
            if(vertex.getId() == id) {
                return vertex;
            }
        }
        return null;
    }

    public Vertex getVertex(String title) {
        for(Vertex vertex : this.vertices) {
            if(vertex.getTitle().equals(title)) {
                return vertex;
            }
        }
        return null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVerticesList(ArrayList<Vertex> vertices){
        this.vertices = vertices;
    }

    public int numberOfColors() {
        ArrayList<Color> colors = new ArrayList<Color>();
        for (Vertex vertex : this.vertices) {
            if(!colors.contains(vertex.getColor())) {
                colors.add(vertex.getColor());
            }
        }
        return colors.size();
        
    }
    
    public static Graph randomGraph(int nb) {
        int c = 0;
        Graph graph = new Graph("Test");
        for(int i=1;i<=nb;i++)
            graph.addVertex();

        while (c<graph.vertices.size()) {
            graph.randomConnection();
            c++;
        }
        return graph;
    }

    public void randomConnection() {
        if(vertices.size() < 2)
            return;
        Random rd = new Random();
        int r1 = rd.nextInt(vertices.size());
        int r2;
        do {
            r2 = rd.nextInt(vertices.size());
        } while(r2 == r1);
        addEdge(vertices.get(r1), vertices.get(r2));
    }


    /**
     * [Possible Bug/Issue]: 
     * Not sure this can work. 
     * This vertex may have vertices 
     * that are not in the graph. 
    */

    // public Vertex addVertex(Vertex v) {
    //     if(!this.vertices.contains(v)){
    //         this.vertices.add(v);
    //     }
    //     return v;  
    // }

    public Vertex getVertex(int x, int y) {
        for(Vertex v : vertices) {
            if(v.getX() == x && v.getY() == y)
                return v;
        }
        return null;
    }

    public int getMaxId() {
        int max = 0;
        for(Vertex v : vertices) {
            if(max < v.getId())
                max = v.getId();
        }
        return max;
    }

    // public void print() {
    //     for(Vertex v : vertices)
    //         System.out.println(v.printId());
    // }

    @Override
    public String toString() {
        String str = "<---------------GRAPH INFO--------------->";
        str += "\nTitle: "+this.title;
        str+= "\nSize: "+this.vertices.size();
        str+="\n\n\tVertices\n";
        for(Vertex v : vertices)
            str += v.printInfo()+"\n";
        str+="<----------------------------------------->\n";
        return str;
    }

    public void save(String path) throws FileNotFoundException, IOException {
        save(this, path);
    }

    public static void save(Graph graph, String path) throws FileNotFoundException, IOException {
        File file = new File(path);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(graph);
        oos.close();
    }

    public static Graph load(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
        File file =  new File(path) ;

        // ouverture d'un flux sur un fichier
        ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(file));

        // désérialization de l'objet
        Graph graph = (Graph)ois.readObject();

        ois.close();
        
        return graph;
    }

    public void applyAlgo(String algo, Color[] colors) {
        applyAlgo(algo, this, colors);
    }

    public static void resetColors(Graph graph){
        ArrayList<Vertex> vertices = graph.getVertices();
        for(Vertex v : vertices)
            v.setColor(Color.white);
    }

    public static void applyAlgo(String algo, Graph graph, Color[] colors) {
        algo = algo.toUpperCase();
        //ArrayList<Vertex> vertices = graph.getVertices();
        switch(algo) {
            case "DSATUR":
                resetColors(graph);         //Au lieu de faire ça on pourrait par exemple donner un booleen colored aux graph qui vaut true si ils ont au moins un vertex coloré. Pour ne pas appeler cette fonction à chaque fois.
                Dsatur.dsatur(graph, colors);
                break;
            case "WELSHPOWELL":
                resetColors(graph);
                WelshPowell.welshPowell(graph, colors);
                break;
            case "GREEDY":
                resetColors(graph);
                Greedy.greedy(graph, colors);
                break;
            case "BESTGREEDY":
                resetColors(graph);
                Greedy.bestGreedy(graph, colors);
                break;
            case "KEMPE":
                resetColors(graph);
                Kempe.kempe(graph, colors);
                break;
        }
        //graph.setVerticesList(vertices);
    }

    public boolean isWellColored() {
        return isWellColored(this);
    }

    public static boolean isWellColored(Graph graph){//retourne false si le graphe n'est pas entiérement coloré ou si il est mal coloré (2 vertex voisins avec la même couleur) et true sinon
        ArrayList<Vertex> vertices = graph.getVertices();
        for(Vertex v : vertices){
            if(v.getTitle() == "None")
                continue;
            Color c = v.getColor();
            if(c.equals(Color.white)){
                return false;
            }
            for(Vertex next : v.getVertices()){//version naive donc on va comparer plusieurs fois la même chose
                if(next.getColor().equals(c))
                    return false;
            }
        }
        return true;
    }

    public void cleanVertices(Color color) {
        for(Vertex v : getVertices()) {
            v.setColor(color);
        }
    }

}
