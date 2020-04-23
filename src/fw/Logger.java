package fw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Logger extends JPanel implements Runnable {
    private static Logger               instance;
    private final BlockingQueue<String> log      = new LinkedBlockingQueue<String>();
    private CountDownLatch              doneSignal;
    private final JTextArea             textArea = new JTextArea();

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }

        return instance;
    }

    private Logger() {
        super(new BorderLayout());

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setSelectionColor(Color.LIGHT_GRAY);
        textArea.setRows(5);

        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

    }

    public void setDoneSignal(CountDownLatch cdl) {
        doneSignal = cdl;
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            doneSignal.await(); // wait for all to finish

            int size = log.size();
            for (int l = 0; l < size; l++) {
                String logMsg = null;
                try {
                    logMsg = log.remove();
                } catch (Exception e) {
                    System.out.println("l=" + l + " logSize=" + size);

                    e.printStackTrace();

                    return;
                }
                System.out.println(logMsg);

            }
            System.out.println("logSize=" + size);
        } catch (InterruptedException ex) {
            String msg = "\nLogger INTERRUPTED";
            System.out.println(msg);
        }
    }

    public void consumerInterrupted() {
        doneSignal.countDown();
    }

    public void log(String m) {
        String msg = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()) + " " + m;
        // System.out.println(msg);
        log.add(msg);
        setText(msg + "\n");
    }

    public void clear() {
        if (SwingUtilities.isEventDispatchThread()) {
            textArea.setText(null);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    textArea.setText(null);
                }
            });
        }
    }

    private void setText(final String textIn) {
        if (SwingUtilities.isEventDispatchThread()) {
            textArea.append(textIn);
            scrollToEnd();
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    textArea.append(textIn);
                    scrollToEnd();
                }
            });
        }
    }

    private void scrollToEnd() {
        Dimension size = getSize();
        Rectangle rectangle = new Rectangle(new Point(1, size.height + 1000));
        scrollRectToVisible(rectangle);
    }

    @Override
    public void updateUI() {

        super.updateUI();

        /*
         * Set the text area's border, colors and font to that of a label
         */
        LookAndFeel.installBorder(this, "Label.border");
        LookAndFeel
                .installColorsAndFont(this, "Label.background", "Label.foreground", "Label.font");
        setBackground(Color.WHITE);
    }
}
