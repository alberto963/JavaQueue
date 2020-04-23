package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import fw.AbstractDemo;
import fw.Demo;
import fw.DemoPolicy;
import fw.Logger;

@SuppressWarnings("serial")
public class ConfPanel extends JPanel {

    public interface StopAction {
        public void actionPerformed();
    }

    private AbstractDemo demo;

    public ConfPanel(final JPanel mapPanel) {
        super(new BorderLayout());

        final Font font = new Font("Serif", Font.ITALIC, 8);

        final JComboBox<String> queuePolicy = new JComboBox<String>(new String[] {
                "Deque put first, take last", "Deque put last, take first",
                "Deque put first, take first", "Deque put last, take last" });

        final JSlider numOfConsumers = new JSlider(JSlider.HORIZONTAL, 1, 4, 2);
        final JSlider stopElement = new JSlider(JSlider.HORIZONTAL, 10, 20, 15);

        final JSlider transitionTime = new JSlider(JSlider.HORIZONTAL, 100, 500, 200);
        final JSlider producerRate = new JSlider(JSlider.HORIZONTAL, 1, 10, 4);
        final JSlider consumerRate = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);

        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.black));

        /*
         * Buttons
         */
        final JButton start = new JButton("Start");
        final JButton stop = new JButton("Stop");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop.setEnabled(true);
                start.setEnabled(false);
                int nc = numOfConsumers.getValue();
                demo = new Demo(mapPanel, new DemoPolicy() {

                    @Override
                    public void setObject(BlockingQueue<Object> queue, Object object)
                            throws InterruptedException {

                        if (queue instanceof LinkedBlockingDeque) {
                            LinkedBlockingDeque<Object> deque = (LinkedBlockingDeque<Object>) queue;

                            int item = queuePolicy.getSelectedIndex();
                            switch (item) {
                            case 0:
                            case 2:

                                /*
                                 * Inserts the specified element at the front of
                                 * this deque, waiting if necessary for space to
                                 * become available.
                                 */
                                deque.putFirst(object);

                                break;

                            case 1:
                            case 3:
                                /*
                                 * Inserts the specified element at the end of
                                 * this deque, waiting if necessary for space to
                                 * become available.
                                 */
                                deque.putLast(object);

                                break;

                            default:
                                break;
                            }

                        }
                    }

                    @Override
                    public BlockingQueue<Object> newQueue() {
                        BlockingQueue<Object> demoQueue = new LinkedBlockingDeque<Object>();

                        return demoQueue;
                    }

                    @Override
                    public Object getObject(BlockingQueue<Object> queue)
                            throws InterruptedException {
                        if (queue instanceof LinkedBlockingDeque) {

                            LinkedBlockingDeque<Object> deque = (LinkedBlockingDeque<Object>) queue;

                            int item = queuePolicy.getSelectedIndex();
                            switch (item) {
                            case 0:
                            case 3:
                                /*
                                 * Retrieves and removes the last element of
                                 * this deque, waiting if necessary until an
                                 * element becomes available.
                                 */
                                return deque.takeLast();

                            case 1:
                            case 2:
                                /*
                                 * Retrieves and removes the first element of
                                 * this deque, waiting if necessary until an
                                 * element becomes available.
                                 */
                                return deque.takeFirst();

                            default:
                                break;
                            }
                        }

                        return null;
                    }

                    @Override
                    public int getX(int v) {
                        int item = queuePolicy.getSelectedIndex();
                        switch (item) {
                        case 0:
                        case 2:
                            return 400 + 20 * v;

                        default:
                            return 400 - 20 * v;
                        }
                    }

                    @Override
                    public int getY(int v) {
                        return 50 + 50;
                    }

                    @Override
                    public boolean isHidden(int v) {
                        // Not used
                        return false;
                    }

                    @Override
                    public Object beforeSetting(Object v, Object p) throws InterruptedException {

                        if (v instanceof Node) {
                            Node n = (Node) v;
                            Dimension dest = (Dimension) p;
                            n.drift(dest.width, dest.height);
                        }

                        return v;
                    }

                    @Override
                    public void stopActionPerformed() {
                        stop.setEnabled(false);
                        start.setEnabled(true);
                    }

                }, nc);

                Logger.getInstance().setDoneSignal(new CountDownLatch(nc));
                Logger.getInstance().start();

                demo.setProducerRate(producerRate.getValue());
                demo.setConsumerRate(consumerRate.getValue());
                demo.setTransitionTime(transitionTime.getValue());

                /*
                 * Start the demo
                 */
                demo.start(stopElement.getValue());
            }
        });

        /*
         * Configuration info
         */
        JPanel panelIn = new JPanel(new GridBagLayout());
        panelIn.setBorder(BorderFactory
                .createTitledBorder("Choose demo parameters (1 producer, 1-4 consumers)"));
        GridBagConstraints gbcIn = new GridBagConstraints();
        gbcIn.gridheight = 2;
        gbcIn.fill = GridBagConstraints.BOTH;

        /*
         * Number of consumer slider
         */
        numOfConsumers.setPreferredSize(new Dimension(150, 20));
        numOfConsumers.setMinorTickSpacing(1);
        numOfConsumers.setMajorTickSpacing(1);
        numOfConsumers.setPaintTicks(true);
        numOfConsumers.setPaintLabels(true);
        numOfConsumers.setFont(font);
        gbcIn.insets = new Insets(20, 5, 5, 20);
        gbcIn.gridx = 0;
        gbcIn.gridy = 0;
        panelIn.add(new JLabel("# of consumers"), gbcIn);
        gbcIn.gridx = 1;
        gbcIn.ipadx = 5;
        gbcIn.ipady = 18;
        gbcIn.gridwidth = GridBagConstraints.RELATIVE;
        panelIn.add(numOfConsumers, gbcIn);

        /*
         * Stop element slider
         */
        stopElement.setPreferredSize(new Dimension(150, 20));
        stopElement.setMinorTickSpacing(1);
        stopElement.setMajorTickSpacing(1);
        stopElement.setPaintTicks(true);
        stopElement.setPaintLabels(true);
        stopElement.setFont(font);
        gbcIn.gridx = 0;
        gbcIn.gridy = 2;
        panelIn.add(new JLabel("Stop element (>=)"), gbcIn);
        gbcIn.gridx = 1;
        gbcIn.ipadx = 5;
        gbcIn.ipady = 18;
        gbcIn.gridwidth = GridBagConstraints.RELATIVE;
        panelIn.add(stopElement, gbcIn);

        /*
         * Queue policy choice
         */
        gbcIn.gridx = 0;
        gbcIn.gridy = 4;
        panelIn.add(new JLabel("Queue policy"), gbcIn);
        gbcIn.gridx = 1;
        gbcIn.gridwidth = GridBagConstraints.RELATIVE;
        panelIn.add(queuePolicy, gbcIn);

        /*
         * Add choose panel
         */
        add(panelIn, BorderLayout.CENTER);

        JPanel panelAc = new JPanel(new GridBagLayout());
        panelAc.setBorder(BorderFactory.createTitledBorder("Control buttons"));
        GridBagConstraints gbcAc = new GridBagConstraints();
        gbcAc.insets = new Insets(3, 2, 3, 2);
        add(panelAc, BorderLayout.NORTH);

        stop.setEnabled(false);
        stop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (demo != null) {
                    demo.stop();
                    stop.setEnabled(false);
                    start.setEnabled(true);
                }
            }
        });

        final JButton clear = new JButton("Clear");
        clear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (demo != null) {
                    demo.clear();
                }
            }
        });

        final JButton clearLog = new JButton("ClearLog");
        clearLog.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Logger.getInstance().clear();
            }
        });

        panelAc.add(start, gbcAc);
        panelAc.add(stop, gbcAc);
        panelAc.add(clear, gbcAc);
        panelAc.add(clearLog, gbcAc);

        /*
         * Advanced settings
         */
        JPanel panelAd = new JPanel(new GridBagLayout());
        panelAd.setBorder(BorderFactory.createTitledBorder("Advanced settings"));
        GridBagConstraints gbcAd = new GridBagConstraints();
        gbcAd.insets = new Insets(3, 2, 3, 2);

        /*
         * Transition time slider
         */
        transitionTime.setPreferredSize(new Dimension(150, 20));
        transitionTime.setMinorTickSpacing(50);
        transitionTime.setMajorTickSpacing(100);
        transitionTime.setPaintTicks(true);
        transitionTime.setPaintLabels(true);
        transitionTime.setFont(font);
        gbcAd.fill = GridBagConstraints.BOTH;
        gbcAd.ipadx = 5;
        gbcAd.ipady = 18;
        gbcAd.gridx = 0;
        gbcAd.gridy = 0;
        panelAd.add(new JLabel("Transition time (msec)"), gbcAd);
        gbcAd.gridx = 1;
        panelAd.add(transitionTime, gbcAd);

        /*
         * Producer rate slider
         */
        producerRate.setPreferredSize(new Dimension(150, 20));
        producerRate.setMinorTickSpacing(1);
        producerRate.setMajorTickSpacing(1);
        producerRate.setPaintTicks(true);
        producerRate.setPaintLabels(true);
        producerRate.setFont(font);
        gbcAd.fill = GridBagConstraints.BOTH;
        gbcAd.gridx = 0;
        gbcAd.gridy = 1;
        panelAd.add(new JLabel("Producer rate (n/sec)"), gbcAd);
        gbcAd.gridx = 1;
        panelAd.add(producerRate, gbcAd);

        /*
         * Consumer rate slider
         */
        consumerRate.setPreferredSize(new Dimension(150, 20));
        consumerRate.setMinorTickSpacing(1);
        consumerRate.setMajorTickSpacing(1);
        consumerRate.setPaintTicks(true);
        consumerRate.setPaintLabels(true);
        consumerRate.setFont(font);
        gbcAd.fill = GridBagConstraints.BOTH;
        gbcAd.gridx = 0;
        gbcAd.gridy = 2;
        panelAd.add(new JLabel("Consumer rate (n/sec)"), gbcAd);
        gbcAd.gridx = 1;
        panelAd.add(consumerRate, gbcAd);

        add(panelAd, BorderLayout.SOUTH);
    }
}
