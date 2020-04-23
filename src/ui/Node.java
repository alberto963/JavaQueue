/**
 * 
 */
package ui;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author apetazzi
 * 
 */
public class Node extends MapElement {
    private int transitionTime = 100;

    public Node(int xIn, int yIn, final int transitionTime) {
        x = xIn;
        y = yIn;
        this.transitionTime = transitionTime;
        label = Integer.toString(x) + "," + Integer.toString(y);
    }

    @Override
    public void draw(Graphics g) {
        // System.out.println("draw: x="+ x + " y="+y);

        g.setColor(Color.GREEN);
        g.fillOval(x - 4, y - 4, 9, 9);
        g.setColor(Color.WHITE);
        g.drawString(label, x + 3, y + 2);
    }

    /**
     * @param ex
     * @param ey
     * @param transitionTime
     * @throws InterruptedException
     */
    public void drift(int ex, int ey) throws InterruptedException {
        final int step = 100;
        int ix = x, iy = y;

        double rx = ex - x;
        double ry = ey - y;
        final double sx = rx / step;
        final double sy = ry / step;

        // System.out.println("drift x=" + x + " y=" + y + " ex=" + ex + " ey="
        // + ey + " label="
        // + getLabel());
        // System.out.println("drift rx= " + rx + " ry=" + ry + " sx=" + sx +
        // " sy=" + sy + " label="
        // + getLabel());

        for (int i = 0; i <= step; i++) {
            int x2 = (int) (ix + (i) * sx);
            setX(x2);
            int y2 = (int) (iy + (i) * sy);
            setY(y2);

            // System.out.println("drift LOOP step=" + i + " x2= " + x2 + " y2="
            // + y2 + " label="
            // + getLabel());

            Thread.sleep(transitionTime / step);
        }

        // System.out.println("drift ENDED x= " + getX() + " y=" + getY() +
        // " label=" + getLabel());
    }
}
