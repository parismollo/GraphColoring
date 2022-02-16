package gui;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JFrame;

import graphs.Graph;
import graphs.Vertex;
import utils.Converter;

import java.awt.Color;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private int width;
	private int height;

	// Pour l'instant la GUI prend un game en parametre.
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
		
		/*Test pour Greedy : (qui retourne un boolean)
		
		Color[] nb = {Color.blue, Color.BLACK, Color.CYAN, Color.GREEN};
		Color[] color = new Color[15];
		
		for(int i=0;i<15;i++){
			color[i] = Color.WHITE;
		}
		
		ArrayList<Vertex> list = algorithms.WelshPowell.selectionSort(graph.getVertices()); //Comme on test sur un graph random si je ne fais pas ça les id des sommets sont nuls et greedy ne marche pas (afficher un message d'erreur dans ce cas ?)
		System.out.println(algorithms.Greedy.graphColoring(0, nb, color, list));
		graph.setVerticesList(algorithms.Greedy.greedy(graph.getVertices(), nb, color));
		System.out.println(graph);
		
		*/

		try {
			graph = Converter.mapToGraph("src/resources/USA.csv");
			graph.setVerticesList(algorithms.Dsatur.dsatur(graph.getVertices()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		setGraphViewPage(graph);
		
		////// TEST: on clique sur le pays pour le dessiner
		// Il faut commenter setGraphViewPage si vous voulez tester
		// setFillImagePage();
		///////////
		this.setVisible(true);
	}
	
	public void setHomePage() {
		// TODO: Home page.
	}

	public void setGraphViewPage(Graph graph) {
		this.getContentPane().removeAll();
		this.setResizable(true);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		GraphPlayView graphPlayView = new GraphPlayView(graph, width, height);
		this.getContentPane().add(graphPlayView);
		revalidate();
		repaint();
	}
	
	/////////////// TEST ////////////
	public void setMapPage(Graph graph, String mapURL, boolean devMode) {
		this.getContentPane().removeAll();
		this.setResizable(false);
		MapView map = new MapView(graph, mapURL, devMode);
		this.setMinimumSize(map.getMapDim());
		this.setSize(map.getMapDim());
		
		this.getContentPane().add(map);
		revalidate();
		repaint();
	}
	/////////////////////////////////

	public void close() {
    	this.dispose();
        System.exit(0);
	}
	
}
