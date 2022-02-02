package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import graphs.Vertex;

public class VertexView extends JPanel {
    
    public static int DEFAULT_SIZE = 70;

    private Vertex vertex;

    public VertexView(Vertex vertex) {
        this.vertex = vertex;
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
        int size = getWidth();
        int space = 3;
        g.setColor(Color.BLACK);
        g.fillRoundRect(0, 0, size, size, size, size);
        g.setColor(Color.WHITE);
        size -= 2*space;
        g.fillRoundRect(space, space, size, size, size, size);
        size += 2*space;
    }

    public Vertex getVertex() {
        return vertex;
    }

}
