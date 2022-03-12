package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import algorithms.Dsatur;
import algorithms.Greedy;
import algorithms.WelshPowell;
import graphs.Graph;
import graphs.Vertex;
import utils.Converter;

public class GraphView extends JPanel {
    
    private Graph graph;

    private GraphPlayView graphPlayView;
    private List<VertexView> verticesView;

    public GraphView(GraphPlayView graphPlayView, Graph graph, int width, int height) {
        this(graph, width, height);
        this.graphPlayView = graphPlayView;
    }

    public GraphView(String name, int width, int height){
        this(loadGraph(name), width, height);
    }

    public GraphView(GraphPlayView graphPlayView, String name, int width, int height){
        this(name, width, height);
        this.graphPlayView = graphPlayView;
    }

    public GraphView(GraphPlayView graphPlayView, String name, String algo, int width, int height, Color[] colors){
        setup(loadGraph(name), algo, width, height, colors);
        this.graphPlayView = graphPlayView;
    }

    // On peut faire un GraphView sans forcement utiliser
    // un graphPlayView
    public GraphView(Graph graph, int width, int height) {
        setup(graph, null, width, height, null);
    }

    private void setup(Graph graph, String algo, int width, int height, Color[] colors) {
        this.graph = graph;
        this.verticesView = new ArrayList<VertexView>();
        
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(width, height));

        if(algo != null)
            graph.applyAlgo(algo, colors);

        List<Vertex> vertices = graph.getVertices();

        if(hasNoPositions(vertices))
            randomPosition(vertices);
        else
            setupPosition(vertices);
    }

    private void setupPosition(List<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            if(vertex.getPosition()!=null) {
                VertexView vertexView = new VertexView(this, vertex);
                verticesView.add(vertexView);
                this.add(vertexView);
                vertexView.setLocation(vertex.getPosition());
            }
        }
    }

    private boolean hasNoPositions(List<Vertex> vertices) {
        for(Vertex v : vertices) {
            if(v.getPosition() != null)
                return false;
        }
        return true;
    }

    private void randomPosition(List<Vertex> vertices) {
        int maxLine = 7, maxColumn = 8;
        int coeffW = (int)getPreferredSize().getWidth() / maxColumn;
        int coeffH = (int)getPreferredSize().getHeight() / maxLine;
        int x = 0, y = 0;

        for(int i=0;i<vertices.size();i++) {
            VertexView vertex = new VertexView(this, vertices.get(i));
            verticesView.add(vertex);
            this.add(vertex);
            vertex.setLocation(x, y);
            // vertex.setSize(width, height); // Peut etre un jour
            if((i+1) % maxColumn == 0 && i != 0) {
                x = 0;
                y += coeffH;
            }
            else
                x += coeffW;
        }
    }

    public VertexView getVertexView(Vertex vertex) {
        for(VertexView v : verticesView)
            if(v.getVertex() == vertex)
                return v;
        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // On change la taille des arretes.
        Graphics2D g2d = (Graphics2D)g;

        // INCROYABLE !!
        // Effet d'antialiasing. Le graphique est beaucoup plus lisse qu'avant.
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        ////////////////

        g2d.setStroke(new BasicStroke(5));

        int center = -1;
        VertexView v2;
        for(VertexView vertexView : verticesView) {
            // Le centre est le meme pour v1 et v2
            // Car tous les noeuds ont la meme taille.
            if(center == -1) // On le calcul que une seule fois.
                center = vertexView.getWidth()/2;
            for(Vertex vertex : vertexView.getVertex().getVertices()) {
                v2 = getVertexView(vertex);
                if(v2 == null)
                    continue;
                g2d.drawLine(vertexView.getX()+center, vertexView.getY()+center,
                           v2.getX()+center, v2.getY()+center);
            }
        }

        /*
        // Pour tracer des courbes
        Path2D path = new Path2D.Double();
        path.moveTo(200, 200);
        path.curveTo(0, 0, 100, 100, 200, 0);

        g2d.draw(path); // Courbe vide
        // g2d.fill(path); // Courbe remplie
        */
    }   

    public Graph getGraph() {
        return graph;
    }

    public GraphPlayView getGraphPlayView() {
        return graphPlayView;
    }

    private static Graph loadGraph(String name) {
        try {
            return Converter.mapToGraph(MapView.RESOURCES_FOLDER+name+".csv");
        } catch (FileNotFoundException e) {
            return new Graph("Empty Graph");
        }
    }

    /*
    public int getWidthCoeff(int size, int maxColumn) {
        return (getWidth() - maxColumn*size) / (maxColumn-1);
    }

    public int getHeightCoeff(int size, int maxLine) {
        return (getWidth() - maxLine*size) / (maxLine-1);
    }
    */



}
