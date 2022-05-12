package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import algorithms.Dsatur;
import algorithms.Kempe;
import graphs.Graph;
import utils.Voronoi;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;

	public static Color BACKGROUND_COLOR = new Color(41, 50, 65),
						DARK_COLOR1 = new Color(61, 90, 128),
						DARK_COLOR2 = new Color(46, 55, 75),
						LIGHT_COLOR1 = new Color(152, 193, 217),
						LIGHT_COLOR2 = new Color(224, 251, 252),
						RED = new Color(238, 108, 77);
/*
	public static Color BACKGROUND_COLOR = new Color(0, 27, 46),
						DARK_COLOR1 = new Color(41, 76, 96),
						LIGHT_COLOR1 = new Color(173, 182, 196),
						LIGHT_COLOR2 = new Color(255, 239, 211),
						RED = new Color(238, 108, 77);
*/
	private JPanel lastPanel;

	private int width;
	private int height;
	
	public GUI(int w, int h) {
		this.setTitle("Graph coloring");
		this.width = w;
		this.height = h;
		this.setMinimumSize(new Dimension(width+75, height));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);

		//setHomePage();
		
		//Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.ORANGE};
		//Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA};
		//setGraphViewPage("France", "WelshPowell", colors);
		//setGraphViewPage("France", "Dsatur", colors);
		//setGraphViewPage("France", "Greedy", colors);
		//setGraphViewPage("France", "BestGreedy", colors);
		//setGraphViewPage("France", "kempe", colors);
		//testGreedyRandom();
		//testGreedy();
		//testBestGreedy();
		//testKempeChain(colors);
		//testKempeChainEchec(colors);
		
		
		
		
		this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
				if(MapView.sc != null)
					MapView.sc.close();
				System.exit(0);
            }
        });

		this.setVisible(true);
	}
	
	public void setHomeView() {
		this.getContentPane().removeAll();
		this.setResizable(true);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		lastPanel = new HomeView(this);
		this.getContentPane().add(lastPanel);
		revalidate();
		repaint();
	}

	public void setRandomGraphView(int edges, Color[] colors) {
		this.getContentPane().removeAll();
		this.setResizable(true);
		Graph g = Voronoi.runVoronoi(edges, false, false, 500, false);
		RandomGraphView rgv = new RandomGraphView(this, g, colors, width, height);
		this.getContentPane().add(rgv);
		revalidate();
		repaint();
	}

	public void setCreatorPage(Color[] colors) {
		this.getContentPane().removeAll();
		this.setResizable(true);
		this.getContentPane().add(new GraphCreatorView(this, colors));
		revalidate();
		repaint();
	}

	public void setGraphViewPage(Graph graph) {
		setGraphViewPage(graph, null, null, null, true);
	}
	
	public void setGraphViewPage(String name, String algo, Color[] colors) {
		setGraphViewPage(null, name, algo, colors, true);
	}

	public void setGraphViewPage(Graph graph, String name, String algo, Color[] colors, boolean isGraph) {
		this.getContentPane().removeAll();
		this.setResizable(true);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		GraphPlayView graphPlayView;
		if(graph != null)
			graphPlayView = new GraphPlayView(this, graph, colors, width, height);
		else
			graphPlayView = new GraphPlayView(this, name, algo, width, height, colors, isGraph);
		this.getContentPane().add(graphPlayView);
		revalidate();
		repaint();
	}
	
	public void setMapPage(String mapName, boolean devMode) {
		this.getContentPane().removeAll();
		//this.setResizable(false);
		MapView map = new MapView(mapName, devMode);
		//this.setMinimumSize(map.getMapDim());
		//this.setSize(map.getMapDim());
		this.getContentPane().add(map);
		revalidate();
		repaint();
	}

	public void setMapChooser(boolean devMode) {
		this.getContentPane().removeAll();
		this.setResizable(true);
		lastPanel = new MapChooser(this, devMode);
		this.getContentPane().add(lastPanel);
		revalidate();
		repaint();
		lastPanel.requestFocus(); // IMPORTANT pour faire fonctionner le keyboard listener
	}

	public void setLastPage() {
		if(lastPanel == null)
			return;
		this.getContentPane().removeAll();
		this.setResizable(true);
		this.getContentPane().add(lastPanel);
		revalidate();
		repaint();
		if(lastPanel instanceof MapChooser)
			lastPanel.requestFocus();
	}

	public void close() {
    	this.dispose();
        System.exit(0);
	}

	public void testGreedyRandom(){
		//Test pour Greedy : (qui retourne un boolean)
		//1er test sur un graph random avec greedy et boolGraphColoringGreedy

		Graph graph = Graph.randomGraph(9);
		
		Color[] colors = {Color.blue, Color.BLACK, Color.CYAN, Color.GREEN};
		Color[] graph_colors = new Color[graph.getVertices().size()+1];
		for(int i=0;i<graph_colors.length;i++){
			graph_colors[i] = Color.WHITE;
		}
		
		//ArrayList<Vertex> list = algorithms.WelshPowell.selectionSort(graph.getVertices());
		System.out.println(algorithms.Greedy.boolGraphColoringGreedy(0, colors, graph_colors, graph.getVertices()));
		algorithms.Greedy.greedy(graph, colors);
		// graph.print();
		setGraphViewPage(graph);
	}

	public void testBestGreedy(){
		//Test pour greedy de la fonction bestGraphColoringGreedy 
		//La fonction bestGraphColoringGreedy ne fonctionne pas pour plus de 11 sommets environ, c'est trop long
		Graph graph = Graph.randomGraph(8);
		Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.ORANGE};
		algorithms.Greedy.bestGreedy(graph, colors);
		// graph.print();
		setGraphViewPage(graph);
	}

	public void testBebou(String algo, Color[] colors){
		algo = algo.toUpperCase();
		Graph bebou = null;
		try {
			bebou = Graph.load("src/resources/graphBebou.txt");
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		// bebou.print();
		switch(algo){
			case "DSATUR":
                algorithms.Dsatur.dsatur(bebou, colors);
                break;
            case "WELSHPOWELL":
				algorithms.WelshPowell.welshPowell(bebou, colors);
                break;
            case "GREEDY":
				algorithms.Greedy.greedy(bebou, colors);
                break;
            case "BESTGREEDY":
				algorithms.Greedy.bestGreedy(bebou, colors);
            case "KEMPE":
                Kempe.kempe(bebou, colors);
                break;
		}
		
		setGraphViewPage(bebou);
	}

	public void testKempeChain(Color[] colors){
		Graph graph = null;
		
		
		try {
			graph = Graph.load("src/resources/graphKempe.txt");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
		Kempe.kempe(graph, colors);
		System.out.println();
		// graph.print();
		setGraphViewPage(graph);
	}

	public void testKempeChainEchec(Color[] colors){
		Graph graph = null;
		
		
		try {
			//graph = Graph.load("src/resources/graphKempe.txt");
			graph = Graph.load("src/resources/graphKempe.txt");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
		//Greedy.greedy(graph, colors);//Ca ne marche pas avec greedy (enfin ça ne fait pas d'echec) car greedy modifie les couleurs du graph partiellement coloré si besoin
		Dsatur.dsatur(graph, colors);
		//Graph.resetColors(graph);
		System.out.println();
		// graph.print();
		setGraphViewPage(graph);
	}

	// Quelques tests :
	/* Test de la fonction isWellColored
		Graph graph = null;
		try {
			graph = Converter.mapToGraph(MapView.RESOURCES_FOLDER+"Europe.csv");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(Graph.isWellColored(graph));
		Kempe.kempe(graph, colors);
		System.out.println(Graph.isWellColored(graph));
		*/

		// TEST GRAPH BEBOU
		/*
		Graph random = Graph.randomGraph(11);
		try {
			random.save("src/resources/graphBebou.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		//testBebou("KEMPE", colors);
	
}
