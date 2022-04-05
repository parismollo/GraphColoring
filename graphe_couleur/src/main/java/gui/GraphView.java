package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import graphs.Graph;
import graphs.Vertex;
import utils.Converter;

public class GraphView extends JPanel {
    
    private Graph graph;

    private GraphPlayView graphPlayView;
    private ArrayList<VertexView> verticesView = new ArrayList<VertexView>();;

    private static int nextId;
    private boolean devMode, lineMode;
    private VertexView actualVertex;

    public GraphView(GraphPlayView graphPlayView) {
        this.graphPlayView = graphPlayView;
        setup(null, null, -1, -1, null, true);
    }

    public GraphView(GraphPlayView graphPlayView, Graph graph) {
        this.graphPlayView = graphPlayView;
        setup(graph, null, -1, -1, null, true);
    }

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
        setup(loadGraph(name), algo, width, height, colors, false);
        this.graphPlayView = graphPlayView;
    }

    // On peut faire un GraphView sans forcement utiliser
    // un graphPlayView
    public GraphView(Graph graph, int width, int height) {
        setup(graph, null, width, height, null, false);
    }

    private void setup(Graph graph, String algo, int width, int height, Color[] colors, boolean devMode) {
        this.graph = graph == null ? new Graph("No title") : graph;
        this.devMode = devMode;

        this.setLayout(null);
        this.setBackground(Color.WHITE);

        if(devMode) {
            nextId = this.graph.getMaxId()+1;
            // If devMode true
            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if(e.isPopupTrigger())
                        return;
                    Vertex v = new Vertex(nextId++);
                    GraphView.this.graph.addVertex(v);

                    v.setPosition(e.getX()-VertexView.DEFAULT_SIZE/2, e.getY()-VertexView.DEFAULT_SIZE/2);
                    VertexView vView = new VertexView(GraphView.this, v, true);
                    vView.setLocation(v.getPosition());
                    verticesView.add(vView);
                    GraphView.this.add(vView);
                    revalidate();
                    repaint();
                }
            });
        }

        if(width != -1 && height != -1)
            this.setPreferredSize(new Dimension(width, height));

        if(algo != null)
            this.graph.applyAlgo(algo, colors);

        List<Vertex> vertices = this.graph.getVertices();

        if(hasNoPositions(vertices))
            randomPosition(vertices);
        else
            setupPosition(vertices);
    }

    private void setupPosition(List<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            if(vertex.getPosition()!=null) {
                VertexView vertexView = new VertexView(this, vertex, devMode);
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

    public VertexView getVertexView(Point pos) {
        for(VertexView v : verticesView)
            if(v.isOnMe(pos))
                return v;
        return null;
    }

    public void refreshVerticesViewLocations(MapView map) {
        int vertexSize = getVertexViewSize();
        Dimension scaled = map.getScaledMapDim(getSize());
        Dimension mapDim = map.getMapDim();
        mapDim = new Dimension((int)mapDim.getWidth()+vertexSize, (int)mapDim.getHeight()+vertexSize);
        for(VertexView vView : verticesView) {
            if(vView.getVertex() == null || vView.getVertex().getX() == -1)
                continue;
            int x = (vView.getVertex().getX() * (int)scaled.getWidth()) / (int)mapDim.getWidth();
            int y = (vView.getVertex().getY() * (int)scaled.getHeight()) / (int)mapDim.getHeight();
            vView.setLocation(x, y);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(graphPlayView != null) {
            MapView map = graphPlayView.getMapView();
            if(map != null) {
                refreshVerticesViewLocations(map);
            }
        }
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
        Point mouse = getMousePosition();
        if(actualVertex != null && mouse != null) {
            g2d.drawLine(actualVertex.getX()+actualVertex.getWidth()/2,
                         actualVertex.getY()+actualVertex.getHeight()/2,
                         (int)mouse.getX(), (int)mouse.getY());
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

    public VertexView getActualVertex() {
        return actualVertex;
    }

    public void setActualVertex(VertexView actualVertex) {
        this.actualVertex = actualVertex;
    }

    // On relie le vertex v1 a un second vertex a la position pos.
    public void addEdge(VertexView v1, Point pos) {
        VertexView v2 = getVertexView(pos);
        if(v1 == null || v2 == null)
            return;
        graph.addEdge(v1.getVertex(), v2.getVertex());
    }

    public void setLineMode(boolean lineMode) {
        this.lineMode = lineMode;
    }

    public boolean isInLineMode() {
        return lineMode;
    }

    public ArrayList<VertexView> getVerticesView() {
        return verticesView;
    }

    public int getVertexViewSize() {
        try {
            return (int)verticesView.get(0).getSize().getWidth();
        } catch(Exception e) {
            return 0;
        }
    }

}
