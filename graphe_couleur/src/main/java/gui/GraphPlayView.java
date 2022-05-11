package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;

import graphs.Graph;
import utils.ComplexityInterface;

public class GraphPlayView extends JPanel {

	private static final long serialVersionUID = 1L;

	private GUI gui;
    protected String title;

    private ColorButton selectedColorBut;
    private ImageButton selectedDrawBut;
    private ImageButton moveBut, lineBut;
    
    private JToolBar toolBar;

    private MapView mapView;
    private GraphView graphView;

    private Graph graph;

    private String name, algo;
    private int width, height;
    private Color[] colors;

    private JPanel westPanel;
    protected TopPanel topPanel;
    private boolean creatorMode;

    private boolean isGraph = true;
    
    public GraphPlayView(GUI gui, Color[] colors) {
        this.creatorMode = true;
        this.gui = gui;
        this.colors = colors;
        this.graphView = new GraphView(this);
        this.graph = graphView.getGraph();
        this.topPanel = new TopPanel();
        this.setLayout(new BorderLayout());
        //setupToolBar(true); // devMode true

        this.add(getTopContainer(), BorderLayout.NORTH);
        setupWestPanel();
        refreshWestPanel();
        this.add(westPanel, BorderLayout.WEST);
        this.add(graphView, BorderLayout.CENTER);
        topPanel.refreshTitle();

        this.setBackground(GUI.BACKGROUND_COLOR);
    }

    public GraphPlayView(GUI gui, Graph graph, Color[] colors, int width, int height) {
        this.gui = gui;
        this.colors = colors;
        this.graphView = new GraphView(this, graph, width, height);
        this.graph = graphView.getGraph();
        this.topPanel = new TopPanel();
        this.setLayout(new BorderLayout());
        //setupToolBar(false);

        this.add(getTopContainer(), BorderLayout.NORTH);
        setupWestPanel();
        refreshWestPanel();
        this.add(westPanel, BorderLayout.WEST);
        this.add(graphView, BorderLayout.CENTER);
        topPanel.refreshTitle();
        this.setBackground(GUI.BACKGROUND_COLOR);
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
        this.topPanel = new TopPanel();
        this.setBackground(GUI.BACKGROUND_COLOR);
        if(name != null) {
            mapView = new MapView(this, name, false);
            this.graph = mapView.getGraph();
            graphView = new GraphView(this, graph, width, height);
        }
        setupWestPanel();
        this.setLayout(new BorderLayout());
        //setupToolBar(false);
        if(isGraph)
            switchToGraph();
        else
            switchToMap();
    }

    private class ColorButton extends JPanel {

		private static final long serialVersionUID = 1L;

		//private int borderSize = 4;

        private Color color,
                      lightColor,
                      darkColor;

        private boolean selected;

        public ColorButton(Color c, int size) {
            this.color = c;
            this.lightColor = getLighterColor(color, 0.55f);
            this.darkColor = lightColor;
            this.setPreferredSize(new Dimension(size, size));
            this.setOpaque(false);
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
            this.selected = selected;
            repaint();
            /*
            if(selected)
                this.setBorder(BorderFactory.createLineBorder(Color.RED, borderSize));
            else
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderSize-2));
            */
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            // if mouseEntered change color
            int w = (int)getPreferredSize().getWidth(),
            	h = (int)getPreferredSize().getHeight();
            int space = 4;
            g.setColor(color);
            g.fillOval(space, space, w-2*space, h-2*space);
            g.setColor(selected ? Color.RED : Color.BLACK);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(2.0f));
            g.drawOval(space, space, w-2*space, h-2*space);
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

		private static final long serialVersionUID = 1L;

		private int borderSize = 4;

        private String path;
        private BufferedImage icon;
    
