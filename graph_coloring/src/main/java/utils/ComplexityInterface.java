package utils;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import graphs.Graph;  


public class ComplexityInterface {

    public static ArrayList<Graph> graphs;

    public static JFrame getFrame() {

        JFrame frame = new JFrame();
        JPanel resultsPanel = new JPanel();
        resultsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        resultsPanel.setBackground(new Color(83, 124, 231));
        GridLayout experimentLayout = new GridLayout(0,5);
        resultsPanel.setLayout(experimentLayout);

        Font customFont = null;

        try {

            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/Poppins-Bold.ttf")).deriveFont(18f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

        } catch (Exception e) {

            customFont = new Font(Font.SERIF, Font.PLAIN,  10);
        }

        setTableHeader(resultsPanel, customFont);

        String[] dsaturResults = Complexity.computePerfomance(graphs, "dsatur");
        String[] greedyResults = Complexity.computePerfomance(graphs, "greedy");
        String[] welshResults = Complexity.computePerfomance(graphs, "welsh");
        String[] kempeResults = Complexity.computePerfomance(graphs, "kempe");


        for (String string : dsaturResults) {
            resultsPanel.add(getJlabel(string, customFont, true));
        }
        for (String string : greedyResults) {
            resultsPanel.add(getJlabel(string, customFont, true));
        }
        for (String string : welshResults) {
            resultsPanel.add(getJlabel(string, customFont, true));
        }
        for (String string : kempeResults) {
            resultsPanel.add(getJlabel(string, customFont, true));
        }
        frame.add(resultsPanel);    
        frame.setSize(700,400); 
        frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;

    }


    public static void setGraphs(ArrayList<Graph> graphs) {
        ComplexityInterface.graphs = null;
        ComplexityInterface.graphs = graphs;
    } 

    private static void setTableHeader(JPanel resultsPanel, Font customFont) {
        resultsPanel.add(getJlabel("Algorithm", customFont, false));
        resultsPanel.add(getJlabel("Input size", customFont, false));
        resultsPanel.add(getJlabel("Operations", customFont, false));
        resultsPanel.add(getJlabel("Runtime/ns", customFont, false));
        resultsPanel.add(getJlabel("# Colors", customFont, false));
    }

    private static JLabel getJlabel(String title, Font customFont, boolean white) {
        JLabel graphLabel = new JLabel(title, SwingConstants.CENTER);
        graphLabel.setFont(customFont);
        if(white) graphLabel.setForeground(Color.white);
        else graphLabel.setForeground(Color.black);
        return graphLabel;
    }
}
