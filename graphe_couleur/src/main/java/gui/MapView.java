package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import graphs.Graph;
import graphs.Vertex;
import utils.Converter;

public class MapView extends JPanel {
    
    public static String RESOURCES_FOLDER = "src/resources/";

    private GraphPlayView graphPlayView;
    private Graph graph;
    private final String name;
    private BufferedImage image;
    public static Scanner sc;
    private boolean devMode;
    // private boolean otherPos;

    //private Dimension scaledDim; // On enregistre avec setSize finalement

    public MapView(final String name) {
        this(name, false);
    }

    public MapView(final String name, boolean devMode) {
        this(null, name, devMode);
    }

    public MapView(GraphPlayView graphPlayView, final String name, boolean devMode) {
        this.graphPlayView = graphPlayView;
        this.name = name;
        this.devMode = devMode;
        setOpaque(false);
        
        try {
            this.graph = Converter.mapToGraph(RESOURCES_FOLDER+name+".csv");
            // Permet de colorier la carte directement.
            // Je pense que ce n'est plus necessaire.
            // if(graphPlayView != null && graphPlayView.getAlgo() != null && graphPlayView.getColors() != null)
            //    graph.applyAlgo(graphPlayView.getAlgo(), graphPlayView.getColors());
            this.image = getImage(name);

            if(devMode) {
                System.out.println("\n[LOG]: Regions in blue have coordinates set already.\n");
                for (Vertex v : this.graph.getVertices()) {
                    if(v.getX() != -1 && v.getY() != -1) {
                        colorImage3(v.getX(), v.getY(),
                        new boolean[this.image.getWidth()][this.image.getHeight()],
                        Color.BLUE);

                        for (Point p : v.getOutsidePositions()) {
                            colorImage3(p.x, p.y,
                                new boolean[this.image.getWidth()][this.image.getHeight()],
                                Color.BLUE);
                        }
                    }
                }
                checkAndChangeVertex();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        if(sc == null && devMode)
            sc = new Scanner(System.in);
        
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

                super.mousePressed(e);
                Color color = graphPlayView != null ? graphPlayView.getUserColor() : Color.RED;
                int x = getScaledX(e.getX()), y = getScaledY(e.getY());
                // System.out.println("[LOG]: "+getSize()+" "+image.getWidth()+" "+image.getHeight());
                colorImage3(x, y, new boolean[image.getWidth()][image.getHeight()], color);
                checkAndChangeVertex();

                if(MapView.this.devMode) {
                    
                    // System.out.println(e.getPoint());
                    System.out.print("Indicate the name of this place : ");

                    try {
                        
                        String region  = sc.nextLine();
                        System.out.println("Name inserted: "+region);
                        Vertex v = MapView.this.graph.getVertex(region);
                        boolean otherPos = askQuestion();

                        if(otherPos) {
                            System.out.println("[LOG]: Adding aditional position for "+region);
                            v.setOutsidePosition(e.getPoint());
                            System.out.println("[LOG]: otherpos: "+v.getOutsidePositions());
                        
                        }else {
                            System.out.println("[LOG]: Adding position for "+region);
                            v.setPosition(e.getPoint());
                        }
                        System.out.println();
                        Converter.writeCoordinates(v, RESOURCES_FOLDER+name+".csv");
                    } catch (Exception e1) {
                        System.out.println("Vertex not found. Please insert a valid title");
                    }
                }
                repaint();
            }
        });

        if(graph == null)
            return;

