package gui;

import java.awt.Color;

import graphs.Graph;

public class RandomGraphView extends GraphPlayView {
    
    public RandomGraphView(GUI gui, Graph graph, Color[] colors, int width, int height) {
        super(gui, graph, colors, width, height);
        this.title = "Random Graph";
        topPanel.refreshTitle();
    }

}
