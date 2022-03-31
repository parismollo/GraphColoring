package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MapChooser extends JPanel {
    
    private GUI gui;

    private JLabel title;
    private IconPanel previousBut = new IconPanel("left_arrow", 55),
                      nextBut = new IconPanel("right_arrow", 55);
    private JButton selectBut = new JButton("Select");
    private List<File> maps;
    private ImageView imageView;
    private boolean devMode;

    public MapChooser(GUI gui, boolean devMode) {
        this.gui = gui;
        this.devMode = devMode;
        this.setBackground(Color.WHITE);
        title = new JLabel("Choose your map : ");
        title.setFont(title.getFont().deriveFont(24.0f));
        title.setHorizontalAlignment(JLabel.CENTER);

        this.maps = getAllMaps();
        this.imageView = new ImageView(maps);

        if(maps != null && maps.size() >= 0) {
            title.setText("Choose your map : "+getName(maps.get(0)));
        }
        
        previousBut.setEnabled(false);

        previousBut.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                previousMap();
            }
        });

        nextBut.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                nextMap();
            }
        });

        selectBut.addActionListener(e -> {
            selectMap();
        });

        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        previousMap();
                        break;
                    case KeyEvent.VK_RIGHT:
                        nextMap();
                        break;
                    case KeyEvent.VK_ENTER:
                        selectMap();
                        break;
                }
            }
        });

        this.setLayout(new BorderLayout());

        JPanel previousPan = new JPanel();
        previousPan.setOpaque(false);
        previousPan.add(previousBut);

        JPanel nextPan = new JPanel();
        nextPan.setOpaque(false);
        nextPan.add(nextBut);

        JPanel temp = new JPanel();
        temp.setLayout(new GridLayout(1, 3));
        JPanel temp2 = new JPanel();
        temp2.setBorder(new EmptyBorder(0, 10, 0, 0));
        temp2.setLayout(new BorderLayout());
        IconPanel left_arrow = new IconPanel("return", 40);
        left_arrow.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                gui.setHomeView();
            }
        });
        temp2.add(left_arrow, BorderLayout.WEST);
        temp.add(temp2);
        temp2 = new JPanel();
        temp2.add(title);
        temp.add(temp2);
        temp.add(new JPanel());

        this.add(temp, BorderLayout.NORTH);
        this.add(previousPan, BorderLayout.WEST);
        this.add(nextPan, BorderLayout.EAST);
        this.add(selectBut, BorderLayout.SOUTH);

        this.add(imageView, BorderLayout.CENTER);

        this.requestFocus();
    }

    public void selectMap() {
            Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA};
            MapChooser.this.gui.setGraphViewPage(null, getName(maps.get(imageView.mapIndex)), "Kempe", colors, false);
            /*MapChooser.this.gui.setMapPage(
                getName(maps.get(imageView.mapIndex)), 
                MapChooser.this.devMode
            );*/
    }

    public void previousMap() {
        if(imageView.mapIndex > 0) {
            if(!nextBut.isEnabled())
                nextBut.setEnabled(true);
            imageView.mapIndex--;
            title.setText("Choose your map : "+getName(maps.get(imageView.getMapIndex())));
            if(imageView.mapIndex <= 0)
                previousBut.setEnabled(false);
        }
        imageView.revalidate();
        imageView.repaint();
    }

    public void nextMap() {
        if(imageView.mapIndex < maps.size() - 1) {
            if(!previousBut.isEnabled())
                previousBut.setEnabled(true);
            imageView.mapIndex++;
            title.setText("Choose your map : "+getName(maps.get(imageView.getMapIndex())));
            if(imageView.mapIndex >= maps.size() - 1)
                nextBut.setEnabled(false);
        }
        imageView.revalidate();
        imageView.repaint();
    }

    public static List<File> getAllMaps() {
        File dir = new File(MapView.RESOURCES_FOLDER);
        List<File> maps = new ArrayList<File>();
        for(File f : dir.listFiles())
            if(isImage(f))
                maps.add(f);
        return maps;
    }

    // Renvoie le nom du fichier sans l'extension
    public static String getName(File f) {
        String name = f.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }

    public static boolean isImage(File f) {
        if(f == null || !f.exists() || !f.isFile())
            return false;
        String name = f.getName();
        name = name.substring(name.lastIndexOf('.')+1, name.length());
        return name.equals("jpg") || name.equals("jpeg");
    }
    
    public static Dimension resize(BufferedImage pic, Dimension maxDim) {
        return resize(pic, (int)maxDim.getWidth(), (int)maxDim.getHeight());
    }

    public static Dimension resize(BufferedImage pic, int w_max, int h_max) {
        if(pic == null)
            return null;
        int w, h;
        if(pic.getWidth(null) > w_max)
            w = w_max;
        else
            w = pic.getWidth(null);
        
        h = pic.getHeight(null);
        float coeff = (float)w / (float)pic.getWidth();
        h *= coeff;
        
        if(h > h_max) {
            coeff = (float)h_max / (float)h;
            h = h_max;
            w *= coeff;
        }

        return new Dimension(w, h);
    }

    public class ImageView extends JPanel {
        private static final long serialVersionUID = 1L;
        
        private int mapIndex = 0;
        private List<BufferedImage> mapsImg;

        public ImageView(List<File> maps) {
            this.mapsImg = getImages(maps);
            setOpaque(false);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(mapsImg != null && mapIndex >= 0 && mapIndex < mapsImg.size()) {
                drawImg(mapsImg.get(mapIndex), g);	
            }
        }

        public void drawImg(BufferedImage img, Graphics g) {
			if(img == null)
                return;
            Dimension dim = MapChooser.resize(img, getWidth(), getHeight());

			g.drawImage(img, (this.getWidth()/2)-((int)dim.getWidth()/2), 
				(this.getHeight()/2)-((int)dim.getHeight()/2), 
				(int)dim.getWidth(), (int)dim.getHeight(), null);
        }

        public List<BufferedImage> getImages(List<File> list) {
            List<BufferedImage> images = new ArrayList<BufferedImage>();
            for(File map : list) {
                try {
                    images.add(ImageIO.read(map));
                } catch (IOException e) {e.printStackTrace();}
            }
            return images;
        }

        public int getMapIndex() {
            return mapIndex;
        }

    }

}
