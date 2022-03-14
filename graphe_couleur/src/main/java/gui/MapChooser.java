package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
        });

        nextBut.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
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
        });

        selectBut.addActionListener(e -> {
            //Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA};
            Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.ORANGE};
            MapChooser.this.gui.setGraphViewPage(null, getName(maps.get(imageView.mapIndex)), "Greedy", colors, false);
            /*MapChooser.this.gui.setMapPage(
                getName(maps.get(imageView.mapIndex)), 
                MapChooser.this.devMode
            );*/
        });

        this.setLayout(new BorderLayout());

        JPanel previousPan = new JPanel();
        previousPan.setOpaque(false);
        previousPan.add(previousBut);

        JPanel nextPan = new JPanel();
        nextPan.setOpaque(false);
        nextPan.add(nextBut);

        this.add(title, BorderLayout.NORTH);
        this.add(previousPan, BorderLayout.WEST);
        this.add(nextPan, BorderLayout.EAST);
        this.add(selectBut, BorderLayout.SOUTH);

        this.add(imageView, BorderLayout.CENTER);

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
            Dimension dim = resize(img, getWidth(), getHeight());

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

        public Dimension resize(BufferedImage pic, int w_max, int h_max) {
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

        public int getMapIndex() {
            return mapIndex;
        }

    }

}
