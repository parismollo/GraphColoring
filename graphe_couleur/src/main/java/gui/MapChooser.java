package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
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
    private JButton previousBut = new JButton("<"),
                    nextBut = new JButton(">"),
                    selectBut = new JButton("Select");
    private List<File> maps;
    private ImageView imageView;
    private boolean devMode;

    public MapChooser(GUI gui, boolean devMode) {
        this.gui = gui;
        this.devMode = devMode;
        title = new JLabel("Choose your map");
        title.setFont(title.getFont().deriveFont(24.0f));
        title.setHorizontalAlignment(JLabel.CENTER);

        this.maps = getAllMaps();
        this.imageView = new ImageView(maps);

        previousBut.addActionListener(e -> {
            if(imageView.mapIndex > 0)
                imageView.mapIndex--;
            imageView.revalidate();
            imageView.repaint();
        });

        nextBut.addActionListener(e -> {
            if(imageView.mapIndex < maps.size() - 1)
                imageView.mapIndex++;
            imageView.revalidate();
            imageView.repaint();
        });

        selectBut.addActionListener(e -> {
            MapChooser.this.gui.setMapPage(
                getName(maps.get(imageView.mapIndex)), 
                MapChooser.this.devMode
            );
        });

        this.setLayout(new BorderLayout());

        this.add(title, BorderLayout.NORTH);
        this.add(previousBut, BorderLayout.WEST);
        this.add(nextBut, BorderLayout.EAST);
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
                g.drawImage(mapsImg.get(mapIndex), 0, 0, getWidth(), getHeight(), null);	
            }
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
