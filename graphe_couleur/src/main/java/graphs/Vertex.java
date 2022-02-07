package graphs;

import java.awt.Color;
import java.util.ArrayList;

public class Vertex {
    private static int counter = 1;
    private final int id;
    private Color color = Color.WHITE;
    private ArrayList<Vertex> vertices;
    private String title; 

    public Vertex(ArrayList<Vertex> vertices) {
        this.id = counter++;
        this.vertices = vertices;
    }

    public Vertex() {
        this(new ArrayList<Vertex>());
    }

    public Vertex(int id, ArrayList<Vertex> vertices) {
        this.id = id;
        this.vertices = vertices;
    }

    public Vertex(int id) {
        this(id, new ArrayList<Vertex>());
    }

    public Vertex(int id, String title) {
        this(id, new ArrayList<Vertex>());
        this.title = title;
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

    public boolean removeVertex(Vertex v) {
        return this.vertices.remove(v);
    }

    public String printInfo() {
        String str = "Vertex Title: "+this.title+"----> {";
        for(Vertex v : vertices)
            str += v.getTitle()+" ";
        str+="}";
        return str;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
    
    public String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        return "Vertex Id:"+id + "Vertex Title: "+this.title;
    }

}
