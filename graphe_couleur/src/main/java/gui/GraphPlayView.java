package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;

import graphs.Graph;

public class GraphPlayView extends JPanel {
    
    private GUI gui;

    private ColorButton selectedColorBut;
    private ImageButton selectedDrawBut;
    private ImageButton moveBut, lineBut;
    private JButton switchBut, saveBut, openBut;
    private JButton[] algosButtons;
    private JToolBar toolBar;

    private MapView mapView;
    private GraphView graphView;

    private Graph graph;

    private String name, algo;
    private int width, height;
    private Color[] colors;

    private boolean isGraph = true;
    
    public GraphPlayView(GUI gui) {
        this.gui = gui;
        this.graphView = new GraphView(this);
        this.setLayout(new BorderLayout());
        setupToolBar(true); // devMode true
        this.add(graphView, BorderLayout.CENTER);
    }

    // ICI : isGraph = true.
    public GraphPlayView(GUI gui, Graph graph, int width, int height) {
        this.gui = gui;
        this.graphView = new GraphView(this, graph, width, height);
        this.setLayout(new BorderLayout());
        setupToolBar(false);
        this.add(graphView, BorderLayout.CENTER);
    }

    public GraphPlayView(GUI gui, String name, int width, int height, boolean isGraph) {
        this(gui, name, null, width, height, null, isGraph);
    }

    public GraphPlayView(GUI gui, String name, String algo, int width, int height, Color[] colors, boolean isGraph) {
        this.gui = gui;
        this.name = name;
        this.algo = algo;
        this.width = width;
        this.height = height;
        this.colors = colors;
        this.isGraph = isGraph;
        if(name != null) {
            mapView = new MapView(this, name, false);
            this.graph = mapView.getGraph();
            graphView = new GraphView(this, graph, width, height);
        }
        this.setLayout(new BorderLayout());
        setupToolBar(false);
        if(isGraph)
            switchToGraph();
        else
            switchToMap();
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

    private class ImageButton extends JPanel {
        
        private int borderSize = 4;

        private String path;
        private BufferedImage icon;
    
        public ImageButton(String path, int size) {
            this.setMaximumSize(new Dimension(size, size));
            setOpaque(false);
            this.path = convertPath(path);
            
            try {
                icon = ImageIO.read(new File(this.path));
            }
            catch(IOException e) {
                e.printStackTrace();
                System.out.println(this.path);
            }
            this.addMouseListener(new MouseAdapter() {
                
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    if(selectedDrawBut != null)
                        selectedDrawBut.setSelected(false);
                    selectedDrawBut = ImageButton.this;
                    setSelected(true);
                    if(ImageButton.this.path.contains("line")) {
                        graphView.setLineMode(true);
                    }
                    else {
                        graphView.setLineMode(false);
                    }
                }

            });
        }
        
        public String convertPath(String path) {
            path = path.charAt(0)+path.substring(1).toLowerCase();
            path = "src/resources/"+path+".png";
            return path;
        }
        
        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            revalidate();
            repaint();
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int space = 8;
            if(icon != null) {
                g.drawImage(icon, space, space, getWidth()-space*2, getHeight()-space*2, null);	
            }
        }

