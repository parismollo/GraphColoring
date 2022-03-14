package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

import algorithms.Kempe;
import algorithms.WelshPowell;
import graphs.Graph;
import graphs.Vertex;
import utils.Converter;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private int width;
	private int height;
	
	public GUI(int w, int h) {
		this.setTitle("Graph");
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
		Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA};
		//setGraphViewPage("France", "WelshPowell", colors);
		//setGraphViewPage("France", "Dsatur", colors);
		setGraphViewPage("France", "Greedy", colors);
		//setGraphViewPage("France", "BestGreedy", colors);
		//setGraphViewPage("France", "Kempe", colors);
		//testGreedyRandom();
		//testGreedy();
		// testBestGreedy();
		
		// TEST GRAPH BEBOU
		/*Graph random = Graph.randomGraph(11);
		try {
			random.save("src/resources/graphBebou.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		//testBebou("BESTGREEDY", colors);

		 // TEST KEMPE
		/*Graph graph = null;
		Vertex v = null;
		try {
			graph = Converter.mapToGraph("src/resources/France.csv");
			v = graph.getVertices().get(10);
			System.out.println(v.getId());
			WelshPowell.welshPowell(graph, colors);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		graph = Kempe.kempeChain(v);
		System.out.println(graph.getVertices());
		Kempe.reverseKempeChain(graph);
		setGraphViewPage(graph);
		*/

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
		this.getContentPane().add(new HomeView(this));
		revalidate();
		repaint();
	}

	public void setRandomGraphView(int edges) {
		setGraphViewPage(Graph.randomGraph(edges));
	}

	public void setCreatorPage() {
		this.getContentPane().removeAll();
		this.setResizable(true);
		this.getContentPane().add(new GraphPlayView());
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
			graphPlayView = new GraphPlayView(graph, width, height);
		else
			graphPlayView = new GraphPlayView(name, algo, width, height, colors, isGraph);
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
		
		this.getContentPane().add(new MapChooser(this, devMode));
		revalidate();
		repaint();
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
		graph.print();
		setGraphViewPage(graph);
	}

	public void testBestGreedy(){
		//Test pour greedy de la fonction bestGraphColoringGreedy 
		//La fonction bestGraphColoringGreedy ne fonctionne pas pour plus de 11 sommets environ, c'est trop long
		Graph graph = Graph.randomGraph(8);
		Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.ORANGE};
		algorithms.Greedy.bestGreedy(graph, colors);
		graph.print();
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
		bebou.print();
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
                //vertices = Kempe.kempe(vertices);
                break;
		}
		
		setGraphViewPage(bebou);
	}
	
}
