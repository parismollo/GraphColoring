package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.Color;

import javax.swing.JFrame;

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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);

		//setHomePage();
		
		// Pour l'instant, pour tester :
		Graph graph = Graph.randomGraph(9);
		//graph.setVerticesList(algorithms.WelshPowell.welshPowell(graph.getVertices()));
		//System.out.println(graph);
		
		//Test pour Greedy : (qui retourne un boolean)
		/* 1er test sur un graph random avec greedy et boolGraphColoringGreedy
		Color[] nb = {Color.blue, Color.BLACK, Color.CYAN, Color.GREEN};
		Color[] color = new Color[graph.getVertices().size()+1];
		
		for(int i=0;i<color.length;i++){
			color[i] = Color.WHITE;
		}
		
		//ArrayList<Vertex> list = algorithms.WelshPowell.selectionSort(graph.getVertices()); //Comme on test sur un graph random si je ne fais pas ça les id des sommets sont nuls et greedy ne marche pas (afficher un message d'erreur dans ce cas ?)
		System.out.println(algorithms.Greedy.boolGraphColoringGreedy(0, nb, color, graph));
		for(Color c : color){
			System.out.println(c.toString());
		}
		graph = algorithms.Greedy.greedy(graph, nb);
		graph.print();;
		*/
		//Test greedy sur graph de la France
		/*
		try {
			Color[] nb = {Color.blue, Color.BLACK, Color.CYAN, Color.GREEN};
			graph = Converter.mapToGraph("src/resources/France.csv");
			Color[] color = new Color[graph.getVertices().size()+1];
			for(int i=0;i<color.length;i++){
				color[i] = Color.WHITE;
			}
			//ArrayList<Vertex> list = algorithms.WelshPowell.selectionSort(graph.getVertices());
			System.out.println(algorithms.Greedy.boolGraphColoringGreedy(0, nb, color, graph));
			graph = algorithms.Greedy.greedy(graph, nb);
			graph.print();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/

		//Test pour greedy de la fonction bestGraphColoringGreedy 
		/* 
		try {
			graph = Converter.mapToGraph("src/resources/France.csv");// Il y a trop de sommets pour que ça fonctionne
			graph = Graph.randomGraph(11);
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
			graph.print();;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
			
		*/
			//////////////////////////////////////////////
			//////////////////////////////////////////////
			// OUBLIE PAS DE COMMENTER LE
			// setGraphViewPage QUI EST EN BAS !!!
			setGraphViewPage(graph);
			//////////////////////////////////////////////
			//////////////////////////////////////////////
		
		/*
		 try {
		 	graph = Converter.mapToGraph("src/resources/France.csv");
		 	graph.setVerticesList(algorithms.Dsatur.dsatur(graph.getVertices()));
		 } catch (FileNotFoundException e) {
		 	e.printStackTrace();
		 }
		*/
		setGraphViewPage("France", "WelshPowell");

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
	
	/////////////// TEST ////////////
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
	/////////////////////////////////

	public void setMapChooser(boolean devMode) {
		this.getContentPane().removeAll();
		this.setResizable(false);
		
		this.getContentPane().add(new MapChooser(this, devMode));
		revalidate();
		repaint();
	}

	public void close() {
    	this.dispose();
        System.exit(0);
	}
	
}
