package graphs;

import java.util.ArrayList;

public class Vertex {
    private static int counter = 0;
    private final int id;
    private ArrayList<Vertex> vertices;

    public Vertex(ArrayList<Vertex> vertices) {
        this.id = counter++;
        this.vertices = vertices;
    }

    public Vertex() {
        this(new ArrayList<Vertex>());
    }

    public int getId() {
        return this.id;
    }
    public ArrayList<Vertex> getVertices() {
        return this.vertices;
    }

    public boolean addVertex(Vertex v) {
        if(!vertices.contains(v))
            return vertices.add(v);
        return false;
    }

    public boolean removeVertex(Vertex node) {
        return vertices.remove(node);
    }

    public String printInfo() {
        String str = "Vertex ID: "+this.id+"----> {";
        for(Vertex v : vertices)
            str += v.id+" ";
        str+="}";
        return str;
    }

    @Override
    public String toString() {
        return "Vertex ID:"+id;
    }

}
