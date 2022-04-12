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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
//import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;

import graphs.Graph;

public class GraphPlayView extends JPanel {

	private static final long serialVersionUID = 1L;

	private GUI gui;

    private ColorButton selectedColorBut;
    private ImageButton selectedDrawBut;
    private ImageButton moveBut, lineBut;
    //private JButton saveBut, openBut;
    //private JButton[] algosButtons;
    private JToolBar toolBar;

    private MapView mapView;
    private GraphView graphView;

    private Graph graph;

    private String name, algo;
    private int width, height;
    private Color[] colors;

    private JPanel westPanel;

    private boolean isGraph = true;
    
    public GraphPlayView(GUI gui) {
        this.gui = gui;
        this.graphView = new GraphView(this);
        this.setLayout(new BorderLayout());
        //setupToolBar(true); // devMode true
        /*IconPanel left_arrow = new IconPanel("return", 40);
        left_arrow.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                gui.setLastPage();
            }
        });*/
        this.add(new topPanel(),BorderLayout.NORTH);
        this.add(graphView, BorderLayout.CENTER);
        this.add(new playerPanel(),BorderLayout.WEST);
        this.setBackground(GUI.BACKGROUND_COLOR);
    }

    // ICI : isGraph = true.
    public GraphPlayView(GUI gui, Graph graph, int width, int height) {
        this.gui = gui;
        this.graphView = new GraphView(this, graph, width, height);
        this.setLayout(new BorderLayout());
        //setupToolBar(false);
        /*IconPanel left_arrow = new IconPanel("return", 40);
        left_arrow.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                gui.setLastPage();
            }
        });*/
        this.add(new topPanel(),BorderLayout.NORTH);
        this.add(graphView, BorderLayout.CENTER);
        this.add(new playerPanel(),BorderLayout.WEST);
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
        IconPanel left_arrow = new IconPanel("return", 40);
        left_arrow.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                gui.setLastPage();
            }
        });
        this.add(new topPanel(),BorderLayout.NORTH);
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
            int space = 4;
            g.setColor(color);
            g.fillOval(space, space, getWidth()-2*space, getHeight()-2*space);
            g.setColor(selected ? Color.RED : Color.BLACK);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(3.0f));
            g.drawOval(space, space, getWidth()-2*space, getHeight()-2*space);
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

    private class OptionsPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private int shadowSpace = 25;

        public OptionsPanel() {
            
        	this.setOpaque(false);
            int topBottom = 32, leftRight = 64;

            this.setBorder(new EmptyBorder(topBottom, leftRight,
                                           topBottom + shadowSpace, leftRight + shadowSpace));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(GUI.LIGHT_COLOR2);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
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

            buttons[0].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    westPanel.removeAll();
                    westPanel.add(new AlgoPanel());
                    westPanel.revalidate();
                    westPanel.repaint();
                }
            });
            
            buttons[1].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    westPanel.removeAll();
                    westPanel.add(new PlayingPanel());
                    westPanel.revalidate();
                    westPanel.repaint();
                }
            });

            int column = switchBut == null ? buttons.length-1 : buttons.length;
            setLayout(new GridLayout(column, 1));
            JPanel tmp;
            for(CustomButton b : buttons) {
                if(b == null)
                    continue;
                tmp = new JPanel();
                tmp.setOpaque(false);
              //  tmp.setBorder(new EmptyBorder());
                add(b);
            }
        }

    }

    private class PlayingPanel extends OptionsPanel {

		private static final long serialVersionUID = 1L;

		public PlayingPanel() {
        	
    		setName("PlayingPanel");
            StrButton selectColor = new StrButton("Select Color", GUI.LIGHT_COLOR1);
            selectColor.setEnabled(false);

            CustomButton switchBut = createSwitchBut();

            int line = switchBut == null ? 2 : 3;
            setLayout(new GridLayout(line, 1));
            JPanel tmp = new JPanel();
            tmp.setLayout(new GridLayout());
            tmp.setOpaque(false);
            tmp.setBorder(new EmptyBorder(60, 0, 60, 0));
            tmp.add(selectColor);
            add(tmp);

            add(new ColorPanel());

            tmp = new JPanel();
            tmp.setLayout(new GridBagLayout());
            tmp.setOpaque(false);
            tmp.add(switchBut);
            add(tmp);
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
                    selectedColorBut = new ColorButton(colors[i], 50);
                    selectedColorBut.setSelected(true);
                    tmp.add(selectedColorBut);
                }
                else
                    tmp.add(new ColorButton(colors[i], 50));
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
            
            title.setFont(title.getFont().deriveFont(Font.BOLD, 32f));
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

    }

    public CustomButton createSwitchBut() {
        CustomButton switchBut = null;
        if(name != null) {
            switchBut = new CustomButton("graph", isGraph ? "See map" : "See graph");
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
            tmp.setLayout(new GridLayout());
            tmp.setOpaque(false);
            tmp.setBorder(new EmptyBorder(60, 0, 60, 0));
            tmp.add(selectAlgo);
            add(tmp);
            
            tmp = new AlgochoicePanel();
            add(tmp);
            
            CustomButton runbut = new CustomButton("run", "Run Simulation");
            this.add(runbut);
            
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
                        	System.out.println(b.getTitle());
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
                        b.setBackground(GUI.LIGHT_COLOR2); 
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    	b.setBackground(GUI.LIGHT_COLOR1);
                        repaint();
                    }
                });

    			add(b);
    		}
    		
    	}
    	
    }
    
    private class playerPanel extends OptionsPanel{
    	
		private static final long serialVersionUID = 1L;

		public playerPanel() {
    		setOpaque(false);
    		setName("PlayerPanel");
    		ColorPanel c = new ColorPanel();
    		saveOpenPanel so = new saveOpenPanel();
    		graphCreatorPanel g = new graphCreatorPanel();
    		this.setLayout(new GridLayout(4,1));
    		this.add(c);
    		this.add(so);
    		this.add(g);
    		
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
    	}
    }
    
    private class saveOpenPanel extends JPanel{

		private static final long serialVersionUID = 1L;

		public saveOpenPanel() {
			
			setOpaque(false);
			
    		CustomButton[] buttons = new CustomButton [] {
    				new CustomButton("save", "Save"),
                    new CustomButton("open", "Open"),
    		};
    		this.setLayout(new GridLayout(1,2));
    		buttons[0].addMouseListener(new MouseAdapter(){
    			public void mousePressed(MouseEvent e) {
    				saveGraphDialog();
    			}
    		});
    		buttons[0].setLayout(new GridBagLayout());
    		add(buttons[0]);
    		buttons[1].addMouseListener(new MouseAdapter(){
    			public void mousePressed(MouseEvent e) {
    				openGraphDialog();	
    			}
    		});
    		buttons[1].setLayout(new GridBagLayout());
    		add(buttons[1]);
    	}
    }
    
    private class graphCreatorPanel extends JPanel{

		private static final long serialVersionUID = 1L;

		public graphCreatorPanel() {
    		
			setOpaque(false);
			
    		this.setLayout(new GridLayout(1,2));
    		
    		moveBut = new ImageButton("move", 45);
            selectedDrawBut = moveBut;
            moveBut.setSelected(true);
            lineBut = new ImageButton("line", 45);
            this.add(moveBut);
            this.add(lineBut);
    		
    	}
    	
    }
    
    private class topPanel extends JPanel{

		private static final long serialVersionUID = 1L;

		public topPanel() {
    		
    		
            setLayout(new GridLayout(1, 3));
            setBackground(GUI.BACKGROUND_COLOR);
            JPanel temp2 = new JPanel();
            temp2.setBorder(new EmptyBorder(0, 10, 0, 0));
            temp2.setLayout(new BorderLayout());
            IconPanel left_arrow = new IconPanel("return", 40);
            left_arrow.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    gui.setHomeView();
                }
            });
            temp2.setBackground(GUI.BACKGROUND_COLOR);
            temp2.add(left_arrow, BorderLayout.WEST);
            add(temp2);
            
            temp2 = new JPanel();
            //temp2.setBackground(GUI.BACKGROUND_COLOR);
            JLabel title;
            if(westPanel != null) {
            	title = new JLabel(westPanel.getComponent(0).getName());
                title.setFont(title.getFont().deriveFont(24.0f));
                title.setHorizontalAlignment(JLabel.CENTER);
                title.setBackground(GUI.BACKGROUND_COLOR);
            }
            else {
            	title = new JLabel("");
            	temp2.setBackground(GUI.BACKGROUND_COLOR);
            }
            temp2.add(title);
            add(temp2);
            temp2 = new JPanel();
            temp2.setBackground(GUI.BACKGROUND_COLOR);
            add(temp2);
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
    

    public void switchToMap() {
        if(mapView == null)
            this.mapView = new MapView(this, name, false);
        else
            this.mapView.refresh(graph);
        this.removeAll();
        
        if(isGraph)
            isGraph = !isGraph;

       /////////////////////////////////////////////////////////////////////////////////////////// 
        //this.add(toolBar, BorderLayout.NORTH);

        // On retire ça pour l'instant
        /*JPanel pan = new JPanel();
        pan.setLayout(new GridBagLayout());
        pan.add(mapView);
        pan.setOpaque(false);
        
        
        this.add(pan, BorderLayout.CENTER);*/
        
        //this.add(mapView, BorderLayout.CENTER);
        
        this.setLayout(new BorderLayout());
        if(westPanel.getComponentCount() != 0) {
	        if(westPanel.getComponent(0).getName().equals("MenuPanel")){
	        	this.add(new topPanel(), BorderLayout.NORTH);
	        	westPanel.removeAll();
	            westPanel.add(new MenuPanel());
	        }
	        if(westPanel.getComponent(0).getName().equals("AlgoPanel")) {

	        	this.add(new topPanel(), BorderLayout.NORTH);
	        	westPanel.removeAll();
	            westPanel.add(new AlgoPanel());
	        }
	        if(westPanel.getComponent(0).getName().equals("PlayingPanel")) {

	        	this.add(new topPanel(), BorderLayout.NORTH);
	        	westPanel.removeAll();
	            westPanel.add(new PlayingPanel());
	        }
        }
        else {
        	westPanel.removeAll();
            westPanel.add(new MenuPanel());
        }

        this.add(westPanel, BorderLayout.WEST);
        this.add(mapView);
        
        this.revalidate();
        this.repaint();
    }
    
    public void setupWestPanel() {
        westPanel = new JPanel();
        westPanel.setOpaque(false);
        westPanel.setLayout(new GridLayout());
        westPanel.setBorder(new EmptyBorder(80, 50, 80, 50));
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
        if(westPanel.getComponentCount() != 0) {
	        if(westPanel.getComponent(0).getName().equals("MenuPanel")){
	        	this.add(new topPanel(), BorderLayout.NORTH);
	        	westPanel.removeAll();
	            westPanel.add(new MenuPanel());
	        }
	        if(westPanel.getComponent(0).getName().equals("AlgoPanel")) {
	        	this.add(new topPanel(), BorderLayout.NORTH);
	        	westPanel.removeAll();
	            westPanel.add(new AlgoPanel());
	        }
	        if(westPanel.getComponent(0).getName().equals("PlayingPanel")) {
	        	this.add(new topPanel(), BorderLayout.NORTH);
	        	westPanel.removeAll();
	            westPanel.add(new PlayingPanel());
	        }
	        
        else {
        	westPanel.removeAll();
            westPanel.add(new MenuPanel());
        }

        this.add(westPanel, BorderLayout.WEST);
        this.add(graphView, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
        }
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
