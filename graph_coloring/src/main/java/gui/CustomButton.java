package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class CustomButton extends JPanel implements MouseListener {

    private IconPanel icon;
    private JLabel lbl;

    private Color background = GUI.LIGHT_COLOR1;

    public CustomButton(String path, String text) {
        this.icon = new IconPanel(path, 128);
        this.lbl = new JLabel(text);
        
        setOpaque(false);
        addMouseListener(this);

        int border = 20;
        this.setBorder(new EmptyBorder(0, border, 0, border));

        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 32f));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel leftPan = new JPanel();
        leftPan.setOpaque(false);
        leftPan.setLayout(new GridBagLayout());
        leftPan.add(icon);
        
        this.setLayout(new BorderLayout());
        this.add(leftPan, BorderLayout.WEST);
        this.add(lbl, BorderLayout.CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        int radius = 50;
        g2d.setColor(background);
        g2d.fillRoundRect(0, 10, getWidth(), getHeight()-10, radius, radius);
    }

    public void changeIconSize(int size) {
        icon.changeSize(size);
    }

    public void changeTextSize(int size) {
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, size));
    }

    public void setHomeListener(GUI gui, int type) {
        Color[] colors = {Color.BLUE,Color.RED,Color.GREEN, Color.YELLOW, Color.MAGENTA};
        this.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                switch(type) {
                    case 0:
                        gui.setCreatorPage(colors);
                        break;
                    case 1:
                        gui.setMapChooser(false);
                        break;
                    case 2:
                        gui.setRandomGraphView(18, colors);
                        break;
                }
                background = GUI.LIGHT_COLOR1;
                repaint();
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        background = GUI.LIGHT_COLOR2;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        background = GUI.LIGHT_COLOR1;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}