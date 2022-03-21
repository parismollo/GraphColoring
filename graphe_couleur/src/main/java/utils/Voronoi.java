/**
 * Voronoi Diagram
 * (1) Generate random planar graphs. (visually)
 * (2) Connect to graph structure. 
 */
package utils;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

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
     * px[], py[] -> coordinates for x-axis and y-axis. 
     * cells -> number of dots (or vertices)
     * I -> image to display graph (temporary)
     */
    static int px[], py[]; 
    static int color[], cells = 100, size = 1000;
    static double p = 3; // ?
    static BufferedImage I; 

    public Voronoi() { 
        setTitle("Test Voronoi");
        setBounds(0, 0, size, size);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        I = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        px = new int[cells];
        py = new int[cells];
        int n = 0;
        color = new int[cells];
        Random rand = new Random();
        
        for(int i=0; i<cells; i++) { // set coordinates in screen for vertices. 
            px[i] = rand.nextInt(size);
            py[i] = rand.nextInt(size);

            color[i] = rand.nextInt(16777215); // ? Here I think that we set the same color for every dot / vertice
        }
        // Looping over screen pixels
        for (int x=0; x<size; x++) {
            for(int y=0; y<size; y++) {
                n = 0;
                for(byte i =0; i<cells; i++) {
                    if (distance(px[i], x, py[i], y) < distance(px[n], x, py[n], y)) {
                        n = i;
                    }
                }
                I.setRGB(x, y, color[n]); // ? How we get different color schemes here?
            }
        }
        Graphics2D graphics = I.createGraphics();
        graphics.setColor(Color.BLACK);
        for(int i=0; i<cells; i++) {
            graphics.fill(new Ellipse2D .Double(px[i]-2.5, py[i]-2.5, 5, 5));
        }
        try {
            ImageIO.write(I, "png", new File("voronoi.png"));
        } catch(IOException e) {}
    }
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0,  this);
    }
    static double distance(int x1, int x2, int y1, int y2) {
        double d;
	    d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); // Euclidian
	//  d = Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
	  	return d;
    }

    public static void main(String[] args) {
        new Voronoi().setVisible(true);
    }
}