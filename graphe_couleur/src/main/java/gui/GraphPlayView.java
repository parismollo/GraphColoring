package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import graphs.Graph;

public class GraphPlayView extends JPanel {
    
    private ColorButton selectedColorBut;
    private JToolBar toolBar;

    public GraphPlayView(Graph graph, int width, int height) {
        this.setLayout(new BorderLayout());
        setupToolBar();
        this.add(new GraphView(this, graph, width, height), BorderLayout.CENTER);
    }

    public GraphPlayView(String name, int width, int height) {
        this.setLayout(new BorderLayout());
        setupToolBar();
        this.add(new GraphView(this, name, width, height), BorderLayout.CENTER);
    }

    public GraphPlayView(String name, String algo, int width, int height) {
        this.setLayout(new BorderLayout());
        setupToolBar();
        this.add(new GraphView(this, name, algo, width, height), BorderLayout.CENTER);
    }

    private class ColorButton extends JPanel {

        private int borderSize = 4;

        private Color color,
                      lightColor,
                      darkColor;

        public ColorButton(Color c) {
            this.color = c;
            this.lightColor = getLighterColor(color, 0.55f);
            this.darkColor = lightColor;
            this.setMaximumSize(new Dimension(50, 30));
            this.setBackground(color);
            setSelected(false);
            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    setBackground(darkColor);
                }

                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    setBackground(color);
                    if(selectedColorBut != null)
                        selectedColorBut.setSelected(false);;
                    setSelected(true);
                    selectedColorBut = ColorButton.this;

                }

                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    setBackground(lightColor);
                }

                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    setBackground(color);
                }

            });
            
        }

        public void setSelected(boolean selected) {
            if(selected)
                this.setBorder(BorderFactory.createLineBorder(Color.RED, borderSize));
            else
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderSize-2));
        }

        public Color getColor() {
            return color;
        }

        public Color getLighterColor(Color color, double fraction) {

            int red = (int) Math.round(Math.min(255, color.getRed() + 255 * fraction));
            int green = (int) Math.round(Math.min(255, color.getGreen() + 255 * fraction));
            int blue = (int) Math.round(Math.min(255, color.getBlue() + 255 * fraction));
    
            int alpha = color.getAlpha();
    
            return new Color(red, green, blue, alpha);
    
        }

    }

    public void setupToolBar() {
        this.toolBar = new JToolBar();
        toolBar.setPreferredSize(new Dimension(toolBar.getWidth(), 60));
        Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};
        this.selectedColorBut = new ColorButton(colors[0]);
        this.selectedColorBut.setSelected(true);
        for(int i=0;i<colors.length;i++) {
            if(i == 0)
                toolBar.add(selectedColorBut);
            else
                toolBar.add(new ColorButton(colors[i]));
            toolBar.addSeparator();
        }
        this.add(toolBar, BorderLayout.NORTH);
    }

    public Color getUserColor() {
        return selectedColorBut == null ? null : selectedColorBut.getColor();
    }

}
