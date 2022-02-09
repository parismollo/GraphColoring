package gui;

import java.awt.Dimension;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

import graphs.Graph;
import utils.Converter;

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
	public void setMapPage(Graph graph, String mapURL) {
		this.getContentPane().removeAll();
		this.setResizable(false);
		MapView map = new MapView(graph, mapURL, true);
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