        public ImageButton(String path, int size) {
            this.setPreferredSize(new Dimension(size, size));
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

    public static class OptionsPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private int shadowSpace = 25;

        public OptionsPanel() {
            
        	this.setOpaque(false);
            int topBottom = 16, leftRight = 64;

            this.setBorder(new EmptyBorder(topBottom, leftRight,
                                           topBottom + shadowSpace, leftRight + shadowSpace));
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

    private class MenuPanel extends OptionsPanel {

		private static final long serialVersionUID = 1L;

		public MenuPanel() {
    		setName("MenuPanel");
    		setOpaque(false);
            CustomButton switchBut = createSwitchBut();

            CustomButton[] buttons = new CustomButton[] {
                new CustomButton("algorithm", "Algorithms"),
                new CustomButton("playing", "Playing"),
                switchBut
            };
            for(int i=0;i<2;i++) {
                buttons[i].changeIconSize(64);
                buttons[i].changeTextSize(20);
            }
            buttons[0].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    westPanel.removeAll();
                    westPanel.add(new AlgoPanel());
                    westPanel.revalidate();
                    westPanel.repaint();
                    topPanel.refreshTitle();
                }
            });
            
            buttons[1].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    westPanel.removeAll();
                    westPanel.add(new PlayingPanel());
                    westPanel.revalidate();
                    westPanel.repaint();
                    topPanel.refreshTitle();
                }
            });

            int column = switchBut == null ? buttons.length-1 : buttons.length;
            setLayout(new GridLayout(column, 1));
            for(CustomButton b : buttons) {
                if(b == null)
                    continue;
                add(b);
            }
        }

    }

    private class PlayingPanel extends OptionsPanel {

		private static final long serialVersionUID = 1L;

		public PlayingPanel() {
    		setName("PlayingPanel");

            int line = creatorMode ? 6 : 5;
            CustomButton switchBut = createSwitchBut();
            if(!creatorMode && switchBut == null)
                line--;
            setLayout(new GridLayout(line, 1));

            StrButton selectColor = new StrButton("Select Color", GUI.LIGHT_COLOR1);
            selectColor.setEnabled(false);
            JPanel tmp = getTempPanel();
            tmp.setLayout(new GridLayout());
            tmp.setBorder(new EmptyBorder(10, 0, 10, 0));
            tmp.add(selectColor);

            this.add(tmp);
    		this.add(new ColorPanel());
            if(creatorMode) {
                this.add(new SaveOpenPanel());
    		    this.add(new GraphCreatorPanel());
            }
    		
            this.add(new CheckColoringPanel());

    		JSlider slider = getSlider(VertexView.DEFAULT_SIZE, 0, 300, 75, 75);
    		slider.setOpaque(false);
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
            
            this.add(slider);
            if(!creatorMode && switchBut != null)
                this.add(createSwitchBut());
        }

    }

    private class CheckColoringPanel extends JPanel {

        private JLabel msg;

        public CheckColoringPanel() {
            this.setOpaque(false);
            StrButton reset = new StrButton("Reset", GUI.LIGHT_COLOR1);
            StrButton check = new StrButton("Check coloring", GUI.LIGHT_COLOR1);

            reset.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    graph.cleanVertices(Color.WHITE);
                    if (isGraph)
                        switchToGraph();
                    else
                        switchToMap();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    reset.setBgColor(GUI.LIGHT_COLOR2);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    reset.setBgColor(GUI.LIGHT_COLOR1);
                    repaint();
                }
            });

            check.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if(graph.isWellColored())
                        setMsg("Le graphe est bien colorié !", Color.GREEN);
                    else
                        setMsg("Erreur ! Le graphe a été mal colorié.", Color.RED);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    check.setBgColor(GUI.LIGHT_COLOR2);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    check.setBgColor(GUI.LIGHT_COLOR1);
                    repaint();
                }
            });

            this.setLayout(new GridLayout(2, 1));
            
            JPanel pan = getTempPanel();
            pan.setLayout(new GridLayout(1, 2));
            pan.add(reset);
            pan.add(check);
            this.add(pan);

            msg = new JLabel("Check your graph !");
            msg.setHorizontalAlignment(JLabel.CENTER);
            pan = getTempPanel();
            pan.setLayout(new GridBagLayout());
            pan.add(msg);
            this.add(pan);
        }

        public void setMsg(String message) {
            setMsg(message, Color.BLACK);
        }

        public void setMsg(String message, Color textColor) {
            msg.setForeground(textColor);
            this.msg.setText(message);
        }
    }

    private class ColorPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public ColorPanel() {
            this.setOpaque(false);
            this.setLayout(new GridLayout(2, 3));

            Color[] colors = {Color.WHITE, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA};
            JPanel tmp;
            for(int i=0;i<colors.length;i++) {
                tmp = new JPanel();
                tmp.setOpaque(false);
                if(i == 0) {
                    selectedColorBut = new ColorButton(colors[i], 42);
                    selectedColorBut.setSelected(true);
                    tmp.add(selectedColorBut);
                }
                else
                    tmp.add(new ColorButton(colors[i], 42));
                this.add(tmp);
                //this.add(new ColorButton(colors[i]));
            }
            /*JPanel tmp;
            for(int i=1;i<colors.length;i++) {
                tmp = new JPanel();
                tmp.setOpaque(false);
                if(i == 0)
                    tmp.add(selectedColorBut);
                else
                    tmp.add(new ColorButton(colors[i]));
                tmp.setBorder(new EmptyBorder(3, 3, 3, 3));
                this.add(tmp);
            }*/
        }
    }

    private class StrButton extends JPanel {

		private static final long serialVersionUID = 1L;
		
		private JLabel title;
        private Color bgColor;

        public StrButton(String str, Color color) {
            this.title = new JLabel(str);
            this.bgColor = color;
            setOpaque(false);
            setLayout(new GridLayout());
            setPreferredSize(new Dimension(180, 80));

            title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
            title.setHorizontalAlignment(SwingConstants.CENTER);

            this.add(this.title);
        }

       /* @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            
        }*/

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(bgColor);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
        }
        
        public String getTitle() {
        	return title.getText();
        }

        public void setBgColor(Color color) {
            this.bgColor = color;
        }

    }

    public CustomButton createSwitchBut() {
        CustomButton switchBut = null;
        if(name != null) {
            switchBut = new CustomButton("graph", isGraph ? "See map" : "See graph");
            switchBut.changeIconSize(64);
            switchBut.changeTextSize(20);
            switchBut.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (isGraph)
                        switchToMap();
                    else
                        switchToGraph();
                }
            });
        }
        return switchBut;
    }
    
    private class AlgoPanel extends OptionsPanel{

		private static final long serialVersionUID = 1L;

		public AlgoPanel() {
    		setName("AlgoPanel");
            StrButton selectAlgo = new StrButton("Select Algorithms", GUI.LIGHT_COLOR1);

            CustomButton switchBut = createSwitchBut();
    		
    		int line = switchBut == null ? 3 : 4;
            setLayout(new GridLayout(line, 1));
            JPanel tmp = new JPanel();
            tmp.setOpaque(false);
            tmp.setLayout(new GridLayout());
            tmp.setBorder(new EmptyBorder(10, 0, 20, 0));
            tmp.add(selectAlgo);
            add(tmp);
            
            tmp = new AlgochoicePanel();
            add(tmp);
            
            CustomButton runbut = new CustomButton("run", "Run Simulation");
            runbut.changeIconSize(64);
            runbut.changeTextSize(20);
            runbut.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    // Complexity.resetParams();
                    System.out.println("[LOG]: Run simulation: Mouse Pressed");
                    if (graph.getVertices().size() > 0) {                
                        ArrayList<Graph> graphs = new ArrayList<Graph>(); // Create an ArrayList object
                        graphs.add(graph);
                        System.out.println(graph.getVertices().size());
                        ComplexityInterface.setGraphs(graphs);
                        JFrame frame = ComplexityInterface.getFrame();
                        frame.setVisible(true);
                        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    } else {
                        System.out.println("[LOG]: Graph is empty, try again later...");
                        JOptionPane.showMessageDialog(getPanel("AlgoPanel"), "Nothing here so far. Please create a graph!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            this.add(runbut);
            
            if(switchBut != null)
                this.add(switchBut);
    	}
    }
    
    private class AlgochoicePanel extends JPanel{

		private static final long serialVersionUID = 1L;

		public AlgochoicePanel() {
    		setOpaque(false);
    		
    		StrButton[] buttons = new StrButton[] {
    				new StrButton("Dsatur", GUI.LIGHT_COLOR1),
    				new StrButton("WelshPowell", GUI.LIGHT_COLOR1),
    				new StrButton("Kempe", GUI.LIGHT_COLOR1),
    				new StrButton("Greedy", GUI.LIGHT_COLOR1)
    		};
    		
    		this.setLayout(new GridLayout(2,2));
    		for(StrButton b : buttons) {
    			b.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        if(graph != null) {
                            graph.applyAlgo(b.getTitle(), colors);
                            if(mapView != null)
                                mapView.refresh(graph);
                            if(graphView != null)
                                graphView.repaint();
                            revalidate();
                            repaint();
                        }
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        b.setBgColor(GUI.LIGHT_COLOR2);
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    	b.setBgColor(GUI.LIGHT_COLOR1);
                        repaint();
                    }
                });
                JPanel tmp = getTempPanel();
                tmp.setLayout(new GridLayout());
                tmp.setBorder(new EmptyBorder(5, 5, 5, 5));
                tmp.add(b);
    			add(tmp);
    		}
    		
    	}
    	
    }
    
    private class SaveOpenPanel extends JPanel{

		private static final long serialVersionUID = 1L;

		public SaveOpenPanel() {
			
			setOpaque(false);
			
    		CustomButton[] buttons = new CustomButton [] {
    				new CustomButton("save", "Save"),
                    new CustomButton("open", "Open"),
    		};

            for(CustomButton cB : buttons) {
                cB.setPreferredSize(new Dimension(180, 80));
                cB.changeIconSize(40);
                cB.changeTextSize(20);
            }

    		this.setLayout(new GridLayout(1,2));
    		buttons[0].addMouseListener(new MouseAdapter(){
    			public void mousePressed(MouseEvent e) {
    				saveGraphDialog();
    			}
    		});
            
            JPanel tmp = getTempPanel();
            tmp.add(buttons[0]);
    		add(tmp);
    		buttons[1].addMouseListener(new MouseAdapter(){
    			public void mousePressed(MouseEvent e) {
    				openGraphDialog();	
    			}
    		});
            tmp = getTempPanel();
            tmp.add(buttons[1]);
    		add(tmp);
    	}
    }
    
    private class GraphCreatorPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public GraphCreatorPanel() {
			setOpaque(false);
    		this.setLayout(new GridLayout(1,2));
    		
    		moveBut = new ImageButton("move", 70);
            selectedDrawBut = moveBut;
            moveBut.setSelected(true);
            lineBut = new ImageButton("line", 70);

            JPanel tmp = getTempPanel();
            tmp.setLayout(new GridBagLayout());
            tmp.add(moveBut);
            this.add(tmp);

            tmp = getTempPanel();
            tmp.setLayout(new GridBagLayout());
            tmp.add(lineBut);
            this.add(tmp);
    		
    	}
    	
    }

    protected class TopPanel extends JPanel {

		private static final long serialVersionUID = 1L;
        
        private JLabel title;

		public TopPanel() {
            setLayout(new GridLayout(1, 3));
            setOpaque(false);
            setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, GUI.DARK_COLOR2));
            JPanel temp2 = new JPanel();
            temp2.setBorder(new EmptyBorder(0, 10, 0, 0));
            temp2.setLayout(new BorderLayout());
            IconPanel left_arrow = new IconPanel("return", 40);
            left_arrow.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if(getSubPanel() == null || getSubPanel() instanceof MenuPanel)// ||  getSubPanel() instanceof PlayerPanel)
                        gui.setLastPage();
                    else {
                        westPanel.removeAll();
                        refreshWestPanel();
                    }
                }
            });

            IconPanel home = new IconPanel("home", 40);
            home.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    gui.setHomeView();
                }
            });

            temp2.setOpaque(false);
            temp2.add(home,BorderLayout.EAST);
            temp2.add(left_arrow, BorderLayout.WEST);
            add(temp2);
            
            temp2 = new JPanel();
            temp2.setOpaque(false);
            temp2.setLayout(new GridBagLayout());

            title = new JLabel();
            title.setFont(title.getFont().deriveFont(24.0f));
            title.setHorizontalAlignment(JLabel.CENTER);
            title.setForeground(Color.WHITE);

            refreshTitle();

            temp2.add(title);
            add(temp2);
            temp2 = new JPanel();
            temp2.setOpaque(false);
            add(temp2);
    	}

        public void refreshTitle() {
            if(GraphPlayView.this.title != null) {
                title.setText(GraphPlayView.this.title);
                return;
            }
            if(westPanel != null && westPanel.getComponentCount() > 0) {
                title.setText(westPanel.getComponent(0).getName());
            }
            else
                title.setText("");
        }
    }
    

    /*
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
    */
    public ActionListener algoListener(final String algo){
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
    
    public JPanel getSubPanel() {
        if(westPanel == null || westPanel.getComponentCount() == 0)
            return null;
        return (JPanel)westPanel.getComponent(0);
    }

    public void switchToMap() {
        if(mapView == null)
            this.mapView = new MapView(this, name, false);
        else
            this.mapView.refresh(graph);
        this.removeAll();
        
        if(isGraph)
            isGraph = !isGraph;
        
        this.setLayout(new BorderLayout());

        refreshWestPanel();
        topPanel.refreshTitle();

        this.add(getTopContainer(), BorderLayout.NORTH);
        this.add(westPanel, BorderLayout.WEST);

        this.add(mapView);
        
        this.revalidate();
        this.repaint();
    }
    
    public JPanel getPanel(String pnl) {
        switch(pnl) {
            case "AlgoPanel":
                return new AlgoPanel();
            case "PlayingPanel":
                return new PlayingPanel();
            case "MenuPanel":
            default:
                return new MenuPanel();
        }
    }

    public JPanel getTopContainer() {
        JPanel nPan = new JPanel();
        nPan.setOpaque(false);
        nPan.setLayout(new GridLayout());
        nPan.add(topPanel);
        nPan.setBorder(new EmptyBorder(8, 0, 35, 0));
        return nPan;
    }

    public void refreshWestPanel() {
        if(westPanel.getComponentCount() == 0) {
            westPanel.add(new MenuPanel());
        }
        else {
            // Inutile ?
            /*
            String[] panels = {"AlgoPanel", "PlayingPanel", "PlayerPanel"};
            for(String pnl : panels) {
                if(westPanel.getComponent(0).getName().equals(pnl)) {
                    westPanel.removeAll();
                    westPanel.add(getPanel(pnl));
                    break;
                }
            }
            */
        }
        topPanel.refreshTitle();
        westPanel.revalidate();
        westPanel.repaint();
    }

    public void setupWestPanel() {
        westPanel = new JPanel();
        westPanel.setOpaque(false);
        westPanel.setLayout(new GridLayout());
        westPanel.setBorder(new EmptyBorder(0, 50, 30, 50)); // 0 pour top car 80 dans topPanel
    }

    public void switchToGraph() {
        if(graphView == null) {
            this.graphView = new GraphView(this, name, algo, width, height, GraphPlayView.this.colors);
        }
        else {
            // refresh this.graphView
        }

        if(!isGraph)
            isGraph = !isGraph;

        this.removeAll();
        //this.add(toolBar, BorderLayout.NORTH);
        this.setLayout(new BorderLayout());

        refreshWestPanel();
        topPanel.refreshTitle();

        this.add(getTopContainer(), BorderLayout.NORTH);
        this.add(westPanel, BorderLayout.WEST);

        this.add(graphView, BorderLayout.CENTER);

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
                this.graph = Graph.load(file_path.getAbsolutePath());
                this.graphView = new GraphView(this, graph);
                switchToGraph();
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

    public JPanel getTempPanel() {
        JPanel tmp = new JPanel();
        tmp.setOpaque(false);
        return tmp;
    }

    public String getAlgo() {
        return algo;
    }

    public Color[] getColors() {
        return colors;
    }

    public Color getUserColor() {
        return selectedColorBut == null ? Color.WHITE: selectedColorBut.getColor();
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public MapView getMapView() {
        return mapView;
    }

}
