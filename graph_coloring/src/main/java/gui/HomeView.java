package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HomeView extends JPanel {
    
    private GUI gui;

    public HomeView(GUI gui) {
        this.gui = gui;

        /*
        JPanel centerPan = new JPanel();
        centerPan.setPreferredSize(new Dimension(300, 150));
        centerPan.setOpaque(false);
        centerPan.setLayout(new GridLayout(2, 1));

        centerPan.add(graphCreator);
        centerPan.add(mapChooser);
        centerPan.add(randomGraph);

        this.setLayout(new GridBagLayout());
        this.add(centerPan);*/

        setBackground(GUI.BACKGROUND_COLOR);

        final JPanel pan = new JPanel();
        pan.setOpaque(false);
        pan.setLayout(new GridLayout());
        //pan.setBorder(new EmptyBorder(100, 300, 100, 300));
        pan.setBorder(new EmptyBorder(170, 500, 170, 500));
        final CenterPan centerPan = new CenterPan();
        pan.add(centerPan);
        this.setLayout(new BorderLayout());
        this.add(pan, BorderLayout.CENTER);

        resize(pan, centerPan);
        gui.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	resize(pan, centerPan);
            }
        });
    }

    public void resize(JPanel pan, CenterPan centerPan) {
    	Dimension screen = gui.getSize();
        int w = 1920, h = 1010;
        int top = ((int)screen.getHeight() * 170) / h;
        int left = ((int)screen.getWidth() * 500) / w;
        pan.setBorder(new EmptyBorder(top, left, top, left));
        int iconSize = 128, textSize = 32;
        int s1 = ((int)screen.getHeight() * iconSize) / h;
        int s2 = ((int)screen.getWidth() * iconSize) / w;
        s1 = s1 < s2 ? s1 : s2;
        int text = ((int)screen.getWidth() * textSize) / w;
        for(CustomButton cB : centerPan.buttons) {
            cB.changeIconSize(s1);
            cB.changeTextSize(text);
        }
    }
    
    private class CenterPan extends JPanel {

        private CustomButton[] buttons;
        private int shadowSpace = 25;
        
        public CenterPan() {
            setOpaque(false);
            buttons = new CustomButton[] {
                new CustomButton("graph-creator", "Graph Creator"),
                new CustomButton("map", "Map Chooser"),
                new CustomButton("random-graph", "Random Graph")
            };
            
            for(int i=0;i<buttons.length;i++)
                buttons[i].setHomeListener(gui, i);

            int topBottom = 32, leftRight = 64;

            this.setBorder(new EmptyBorder(topBottom, leftRight,
                                           topBottom + shadowSpace, leftRight + shadowSpace));
            this.setLayout(new GridLayout(buttons.length, 1));
            
            for(CustomButton but : buttons)
                this.add(but);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;
            
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            
            int radius = 50;
            g2d.setColor(Color.BLACK);
            int space = 9;
            g2d.fillRoundRect(space, space, getWidth()-space, getHeight()-space, radius, radius);
            g2d.setColor(GUI.DARK_COLOR1);
            g2d.fillRoundRect(0, 0, getWidth()-shadowSpace, getHeight()-shadowSpace, radius, radius);
        }

    }

}
