package gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class HomeView extends JPanel {
    
    private GUI gui;

    private JButton mapChooser = new JButton("Choose Map"),
                    randomGraph = new JButton("Random Graph");

    public HomeView(GUI gui) {
        this.gui = gui;

        mapChooser.addActionListener(e -> {
            gui.setMapChooser(false);
        });

        randomGraph.addActionListener(e -> {
            gui.setRandomGraphView(18);
        });

        JPanel centerPan = new JPanel();
        centerPan.setPreferredSize(new Dimension(300, 150));
        centerPan.setOpaque(false);
        centerPan.setLayout(new GridLayout(2, 1));

        centerPan.add(mapChooser);
        centerPan.add(randomGraph);

        this.setLayout(new GridBagLayout());
        this.add(centerPan);
    }

}
