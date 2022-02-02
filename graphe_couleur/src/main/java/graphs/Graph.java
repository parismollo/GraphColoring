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

    public boolean removeVertex(Vertex vertex) {
        boolean allEdgesRemoved = true;
        for (Vertex  otherVertex: vertex.getVertices()) {
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

    public void setTitle(String title) {
        this.title = title;
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

    public static Graph randomGraph(int nb) {
        Graph graph = new Graph("Graph test");

        for(int i=1;i<=nb;i++)
            graph.addVertex();

        for(Vertex n : graph.vertices)
            graph.randomConnection();

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

    @Override
    public String toString() {
        String str = vertices.size()+" noeuds :\n";

       for(Vertex vertex : vertices)
            str += vertex.infos()+"\n";

      return str;
    }

}
