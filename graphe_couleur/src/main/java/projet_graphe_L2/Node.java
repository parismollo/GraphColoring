package projet_graphe_L2;

import java.util.ArrayList;

public class Node {
    private String label;
    private ArrayList<Node> nodes;

    public Node(String label) {
        this.label = label;
        this.nodes = new ArrayList<Node>();
    }

    public void addNode(Node node) {
        if(!nodes.contains(node))
            nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public String infos() {
        String str = "";
        for(Node n : nodes)
            str += label+" -> "+n.label+"\n";
        return str;
    }

    @Override
    public String toString() {
        return label;
    }

}
