/**
 * (1) Generate random planar graphs. (visually)
 * (2) Connect to graph structure.
 *    - Create Graph
 *    - Add Vertices
 *    - Add edges
 */
package utils;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import graphs.Graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.awt.geom.Ellipse2D;

public class Voronoi extends JFrame {
    /**
     * (1) Dot coordinates.
     * (2) Colors 
     * (3) Number of Vertices (cells) and Screen size.  
     */
    static int px[], py[]; 
    static int color[], cells, size;
    static BufferedImage I; 
    static int SHIFT_MARGIN = 5;
    static Graph graph;
    static boolean euclidien;

    public Voronoi(int cells, boolean eucliedian, boolean saveImage, int screenRes) {
        Voronoi.cells = cells;
        Voronoi.size = screenRes;
        Voronoi.euclidien = eucliedian;

        /**
         * (1) JFrame configurations. 
         */
        setTitle("Voronoi Diagram");
        setBounds(0, 0, size, size);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.dispose();
        /**
         * (1) Image for display and drawing.
         * (2) x and y coordinates of vertices. 
         */
        I = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        px = new int[cells];
        py = new int[cells];
        color = new int[cells];

        Voronoi.graph = new Graph("Random Voronoi");
        

        int n = 0;
        Random rand = new Random();
        
        /**
         * (1) Set vertices randomly in screen.
         * (2) Set color for each vertice. 
         */
        for(int i=0; i<cells; i++) {
            px[i] = rand.nextInt(size); 
            py[i] = rand.nextInt(size);
            color[i] = rand.nextInt(16777215);
            String title = Integer.toString(i);
            Voronoi.graph.addVertex(title);
            Voronoi.graph.getVertex(title).setPosition(px[i], py[i]);
        }

        /**
         * (1) For each pixel search closest vertice and color current pixel according to it. 
         *      - (Using eucliedian or manhatan distance).
         */
        for (int x=0; x<size; x++) {
            for(int y=0; y<size; y++) {
                n = 0;
                for(byte i =0; i<cells; i++) {
                    if (distance(px[i], x, py[i], y, eucliedian) < distance(px[n], x, py[n], y, eucliedian)) {
                        n = i;
                    }
                }
                I.setRGB(x, y, color[n]);
            }
        }

        /**
         * (1) Draw vertices in screen. 
         */
        Graphics2D graphics = I.createGraphics();
        graphics.setColor(Color.BLACK);
        for(int i=0; i<cells; i++) {
            graphics.fill(new Ellipse2D .Double(px[i]-2.5, py[i]-2.5, 2, 2));
            graphics.drawString(Integer.toString(i), px[i], py[i]);
            // System.out.println("i: "+i+" px: "+px[i]+" py: "+py[i]);
        }

        /**
         * (1) Try to save image or catch it if something goes wrong.
         */
        if (saveImage) {
            try {
                ImageIO.write(I, "png", new File("voronoi.png"));
            } catch(IOException e) {}
        }


        Voronoi.findNeighbors(Voronoi.graph);
        // System.out.println(Voronoi.graph);

    }
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0,  getContentPane());
    }

    /**
     * (1) Distance functions.
     */

    static double distance(int x1, int x2, int y1, int y2, boolean eucliedian) {
        double d;
        if (eucliedian) {
            d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); // Euclidian
        }else{
            d = Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
        }
	  	return d;
    }

    static void findNeighbors(Graph graph) {
        /**
         * (1) For each vertex / color:
         *  - Get dot / vertex center and move towards the 8 directions
         *  - top, bottom, left, right, top right, top left, bottom left, bottom right. 
         *  - for each pixel, if color changes from original center, add egde if it have not been added already.   
         */
        for(int n=0; n<cells; n++) {
            checkTop(n, graph);
            checkBottom(n, graph);
            checkLeft(n, graph);
            checkRight(n, graph);
            checkBottomLeft(n, graph);
            checkTopLeft(n, graph);
            checkBottomRight(n, graph);
            checkTopRight(n, graph);
        }


    }

    static int getColor(int n) {
        int rgb = 0;
        int[] shifts = {SHIFT_MARGIN, 0, -SHIFT_MARGIN, 0, 0, SHIFT_MARGIN, 0, -SHIFT_MARGIN};
        for(int i=0; i<shifts.length; i+=2) {
            try {
                rgb = I.getRGB(px[n]+shifts[i], py[n]+shifts[i+1]);
                return rgb;
            } catch(Exception e) {}
        }
        return -1;
    }

    // static int setRGB() {
    //     return I.setRGB(x, y, rgb);
    // }
    static void checkTop(int n, Graph graph) {
        int rgb = getColor(n);
        int x = px[n];
        int y = py[n];

        while (y < Voronoi.size-1 && I.getRGB(x, y) == rgb ) {
            y++;
        };
        if (I.getRGB(x, y) != rgb) {
            // System.out.println("*");
            // System.out.println(graph.getVertex(n));
            // System.out.println(findVertex(I.getRGB(x, y)));
            String title = Integer.toString(n);
            graph.addEdge(graph.getVertex(title), graph.getVertex(findVertex(I.getRGB(x, y))));
        }

        // System.out.println("Vertex: "+n+" Color: "+rgb);

    }

    static void checkBottom(int n, Graph graph) {
        int rgb = getColor(n);
        int x = px[n];
        int y = py[n];
        while (y > 0 && I.getRGB(x, y) == rgb) {
            y--;
        }
        if (I.getRGB(x, y) != rgb) {
            // System.out.println("**");
            // System.out.println(graph.getVertex(n));
            // System.out.println(findVertex(I.getRGB(x, y)));
            String title = Integer.toString(n);
            graph.addEdge(graph.getVertex(title), graph.getVertex(findVertex(I.getRGB(x, y))));
        }

    }

    static void checkLeft(int n, Graph graph) {
        int rgb = getColor(n);
        int x = px[n];
        int y = py[n];

        while (x > 0 && I.getRGB(x, y) == rgb) {
            x--;
        }
        if (I.getRGB(x, y) != rgb) {
            // System.out.println("***");
            // System.out.println(graph.getVertex(n));
            // System.out.println(findVertex(I.getRGB(x, y)));
            String title = Integer.toString(n);
            graph.addEdge(graph.getVertex(title), graph.getVertex(findVertex(I.getRGB(x, y))));
        }

    }


    static void checkRight(int n, Graph graph) {
        int rgb = I.getRGB(px[n], py[n]);
        int x = px[n];
        int y = py[n];

        while (x < Voronoi.size-1 && I.getRGB(x, y) == rgb) {
            x++;
        }
        if (I.getRGB(x, y) != rgb) {
            // System.out.println("****");
            // System.out.println(graph.getVertex(n));
            // System.out.println(findVertex(I.getRGB(x, y)));
            String title = Integer.toString(n);
            graph.addEdge(graph.getVertex(title), graph.getVertex(findVertex(I.getRGB(x, y))));
        }

    }

    static void checkBottomLeft(int n, Graph graph) {
        int rgb = getColor(n);
        int x = px[n];
        int y = py[n];

        while (x > 0 && y > 0 && I.getRGB(x, y) == rgb) {
            x--;
            y--;
        }
        if (I.getRGB(x, y) != rgb) {
            // System.out.println("*****");
            // System.out.println(graph.getVertex(n));
            // System.out.println(findVertex(I.getRGB(x, y)));
            String title = Integer.toString(n);
            graph.addEdge(graph.getVertex(title), graph.getVertex(findVertex(I.getRGB(x, y))));
        }

    }


    static void checkTopLeft(int n, Graph graph) {
        int rgb = getColor(n);
        int x = px[n];
        int y = py[n];

        while (x > 0 && y < Voronoi.size-1 && I.getRGB(x, y) == rgb) {
            x--;
            y++;
        }
        if (I.getRGB(x, y) != rgb) {
            // System.out.println("******");
            // System.out.println(graph.getVertex(n));
            // System.out.println(findVertex(I.getRGB(x, y)));
            String title = Integer.toString(n);
            graph.addEdge(graph.getVertex(title), graph.getVertex(findVertex(I.getRGB(x, y))));
        }

    }

    static void checkBottomRight(int n, Graph graph) {
        int rgb = getColor(n);
        int x = px[n];
        int y = py[n];

        while (x < Voronoi.size-1 && y > 0 && I.getRGB(x, y) == rgb) {
            x++;
            y--;
        }
        if (I.getRGB(x, y) != rgb) {
            // System.out.println("*******");
            // System.out.println(graph.getVertex(n));
            // System.out.println(findVertex(I.getRGB(x, y)));
            String title = Integer.toString(n);
            graph.addEdge(graph.getVertex(title), graph.getVertex(findVertex(I.getRGB(x, y))));
        }

    }

    static void checkTopRight(int n, Graph graph) {
        int rgb = getColor(n);
        int x = px[n];
        int y = py[n];

        while (x < Voronoi.size-1 && y < Voronoi.size-1 && I.getRGB(x, y) == rgb) {
            x++;
            y++;
        }
        if (I.getRGB(x, y) != rgb) {
            // System.out.println(graph.getVertex(n));
            // System.out.println(findVertex(I.getRGB(x, y)));
            String title = Integer.toString(n);
            graph.addEdge(graph.getVertex(title), graph.getVertex(findVertex(I.getRGB(x, y))));
        }

    }

    static String findVertex(int rgb) {
        for(int i=0; i<cells; i++) {
            // System.out.println("getColor("+i+"): "+getColor(i)+" rgb: "+rgb);
            if (getColor(i) == rgb) {
                // System.out.println("*");
                return Integer.toString(i);
            }
        }
        return "-1";
    }

    static Graph getVoronoiGraph() {
        return Voronoi.graph;
    }

    public static String info() {
        String s ="";
        s+="\nVoronoi Information\n";
        s+="(1) Vertices: "+Voronoi.cells+"\n";
        s+="(2) Distance: "+(Voronoi.euclidien ? "Euclidien\n" : "Manhatan\n");
        s+="(3) Shift Margin: "+Voronoi.SHIFT_MARGIN+"\n";
        s+="(4) Screen Resolution: ("+Voronoi.size+", "+Voronoi.size+")\n";
        return s;
    }

    public static Graph runVoronoi(int cells, boolean eucliedian, boolean saveImage, int screenRes, boolean displayVoronoi) {
        new Voronoi(cells, eucliedian, saveImage, screenRes).setVisible(displayVoronoi);
        // System.out.println(Voronoi.info());
        return Voronoi.graph;
    }

    public static void main(String[] args) {
        // new Voronoi(20, false, false, 500).setVisible(true);
        // System.out.println(Voronoi.info());
        Graph g = runVoronoi(20, false, false, 500, false);
        System.out.println(g.toString());
    }
}