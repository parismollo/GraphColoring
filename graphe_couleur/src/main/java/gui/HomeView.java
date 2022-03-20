package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class HomeView extends JPanel {
    
    private GUI gui;

    private JButton graphCreator = new JButton("Graph Creator"),
                    mapChooser = new JButton("Choose Map"),
                    randomGraph = new JButton("Random Graph");

    public HomeView(GUI gui) {
        this.gui = gui;

        graphCreator.addActionListener(e -> {
            gui.setCreatorPage();
        });

        mapChooser.addActionListener(e -> {
            gui.setMapChooser(false);
        });

        randomGraph.addActionListener(e -> {
            gui.setRandomGraphView(18);
        });
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

        gui.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = 1920, h = 1010;
                int top = (getWidth() * 170) / w;
                int left = (getHeight() * 500) / h;
                pan.setBorder(new EmptyBorder(top, left, top, left));
                int iconSize = 128, textSize = 32;
                int s1 = (getHeight() * iconSize) / h;
                int s2 = (getWidth() * iconSize) / w;
                s1 = s1 < s2 ? s1 : s2;
                int text = (getWidth() * textSize) / w;
                for(CenterPan.CustomButton cB : centerPan.buttons) {
                    cB.changeIconSize(s1);
                    cB.changeTextSize(text);
                }
            }
        });
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
                buttons[i].setListener(i);

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

        private class CustomButton extends JPanel implements MouseListener {

            private IconPanel icon;
            private JLabel lbl;

            private Color background = GUI.LIGHT_COLOR1;

            public CustomButton(String path, String text) {
                this.icon = new IconPanel(path, 128);
                this.lbl = new JLabel(text);
                
                setOpaque(false);
                addMouseListener(this);

                int border = 20;
                this.setBorder(new EmptyBorder(0, border, 0, border));

                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 32f));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);

                JPanel leftPan = new JPanel();
                leftPan.setOpaque(false);
                leftPan.setLayout(new GridBagLayout());
                leftPan.add(icon);
                
                this.setLayout(new BorderLayout());
                this.add(leftPan, BorderLayout.WEST);
                this.add(lbl, BorderLayout.CENTER);
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D)g;
                
                g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                int radius = 50;
                g2d.setColor(background);
                g2d.fillRoundRect(0, 10, getWidth(), getHeight()-10, radius, radius);
            }

            public void changeIconSize(int size) {
                icon.changeSize(size);
            }

            public void changeTextSize(int size) {
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, size));
            }

            public void setListener(int type) {
                this.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        switch(type) {
                            case 0:
                               gui.setCreatorPage();
                                break;
                            case 1:
                                gui.setMapChooser(false);
                                break;
                            case 2:
                                gui.setRandomGraphView(18);
                                break;
                        }
                        background = GUI.LIGHT_COLOR1;
                        repaint();
                    }
                });
            }

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                background = GUI.LIGHT_COLOR2;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                background = GUI.LIGHT_COLOR1;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

        }

    }

}