        refresh(graph);
    }

    private boolean askQuestion() {
        System.out.println("[LOG]: Would you like to append a position to an existant Vertex? [y/n] ");
        String selected = sc.nextLine().toUpperCase();
        return selected.equals("Y");
    }

    public void checkAndChangeVertex() {
        if(graph == null)
            return;
        Color pixel;
        for(Vertex v : graph.getVertices()) {
            if(v.getX() == -1 || v.getY() == -1)
                continue;
            pixel = new Color(image.getRGB(v.getX(), v.getY()));
            if(!v.getColor().equals(pixel)) {
                v.setColor(pixel);
            }
        }
    }

    public void refresh(Graph graph) {
        if(graph == null)
            return;
        this.graph = graph;
        for(Vertex v : graph.getVertices()) {
            if(v.getPosition() != null) {
                colorImage3(v.getX(), v.getY(),
                    new boolean[image.getWidth()][image.getHeight()],
                    v.getColor() != null ? v.getColor() : Color.WHITE);
                for (Point p : v.getOutsidePositions()) {
                    colorImage3(p.x, p.y,
                        new boolean[this.image.getWidth()][this.image.getHeight()],
                        v.getColor());
                }
            }
        }
    }
    
    private BufferedImage getImage(final String name){
        String[] ext = {"png", "jpeg", "jpg"};
        BufferedImage img = null;
        for (String ex : ext) {
            try {
                img = ImageIO.read(new File(RESOURCES_FOLDER+name+"."+ex));
                break;
            } catch (IOException e) {}
        }
        return img;
    }
    // Pour compter le nombre de recursivite ou de tours de boucle
    // private static int count = 0;

    public boolean outOfBorders(int x, int y) {
        return (x < 0 || y < 0) || (x >= image.getWidth() || y >= image.getHeight());
    }
    
    public void colorImage(int x, int y, boolean[][] visited) {
        /* Pour compter le nombre d'appels.
        // ATTENTION: ralenti beaucoup la vitesse de l'algo
        count++;
        System.out.println(count);
        */
        if(outOfBorders(x, y) || visited[x][y])
            return;
        Color c = new Color(image.getRGB(x, y));
        int min = 180;
        if(c.getRed() < min || c.getGreen() < min || c.getBlue() < min)
            return;
        ////// On colorise le pixel
        image.setRGB(x, y, Color.RED.getRGB());
        //////
        visited[x][y] = true;
        int[] coords = {-1, 0, 0, 1, 0, -1, 1, 0};
        for(int i=0;i<coords.length;i+=2) {
            colorImage(x+coords[i], y+coords[i+1], visited);
        }

        visited[x][y] = false;
    }

    public void colorImage2(int x, int y, boolean[][] visited) {
        /* Pour compter le nombre d'appels.
        // ATTENTION: ralenti beaucoup la vitesse de l'algo
        count++;
        System.out.println(count);
        */
        if(outOfBorders(x, y) || visited[x][y])
            return;
        Color c = new Color(image.getRGB(x, y));
        int min = 120; // On considere que c'est du blanc jusqu'a (120,120,120)
        if(c.getRed() < min || c.getGreen() < min || c.getBlue() < min)
            return;
        if(c == Color.RED)
            return;
        ////// On colorise le pixel
        image.setRGB(x, y, Color.RED.getRGB());
        //////
        visited[x][y] = true;
        int[] coords = {-1, 0, 0, 1, 0, -1, 1, 0};
        for(int i=0;i<coords.length;i+=2) {
            if(outOfBorders(x+coords[i], y+coords[i+1]) || visited[x+coords[i]][y+coords[i+1]])
                continue;
            c = new Color(image.getRGB(x+coords[i], y+coords[i+1]));
            if(c.getRed() < min || c.getGreen() < min || c.getBlue() < min)
                continue;
            if(c == Color.RED)
                continue;
            image.setRGB(x+coords[i], y+coords[i+1], Color.RED.getRGB());
        }
        coords = new int[] {-1, -1, 1, -1, 1, 1, -1, 1};
        for(int i=0;i<coords.length;i+=2) {
            colorImage2(x+coords[i], y+coords[i+1], visited);
        }
        visited[x][y] = false;
    }
    
    public void colorImage3(int x, int y, boolean[][] visited, Color color) {
        Stack<Point> s = new Stack<Point>();
        s.add(new Point(x, y));
        while(!s.empty()) {
            Point p = s.pop();
            /* Pour compter le nombre de tours de boucle
            // ATTENTION: ralenti beaucoup la vitesse de l'algo
            count++;
            System.out.println(count);
            */
            if(outOfBorders(p.x, p.y) || visited[p.x][p.y])
                continue;
            Color c = new Color(image.getRGB(p.x, p.y));
            int min = 180;
            if(c.getRed() < min && c.getGreen() < min && c.getBlue() < min ||
               c == color)
                continue;
            ////// On colorise le pixel
            image.setRGB(p.x, p.y, color.getRGB());
            //////
            visited[p.x][p.y] = true;
            int[] coords = {-1, 0, 0, 1, 0, -1, 1, 0};
            for(int i=coords.length-2;i>=0;i-=2) {
                s.push(new Point(p.x+coords[i], p.y+coords[i+1]));
            }

            //visited[p.x][p.y] = false; // ? Marche sans.
        }
    }

    public boolean isWhite(Point p) {
        Color c = new Color(image.getRGB(p.x, p.y));
        int min = 220;
        return c.getRed() > min && c.getGreen() > min && c.getBlue() > min;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image == null)
            return;
        if(devMode)
            this.setSize(new Dimension(image.getWidth(), image.getHeight()));
        else
            this.setSize(getScaledMapDim());
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }

    private Dimension getScaledMapDim() {
        return MapChooser.resize(image, getSize());
    }

    public Dimension getScaledMapDim(Dimension size) {
        return MapChooser.resize(image, size);
    }

    public Dimension getMapDim() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    public int getScaledX(int x) {
        return (x * image.getWidth()) / getWidth();
    }

    public int getScaledY(int y) {
        return (y * image.getHeight()) / getHeight();
    }

    public String getMapName() {
        return name;
    }

    public Graph getGraph() {
        return graph;
    }

}
