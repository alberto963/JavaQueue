package ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import fw.Logger;

@SuppressWarnings("serial")
public class Main extends JFrame {

    /**
	 * Main
	 */
    public Main() {

        setTitle("Concurrent Queue Exerciser");

        JPanel workingPanel = new JPanel(new BorderLayout());
        workingPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));

        JPanel mapPanel = new MapPanel();

        JScrollPane demoPanel = new JScrollPane(mapPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel confPanel = new ConfPanel(mapPanel);

        JSplitPane verticalSp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, confPanel, demoPanel);
        verticalSp.setDividerLocation(350);

        JSplitPane horizontalSp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, verticalSp,
                Logger.getInstance());
        horizontalSp.setDividerLocation(500);

        workingPanel.add(horizontalSp, BorderLayout.CENTER);
        getContentPane().add(workingPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 768);
        setVisible(true);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
}