        public void setSelected(boolean selected) {
            if(selected)
                this.setBorder(BorderFactory.createLineBorder(Color.RED, borderSize));
            else 
                this.setBorder(null);
                revalidate();
                repaint();
        }

    }

    public void setupToolBar(boolean devMode) {
        //this.setBackground(GUI.BACKGROUND_COLOR);
        this.setBackground(Color.WHITE);

        this.toolBar = new JToolBar();
        toolBar.setPreferredSize(new Dimension(toolBar.getWidth(), 60));

        IconPanel left_arrow = new IconPanel("return", 40);
        toolBar.add(left_arrow);
        toolBar.addSeparator();
        left_arrow.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                gui.setLastPage();
            }
        });
        Color[] colors = {Color.WHITE, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA};
        this.selectedColorBut = new ColorButton(colors[0]);
        this.selectedColorBut.setSelected(true);
        for(int i=0;i<colors.length;i++) {
            if(i == 0)
                toolBar.add(selectedColorBut);
            else
                toolBar.add(new ColorButton(colors[i]));
            toolBar.addSeparator();
        }
        JSlider slider = getSlider(VertexView.DEFAULT_SIZE, 0, 300, 75, 75);
        toolBar.add(slider);
        slider.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent event) {
                if(graphView == null)
                    return;
                for(VertexView v : graphView.getVerticesView()) {
                    v.setSize(slider.getValue(), slider.getValue());
                    v.revalidate();
                    v.repaint();
                }
	        }
	    });

        if(name != null) {
            toolBar.addSeparator();
            switchBut = new JButton(isGraph ? "See map" : "See graph");
            switchBut.addActionListener(e -> {
                if(isGraph)
                    switchToMap();
                else
                    switchToGraph();
            });
            toolBar.add(switchBut);
        }

        if(devMode) {
            toolBar.addSeparator();
            saveBut = new JButton("Save");
            saveBut.addActionListener(e -> {
                saveGraphDialog();
            });
            openBut = new JButton("Open");
            openBut.addActionListener(e -> {
                openGraphDialog();
            });
            moveBut = new ImageButton("move", 45);
            selectedDrawBut = moveBut;
            moveBut.setSelected(true);
            lineBut = new ImageButton("line", 45);
            toolBar.add(moveBut);
            toolBar.add(lineBut);
            toolBar.addSeparator();
            toolBar.add(saveBut);
            toolBar.addSeparator();
            toolBar.add(openBut);
        }

        toolBar.addSeparator();
        toolBar.addSeparator();

        String[] butNames = {"Greedy", "Kempe", "WelshPowell", "BestGreedy", "Dsatur"};

        algosButtons = new JButton[butNames.length];
        for(int i=0;i<algosButtons.length;i++) {
            algosButtons[i] = new JButton(butNames[i]);
            algosButtons[i].addActionListener(algoListener(butNames[i]));
            toolBar.add(algosButtons[i]);
        }

        this.add(toolBar, BorderLayout.NORTH);
    }

    public ActionListener algoListener(final String algo) {
        return e-> {
            if(graph != null) {
                graph.applyAlgo(algo, colors);
                if(mapView != null)
                    mapView.refresh(graph);
                if(graphView != null)
                    graphView.repaint();
                revalidate();
                repaint();
            }
        };
    }

    public void switchToMap() {
        if(mapView == null)
            this.mapView = new MapView(this, name, false);
        else
            this.mapView.refresh(graph);
        this.removeAll();
        this.add(toolBar, BorderLayout.NORTH);

        // On retire ça pour l'instant
        /*JPanel pan = new JPanel();
        pan.setLayout(new GridBagLayout());
        pan.add(mapView);
        pan.setOpaque(false);
        
        
        this.add(pan, BorderLayout.CENTER);*/
        this.add(mapView, BorderLayout.CENTER);
        if(isGraph) {
            isGraph = !isGraph;
            switchBut.setText(isGraph ? "See map" : "See graph");
        }
        this.revalidate();
        this.repaint();
    }

    public void switchToGraph() {
        if(graphView == null) {
            this.graphView = new GraphView(this, name, algo, width, height, GraphPlayView.this.colors);
        }
        else {
            // refresh this.graphView
        }

        this.removeAll();
        this.add(toolBar, BorderLayout.NORTH);
        this.add(graphView, BorderLayout.CENTER);
        if(!isGraph) {
            isGraph = !isGraph;
            switchBut.setText(isGraph ? "See map" : "See graph");
        }
        this.revalidate();
        this.repaint();
    }

    public void saveGraphDialog() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Save your graph");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        jfc.showSaveDialog(null);
		if (jfc.getSelectedFile() != null) {
			File file_path = jfc.getSelectedFile();
            try {
                graphView.getGraph().save(file_path.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
    }

    public void openGraphDialog() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Open your graph");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        jfc.showOpenDialog(null);
		if(jfc.getSelectedFile() != null) {
			File file_path = jfc.getSelectedFile();
            try {
                Graph graph = Graph.load(file_path.getAbsolutePath());
                this.graphView = new GraphView(this, graph);
                this.removeAll();
                this.add(toolBar, BorderLayout.NORTH);
                this.add(graphView, BorderLayout.CENTER);
                this.revalidate();
                this.repaint();
            }catch(Exception e) {
                e.printStackTrace();
            }
		}
    }

	public static JSlider getSlider(int value, int min, int max, int minorTick, int majorTick)
	{
		JSlider slider = new JSlider();
	    slider.setMaximum(max);
	    slider.setMinimum(min);
	    slider.setValue(value);
	    slider.setPaintTicks(true);
	    slider.setPaintLabels(true);
	    slider.setMinorTickSpacing(minorTick);
	    slider.setMajorTickSpacing(majorTick); 
	    
	    slider.setMaximumSize(new Dimension(200, 60));

	    return slider;
	}

    public String getAlgo() {
        return algo;
    }

    public Color[] getColors() {
        return colors;
    }

    public Color getUserColor() {
        return selectedColorBut == null ? null : selectedColorBut.getColor();
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public MapView getMapView() {
        return mapView;
    }

}
