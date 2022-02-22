package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;

import graphs.Graph;
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
		
		/*
		 try {
		 	graph = Converter.mapToGraph("src/resources/France.csv");
		 	graph.setVerticesList(algorithms.Dsatur.dsatur(graph.getVertices()));
		 } catch (FileNotFoundException e) {
		 	e.printStackTrace();
		 }
		*/
		//setGraphViewPage("France", "WelshPowell");
		//testGreedyRandom();
		//testGreedy();
		// testBestGreedy();
		////// TEST: on clique sur le pays pour le dessiner
		// Il faut commenter setGraphViewPage si vous voulez tester
		// setFillImagePage();
		///////////

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
	
	public void setHomePage() {
		// TODO: Home page.
	}

	public void setGraphViewPage(Graph graph) {
		setGraphViewPage(graph, null, null, true);
	}
	
	public void setGraphViewPage(String name, String algo) {
		setGraphViewPage(null, name, algo, false);
	}

	public void setGraphViewPage(Graph graph, String name, String algo, boolean isGraph) {
		this.getContentPane().removeAll();
		this.setResizable(true);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		GraphPlayView graphPlayView;
		if(isGraph)
			graphPlayView = new GraphPlayView(graph, width, height);
		else
			graphPlayView = new GraphPlayView(name, algo, width, height);
		this.getContentPane().add(graphPlayView);
		revalidate();
		repaint();
	}
	
	
	public void setMapPage(String mapName, boolean devMode) {
		this.getContentPane().removeAll();
		this.setResizable(false);
		MapView map = new MapView(mapName, devMode);
		this.setMinimumSize(map.getMapDim());
		this.setSize(map.getMapDim());
		
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
		
		Color[] nb = {Color.blue, Color.BLACK, Color.CYAN, Color.GREEN};
		Color[] color = new Color[graph.getVertices().size()+1];
		for(int i=0;i<color.length;i++){
			color[i] = Color.WHITE;
		}
		
		//ArrayList<Vertex> list = algorithms.WelshPowell.selectionSort(graph.getVertices()); //Comme on test sur un graph random si je ne fais pas ça les id des sommets sont nuls et greedy ne marche pas (afficher un message d'erreur dans ce cas ?)
		System.out.println(algorithms.Greedy.boolGraphColoringGreedy(0, nb, color, graph));
		graph = algorithms.Greedy.greedy(graph, nb);
		graph.print();
		setGraphViewPage(graph);
	}

	public void testGreedy(){
		try {
			Graph graph = Converter.mapToGraph("src/resources/France.csv");
			Color[] nb = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.ORANGE};
			//graph.setVerticesList(algorithms.WelshPowell.selectionSort(graph.getVertices()));
			graph = algorithms.Greedy.greedy(graph, nb);
			graph.print();
			setGraphViewPage(graph);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void testBestGreedy(){
		//Test pour greedy de la fonction bestGraphColoringGreedy 
		//La fonction bestGraphColoringGreedy ne fonctionne pas pour plus de 11 sommets environ, c'est trop long
		Graph graph = Graph.randomGraph(10);
		Color[] nb = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.ORANGE};
		Color[] color = new Color[graph.getVertices().size()+1];
		for(int i=0;i<color.length;i++){
			color[i] = Color.white;
		}
		Map<Color[],Integer> test = new HashMap<>();
		test.put(nb,6);//Important sinon ne marche pas
		algorithms.Greedy.bestGraphColoringGreedy(0, nb, color, graph, test);
		Color[] fnl = new Color[color.length];
		for(Entry<Color[], Integer> yes : test.entrySet()){
			fnl = yes.getKey().clone();
		}
		for(int i=1;i<=graph.getVertices().size();i++){
			graph.getVertex(i).setColor(fnl[i]);
		}
		graph.print();
		setGraphViewPage(graph);
	}
	
}
