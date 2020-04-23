package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapPanel extends JPanel {

    private BlockingQueue<Object> elements;

    public MapPanel() {
        super(new BorderLayout());

        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.black));

        elements = new LinkedBlockingQueue<Object>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }
        });

        ScheduledThreadPoolExecutor ex = new ScheduledThreadPoolExecutor(1);
        ex.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                repaint();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void setElements(BlockingQueue<Object> elements) {
        this.elements = elements;
    }

    public void addElement(MapElement me) {
        elements.add(me);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 450);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // String msg = "\npaintComponent";
        // System.out.println(msg);
        // log.add(msg);

        Iterator<Object> it1 = elements.iterator();
        while (it1.hasNext()) {
            MapElement me = (MapElement) it1.next();
            if (!me.isHidden()) {
                me.draw(g);
            }
        }

    }

    public void clearAll() {
        elements.clear();

        repaint();
    }

    public void clear() {
        elements.clear();

        repaint();
    }

    public void removeElement(MapElement element) {
        elements.remove(element);
    }

}
