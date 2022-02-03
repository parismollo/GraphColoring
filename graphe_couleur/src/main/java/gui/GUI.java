package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import graphs.Graph;

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
		System.out.println(graph);
		setGraphViewPage(graph);

		this.setVisible(true);
	}
	
	public void setHomePage() {
		// TODO: Home page.
	}

	public void setGraphViewPage(Graph graph) {
		this.getContentPane().removeAll();
		this.setResizable(true);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);*
		GraphPlayView graphPlayView = new GraphPlayView(graph, width, height);
		this.getContentPane().add(graphPlayView);
		revalidate();
		repaint();
	}
	
	public void close() {
    	this.dispose();
        System.exit(0);
	}
	
}
