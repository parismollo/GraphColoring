package gui;

import java.awt.Color;

public class GraphCreatorView extends GraphPlayView {

    public GraphCreatorView(GUI gui, Color[] colors) {
        super(gui, colors);
        this.title = "Graph Creator";
        topPanel.refreshTitle();
    }

}
