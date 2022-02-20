package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import graphs.Vertex;

public class VertexView extends JPanel {
    
    public static int DEFAULT_SIZE = 50;

    private GraphView graphView;
    private Vertex vertex;

    // On utilise le graphView uniquement pour recuperer la
    // couleur selectionné par l'utilisateur pour colorier
    // les noeuds. Mais on peut également creer des noeuds sans graphView.
    public VertexView(GraphView graphView, Vertex vertex) {
        this(vertex);
        this.graphView = graphView;
    }

    public VertexView(Vertex v) {
        this.vertex = v;

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
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
        });

        this.setOpaque(false);
        this.setSize(DEFAULT_SIZE, DEFAULT_SIZE);
        JLabel label = new JLabel(vertex.getId()+"");
        label.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        label.setHorizontalAlignment(JLabel.CENTER);
        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);
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
        g.setColor(vertex.getColor());
        size -= 2*space;
        g.fillRoundRect(space, space, size, size, size, size);
        size += 2*space;
    }

    public Vertex getVertex() {
        return vertex;
    }

}
