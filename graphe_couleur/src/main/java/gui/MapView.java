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
    
    private Graph graph;
    private BufferedImage image;
    private static Scanner sc = new Scanner(System.in);
    private boolean devMode;

    public MapView(Graph graph, String url) {
        this(graph, url, false);
    }

    public MapView(Graph graph, String url, boolean devMode) {
        try {
            image = ImageIO.read(new File(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                colorImage3(e.getX(), e.getY(),
                    new boolean[image.getWidth()][image.getHeight()]);
               /* if(graph != null) {
                    for(Vertex v : graph.getVertices()) {
                        if(!isWhite(v.getPosition()))
                            System.out.println("aaaa");

                    }
                }
                */
                if(devMode) {
                    System.out.println(e.getPoint());
                    System.out.print("Nom du lieu : ");
                    Vertex v = graph.getVertex(sc.nextLine());
                    v.setPosition(e.getPoint());
                    System.out.println();
                    
                    try {
                        Converter.write(v, url.substring(0, url.lastIndexOf("."))+".csv");
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                repaint();
            }
        });
    }

    public MapView(String url) {
        this(null, url);
    }

    // Pour compter le nombre de recursivite ou de tours de boucle
    static int count = 0;

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

    public void colorImage3(int x, int y, boolean[][] visited) {
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
            if(c.getRed() < min || c.getGreen() < min || c.getBlue() < min)
                continue;
            if(c == Color.RED)
               continue;
            ////// On colorise le pip.xel
            image.setRGB(p.x, p.y, Color.RED.getRGB());
            //////
            visited[p.x][p.y] = true;
            int[] coords = {-1, 0, 0, 1, 0, -1, 1, 0};
            for(int i=coords.length-2;i>=0;i-=2) {
                s.push(new Point(p.x+coords[i], p.y+coords[i+1]));
            }

            //visited[p.x][p.y] = false;
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
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        /*
        int imgW = 3*image.getWidth()/5;
        int imgH = 3*image.getHeight()/5;
        int spaceX = getWidth() - imgW;
        spaceX /= 2;
        int spaceY = getHeight() - imgH;
        spaceY /= 2;
        g.drawImage(image, spaceX, spaceY, imgW, imgH, null);
        */
    }

    public Dimension getMapDim() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

}
