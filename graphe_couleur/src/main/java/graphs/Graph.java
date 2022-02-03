package graphs;

import java.util.ArrayList;
import java.util.Random;

public class Graph {
    private String title;
    private ArrayList<Vertex> vertices;

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
        Vertex v = new Vertex();
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

    private boolean addEdge(Vertex v1, Vertex v2) {
        if(this.vertices.contains(v1) && this.vertices.contains(v2)){
            return v1.addVertex(v2) && v2.addVertex(v1);
        }
        return false;
    }
    
    private boolean removeEdge(Vertex v1, Vertex v2) {
        if(this.vertices.contains(v1) && this.vertices.contains(v2)){
            return v1.removeVertex(v2) && v2.removeVertex(v1);
        }
        return false;
    }

    public Vertex searchVertex(Vertex v) {
        for(Vertex vertex : this.vertices) {
            if(vertex.equals(v)) {
                return v;
            }
        }
        return null;
    }

    public Vertex searchVertex(int id) {
        for(Vertex vertex : this.vertices) {
            if(vertex.getId() == id) {
                return vertex;
            }
        }
        return null;
    }

    public void setTitle(String title) {
        this.title = title;
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

}
