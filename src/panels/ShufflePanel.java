package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import utils.ShuffleGraph;

public class ShufflePanel extends JPanel implements KeyListener {
    int prevX, prevY;
    public ShuffleGraph graph;
    
    public ShufflePanel() {
        setPreferredSize(new Dimension(700, 450));
        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g.setFont(new Font("ariel", Font.PLAIN, 24));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(128, 128, 128));
        g.fillRect(0, 0, getWidth(), getHeight());
        graph.draw(g2d);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            graph.deleteNode();
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    protected class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            requestFocus(); // Java doesn't handle this for us?
            if (SwingUtilities.isLeftMouseButton(e)) {
                graph.select(e.getPoint());
                prevX = e.getX();
                prevY = e.getY();
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                graph.endConnection();
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                graph.drag(new Point(e.getX() - prevX, e.getY() - prevY));
                prevX = e.getX();
                prevY = e.getY();
                repaint();
            }
        }
    }
}
