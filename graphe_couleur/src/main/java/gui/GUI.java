package gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import java.awt.Color;

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
		
		//Test greedy sur graph des usa
		
		
		try {
			graph = Converter.mapToGraph("src/resources/France.csv");
			graph = Graph.randomGraph(11);
			Color[] nb = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.ORANGE};
			Color[] color = new Color[graph.getVertices().size()+1];
		
			for(int i=0;i<color.length;i++){
				color[i] = Color.WHITE;
			}

			Map<Color[],Integer> test = new HashMap<>();
			test.put(nb, 6);
			System.out.println(test.toString());
			algorithms.Greedy.bestGraphColoringGreedy(0, nb, color, graph.getVertices(), test);
			System.out.println(test.toString());
			for(Entry<Color[], Integer> yes : test.entrySet()){
				for(Color c : yes.getKey()){
					System.out.println(c.toString());
				}
			}
			
			Color[] fnl = new Color[color.length];
			for(Entry<Color[], Integer> yes : test.entrySet()){
				fnl = yes.getKey().clone();
			}
			for(int i=1;i<=graph.getVertices().size();i++){
				graph.getVertex(i).setColor(fnl[i]);
			}
			System.out.println(graph.getVertex(1).getColor());
			System.out.println(graph);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			graph = Converter.mapToGraph("src/resources/USA.csv");
			//ArrayList<Vertex> list = algorithms.WelshPowell.selectionSort(graph.getVertices());
			//System.out.println(algorithms.Greedy.boolGraphColoringGreedy(0, nb, color, graph.getVertices()));
			//Si j'appelle boolGraphColoringGreedy et greedy sans reinitialiser color alors je vais avoir des erreurs à l'affichage car le tableau passé en paramétre ne sera pas blanc et donc des voisins qui auraient du être blancs ne le seront pas
			//Donc soit je laisse comme ça soit une solution peut être de reinitialiser a blanc toutes les cases du tableaux au début de ces fonctions.
			graph.setVerticesList(algorithms.Greedy.greedy(graph.getVertices(), nb));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/
			//////////////////////////////////////////////
			//////////////////////////////////////////////
			// OUBLIE PAS DE COMMENTER LE
			// setGraphViewPage QUI EST EN BAS !!!
			setGraphViewPage(graph);
			//////////////////////////////////////////////
			//////////////////////////////////////////////
		
		
		// try {
		// 	graph = Converter.mapToGraph("src/resources/USA.csv");
		// 	graph.setVerticesList(algorithms.Dsatur.dsatur(graph.getVertices()));
		// } catch (FileNotFoundException e) {
		// 	e.printStackTrace();
		// }
		
		//setGraphViewPage("France", "WelshPowell");

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
