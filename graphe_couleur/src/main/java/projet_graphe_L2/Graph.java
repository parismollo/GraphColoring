package projet_graphe_L2;

import java.util.ArrayList;
import java.util.Random;

public class Graph {
    private ArrayList<Node> nodes;

    public Graph() {
        this.nodes = new ArrayList<Node>();
    }

    public static Graph randomGraph(int nb) {
        Graph graph = new Graph();

        for(int i=1;i<=nb;i++)
            graph.addNode(i+"");

        for(Node n : graph.nodes)
            graph.randomConnection();

        return graph;
    }

    public void randomConnection() {
        if(nodes.size() < 2)
            return;
        Random rd = new Random();
        int r1 = rd.nextInt(nodes.size());
        int r2;
        do {
            r2 = rd.nextInt(nodes.size());
        } while(r2 == r1);

        addEdge(nodes.get(r1), nodes.get(r2));
    }

    public Node addNode(String lbl) {
        Node node = new Node(lbl);
        
        nodes.add(node);
        
        return node;
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public void addEdge(Node n1, Node n2) {
        if(n1 == null || n2 == null)
            return;
        n1.addNode(n2);
        n2.addNode(n1);
    }
    
    public void removeEdge(Node n1, Node n2) {
        if(n1 == null || n2 == null)
            return;
        n1.removeNode(n2);
        n2.removeNode(n1);
    }

    @Override
    public String toString() {
        String str = nodes.size()+" noeuds :\n";
 
        for(Node node : nodes)
            str += node.infos()+"\n";

        return str;
    }

}
