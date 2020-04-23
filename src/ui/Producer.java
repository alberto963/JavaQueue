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
public class Producer extends MapElement {

    public Producer(int xIn, int yIn) {
        x = xIn;
        y = yIn;
        label = Integer.toString(x) + "," + Integer.toString(y);
    }

    @Override
    public void draw(Graphics g) {
        // System.out.println("draw: x="+ x + " y="+y);

        g.setColor(Color.RED);
        g.fillRect(x - 4, y - 4, 8, 8);
        g.setColor(Color.WHITE);
        g.drawString(label, x, y);
    }

}
