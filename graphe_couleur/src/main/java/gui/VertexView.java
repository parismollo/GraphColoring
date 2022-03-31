package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;
import javax.swing.JPanel;

import graphs.Vertex;

public class VertexView extends JPanel {
    
    public static int DEFAULT_SIZE = 50;

    private GraphView graphView;
    private Vertex vertex;

    private boolean devMode, lineMode;
    private Point startPos;

    // On utilise le graphView uniquement pour recuperer la
    // couleur selectionné par l'utilisateur pour colorier
    // les noeuds. Mais on peut également creer des noeuds sans graphView.
    public VertexView(GraphView graphView, Vertex vertex) {
        this(graphView, vertex, false);
    }

    public VertexView(Vertex v) {
        this(null, v, false);
    }

    public VertexView(GraphView gView, Vertex v, boolean devMode) {
        this.graphView = gView;
        this.vertex = v;
        this.devMode = devMode;

        this.setOpaque(false);
        this.setSize(DEFAULT_SIZE, DEFAULT_SIZE);
        JLabel label = new JLabel(vertex.getId()+"");
        label.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        label.setHorizontalAlignment(JLabel.CENTER);
        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                // Si on a un clic droit
                if(devMode && (e.isPopupTrigger() || graphView.isInLineMode())) {
                    graphView.setActualVertex(VertexView.this);
                    lineMode = true;
                    repaint();
                    return;
                }
                startPos = new Point(e.getX(), e.getY());
                Color c = null;
                try {
                    c = graphView.getGraphPlayView().getUserColor();
                    if(c == null)
                        throw new NullPointerException();
                } catch(NullPointerException e2) {
                    return;
                }
                vertex.setColor(c);
                revalidate();
                repaint();
            }
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if(!devMode)
                    return;
                if(lineMode) {
                    graphView.setActualVertex(null);
                    lineMode = false;
                    graphView.addEdge(VertexView.this, new Point(getX()+e.getX(), getY()+e.getY()));
                    graphView.repaint();
                }
                repaint();
            }
        });
        
        /* On peut mettre en commentaire pour pouvoir redeplacer les vertices */
        if(!devMode)
            return;
        

        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(!lineMode) {
                    Point p = new Point(getX()+e.getX()-(int)startPos.getX(), getY()+e.getY()-(int)startPos.getY());
                    vertex.setPosition(p);
                    VertexView.this.setLocation(p);
                }
                graphView.repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(vertex == null)
            return;
        int size = getWidth();
        int space = 3;
        g.setColor(Color.BLACK);
        g.fillRoundRect(0, 0, size, size, size, size);
        if(lineMode)
            g.setColor(Color.RED);
        else
            g.setColor(vertex.getColor());
        size -= 2*space;
        g.fillRoundRect(space, space, size, size, size, size);
        size += 2*space;
    }

    /* // Peut etre plus tard
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if(vertex != null)
            vertex.changeSize(width, height);
    }
    */

    public boolean isOnMe(Point pos) {
        if(pos == null)
            return false;
        else if(!(pos.getX() >= getX() && pos.getX() <= getX()+getWidth()))
            return false;
        else if(!(pos.getY() >= getY() && pos.getY() <= getY()+getHeight()))
            return false;
        return true;
    }

    public Vertex getVertex() {
        return vertex;
    }

}
